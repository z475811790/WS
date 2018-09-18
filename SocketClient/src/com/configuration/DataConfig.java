package com.configuration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultElement;

import com.core.loader.LoaderBean;
import com.core.util.AssetUtil;

/**
 * @author xYzDl
 * @date 2018年3月15日 下午8:21:48
 * @description 数据库配置数据类
 */
public class DataConfig {
	private static final String PREFIX = "com.conf.";
	// 可能会存在有的表记录太多，频繁遍历记录太多List性能很差，可能部分记录太多的考虑用Map
	// 使用的时候先从List中找，没有再从Map中找
	private Map<String, List<Object>> _confMap = new HashMap<>();
	private static DataConfig _singleton;

	public static DataConfig singleton() {
		if (_singleton == null)
			_singleton = new DataConfig();
		return _singleton;
	}

	public DataConfig() {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ExecutorService pool = Executors.newFixedThreadPool(25);

	private void init() throws Exception {
		// Map<String, List<Object>> confMap = new HashMap<>();
		Document xml = LoaderBean.getResXML(AssetUtil.DATA_CONFIG);
		Element _root = xml.getRootElement();
		parseStartTime = System.currentTimeMillis();
		List<DefaultElement> list = _root.elements();
		totalColumn = getTotalColumns(list);

		for (DefaultElement element : list) {
			Runnable t = new Runnable() {
				public void run() {
					try {
						parseElement(element);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			pool.execute(t);
		}
	}

	private int getTotalColumns(List<DefaultElement> list) {
		int c = 0;
		for (DefaultElement element : list) {
			c += element.attributes().size();
		}
		return c;
	}

	private void parseElement(DefaultElement element) throws Exception {
		// CommonUtil.printNow();
		// System.out.println(element.getName());
		String[][] table = parseTable(element);
		// CommonUtil.printNow();
		// System.out.println(element.getName() + " parse over");

		Class<?> klass = Class.forName(PREFIX + element.getName());
		Map<String, Field> typeMap = getClassDef(klass);

		List<Object> rows = new ArrayList<>();
		for (int i = 0; i < element.elements().size(); i++) {
			rows.add(klass.newInstance());
		}

		for (int c = 0; c < element.attributes().size(); c++) {
			DefaultAttribute att = (DefaultAttribute) element.attributes().get(c);
			Field field = typeMap.get(att.getName());

			int column = c;
			Runnable t = new Runnable() {
				public void run() {
					try {
						parseColumn(element.getName(), att.getName(), table, field, rows, element.elements().size(), column);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			pool.execute(t);
		}
		// CommonUtil.printNow();
		// System.out.println(element.getName() + " set over\n");

		putInMap(element.getName(), rows);
	}

	private void parseColumn(String className, String fieldName, String[][] table, Field field, List<Object> rows, int size, int c) throws Exception {
		switch (field.getType().getName()) {
		case "java.lang.Integer":
		case "int":
			for (int i = 0; i < size; i++) {
				// node = element.selectSingleNode("row[" + (i + 1) +
				// "]/p[" + (index + 1) + "]");
				field.set(rows.get(i), Integer.parseInt(table[i][c]));
			}
			break;
		case "java.lang.String":
			for (int i = 0; i < size; i++) {
				// node = element.selectSingleNode("row[" + (i + 1) +
				// "]/p[" + (index + 1) + "]");
				field.set(rows.get(i), table[i][c]);
			}
			break;
		case "java.lang.Boolean":
		case "boolean":
			for (int i = 0; i < size; i++) {
				// node = element.selectSingleNode("row[" + (i + 1) +
				// "]/p[" + (index + 1) + "]");
				field.set(rows.get(i), Boolean.parseBoolean(table[i][c]));
			}
			break;
		case "java.lang.Double":
		case "double":
			for (int i = 0; i < size; i++) {
				// node = element.selectSingleNode("row[" + (i + 1) +
				// "]/p[" + (index + 1) + "]");
				field.set(rows.get(i), Double.parseDouble(table[i][c]));
			}
			break;
		case "java.lang.Byte":
		case "byte":
			for (int i = 0; i < size; i++) {
				// node = element.selectSingleNode("row[" + (i + 1) +
				// "]/p[" + (index + 1) + "]");
				field.set(rows.get(i), Byte.parseByte(table[i][c]));
			}
			break;
		case "java.lang.Short":
		case "short":
			for (int i = 0; i < size; i++) {
				// node = element.selectSingleNode("row[" + (i + 1) +
				// "]/p[" + (index + 1) + "]");
				field.set(rows.get(i), Short.parseShort(table[i][c]));
			}
			break;
		case "java.lang.Long":
		case "long":
			for (int i = 0; i < size; i++) {
				// node = element.selectSingleNode("row[" + (i + 1) +
				// "]/p[" + (index + 1) + "]");
				field.set(rows.get(i), Long.parseLong(table[i][c]));
			}
			break;
		default:
			System.err.println("NOT FIND TARGET TYPE:" + field.getType().getName());
		}
		System.out.println(className + ":" + fieldName + "#解析消耗时间：" + (System.currentTimeMillis() - parseStartTime));
		isParseAllOver();
	}

	private synchronized void isParseAllOver() {
		curColumn++;
		if (curColumn == totalColumn) {
			System.out.println("all Over");
		}
	}

	private long parseStartTime = 0;
	private int totalColumn = 0;
	private int curColumn = 0;

	private synchronized void putInMap(String name, List<Object> rows) {
		_confMap.put(name, rows);
	}

	private Map<String, Field> getClassDef(Class<?> klass) {
		Map<String, Field> map = new HashMap<>();
		Field[] fields = klass.getDeclaredFields();
		for (Field field : fields) {
			map.put(field.getName(), field);
		}
		return map;
	}

	private String[][] parseTable(DefaultElement element) {
		String[][] table = new String[element.elements().size()][element.attributes().size()];
		StringBuilder sb = new StringBuilder();
		for (Object e : element.elements()) {
			sb.append(((DefaultElement) e).asXML());
		}
		String[] rows = sb.toString().replaceAll("<row>", "").replaceAll("<p>", "").split("</row>");
		String[] cols;
		for (int i = 0; i < element.elements().size(); i++) {
			cols = rows[i].split("</p>|<p/>");
			for (int j = 0; j < cols.length; j++) {
				table[i][j] = cols[j];
			}
		}
		return table;
	}
}
