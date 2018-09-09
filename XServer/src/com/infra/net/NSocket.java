package com.infra.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.core.App;
import com.core.crypt.AESCrypt;
import com.core.util.XUtil;
import com.infra.event.ModuleEvent;

/**
 * @author xYzDl
 * @date 2018年9月8日 下午7:59:03
 * @description 非阻塞套接字
 */
public class NSocket {
	public static final int HEAD_LEN = 4; // 数据包头,用来表示实际数据的长度,数据包去掉包头才是实际数据长度
	public static final int BODY_MAX_LEN = 100; // 8192
												// 数据包体，最大长度8k，超出认为是异常包，断开链接

	public int len = 0;
	public int state = 0;
	public AESCrypt aes;

	public NSocket(SocketChannel socketChannel) {
		_socketChannel = socketChannel;
		_innerBuffer = ByteBuffer.allocate(HEAD_LEN + BODY_MAX_LEN);
		aes = new AESCrypt();
	}

	private SocketChannel _socketChannel;
	private ByteBuffer _innerBuffer;
	private int _readIndex = 0;
	private int _length = 0;
	private BlockingQueue<byte[]> outQueue = new LinkedBlockingQueue<>();

	public long lastRecordTime = 0;// 上一次接收消息数据的时间戳

	public void readChannel() throws IOException {
		int len = _socketChannel.read(_innerBuffer);
		lastRecordTime = System.currentTimeMillis();
		if (len < 0) {
			// _dispatcher.dispatch(EventType.CLOSE,
			// XCommonUtil.getSocketId(_socket));
		} else {
			_length += len;
		}
		// if(len>0){
		// System.out.println("len:"+len);
		// }
		if (_innerBuffer.remaining() == 0 && _readIndex > 0) {// 缓冲区填充满了且有数据已经被读取了
			_length -= _readIndex;
			_innerBuffer.position(_readIndex);
			_innerBuffer.compact();// 移动数据
			_readIndex = 0;
		}
	}

	public int bytesAvailable() {
		return _length - _readIndex;
	}

	private int mark;

	public int readHeadLen() {
		mark = _innerBuffer.position();
		_innerBuffer.position(_readIndex);
		int a = _innerBuffer.getInt();
		_innerBuffer.position(mark);
		_readIndex += 4;
		return a;
	}

	public byte[] readBytes(int length) {
		if (bytesAvailable() < length)
			return null;
		byte[] bs = new byte[length];
		mark = _innerBuffer.position();
		_innerBuffer.position(_readIndex);
		_innerBuffer.get(bs, 0, bs.length);
		_innerBuffer.position(mark);
		_readIndex += length;
		return bs;
	}

	public byte[] readDataPack() {
		int bytesLen = readDataPackHead();
		if (bytesLen <= 0) {
			App.dispatch(ModuleEvent.SOCKET_DATA_PACKAGE_EMPTY);
			return null;
		}
		return readDataPackBody(bytesLen);
	}

	/**
	 * 读取数据包头
	 * 
	 * @return
	 */
	public int readDataPackHead() {
		if (bytesAvailable() < 4)
			return -1;
		int bytesLen = readHeadLen();
		if (bytesLen < 0)
			return -1;
		// TODO
		// _dispatcher.dispatch(EventType.CLOSE,
		// XCommonUtil.getSocketId(_socket));
		return bytesLen;
	}

	/**
	 * 读取数据包体
	 * 
	 * @return
	 */
	public byte[] readDataPackBody(int bytesLen) {
		if (bytesAvailable() < bytesLen) {
			App.dispatch(ModuleEvent.SOCKET_DATA_PACKAGE_NOT_ENOUGH);
			return null;
		}
		return readBytes(bytesLen);
	}

	public void writeInt(int value) {
		outQueue.offer(XUtil.intToBytes(value));
	}

	public void writeDataPack(byte[] bs) {
		if (bs == null || bs.length == 0)
			return;
		if (bs.length > BODY_MAX_LEN) {
			// TODO:派发数据包过大消息
			return;
		}
		if (_socketChannel != null && _socketChannel.isConnected()) {
			outQueue.offer(XUtil.intToBytes(bs.length));
			outQueue.offer(bs);
		} else {
			System.out.println("Socket is Disconnected");
		}
	}

	public void writeBytes(byte[] bs) {
		outQueue.offer(bs);
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
}
