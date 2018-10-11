package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

public class Config {
	public static Properties properties = new Properties();

	public static void initProperties() {
		try {
			// 使用InPutStream流读取properties文件
			BufferedReader bufferedReader;
			bufferedReader = new BufferedReader(new FileReader("builderConfig.properties"));
			Config.properties.load(bufferedReader);
			bufferedReader = new BufferedReader(new FileReader("typeMap.properties"));
			Converter.typeProperties.load(bufferedReader);
			// 获取key对应的value值
			// properties.getProperty(String key);
			System.out.println("Load builderConfig.properties Successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
