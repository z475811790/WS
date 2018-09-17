package com.componet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

@Component
public class Cacher {

	private CacheManager cacheManager;

	public Cacher() {
		cacheManager = CacheManager.create();
		List<String> list = getClassName("com.entity");
		list.forEach(e -> {
			Cache c = cacheManager.getCache(e + "Cache");
			if (c == null)
				cacheManager.addCache(e + "Cache");
		});
	}

	private static List<String> getClassName(String packageName) {
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

	private Map<String, Object> map = new HashMap<>(1000);

	/**
	 * 把对象放入Hash中
	 */
	public void set(String key, String field, Object o) {
		map.put(key + field, o);
	}

	/**
	 * 从Hash中获取对象
	 */
	public Object get(String key, String field) {
		return map.get(key + field);
	}

	/**
	 * 从Hash中获取对象,转换成制定类型
	 */
	public <T> T hget(String key, String field, Class<T> clazz) {
		Object text = map.get(key + field);
		T result = null;
		try {
			result = clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 从Hash中删除对象
	 */
	public void del(String key, String field) {
		map.remove(key + field);
	}
}
