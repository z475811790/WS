package xyzdlcore;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;

/**
 * @author xYzDl
 * @date 2018年1月26日 下午5:53:39
 * @description 核心字节数组类,对NIO-ByteBuffer进行一定的封装,为了减少重新申请内存的操作，
 *              尽量保证写入的最大数据量小于申请的内存量
 */
public class ByteArray implements Iterable<Byte> {
	private static final int DEFAULT_LENGTH = 64;// 内部字符数组初始大小
	private static final float GROWTH = 1.2f;// 数组增长因子
	private ByteBuffer _buffer;

	private int _length = 0;

	public ByteArray() {
		_buffer = ByteBuffer.allocate(DEFAULT_LENGTH);
	}

	public ByteArray(int initCapacity) {
		_buffer = ByteBuffer.allocate(initCapacity);
	}

	public ByteArray(byte[] bytes) {
		_buffer = ByteBuffer.allocate(bytes.length);
		putBytes(bytes);
	}

	/**
	 * @return 当前位置
	 */
	public int position() {
		return _buffer.position();
	}

	/**
	 * 设置当前位置,位置取小设置值为0,最大设置值为总长度-1
	 * 
	 * @param position
	 */
	public void position(int position) {
		if (position >= length())
			position = length() - 1;
		if (position < 0)
			position = 0;
		_buffer.position(position);
	}

	/**
	 * @return 数据总长度
	 */
	public int length() {
		return _length;
	}

	/**
	 * @return 当前位置到数据总长度的长度
	 */
	public int bytesAvailable() {
		if (position() >= length())
			return length();
		else
			return length() - position();
	}

	/**
	 * 读取一个整数
	 * 
	 * @return
	 */
	public int getInt() {
		long i = 0;
		for (int j = 0; j < 4; j++) {
			i <<= 8;
			i |= getByte();
		}
		return (int) i;
	}

	/**
	 * 写入一个整数
	 * 
	 * @param value
	 */
	public void putInt(int value) {
		long a = 0;
		byte b = 0;
		for (int i = 3; i >= 0; i--) {
			a = 0xffffffff;
			b = (byte) ((value & a) >> (i * 8));
			putByte(b);
		}
	}

	/**
	 * 读取一个byte数据,如果当前位置已经在末位后了,则将当前位置重置为0
	 * 
	 * @return
	 */
	public byte getByte() {
		if (position() >= _buffer.capacity())
			position(0);
		return _buffer.get();
	}

	/**
	 * 写入一个byte数据
	 * 
	 * @param value
	 */
	public void putByte(byte value) {
		if (position() == _buffer.capacity()) {
			ByteBuffer buffer = ByteBuffer.allocate((int) (length() * GROWTH));
			_buffer.position(0);
			buffer.put(_buffer);
			_buffer = buffer;
		}
		if (_length == _buffer.position())
			_length++;
		_buffer.put(value);
	}

	/**
	 * 将所有可获取的数据到写入bytes中
	 * 
	 * @param bytes
	 */
	public void getBytes(ByteArray bytes) {
		getBytes(bytes, bytesAvailable());
	}

	/**
	 * 从当前位置读取指定长度数据到bytes中
	 * 
	 * @param bytes
	 * @param length
	 */
	public void getBytes(ByteArray bytes, int length) {
		bytes.putBytes(getBytes(length));
	}

	/**
	 * 返回从当前位置开始指定长度的byte数组
	 * 
	 * @param length
	 * @return
	 */
	public byte[] getBytes(int length) {
		byte[] bs = new byte[length];
		if (position() >= length())
			_buffer.position(0);
		_buffer.get(bs);
		return bs;
	}

	/**
	 * 返回所有可获取的字节
	 * 
	 * @return
	 */
	public byte[] getAvailableBytes() {
		return getBytes(bytesAvailable());
	}

	/**
	 * 将整个bytes写入
	 * 
	 * @param bytes
	 */
	public void putBytes(ByteArray bytes) {
		putBytes(bytes, bytes.bytesAvailable());
	}

	/**
	 * 将bytes的指定长度的数据写入
	 * 
	 * @param bytes
	 * @param length
	 */
	public void putBytes(ByteArray bytes, int length) {
		putBytes(bytes.getBytes(length));
	}

	/**
	 * 将指定长度写入,length必须小于或等于bytes.length;
	 * 
	 * @param bytes
	 * @param length
	 */
	public void putBytes(byte[] bytes, int length) {
		byte[] bs = new byte[length];
		System.arraycopy(bytes, 0, bs, 0, length);
		putBytes(bs);
	}

	/**
	 * 将byte数组写入
	 * 
	 * @param bytes
	 */
	public void putBytes(byte[] bytes) {
		if (remain() < bytes.length) {
			ByteBuffer buffer = ByteBuffer.allocate(length() + bytes.length);
			buffer.put(_buffer.array(), 0, length());
			_buffer = buffer;
		}
		_buffer.put(bytes);
		if (length() < _buffer.position())
			_length = _buffer.position();
	}

	/**
	 * @return 未使用的剩余空间长度
	 */
	private int remain() {
		return _buffer.capacity() - position();
	}

	/**
	 * 注意对其操作线程不安全
	 * 
	 * @return
	 */
	public InputStream getInputStream() {
		class XInputStream extends InputStream {
			@Override
			public int read() throws IOException {
				if (position() >= _buffer.capacity())
					return -1;
				return _buffer.get();
			}
		}
		return new XInputStream();
	}

	/**
	 * 清空数据
	 */
	public void clear() {
		position(0);//ByteBuffer.allocate(DEFAULT_LENGTH);
		_length = 0;
	}

	@Override
	public Iterator<Byte> iterator() {

		class Iter implements Iterator<Byte> {
			private int index = 0;

			@Override
			public boolean hasNext() {
				if (index < length())
					return true;
				return false;
			}

			@Override
			public Byte next() {
				return _buffer.array()[index++];
			}

		}

		return new Iter();
	}
}
