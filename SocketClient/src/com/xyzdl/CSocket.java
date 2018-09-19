package com.xyzdl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.event.ModuleEvent;
import com.google.protobuf.Message;
import com.message.Message.MessageEnum.MessageId;

import xyzdlcore.App;
import xyzdlcore.ByteArray;
import xyzdlcore.Console;
import xyzdlcore.Dispatcher;
import xyzdlcore.XTimer;
import xyzdlcore.crypt.AESCrypt;
import xyzdlcore.crypt.RSACrypt;
import xyzdlcore.event.IEventHandler;
import xyzdlcore.event.XEvent;
import xyzdlcore.interfaces.IFunctionNoneArgs;
import xyzdlcore.util.Hex;
import xyzdlcore.util.XUtil;

/**
 * @author xYzDl
 * @date 2018年3月1日 下午10:43:28
 * @description 通信核心：用不同的线程进行读写处理
 */
public class CSocket {
	public static final int HEAD_LEN = 4; // 数据包头,用来表示实际数据的长度,数据包去掉包头才是实际数据长度
	public static final int BODY_MAX_LEN = 8192; // 8192
													// 数据包体，最大长度8k，超出认为是异常包，断开链接

	private static final int DETERMINE_VERSION = 0; // 协议版本验证状态
	private static final int RECEIVE_CHALLENGE = 1; // 接收验证码状态
	private static final int RECEIVE_SEC_KEY = 2; // 接收密钥状态
	private static final int NORMAL = 3; // 正常通信状态

	private int _currState = DETERMINE_VERSION; // 初始状态
	private Socket _socket = new Socket();// 套接字实例
	private AESCrypt _aesCrypt = new AESCrypt();// AES加密对象
	private RSACrypt _rsaCrypt = new RSACrypt();// RSA加密对象
	private IFunctionNoneArgs[] _stateHandlers = new IFunctionNoneArgs[4];// 状态处理方法字典
	private Dispatcher _dispatcher = new Dispatcher(); // 服务器消息事件派发器

	private byte[] LOCK_IN = new byte[0];
	private byte[] LOCK_OUT = new byte[0];

	private InputStream _inputStream;
	private OutputStream _outputStream;
	private ConcurrentLinkedQueue<byte[]> outQueue = new ConcurrentLinkedQueue<>();
	private boolean readMark = false;// 读取标记,用来防止太频繁产生SOCKET_DATA事件
	private boolean runMark = true;// 线程停止标记,因为stop方法不建议使用,所以采用标记停止

	public CSocket() {
		_stateHandlers[DETERMINE_VERSION] = this::receiveVersion;
		_stateHandlers[RECEIVE_CHALLENGE] = this::receiveChallenge;
		_stateHandlers[RECEIVE_SEC_KEY] = this::receiveSecretKey;
		_stateHandlers[NORMAL] = this::readMsg;

		Console.addMsg("Start to Connect...");
		asyncConnect(Config.host, Config.port);
	}

	/**
	 * 异步连接
	 * 
	 * @param host
	 * @param port
	 */
	private void asyncConnect(String host, int port) {
		XTimer.add(new IEventHandler() {
			@Override
			public void execute(XEvent xEvent) throws Exception {
				// 创建RSA密钥
				byte[] nbs = Hex.toArray("d65e6d75aeda689cfab5efa15e134a7fa416765c568940ec93ab51c88be3581561ed258824fb1f366324cb6b412416452972f23737a816933fd3f156c00a0d9d").getAvailableBytes();
				byte[] dbs = Hex.toArray("b38a28b11cbe2e49f3acf74336907f9fc1e5524269f3d09d93dc33c5fc6b6f7407b141a12d1c2c6169d1fcb090a63072ad742d6eba45b326dffdd32b6e361281").getAvailableBytes();

				_rsaCrypt = new RSACrypt(nbs, dbs);
				// _rsaCrypt.generateRandomKey();

				// 建立连接
				try {
					_socket.connect(new InetSocketAddress(host, port), 10000);
					_inputStream = _socket.getInputStream();
					_outputStream = _socket.getOutputStream();
					new InputThread().start();
					new OutputThread().start();
					Console.addMsg("Connect to " + Config.host + ":" + Config.port);
				} catch (IOException e) {
					e.printStackTrace();
					App.dispatch(ModuleEvent.SOCKET_CLOSE, "IO Error:" + e);
				}
			}
		}, 0, true, null, null);
	}

	// ------START-事件注册区
	// ------END---事件注册区
	// ------START-公共方法区
	private ByteArray transitBytes = new ByteArray(HEAD_LEN + BODY_MAX_LEN);

	synchronized public void sendProtoMessage(Message msg) {
		try {
			transitBytes.clear();
			transitBytes.putInt(MessageId.valueOf(msg.getClass().getSimpleName()).getNumber());
			transitBytes.putBytes(msg.toByteArray());
			if (_currState != NORMAL)
				return;
			byte[] bs = transitBytes.getAvailableBytes();
			// System.out.println(Hex.fromArray(bs));
			bs = _aesCrypt.encryptBytes(bs);
			// System.out.println("en:" + Hex.fromArray(bs));
			writeDataPack(bs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		Console.addMsg("Disconnected");
		dispose();
	}

	/**
	 * 添加消息监听
	 * 
	 * @param msgId
	 * @param listener
	 */
	public void addSocketMsgListener(int msgId, IEventHandler listener) {
		_dispatcher.add(msgId, listener, null);
	}

	/**
	 * 实际应用中不会删除监听的消息
	 * 
	 * @param msgId
	 */
	public void removeSocketMsgListener(int msgId) {
		// _dispatcher.remove(msgId, msgId);
	}

	public void dispose() {
		try {
			runMark = false;
			synchronized (LOCK_OUT) {
				LOCK_OUT.notify();
			}
			_socket.close();
			_inputStream.close();
			_outputStream.close();
			_dispatcher.clear();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			_socket = null;
			_inputStream = null;
			_outputStream = null;
		}
	}

	public void addEventListener(Object eventType, IEventHandler listener) {
		_dispatcher.add(eventType, listener, eventType);
	}

	public void removeEventListener(Object eventType) {
		_dispatcher.remove(eventType, eventType);
	}

	/*
	 * 返回127.0.0.1:8080形式字符串
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (_socket != null)
			return _socket.getInetAddress().getHostAddress() + ":" + _socket.getPort();
		return "0:0";
	}

	public String getSocketId() {
		return toString();
	}

	// ------END---公共方法区
	// ------START-私有方法区
	private void receiveVersion() {
		if (bytesAvailable() < HEAD_LEN)
			return;
		int version = readInt();
		Console.addMsg("Server Communication Protocol Version is " + version);
		if (Config.COMMUNICATION_PROTOCOL_VERSION > version) {
			Console.addMsg("Client Communication Protocol Version is Newer than Server's!");
		}
		outQueue.offer(XUtil.intToBytes(version));
		flush();
		_currState = RECEIVE_CHALLENGE;
	}

	private void receiveChallenge() {
		if (bytesAvailable() < HEAD_LEN)
			return;
		int challenge = readInt();
		byte[] n = _rsaCrypt.n();
		Console.addMsg("Server Challenge is " + challenge);
		Console.addMsg("c-n:" + Hex.fromArray(n));
		ByteArray cAndn = new ByteArray(HEAD_LEN + n.length);
		cAndn.putInt(challenge * 2);// 验证算法,后期可以修改为复杂一些的
		cAndn.putBytes(n);
		writeDataPack(cAndn.getAvailableBytes());
		_currState = RECEIVE_SEC_KEY;
	}

	private void receiveSecretKey() {
		byte[] bs = readDataPack();
		if (bs == null || bs.length == 0)
			return;
		Console.addMsg("beforeDe:" + Hex.fromArray(bs));
		bs = _rsaCrypt.decrypt(bs);
		_aesCrypt = new AESCrypt();
		_aesCrypt.setSecretKey(bs);
		Console.addMsg("c-SK:" + Hex.fromArray(bs));
		// System.out.println(Hex.fromArray(_aesCrypt.getSecretKey()));
		_currState = NORMAL;

		App.dispatch(ModuleEvent.SOCKET_STATE_TO_NORMAL);
	}

	private int bytesAvailable() {
		try {
			return _inputStream.available();
		} catch (IOException e) {
			e.printStackTrace();
			App.dispatch(ModuleEvent.SOCKET_CLOSE, "IO Error:" + e);
		}
		return 0;
	}

	private void readMsg() {
		byte[] bs = readDataPack();
		if (bs == null || bs.length == 0)
			return;
		bs = _aesCrypt.decryptBytes(bs);
		int msgId = XUtil.bytesToInt(bs);
		// System.out.println("msgId:"+msgId);
		byte[] msgBs = new byte[bs.length - HEAD_LEN];
		System.arraycopy(bs, HEAD_LEN, msgBs, 0, bs.length - HEAD_LEN);
		_dispatcher.dispatch(msgId, msgBs);
	}

	byte[] intBytes = new byte[4];// 因为不存在多线程访问该下面方法，所以可以定义为全局变量

	/**
	 * 使用前一定要先判断是否可读取到一个int值
	 * 
	 * @return
	 */
	private int readInt() {
		int value = 0;
		synchronized (LOCK_IN) {
			try {
				_inputStream.read(intBytes);
				value = XUtil.bytesToInt(intBytes);
				readMark = true;
				return value;
			} catch (Exception e) {
				e.printStackTrace();
				onSocketClose();
			}
		}
		return value;
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

		byte[] bs = readBytes(bytesLen);
		bytesLen = 0;
		// System.out.println("body:" + Hex.fromArray(bs));
		return bs;
	}

	private byte[] readBytes(int length) {
		byte[] bs = new byte[length];
		int a;
		try {
			a = _inputStream.read(bs);
			if (a != length)
				throw new Exception("EOF OR SOCKET CLOSED");
		} catch (Exception e) {
			e.printStackTrace();
			onSocketClose();
		}
		return bs;
	}

	private void writeDataPack(byte[] bs) {
		if (bs == null || bs.length == 0)
			return;
		if (!_socket.isClosed() && _socket.isConnected()) {
			outQueue.offer(XUtil.intToBytes(bs.length));
			outQueue.offer(bs);
			flush();
		} else {
			System.out.println("Socket is Disconnected");
		}
	}

	private void flush() {
		synchronized (LOCK_OUT) {
			LOCK_OUT.notify();
		}
	}

	private void onSocketClose() {
		if (_currState == DETERMINE_VERSION) {
			App.dispatch(ModuleEvent.SOCKET_CLOSE, "Server Socket is Closed");
			return;
		}
		if (_currState == RECEIVE_CHALLENGE) {
			App.dispatch(ModuleEvent.SOCKET_CLOSE, "Communication Protocol Version is Newer. Socket is Closed");
			return;
		}
		if (_currState == RECEIVE_SEC_KEY) {
			App.dispatch(ModuleEvent.SOCKET_CLOSE, "Challenge Verifying is failed");
			return;
		}
		App.dispatch(ModuleEvent.SOCKET_CLOSE, "Server Socket is Closed");
	}

	// ------END---私有方法区

	class InputThread extends Thread {
		private static final int DATA_INTERVAL = 50;// 数据到达事件发生间隔
		private static final int READ_INTERVAL = 500;// 防止过于频繁产生事件的间隔时间值
		public long lastRecordTime = 0;// 上一次接收消息数据的时间戳

		public InputThread() {
			super(getSocketId() + "-Input");
			setDaemon(true);
		}

		@Override
		public void run() {
			try {
				while (runMark) {
					if ((bytesAvailable() == 0) || (!readMark && System.currentTimeMillis() - lastRecordTime < READ_INTERVAL)) {
						// 如果缓存数据未被读取过,有数据在缓存中,上次读取记录间隔时间小于READ_INTERVAL毫秒则不产生事件
						Thread.sleep(200);
						continue;
					}
					if (System.currentTimeMillis() - lastRecordTime > DATA_INTERVAL) {
						synchronized (LOCK_IN) {
							if (bytesAvailable() > 0 && System.currentTimeMillis() - lastRecordTime > DATA_INTERVAL) {
								_stateHandlers[_currState].execute();
								lastRecordTime = System.currentTimeMillis();
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				onSocketClose();
			}
		}
	}

	class OutputThread extends Thread {

		public OutputThread() {
			super(getSocketId() + "-Output");
			setDaemon(true);
		}

		@Override
		public void run() {
			try {
				byte[] bs;
				while (runMark) {
					synchronized (LOCK_OUT) {
						bs = outQueue.poll();
						if (bs == null) {
							_outputStream.flush();
							LOCK_OUT.wait();
						} else {
							_outputStream.write(bs);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				onSocketClose();
			}
		}
	}
}
