package main;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter {
	public static Properties typeProperties = new Properties();

	public static String formatType(TypeConstant type, Object value) {
		switch (type) {
		case INT:
		case DECIMAL:
		case TINYINT:
		case BIT:
		case SMALLINT:
		case DOUBLE:
		case BIGINT:
			if (value == null)
				return "0";
			return value.toString();
		case VARCHAR:
			if (value == null)
				return "";
			String v = ((String) value);
			if (v.indexOf('<') >= 0) {
				return v.replaceAll("<", "[}").replaceAll(">", "{]");
			}
			return v;
		case DATETIME:
			return ((Timestamp) value).getTime() + "";
		default:
			return value.toString();
		}
	}

	public static ArrayList<FieldInfo> parseDLL2Array(String tableName, String ddlSql, int tableLen) {
		String dbType = CodeBuilderMyBatis.DBTYPE;
		switch (dbType) {
		case "mysql":
			return parseDLL4MySql(tableName, ddlSql, tableLen);
		}
		System.err.println("CAN'T PARSE DLL FOR DBTYPE:" + dbType);
		return new ArrayList<>();
	}

	private static ArrayList<FieldInfo> parseDLL4MySql(String tableName, String ddlSql, int tableLen) {
		ddlSql = ddlSql.replaceAll("\r|\n*", "");
		String className = StringUtil.underlineToCamel(tableName).substring(tableLen);

		// sqlite
		// "CREATE TABLE t_user (id INTEGER PRIMARY KEY ASC AUTOINCREMENT,
		// account TEXT (20) NOT NULL, password TEXT (20) NOT NULL, isAdmin
		// BOOLEAN NOT NULL, createDate REAL NOT NULL)";

		// mysql
		// CREATE TABLE `t_item_info` (
		// `id` int(11) NOT NULL AUTO_INCREMENT,
		// `name` varchar(255) DEFAULT NULL,
		// `des` varchar(255) DEFAULT NULL,
		// `createTime` decimal(10,0) DEFAULT NULL,
		// `isBind` bit(1) DEFAULT b'0',
		// PRIMARY KEY (`id`)
		// ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8
		Pattern pattern = Pattern.compile("(?<=\\().*(?=\\))");
		Matcher matcher = pattern.matcher(ddlSql);
		ArrayList<FieldInfo> arrayList = new ArrayList<>();
		// 每种数据库的表定义语法不太一样，需要分别处理
		if (matcher.find()) {
			System.out.println(matcher.group());
			String matheResult = matcher.group().replaceAll(",\\d+", "");
			pattern = Pattern.compile("COMMENT '.*");
			String[] splitArr = new String[0];

			splitArr = matheResult.split(",\\s\\s`");

			for (String field : splitArr) {
				matcher = pattern.matcher(field);
				String comment = null;
				if (matcher.find()) {
					comment = matcher.group();
					comment = comment.replace("COMMENT '", "").replace("'", "").replaceAll(",  PRIMARY KEY.*", "");
				}
				field = field.trim();
				String[] pi = field.split(" ");
				FieldInfo result = new FieldInfo();
				result.column = pi[0].trim().toLowerCase().replace("\"", "").replaceAll("`", "");
				result.property = StringUtil.underlineToCamel(result.column);
				result.jdbcType = mybatisType2JdbcType(pi[1].trim().toUpperCase().replaceAll("\\(.*\\)", ""));
				if (result.jdbcType.equals("KEY"))
					continue;// 排除mysql PRIMARY KEY (`id`)这个部分
				String[] typeConvert = typeConvert(className + "@" + result.property, result.jdbcType).split("@");
				result.javaType = typeConvert[0];
				if (typeConvert.length > 1)
					result.typeHandler = typeConvert[1];
				result.comment = comment;

				arrayList.add(result);
			}
		}
		return arrayList;
	}

	public static String typeConvert(String tableAtField, String type) {
		if (typeProperties.getProperty(tableAtField.toLowerCase()) != null) {
			return typeProperties.getProperty(tableAtField.toLowerCase());
		}
		if (typeProperties.getProperty(type) == null) {
			System.err.println("Warning:Type Not Defined-" + type);
			return type;
		} else {
			return typeProperties.getProperty(type);
		}
	}

	public static String mybatisType2JdbcType(String type) {
		switch (type) {
		case "DATETIME":
			return "TIMESTAMP";
		default:
			return type;
		}
	}
}
