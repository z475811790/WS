package main;

import java.util.ArrayList;

public class DaoGenerator {
	public static String genSelect(String tableName, String className, ArrayList<FieldInfo> arrayList) {
		String select = "\t@Select(\"SELECT * FROM %s WHERE %s=#{%s}\")\r\n";
		String method = "\tpublic %s selectById(int %s);";
		String results = "\t@Results({\r\n%s\r\n\t\t\t})\r\n";
		String result = "\t\t\t@Result(%s),\r\n";
		String item = "property = \"%s\", column = \"%s\", javaType = %s, jdbcType = %s%s";
		String content = "";
		int index = 0;
		for (FieldInfo fieldInfo : arrayList) {
			String str = String.format(item, fieldInfo.property, fieldInfo.column, formatJavaType(fieldInfo.javaType), "JdbcType." + formatJdbcType(fieldInfo.jdbcType), fieldInfo.typeHandler == null ? "" : ", typeHandler =" + fieldInfo.typeHandler + ".class");
			if (index++ == 0) {
				str = "id = true, " + str;
				select = String.format(select, tableName, fieldInfo.column, fieldInfo.property);
				method = String.format(method, className, fieldInfo.property);
			}
			content += String.format(result, str);
		}
		if (content.length() > 3)
			content = content.substring(0, content.length() - 3);
		content = String.format(results, content);
		return select + content + method;
	}

	public static String genXMLResultMap(String tableName, String className, ArrayList<FieldInfo> arrayList) {
		String resultMap = "<resultMap id=\"entityMap\" type=\"%s\">\r\n%s\t</resultMap>";
		String result = "\t\t<%s property=\"%s\" column=\"%s\" javaType=\"%s\" jdbcType=\"%s\" %s/>\r\n";
		String content = "";
		int index = 0;
		for (FieldInfo fieldInfo : arrayList) {
			String str;
			if (index++ == 0) {
				str = String.format(result, "id", fieldInfo.property, fieldInfo.column, formatXMLJavaType(fieldInfo.javaType), formatJdbcType(fieldInfo.jdbcType), fieldInfo.typeHandler == null ? "" : " typeHandler=\"" + fieldInfo.typeHandler + "\"");
			} else {
				str = String.format(result, "result", fieldInfo.property, fieldInfo.column, formatXMLJavaType(fieldInfo.javaType), formatJdbcType(fieldInfo.jdbcType), fieldInfo.typeHandler == null ? "" : " typeHandler=\"" + fieldInfo.typeHandler + "\"");
			}
			content += str;
		}
		content = String.format(resultMap, className, content);
		return content;
	}

	public static String genInsert(String tableName, String className, ArrayList<FieldInfo> arrayList) {
		String insert = "\t@Insert(\"INSERT INTO %s (%s) VALUES (%s)\")\r\n";
		String options = "\t@Options(useGeneratedKeys = true, keyProperty = \"%s\")\r\n";
		String method = "\tpublic int insert(%s %s);";
		String names = "";
		String v = "#{%s%s}, ";
		String values = "";
		int index = 0;
		for (FieldInfo fieldInfo : arrayList) {
			if (index++ == 0) {
				options = String.format(options, fieldInfo.property);
//				continue;
			}
			names += fieldInfo.column + ", ";
			values += String.format(v, fieldInfo.property, fieldInfo.typeHandler == null ? "" : ", typeHandler=" + fieldInfo.typeHandler);
		}
		if (names.length() > 2)
			names = names.substring(0, names.length() - 2);
		if (values.length() > 2)
			values = values.substring(0, values.length() - 2);
		insert = String.format(insert, tableName, names, values);
		method = String.format(method, className, className.substring(0, 1).toLowerCase() + className.substring(1));

		return insert + options + method;
	}

	public static String genUpdate(String tableName, String className, ArrayList<FieldInfo> arrayList) {
		String update = "\t@Update(\"UPDATE %s SET %s WHERE %s\")\r\n";
		String method = "\tpublic int update(%s %s);";
		String sets = "";
		String set = "%s=#{%s%s}, ";
		String where = "%s=#{%s}";
		int index = 0;
		for (FieldInfo fieldInfo : arrayList) {
			if (index++ == 0) {
				where = String.format(where, fieldInfo.column, fieldInfo.property);
				continue;
			}
			sets += String.format(set, fieldInfo.column, fieldInfo.property, fieldInfo.typeHandler == null ? "" : ", typeHandler=" + fieldInfo.typeHandler);
		}
		if (sets.length() > 2)
			sets = sets.substring(0, sets.length() - 2);
		update = String.format(update, tableName, sets, where);
		method = String.format(method, className, className.substring(0, 1).toLowerCase() + className.substring(1));

		return update + method;
	}

	public static String genDelete(String tableName, String className, ArrayList<FieldInfo> arrayList) {
		String delete = "\t@Delete(\"DELETE FROM %s WHERE %s\")\r\n";
		String method = "\tpublic int deleteById(int %s);";
		String where = "%s=#{%s}";
		int index = 0;
		for (FieldInfo fieldInfo : arrayList) {
			if (index++ == 0) {
				where = String.format(where, fieldInfo.column, fieldInfo.property);
				method = String.format(method, fieldInfo.property);
				break;
			}
		}
		delete = String.format(delete, tableName, where);
		return delete + method;
	}

	private static String formatJavaType(String javaType) {
		switch (javaType) {
		case "int":
		case "Integer":
			return "Integer.class";
		case "String":
			return "String.class";
		case "boolean":
		case "Boolean":
			return "Boolean.class";
		case "byte":
		case "Byte":
			return "Byte.class";
		case "short":
		case "Short":
			return "Short.class";
		case "double":
		case "Double":
			return "Double.class";
		case "long":
		case "Long":
			return "Long.class";
		case "Date":
			return "Date.class";
		case "Map<String, String>":
			return "Map.class";
		case "List<String>":
			return "List.class";
		}
		System.err.println("NOT FIND JAVATYPE CLASS:" + javaType);
		return javaType;
	}

	private static String formatXMLJavaType(String javaType) {
		switch (javaType) {
		case "int":
		case "Integer":
			return "java.lang.Integer";
		case "String":
			return "java.lang.String";
		case "boolean":
		case "Boolean":
			return "java.lang.Boolean";
		case "byte":
		case "Byte":
			return "java.lang.Byte";
		case "short":
		case "Short":
			return "java.lang.Short";
		case "double":
		case "Double":
			return "java.lang.Double";
		case "long":
		case "Long":
			return "java.lang.Long";
		case "Date":
			return "java.util.Date";
		case "Map<String, String>":
			return "java.util.Map";
		case "List<String>":
			return "java.util.List";
		}
		System.err.println("NOT FIND JAVATYPE CLASS:" + javaType);
		return javaType;
	}

	private static String formatJdbcType(String jdbcType) {
		switch (jdbcType) {
		case "INT":
			return "INTEGER";
		}
		return jdbcType;
	}
}
