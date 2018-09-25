package main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xYzDl
 * @date 2018年8月31日 下午10:23:37
 * @description 根据Proto消息文件生成对应Command类，Action接口 包含多条消息的一个消息集合文件对应一个Action接口
 *              每个文件中一条消息对应一个command类和对应Action中的一个方法
 */
public class CodeBuilderMsg {

	public CodeBuilderMsg() {
	}

	public static void main(String[] args) {
		Config.initProperties();
		String msgDir = cfgV("in.dir.message");
		String actionDir = cfgV("out.dir.action");
		String commandDir = cfgV("out.dir.command");
		String protoCommandStr = StringUtil.readToString(cfgV("prototype.command.file"));
		String protoActionStr = StringUtil.readToString(cfgV("prototype.action.file"));
		File file = new File(msgDir);
		if (!file.exists())
			return;
		String[] fs = file.list();
		for (String fileName : fs) {
			if (fileName.equals(cfgV("messageid.file")))
				continue;
			Map<String, Object> map = new HashMap<>();
			String txt = StringUtil.readToString(msgDir + "\\" + fileName);
			Pattern pattern = Pattern.compile("Protobuf type \\{@code com\\.message\\.\\w+");
			Matcher matcher = pattern.matcher(txt);
			String[] matchTxts;
			String klass;
			while (matcher.find()) {
				matchTxts = matcher.group().split("\\.");
				klass = matchTxts[2];
				if (!klass.toUpperCase().startsWith("C_"))// 只需要以C_开头的客户端消息
					continue;
				if (map.get(klass) != null)
					continue;// 匹配出来的是重复定义的，需要去重
				map.put(klass, klass);
				System.out.println(klass);
			}

		}
	}

	public static String cfgV(String k) {
		return Config.properties.getProperty(k);
	}

	private static void genActionFile(String fileName, String protoStr, Map<String, Object> methodMap) {
		String className = StringUtil.underlineToCamel(tableName).substring(TABLE_PRE_LEN) + cfgV("class.suffix");
		String javaName = className + "Service";
		String daoName = cfgV("package.dao") + ".I" + className + "Dao";
		String fileName = cfgV("out.dir.code") + cfgV("package.service").replace('.', '/') + "/" + javaName + ".java";
		String implFileName = cfgV("out.dir.code") + cfgV("package.service").replace('.', '/') + "/impl/" + javaName
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
}
