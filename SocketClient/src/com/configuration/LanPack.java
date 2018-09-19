package com.configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import xyzdlcore.constant.CommonConstant;
import xyzdlcore.loader.LoaderBean;
import xyzdlcore.util.AssetUtil;

/**
 * @author xYzDl
 * @date 2018年3月13日 下午11:17:06
 * @description 语言包
 */
public class LanPack {
	private Map<String, Object> _map = new HashMap<>();
	private static LanPack _singleton;

	public static LanPack singleton() {
		if (_singleton == null)
			_singleton = new LanPack();
		return _singleton;
	}

	private LanPack() {
		Document xml = LoaderBean.getResXML(AssetUtil.LANGUAGE_PACKAGE);
		Element _root = xml.getRootElement();
		List<Element> groups = _root.elements();
		for (Element g : groups) {
			List<Element> items = g.elements();
			Map<String, Object> gMap = new HashMap<>();
			for (Element i : items) {
				gMap.put(i.attributeValue("k"), i.attributeValue("v"));
			}
			_map.put(g.getName(), gMap);
		}
	}

	public String getValueByGroupKey(String group, String key) {
		Map map = (Map) _map.get(group);
		if (map == null || map.get(key) == null)
			return group + ":" + key;
		return map.get(key).toString();
	}

	public static String getVal(String group, String key) {
		return singleton().getValueByGroupKey(group, key);
	}

	public static String getLanVal(String key) {
		return singleton().getValueByGroupKey(CommonConstant.LANGUAGE_CONSTANT_GROUP, key);
	}
}
