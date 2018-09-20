package com.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.StringUtils;

/**
 * @author xYzDl
 * @date 2018年1月23日 下午9:23:54
 * @description 字符串工具类
 */
public class StringUtil {
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