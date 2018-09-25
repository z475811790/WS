package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
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
		String protoCommandStr = StringUtil.readToString(cfgV("prototype.command.file"));
		String protoActionStr = StringUtil.readToString(cfgV("prototype.action.file"));
		String protoActionImplStr = StringUtil.readToString(cfgV("prototype.action.impl.file"));
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

	public static void initProperties() {
		try {
			// 使用InPutStream流读取properties文件
			BufferedReader bufferedReader;
			bufferedReader = new BufferedReader(new FileReader("builderConfig.properties"));
			Config.properties.load(bufferedReader);
			bufferedReader = new BufferedReader(new FileReader("typeMap.properties"));
			Converter.typeProperties.load(bufferedReader);
			// 获取key对应的value值
			System.out.println("Load builderConfig.properties Successfully");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String cfgV(String k) {
		return Config.properties.getProperty(k);
	}
}
