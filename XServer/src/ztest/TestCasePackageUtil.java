package ztest;

import java.io.File;
import java.util.*;

public class TestCasePackageUtil {
	public static void main(String[] args) {
		String packageName = "com.entity";

		List<String> classNames = getClassName(packageName);
		for (String className : classNames) {
			System.out.println(className);
		}
	}

	public static List<String> getClassName(String packageName) {
		String filePath = ClassLoader.getSystemResource("").getPath() + packageName.replace(".", "\\");
		List<String> fileNames = getClassName(filePath, null);
		return fileNames;
	}

	private static List<String> getClassName(String filePath, List<String> className) {
		List<String> myClassName = new ArrayList<String>();
		File[] childFiles = new File(filePath).listFiles();
		for (File childFile : childFiles) {
			if (childFile.isDirectory()) {
				myClassName.addAll(getClassName(childFile.getPath(), myClassName));
			} else {
				String[] strs = childFile.getPath().replace("\\", ".").split("\\.");
				if (strs.length >= 2) {
					myClassName.add(strs[strs.length - 2]);
				}
			}
		}
		return myClassName;
	}
}
