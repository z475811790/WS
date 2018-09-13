package ztest.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class CacheBean {

	private Map<String, Object> map = new HashMap<>();

	/**
	 * 把对象放入Hash中
	 */
	public void hset(String key, String field, Object o) {
		map.put(key + field, o);
	}

	/**
	 * 从Hash中获取对象
	 */
	public Object hget(String key, String field) {
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
	public void hdel(String key, String... field) {
		map.remove(key + field);
	}

}
