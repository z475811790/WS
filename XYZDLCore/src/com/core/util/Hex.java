package com.core.util;

import com.core.ByteArray;

/**
 * @author xYzDl
 * @date 2018年1月26日 下午10:53:59
 * @description 16进制转换工具
 */
public class Hex {

	public static String fromArray(ByteArray bytes) {
		if (bytes == null)
			return "";
		String str = "";
		String s;
		for (byte b : bytes) {
			s = Integer.toHexString(Byte.toUnsignedInt(b));
			if (s.length() < 2)
				str += "0";
			str += s;
		}
		return str;
	}

	public static String fromArray(byte[] bytes) {
		if (bytes == null)
			return "";
		String str = "";
		String s;
		for (byte b : bytes) {
			s = Integer.toHexString(b & 0xff);
			if (s.length() < 2)
				str += "0";
			str += s;
		}
		return str;
	}

	public static ByteArray toArray(String hex) {
		ByteArray bytes = new ByteArray();
		if (hex == null)
			return bytes;
		if ((hex.length() & 1) == 1) {
			if (hex.startsWith("0"))
				hex = hex.substring(1);
			else
				hex = "0" + hex;
		}
		for (int i = 0; i < hex.length(); i += 2) {
			String by = hex.substring(i, i + 2);
			int temi = Integer.parseInt(by, 16);
			byte bbbb = (byte) temi;
			bytes.putByte(bbbb);
		}
		bytes.position(0);
		return bytes;
	}
}
