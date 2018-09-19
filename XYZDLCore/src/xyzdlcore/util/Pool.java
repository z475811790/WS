package xyzdlcore.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * @author xYzDl
 * @date 2018年9月11日 下午10:59:18
 * @description 轻量型对象池，线程不安全
 */
public class Pool<T> {
	private Queue<T> _pool = new LinkedList<>();
	private Map<T, T> _map = new HashMap<>();
	Class<T> _klass;

	public Pool(Class<T> klass) {
		_klass = klass;
	}

	public T pop() {
		if (_pool.peek() == null) {
			try {
				return _klass.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			_map.remove(_pool.peek());
			return _pool.poll();
		}
	}

	public void push(T obj) {
		if (obj == null || _map.containsKey(obj))
			return;
		_map.put(obj, obj);
		_pool.offer(obj);
	}
}
