package com.core.event;

/**
 * @author xYzDl
 * @date 2018年1月23日 上午11:26:57
 * @description 事件处理器接口
 */
@FunctionalInterface
public interface IEventHandler {
	/**
	 * @param xEvent
	 *            事件参数
	 */
	void execute(XEvent xEvent) throws Exception;
}
