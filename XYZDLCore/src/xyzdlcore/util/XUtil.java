package xyzdlcore.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.net.Socket;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.tree.DefaultAttribute;

import xyzdlcore.loader.LoaderBean;

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

	/**
	 * 配置解析
	 * 
	 * @param klass
	 *            配置类
	 * @param confUrl
	 *            配置路径
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void parseConf(Class klass, String confUrl) {
		try {
			Document xml = LoaderBean.getResXML(confUrl);
			Element root = xml.getRootElement();
			for (Field f : klass.getDeclaredFields()) {
				if (f.getModifiers() == Modifier.PUBLIC + Modifier.STATIC) {
					List<DefaultAttribute> list = root.selectNodes("//" + f.getName() + "[1]/@val");
					f.set(klass, typeFormat(list.get(0).getValue()));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("未在配置中找到对应属性值");
			e.printStackTrace();
		}
	}

	/**
	 * @param string
	 *            将字符串转换成对应类型的值
	 * @return
	 */
	public static Object typeFormat(String string) {
		if (string.matches("^(\\-|\\+)?\\d+$")) {
			return Integer.parseInt(string);
		} else if (string.matches("^(\\-|\\+)?\\d+(\\.\\d+)?$")) {
			return Double.parseDouble(string);
		} else if (string.matches("^0[xX][A-Fa-f0-9]+$")) {
			return Double.parseDouble(string);
		} else if ("true".equals(string)) {
			return true;
		} else if ("false".equals(string)) {
			return false;
		} else {
			return string;
		}
	}
}
