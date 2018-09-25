package main;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
		String msgDir = cfgV("out.dir.code") + cfgV("package.message").replace('.', '\\');
		String protoActionStr = StringUtil.readToString(cfgV("prototype.action.file"));
		String protoCommandStr = StringUtil.readToString(cfgV("prototype.command.file"));

		File file = new File(msgDir);
		if (!file.exists())
			return;
		String[] fs = file.list();
		for (String fileName : fs) {
			if (fileName.equals(cfgV("messageid.file")))
				continue;
			Set<String> set = new HashSet<>();
			String txt = StringUtil.readToString(msgDir + "\\" + fileName);
			Pattern pattern = Pattern.compile("Protobuf type \\{@code com\\.message\\.\\w+");
			Matcher matcher = pattern.matcher(txt);
			String[] matchTxts;
			String klass = null;
			while (matcher.find()) {
				matchTxts = matcher.group().split("\\.");
				klass = matchTxts[2];
				if (!klass.toUpperCase().startsWith("C_"))// 只需要以C_开头的客户端消息
					continue;
				set.add(klass);
			}
			genActionAndCommandFile(fileName.split("\\.")[0], set.toArray(), protoActionStr, protoCommandStr);
		}
	}

	public static String cfgV(String k) {
		return Config.properties.getProperty(k);
	}

	private static void genActionAndCommandFile(String actionName, Object[] methodList, String protoActionStr, String protoCommandStr) {
		String javaName = actionName + "Action";
		String fileName = cfgV("out.dir.code") + cfgV("package.action").replace('.', '\\') + "\\" + javaName + ".java";

		Map<String, Object> labelMap = new HashMap<String, Object>();
		labelMap.put("packagename", cfgV("package.action"));
		labelMap.put("interface", javaName);

		String methodItem = "\tvoid %s(CommandData data) throws Exception;\r\n\r\n";
		String methodAnno = "";

		for (Object m : methodList) {
			methodAnno += String.format(methodItem, m);
			genCommandFile(javaName, m, protoCommandStr);
		}

		labelMap.put("methods", methodAnno);

		String resultText = StringUtil.formatLabel(protoActionStr, labelMap);

		System.out.println(resultText);
		StringUtil.contentToTxt(fileName, resultText);
	}

	private static void genCommandFile(String actionName, Object method, String protoCommandStr) {
		String fileName = cfgV("out.dir.code") + cfgV("package.command").replace('.', '\\') + "\\" + method + "CMD.java";

		Map<String, Object> labelMap = new HashMap<String, Object>();
		labelMap.put("packagename", cfgV("package.command"));
		labelMap.put("cmd", method + "CMD");
		labelMap.put("action", actionName);
		labelMap.put("method", method);
		String resultText = StringUtil.formatLabel(protoCommandStr, labelMap);

		StringUtil.contentToTxt(fileName, resultText);
	}
}
