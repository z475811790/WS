package com.configuration;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.tree.DefaultAttribute;

import xyzdlcore.CoreUtil;
import xyzdlcore.loader.LoaderBean;
import xyzdlcore.util.AssetUtil;
import xyzdlcore.util.StringUtil;

/**
 * @author xYzDl
 * @date 2018年3月13日 下午10:21:41
 * @description 一般配置,根据应用具体情况所需要的配置
 */
public class GeneralConfig {
	private Element _root;
	private static GeneralConfig _singleton;

	public static GeneralConfig singleton() {
		if (_singleton == null)
			_singleton = new GeneralConfig();
		return _singleton;
	}

	private GeneralConfig() {
		Document xml = LoaderBean.getResXML(AssetUtil.GENERAL_CONFIG);
		_root = xml.getRootElement();
	}

	public static Object getFirstValue(String name) {
		if (StringUtil.isNullOrWhiteSpaceOrEmpty(name))
			return "" + name;
		List<DefaultAttribute> list = singleton()._root.selectNodes("//" + name + "[1]/@val");
		if (list.isEmpty())
			return "" + name;
		return CoreUtil.typeFormat(list.get(0).getValue());
	}
}
