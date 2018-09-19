package xyzdlcore.util;

import java.math.BigInteger;
import java.net.Socket;

/**
 * @author xYzDl
 * @date 2018年3月5日 下午11:57:23
 * @description 公用工具
 */
public class XUtil {
	public static int bytesToInt(byte[] bs) {
		return (bs[3] & 0xFF) | //
				(bs[2] & 0xFF) << 8 | //
				(bs[1] & 0xFF) << 16 | //
				(bs[0] & 0xFF) << 24;
	}

	public static byte[] intToBytes(int value) {
		byte[] bs = new byte[4];
		bs[0] = (byte) (value >> 24 & 0xFF);
		bs[1] = (byte) (value >> 16 & 0xFF);
		bs[2] = (byte) (value >> 8 & 0xFF);
		bs[3] = (byte) (value & 0xFF);
		return bs;
	}

	public static String getSocketId(Socket socket) {
		return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
	}

	public static BigInteger bytesToBigInteger(byte[] bs) {
		if ((byte) bs[0] < 0) {
			// 非常需要注意的一点就是第一个字节的第一位为1时，转换成会BigInteger时当作负数处理。
			byte[] newbs = new byte[bs.length + 1];
			System.arraycopy(new byte[1], 0, newbs, 0, 1);
			System.arraycopy(bs, 0, newbs, 1, bs.length);
			bs = newbs;
		}
		return new BigInteger(bs);
	}

}
