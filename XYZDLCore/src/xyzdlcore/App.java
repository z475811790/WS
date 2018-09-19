package xyzdlcore;

import xyzdlcore.event.IEventHandler;

/**
 * @author xYzDl
 * @date 2018年3月5日 下午10:10:52
 * @description 全局引用入口
 */
public class App {

	private static Dispatcher moduleDispatcher = new Dispatcher(); // 全局MVC共享事件派发器

	public App() {
	}

	/**
	 * 有需要移除事件的情况用此进行注册
	 * 
	 * @param moduleEvent
	 * @param listener
	 * @param registerMark
	 */
	public static void addModuleListener(String moduleEvent, IEventHandler listener, Object registerMark) {
		moduleDispatcher.add(moduleEvent, listener, registerMark);
	}

	/**
	 * 有registerMark才能移除
	 * 
	 * @param moduleEvent
	 * @param registerMark
	 */
	public static void removeModuleListener(String moduleEvent, Object registerMark) {
		moduleDispatcher.remove(moduleEvent, registerMark);
	}

	/**
	 * 无需要移除事件的情况用此进行注册
	 * 
	 * @param moduleEvent
	 * @param listener
	 */
	public static void addModuleListener(String moduleEvent, IEventHandler listener) {
		moduleDispatcher.add(moduleEvent, listener, null);
	}

	/**
	 * 派发全局事件
	 */
	public static void dispatch(String moduleEvent, Object data) {
		moduleDispatcher.dispatch(moduleEvent, data);
	}

	public static void dispatch(String moduleEvent) {
		moduleDispatcher.dispatch(moduleEvent, null);
	}
}
