package com.componet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * @author xYzDl
 * @date 2018年9月15日 下午11:00:47
 * @description Ehcache的封装类，Entity缓存专用
 */
@Component
public class EntityCacher {

	private CacheManager cacheManager;

	public EntityCacher() {
		// 获取com.entity下所有的实体类，类名+Cache为一个Cache实例的名字
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

	/**
	 * 添加缓存
	 * 
	 * @param cacheName
	 * @param key
	 * @param obj
	 */
	public void set(String cacheName, String key, Object obj) {
		if (cacheManager.getCache(cacheName) == null)
			return;
		cacheManager.getCache(cacheName).put(new Element(key, obj));
	}

	/**
	 * 获取缓存
	 * 
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public Object get(String cacheName, String key) {
		Element e = cacheManager.getCache(cacheName).get(key);
		return e == null ? null : e.getObjectValue();
	}

	/**
	 * 删除缓存
	 * 
	 * @param cacheName
	 * @param key
	 */
	public void del(String cacheName, String key) {
		if (cacheManager.getCache(cacheName) == null)
			return;
		cacheManager.getCache(cacheName).remove(key);
	}
}
