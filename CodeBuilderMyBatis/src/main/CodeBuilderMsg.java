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
		String msgDir = cfgV("out.dir.code") + cfgV("package.message").replace('.', '\\');// 设置消息类包位置
		String protoActionStr = StringUtil.readToString(cfgV("prototype.action.file"));// 读取Action模板
		String protoCommandStr = StringUtil.readToString(cfgV("prototype.command.file"));// 读取Command模板

		File file = new File(msgDir);// 消息包不存在就不继续执行
		if (!file.exists())
			return;
		String[] fs = file.list();
		for (String fileName : fs) {
			if (fileName.equals(cfgV("messageid.file")))
				continue;// 如果消息Id定义类文件就跳过
			Set<String> set = new HashSet<>();
			String txt = StringUtil.readToString(msgDir + "\\" + fileName);// 读取自动生成的消息定义类文件
			Pattern pattern = Pattern.compile("Protobuf type \\{@code com\\.message\\.\\w+");
			Matcher matcher = pattern.matcher(txt);// 匹配消息类里面多条消息名
			String[] matchTxts;
			String klass = null;
			while (matcher.find()) {
				matchTxts = matcher.group().split("\\.");
				klass = matchTxts[2];// C_ChatMsg
				if (!klass.toUpperCase().startsWith("C_"))// 只需要以C_开头的客户端消息
					continue;
				set.add(klass);// 放中集合中
			}
			genActionAndCommandFile(fileName.split("\\.")[0], set.toArray(), protoActionStr, protoCommandStr);
		}
	}

	public static String cfgV(String k) {
		return Config.properties.getProperty(k);
	}

	/**
	 * 生成Action类和Command类
	 * 一个Action类文件对应一个消息类文件，一个Action类包括多个Action方法，对应一条消息，一条消息对应生成一个Command类
	 * 
	 * @param actionName
	 *            Action类名
	 * @param methodList
	 *            方法集合
	 * @param protoActionStr
	 * @param protoCommandStr
	 */
	private static void genActionAndCommandFile(String actionName, Object[] methodList, String protoActionStr,
			String protoCommandStr) {
		String javaName = actionName + "Action";// ChatAction
		String fileName = cfgV("out.dir.code") + cfgV("package.action").replace('.', '\\') + "\\" + javaName + ".java";// ..ChatAction.java

		Map<String, Object> labelMap = new HashMap<String, Object>();
		labelMap.put("packagename", cfgV("package.action"));
		labelMap.put("interface", javaName);

		String methodItem = "\tvoid %s(CommandData data) throws Exception;\r\n\r\n";
		String methodAnno = "";

		for (Object m : methodList) {// C_ChatMsg
			methodAnno += String.format(methodItem, m);
			genCommandFile(javaName, m, protoCommandStr);
		}

		labelMap.put("methods", methodAnno);

		String resultText = StringUtil.formatLabel(protoActionStr, labelMap);

		System.out.println(resultText);
		StringUtil.contentToTxt(fileName, resultText);
	}

	/**
	 * 生成一条消息对应的Command类
	 * 
	 * @param actionName
	 *            Action方法名
	 * @param method
	 * @param protoCommandStr
	 */
	private static void genCommandFile(String actionName, Object method, String protoCommandStr) {
		String fileName = cfgV("out.dir.code") + cfgV("package.command").replace('.', '\\') + "\\" + method
				+ "CMD.java";

		Map<String, Object> labelMap = new HashMap<String, Object>();
		labelMap.put("packagename", cfgV("package.command"));
		labelMap.put("cmd", method + "CMD");// C_ChatMsgCMD
		labelMap.put("action", actionName);// ChatAction
		labelMap.put("method", method);// C_ChatMsg
		String resultText = StringUtil.formatLabel(protoCommandStr, labelMap);

		StringUtil.contentToTxt(fileName, resultText);
	}
}
