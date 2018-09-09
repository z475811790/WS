package com.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.core.event.IEventHandler;
import com.core.event.XEvent;

/**
 * @author xYzDl
 * @date 2018年1月23日 上午11:26:20
 * @description 事件派发核心类 由于同一实例的同一方法每次会转换成一个新的lambda表示达实例 且无法从lambda实例获取对应方法的相关信息
 *              所以无法用lambda作为映射的键 导致以下问题需要注意:不使用registerMark来注册方法的话
 *              同一实例的同一方法可以重复添加,且无法remove
 *              因此,不使用registerMark时,注意不要重复添加监听,需要remove时,一定要注册方法
 *              实际情况是很少会remove,所以多注意不要重要添加监听即可
 */
public class Dispatcher {
	private Map<Object, List<IEventHandler>> _listenersMap = new ConcurrentHashMap<>();
	private Map<Object, IEventHandler> _registerMarkMap = new ConcurrentHashMap<>();

	public Dispatcher() {
	}

	/**
	 * @param eventType
	 *            事件类型
	 * @param listener
	 *            监听方法
	 * @param registerMark
	 *            注册键 只有为有效字符串时才可用,该参数可以为null
	 */
	public void add(Object eventType, IEventHandler listener, Object registerMark) {
		List<IEventHandler> listeners = _listenersMap.get(eventType);
		if (listeners == null) {
			listeners = new ArrayList<>();
			_listenersMap.put(eventType, listeners);
		}
		if (registerMark != null) {
			if (_registerMarkMap.get(registerMark) != null) {
				return;
			}
			_registerMarkMap.put(registerMark, listener);
		}
		listeners.add(listener);
	}

	/**
	 * @param eventType
	 *            事件类型
	 * @param registerMark
	 *            注册键
	 */
	public void remove(Object eventType, Object registerMark) {
		List<IEventHandler> listeners = _listenersMap.get(eventType);
		if (listeners == null)
			return;
		if (registerMark == null)
			return;
		IEventHandler listener = _registerMarkMap.get(registerMark);
		if (listener == null)
			return;
		_registerMarkMap.remove(registerMark);
		listeners.remove(listener);
	}

	/**
	 * @param eventType
	 *            事件类型
	 * @param data
	 *            事件参数
	 */
	public void dispatch(Object eventType, Object data) {
		List<IEventHandler> listeners = _listenersMap.get(eventType);
		if (listeners == null)
			return;
		// 应该倒序遍历 因为有可能在回调的方法中可能有移除监听方法的代码
		for (int i = listeners.size() - 1; i >= 0; i--) {
			IEventHandler handler = listeners.get(i);
			try {
				handler.execute(new XEvent(data));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 清空所有监听
	 */
	public void clear() {
		for (List<IEventHandler> list : _listenersMap.values()) {
			if (list != null)
				list.clear();
		}
		_listenersMap.clear();
		_registerMarkMap.clear();
	}
}
