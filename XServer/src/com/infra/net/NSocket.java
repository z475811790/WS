package com.infra.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.core.App;
import com.core.ByteArray;
import com.core.Console;
import com.core.event.XEvent;
import com.core.interfaces.IFunctionNoneArgs;
import com.core.util.XUtil;
import com.infra.Config;
import com.infra.Stringbytes;
import com.infra.event.ModuleEvent;

/**
 * @author xYzDl
 * @date 2018年9月8日 下午7:59:03
 * @description 非阻塞套接字
 */
public class NSocket {
	private static final int SEND_CHALLENGE = 0; // 发送验证状态
	private static final int RECEIVE_CHALLENGE = 1; // 接收码验证和客户端公钥状态
	private static final int NORMAL = 2; // 正常通信状态

	private static final int HEAD_LEN = 4; // 数据包头,用来表示实际数据的长度,数据包去掉包头才是实际数据长度
	private static final int BODY_MAX_LEN = 8192; // 8192
													// 数据包体，最大长度8k，超出认为是异常包，断开链接
	private SocketChannel _socketChannel;
	private ByteBuffer _innerBuffer = ByteBuffer.allocate(HEAD_LEN + BODY_MAX_LEN);
	private int _readIndex = 0;
	private int _length = 0;
	private BlockingQueue<byte[]> outQueue = new LinkedBlockingQueue<>();
	private IFunctionNoneArgs[] _stateHandlers = new IFunctionNoneArgs[3];// 状态处理方法字典
	private int _currState = 0;

	public long lastRecordTime = 0;// 上一次接收消息数据的时间戳

	private NSocket(SocketChannel socketChannel) {
		_stateHandlers[SEND_CHALLENGE] = this::sendChallenge;
		_stateHandlers[RECEIVE_CHALLENGE] = this::receiveChallengeAndPublicKey;
		_stateHandlers[NORMAL] = this::readMsg;

		_socketChannel = socketChannel;
	}

	// 静态区
	private static Map<String, NSocket> _socketMap = new HashMap<>();
	public static int numSocket = 0;

	synchronized public static NSocket create(SocketChannel socketChannel) {
		NSocket s = new NSocket(socketChannel);
		_socketMap.put(XUtil.getSocketId(socketChannel.socket()), s);
		numSocket++;
		return s;
	}

	synchronized public static void delete(NSocket nSocket) {
		if (nSocket == null)
			return;
		String key = nSocket.getSocketId();// XCommonUtil.getSocketIdX();
		if (_socketMap.get(key) != null) {
			nSocket.dispose();
			_socketMap.remove(key);
			numSocket--;
		}
	}

	public static NSocket getSocket(String socketId) {
		return _socketMap.get(socketId);
	}

	//

	// ------START-事件注册区
	static {
		App.addModuleListener(ModuleEvent.CREATE_AES_COMPLETE, NSocket::onCreateAESComplete);
	}

	private static void onCreateAESComplete(XEvent xEvent) {
		Stringbytes args = (Stringbytes) xEvent.data;
		NSocket nSocket = getSocket(args.string);
		if (nSocket == null)
			return;
		nSocket.writeDataPack(args.bs);
		nSocket._currState = NORMAL;
		App.dispatch(ModuleEvent.SOCKET_STATE_TO_NORMAL, NSocket.numSocket);
	}

	// ------END---事件注册区
	// ------START-公共方法区
	public void sendVersion() {
		// 验证协议版本
		outQueue.offer(XUtil.intToBytes(Config.COMMUNICATION_PROTOCOL_VERSION));
		Console.addMsg("sendVersion");
	}

	public void readChannel() throws Exception {
		int len = _socketChannel.read(_innerBuffer);
		lastRecordTime = System.currentTimeMillis();
		if (len < 0) {
			// _dispatcher.dispatch(EventType.CLOSE,
			// XCommonUtil.getSocketId(_socket));
		} else {
			_length += len;
		}
		if (_innerBuffer.remaining() == 0 && _readIndex > 0) {// 缓冲区填充满了且有数据已经被读取了
			_length -= _readIndex;
			_innerBuffer.position(_readIndex);
			_innerBuffer.compact();// 移动数据
			_readIndex = 0;
		}
		_stateHandlers[_currState].execute();
	}

	public void writeDataPack(byte[] bs) {
		if (bs == null || bs.length == 0)
			return;
		if (bs.length > BODY_MAX_LEN) {
			// TODO:派发数据包过大消息
			return;
		}
		if (_socketChannel.isConnected()) {
			outQueue.offer(XUtil.intToBytes(bs.length));
			outQueue.offer(bs);
		} else {
			System.out.println("Socket is Disconnected");
		}
	}

	private ByteBuffer outBytes = ByteBuffer.allocate(HEAD_LEN + BODY_MAX_LEN);

	public void flush() throws IOException {
		byte[] bytes = outQueue.poll();
		if (bytes == null)
			return;
		outBytes.clear();
		outBytes.put(bytes);
		outBytes.flip();
		_socketChannel.write(outBytes);
	}

	public void dispose() {
		try {
			_socketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			_socketChannel = null;
		}
		_innerBuffer = null;
		outBytes = null;
		outQueue.clear();
		outQueue = null;
	}

	/*
	 * 返回127.0.0.1:8080形式字符串
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (_socketChannel != null)
			return XUtil.getSocketId(_socketChannel.socket());
		return "0:0";
	}

	public String getSocketId() {
		return toString();
	}

	// ------END---公共方法区
	// ------START-私有方法区
	private void sendChallenge() {
		if (bytesAvailable() < 4)
			return;
		int ver = readInt();
		if (ver > Config.COMMUNICATION_PROTOCOL_VERSION) {
			Console.addMsg(getSocketId() + "-" + "Client Version is " + ver + ". Newer than Server's.");
			delete(this);
			return;
		}
		outQueue.offer(XUtil.intToBytes(Config.challenge));
		_currState = RECEIVE_CHALLENGE;

		Console.addMsg("sendChallenge");
	}

	private void receiveChallengeAndPublicKey() {
		byte[] buffer = readDataPack();
		if (buffer == null)
			return;
		ByteArray byteArray = new ByteArray(buffer);
		int challenge = byteArray.getInt();
		// 验证码算法
		if (challenge != Config.challenge * 2) {
			Console.addMsg(getSocketId() + "-" + "Client Challenge is Wrong " + challenge + " Socket will be Closed.");
			delete(this);
			return;
		}
		Console.addMsg("State Change to RECEIVE_PUB_KEY");
		App.dispatch(ModuleEvent.SERVER_WORKER_CRYPT_CREAT_AES,
				new Stringbytes(getSocketId(), byteArray.getAvailableBytes()));
	}

	private void readMsg() {
		byte[] bs = readDataPack();
		if (bs == null || bs.length == 0) {
			return;
		}
		// ******多线程派发事件******------第一步-服务端在多线程的情况下,该步骤之后就可以多线程读取访问socket(用专门的IO处理线程的话可以更高效)
		// ******多线程派发事件******------第二步-到这里表示客户端发来的一个完整的消息数据包已经接收到了,派发给解码线程(用可以调用硬件解码的线程解码消息可以更加高效)
		App.dispatch(ModuleEvent.SERVER_WORKER_CRYPT_DECRYPT, new Stringbytes(getSocketId(), bs));
		// ******多线程派发事件******------第三步-向一个根据重要性分组的线程安全消息队列加入消息--暂不实现
		// ******多线程派发事件******------第四步-一个线程专门从消息队列中循环取消息后派发消息
		// ******多线程派发事件******------第五步-消息派发器用多线程的方式去处理每一条消息,每个方法用一个线程去执行
	}

	private int bytesLen = 0;

	private byte[] readDataPack() {
		if (bytesLen == 0) {
			if (bytesAvailable() < HEAD_LEN)
				return null;
			else
				bytesLen = readInt();
			if (bytesLen < 0) {
				App.dispatch(ModuleEvent.SOCKET_CLOSE, "Fatal Error:Data Length Must >= 0!");
				throw new RuntimeException("Fatal Error:Data Length Must >= 0!");
			}
			if (bytesLen == 0) {
				App.dispatch(ModuleEvent.SOCKET_DATA_PACKAGE_EMPTY);
				return null;
			}
		}

		if (bytesAvailable() < bytesLen) {
			App.dispatch(ModuleEvent.SOCKET_DATA_PACKAGE_NOT_ENOUGH);
			return null;
		}

		byte[] bs = new byte[bytesLen];
		bufferMark = _innerBuffer.position();
		_innerBuffer.position(_readIndex);
		_innerBuffer.get(bs, 0, bs.length);
		_innerBuffer.position(bufferMark);
		_readIndex += bytesLen;
		bytesLen = 0;
		// System.out.println("body:" + Hex.fromArray(bs));
		return bs;
	}

	private int bytesAvailable() {
		return _length - _readIndex;
	}

	private int bufferMark;

	public int readInt() {
		bufferMark = _innerBuffer.position();
		_innerBuffer.position(_readIndex);
		int a = _innerBuffer.getInt();
		_innerBuffer.position(bufferMark);
		_readIndex += 4;
		return a;
	}
	// ------END---私有方法区
}
