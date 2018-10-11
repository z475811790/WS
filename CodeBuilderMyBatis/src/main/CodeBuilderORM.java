package main;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xYzDl
 * @date 2018年7月8日 下午11:38:07
 * @description 将数据库配置自动生成类定义,导出数据为XML文件，以及生成Spring+MyBatis持久层框架
 * @version 1.0.0
 */
public class CodeBuilderORM {
	public static int TABLE_PRE_LEN;
	public static String DBTYPE;

	public static Map<String, String> tableDLLMap = new HashMap<String, String>();

	public CodeBuilderORM() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.initProperties();
		Connection conn = getConnection();
		try {
			initDLLMap(conn);
			TABLE_PRE_LEN = cfgV("table.prefix").length() - cfgV("table.prefix").split("_").length;
			DBTYPE = cfgV("db.type");
			String protoStr = StringUtil.readToString(cfgV("prototype.file"));
			String protoStrDaoInterface = StringUtil.readToString(cfgV("prototype.dao.interface.file"));
			String protoStrDaoMapper = StringUtil.readToString(cfgV("prototype.dao.mapper.file"));
			String protoStrService = StringUtil.readToString(cfgV("prototype.service.file"));
			String protoStrServiceImpl = StringUtil.readToString(cfgV("prototype.service.impl.file"));
			String protoStrEntityId = StringUtil.readToString(cfgV("prototype.entity.id.file"));
			if (protoStr == null)
				return;

			Map<String, String> tableIdMap = new HashMap<>();
			for (String key : tableDLLMap.keySet()) {
				String idField = genEntityFile(key, protoStr);
				tableIdMap.put(key, idField);
				genDaoInterfaceFile(key, protoStrDaoInterface);
				genDaoMapperFile(key, protoStrDaoMapper);
				genServiceFile(key, idField, protoStrService, protoStrServiceImpl);
			}
			genEntityIdDefFile(tableIdMap, protoStrEntityId);

			if (cfgV("is.out.data.file").toLowerCase().equals("true")) {
				genConfDataFile(conn);
			}
			System.out.println("DataBuilding Completed!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String cfgV(String k) {
		return Config.properties.getProperty(k);
	}

	public static void initDLLMap(Connection conn) throws Exception {
		Statement state = conn.createStatement();
		Statement state1 = conn.createStatement();
		String tableLike = "'" + cfgV("table.prefix") + "%'";
		String database = "'" + cfgV("database") + "'";
		if (cfgV("db.type").equals("mysql")) {
			ResultSet rs = state.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = "
					+ database + " AND table_type = 'base table' AND table_name LIKE " + tableLike);
			ResultSet rs1;
			while (rs.next()) {
				System.out.println(rs.getString("table_name"));
				rs1 = state1.executeQuery("show create table " + rs.getString("table_name"));
				rs1.next();
				System.out.println(rs1.getString("Create Table"));
				tableDLLMap.put(rs.getString("table_name").toLowerCase(), rs1.getString("Create Table"));// 数据表定义sql语句
			}
		} else if (cfgV("db.type").equals("sqlite")) {
			ResultSet rs = state.executeQuery("SELECT * FROM sqlite_master WHERE type='table' AND name LIKE 't_%'");
			while (rs.next()) {
				System.out.println(rs.getString("name"));
				tableDLLMap.put(rs.getString("name").toLowerCase(), rs.getString("sql"));// 数据表定义sql语句
			}
		}
	}

	public static Connection getConnection() {
		Connection c = null;
		try {
			Class.forName(cfgV("driver"));
			if (cfgV("db.type").equals("mysql")) {
				c = DriverManager.getConnection(cfgV("url") + cfgV("database"), cfgV("username"), cfgV("password"));
			} else if (cfgV("db.type").equals("sqlite")) {
				c = DriverManager.getConnection(cfgV("url") + cfgV("database"));
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");
		return c;
	}

	private static String genEntityFile(String tableName, String protoStr) {
		String className = StringUtil.underlineToCamel(tableName).substring(TABLE_PRE_LEN) + cfgV("class.suffix");
		ArrayList<FieldInfo> arrayList = Converter.parseDLL2Array(tableName, tableDLLMap.get(tableName), TABLE_PRE_LEN);

		String fieldStr = "";
		String arg = "";
		String assign = "";
		String gettersetter = "";
		String targetType, assignItem, XName, argItem, getX, setX;
		String idField = null;
		for (FieldInfo fieldInfo : arrayList) {
			String targetName = fieldInfo.property;
			if (fieldInfo.comment != null)
				fieldStr += "\t/**" + fieldInfo.comment + "*/\r\n";
			targetType = fieldInfo.javaType;
			fieldStr += String.format("\tprivate %s %s;\r\n", targetType, targetName);
			argItem = targetType + " " + targetName;
			arg += argItem + ", ";
			assignItem = String.format("\t\tthis.%s = %s;\r\n", targetName, targetName);
			assign += assignItem;
			XName = targetName.substring(0, 1).toUpperCase() + targetName.substring(1);
			getX = "get" + XName;
			setX = "set" + XName;
			gettersetter += String.format("\r\n\r\n\tpublic %s %s() {\r\n\t\treturn %s;\r\n\t}", targetType, getX,
					targetName);
			gettersetter += String.format("\r\n\r\n\tpublic void %s(%s) {\r\n%s\t}", setX, argItem, assignItem);

			// 主键必须是第一个字段
			if (idField == null)
				idField = XName;
		}
		if (arg.length() > 1)
			arg = arg.substring(0, arg.length() - 2);

		fieldStr = fieldStr.substring(0, fieldStr.length() - 2);

		Map<String, Object> labelMap = new HashMap<String, Object>();
		labelMap.put("classdesc", String.format(cfgV("class.des"), tableName));
		labelMap.put("packagename", cfgV("package.entity"));
		labelMap.put("import", cfgV("import"));
		labelMap.put("common", "");
		labelMap.put("classname", className);
		labelMap.put("extends", cfgV("extends"));
		labelMap.put("fields", fieldStr);
		labelMap.put("constructor", "\tpublic " + className + "() {\r\n\t}");
		labelMap.put("constructorarg", String.format("\tpublic %s(%s) {\r\n%s\t}", className, arg, assign));
		labelMap.put("gettersetter", gettersetter);

		StringUtil.contentToTxt(cfgV("out.dir.code") + cfgV("package.entity").replace('.', '\\') + "\\" + className
				+ cfgV("file.suffix"), StringUtil.formatLabel(protoStr, labelMap));
		return idField;
	}

	private static void genDaoInterfaceFile(String tableName, String protoStr) {
		String className = StringUtil.underlineToCamel(tableName).substring(TABLE_PRE_LEN) + cfgV("class.suffix");
		ArrayList<FieldInfo> arrayList = Converter.parseDLL2Array(tableName, tableDLLMap.get(tableName), TABLE_PRE_LEN);
		String javaName = "I" + className + "Dao";

		Map<String, Object> labelMap = new HashMap<String, Object>();
		labelMap.put("classdesc", String.format(cfgV("class.des"), tableName));
		labelMap.put("packagename", cfgV("package.dao"));
		labelMap.put("import", "import " + cfgV("package.entity") + "." + className + ";\r\n");
		labelMap.put("classname", javaName);
		labelMap.put("select", DaoGenerator.genSelect(tableName, className, arrayList));
		labelMap.put("insert", DaoGenerator.genInsert(tableName, className, arrayList));
		labelMap.put("update", DaoGenerator.genUpdate(tableName, className, arrayList));
		labelMap.put("delete", DaoGenerator.genDelete(tableName, className, arrayList));

		System.out.println(StringUtil.formatLabel(protoStr, labelMap));
		StringUtil.contentToTxt(
				cfgV("out.dir.code") + cfgV("package.dao").replace('.', '\\') + "\\" + javaName + cfgV("file.suffix"),
				StringUtil.formatLabel(protoStr, labelMap));
	}

	private static void genDaoMapperFile(String tableName, String protoStr) {
		String className = StringUtil.underlineToCamel(tableName).substring(TABLE_PRE_LEN) + cfgV("class.suffix");
		String javaName = "I" + className + "Dao";
		String namespace = cfgV("package.dao") + "." + javaName;
		String fileName = cfgV("out.dir.code") + cfgV("package.mapper").replace('.', '\\') + "\\" + javaName + ".xml";
		ArrayList<FieldInfo> arrayList = Converter.parseDLL2Array(tableName, tableDLLMap.get(tableName), TABLE_PRE_LEN);
		String entityMap = DaoGenerator.genXMLResultMap(tableName, className, arrayList);

		String customContent = null;
		String oldFile = StringUtil.readToString(fileName);
		if (oldFile != null) {
			Pattern pattern = Pattern.compile("<!--S -->([\\s\\S]*)<!--E -->");
			Matcher matcher = pattern.matcher(oldFile);
			if (matcher.find()) {
				customContent = matcher.group();
			}
		}

		Map<String, Object> labelMap = new HashMap<String, Object>();
		labelMap.put("namespace", namespace);
		labelMap.put("tablename", tableName);
		labelMap.put("entitymap", entityMap);
		if (customContent != null) {
			labelMap.put("<!--S --><!--E -->", customContent);
		} else {
			labelMap.put("<!--S --><!--E -->", "<!--S -->\r\n\t<!--E -->");
		}

		System.out.println(StringUtil.formatLabel(protoStr, labelMap));
		StringUtil.contentToTxt(fileName, StringUtil.formatLabel(protoStr, labelMap));
	}

	private static void genServiceFile(String tableName, String idField, String protoStr, String protoStrImpl) {
		String className = StringUtil.underlineToCamel(tableName).substring(TABLE_PRE_LEN) + cfgV("class.suffix");
		String javaName = className + "Service";
		String daoName = cfgV("package.dao") + ".I" + className + "Dao";
		String fileName = cfgV("out.dir.code") + cfgV("package.service").replace('.', '\\') + "\\" + javaName + ".java";
		String implFileName = cfgV("out.dir.code") + cfgV("package.service").replace('.', '\\') + "\\impl\\" + javaName
				+ "Impl.java";

		String customImport = null;
		String customMethod = null;

		Pattern pattern;
		Matcher matcher;
		String oldFile = StringUtil.readToString(implFileName);
		if (oldFile != null) {
			pattern = Pattern.compile("//S([\\s\\S]*)//E");
			matcher = pattern.matcher(oldFile);
			if (matcher.find()) {
				customImport = matcher.group();
			}
			pattern = Pattern.compile("/\\* S \\*/([\\s\\S]*)/\\* E \\*/");
			matcher = pattern.matcher(oldFile);
			if (matcher.find()) {
				customMethod = matcher.group();
			}
		}

		Map<String, Object> labelMap = new HashMap<String, Object>();
		labelMap.put("packagename", cfgV("package.service"));
		labelMap.put("interfacepackage", cfgV("package.service") + ".*");
		labelMap.put("implpackagename", cfgV("package.service") + ".impl");
		labelMap.put("javaname", javaName + "Impl");
		labelMap.put("interface", javaName);
		labelMap.put("entityclass", className);
		labelMap.put("daoclass", daoName);
		labelMap.put("idfield", idField);

		if (customImport != null) {
			labelMap.put("//S//E", customImport);
		} else {
			labelMap.put("//S//E", "//S\r\n//E");
		}
		if (customMethod != null) {
			labelMap.put("/* S *//* E */", customMethod);
		} else {
			labelMap.put("/* S *//* E */", "/* S */\r\n\t/* E */");
		}

		String implResultText = StringUtil.formatLabel(protoStrImpl, labelMap);

		pattern = Pattern.compile("public.*\\)[\\s+|\\{]");
		matcher = pattern.matcher(implResultText);
		String methodAnno = "";
		while (matcher.find()) {
			methodAnno += "\t" + matcher.group().trim() + ";\r\n\r\n";
		}

		labelMap.put("methods", methodAnno);

		String resultText = StringUtil.formatLabel(protoStr, labelMap);

		System.out.println(resultText);
		System.out.println(implResultText);
		StringUtil.contentToTxt(fileName, resultText);
		StringUtil.contentToTxt(implFileName, implResultText);
	}

	private static void genEntityIdDefFile(Map<String, String> idMap, String protoStr) {
		String[] strs = idMap.keySet().toArray(new String[idMap.keySet().size()]);
		List<String> list = Arrays.asList(strs);
		Collections.sort(list);// 按字母顺序排序

		String ifString = "\t\tif (entity instanceof %s)\r\n\t\t\treturn ((%s) entity).get%s();\r\n";
		String content = "";

		for (String key : list) {
			String val = idMap.get(key);
			String className = StringUtil.underlineToCamel(key).substring(TABLE_PRE_LEN) + cfgV("class.suffix");
			content += String.format(ifString, className, className, val);
		}

		Map<String, Object> labelMap = new HashMap<String, Object>();
		labelMap.put("packagename", cfgV("package.entity"));
		labelMap.put("classname", cfgV("entity.id.map.class"));
		labelMap.put("content", content);

		String resultText = StringUtil.formatLabel(protoStr, labelMap);
		System.out.println(resultText);
		StringUtil.contentToTxt(cfgV("out.dir.code") + cfgV("package.entity").replace('.', '\\') + "\\"
				+ cfgV("entity.id.map.class") + cfgV("file.suffix"), resultText);
	}

	private static void genConfDataFile(Connection conn) throws Exception {
		// 数据导出
		String outfile = cfgV("out.data.file");
		File dataFile = new File(outfile);
		PrintWriter pw;
		if (dataFile.exists()) {
			dataFile.delete();
		} else {
			dataFile.getParentFile().mkdirs();
		}
		dataFile.createNewFile();
		pw = new PrintWriter(dataFile);
		pw.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<data>");

		Statement state = conn.createStatement();
		ResultSet rs;
		for (String key : tableDLLMap.keySet()) {
			ArrayList<FieldInfo> arrayList = Converter.parseDLL2Array(key, tableDLLMap.get(key), TABLE_PRE_LEN);
			String selectSql = "SELECT * FROM " + key;
			rs = state.executeQuery(selectSql);

			String className = StringUtil.underlineToCamel(key).substring(TABLE_PRE_LEN) + cfgV("class.suffix");
			pw.append("\n<" + className);
			ArrayList<TypeConstant> typeArr = new ArrayList<>();
			for (FieldInfo fieldInfo : arrayList) {
				typeArr.add(TypeConstant.valueOf(fieldInfo.jdbcType));// 不使用字符可以更快地比较
				String tn = StringUtil.underlineToCamel(fieldInfo.property);
				pw.append(" " + tn + "=\"" + tn + "\"");
			}
			pw.append(">\n");

			while (rs.next()) {
				pw.append("\t<row>");
				int index = 0;
				for (FieldInfo fieldInfo : arrayList) {
					pw.append("<p>" + Converter.formatType(typeArr.get(index++), rs.getObject(fieldInfo.property))
							+ "</p>");
				}
				pw.append("</row>\n");
			}
			pw.append("</" + className + ">");
		}

		pw.append("\n</data>");
		pw.flush();
		pw.close();
	}
}

enum TypeConstant {
	INT, VARCHAR, DECIMAL, BIT, TINYINT, SMALLINT, DOUBLE, DATETIME, BIGINT
}