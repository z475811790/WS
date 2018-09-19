package xyzdlcore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.tree.DefaultAttribute;

import xyzdlcore.loader.LoaderBean;

/**
 * @author xYzDl
 * @date 2018年2月4日 上午11:18:49
 * @description 核心工具类
 */
public class CoreUtil {
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
