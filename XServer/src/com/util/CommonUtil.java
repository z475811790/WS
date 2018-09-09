package com.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.StringUtils;

public class CommonUtil {
	public static void outObject(Object o) {
		if (o == null)
			return;
		System.out.println(toString(o, o.getClass()));
	}

	/**
	 * @MethodName : getString
	 * @Description : 获取类中所有属性及属性值
	 * @param o
	 *            操作对象
	 * @param c
	 *            操作类，用于获取类中的方法
	 * @return
	 */
	private static String toString(Object o, Class<?> c) {
		String result = c.getSimpleName() + ":";
		// 获取父类，判断是否为实体类
		if (c.getSuperclass().getName().indexOf("entity") >= 0) {
			result += "\n<" + toString(o, c.getSuperclass()) + ">,\n";
		}
		// 获取类中的所有定义字段
		Field[] fields = c.getDeclaredFields();
		// 循环遍历字段，获取字段对应的属性值
		for (Field field : fields) {
			// 如果不为空，设置可见性，然后返回
			field.setAccessible(true);
			try {
				// 设置字段可见，即可用get方法获取属性值。
				result += field.getName() + "=" + field.get(o) + ",\n";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (result.indexOf(",") >= 0)
			result = result.substring(0, result.length() - 2);
		return result;
	}

	public static Double null2Zero(Object d) {
		return d == null ? new Double(0) : Double.parseDouble(d.toString());
	}

	public static Integer null2Zero(Integer i) {
		return i == null ? new Integer(0) : i;
	}

	/**
	 * 判断是否为null或空白串或空的字符串
	 * 
	 * @param value
	 *            源数据
	 * @return
	 */
	public static Boolean isNullEmpty(Object value) {
		if (null == value)
			return true;
		if ("".equals(value))
			return true;
		if ("".equals(value.toString().trim()))
			return true;
		return false;
	}

	public static Map<String, String> string2Map(String string) {
		Map<String, String> map = new HashMap<>();
		if (StringUtils.isNullOrEmpty(string))
			return map;
		String[] ss = string.split(";");
		String[] kv;
		for (String s : ss) {
			kv = s.split(",");
			if (kv.length > 1)
				map.put(kv[0], kv[1]);
		}
		return map;
	}

	public static String map2String(Map map) {
		String str = "";
		if (map != null) {
			for (Object k : map.keySet()) {
				str += k + "," + map.get(k) + ";";
			}
		}
		if (str.length() > 1)
			str = str.substring(0, str.length() - 1);
		return str;
	}

	public static List<String> string2List(String string) {
		List<String> list = new ArrayList<>();
		if (StringUtils.isNullOrEmpty(string))
			return list;
		for (String s : string.split(";")) {
			list.add(s);
		}
		return list;
	}

	public static String list2String(List list) {
		String str = "";
		if (list != null) {
			Iterator<Object> it = list.iterator();
			while (it.hasNext()) {
				str += it.next() + ";";
			}
		}
		if (str.length() > 1)
			str = str.substring(0, str.length() - 1);
		return str;
	}
}
