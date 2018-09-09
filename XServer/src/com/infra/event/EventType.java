package com.infra.event;

/**
 * @author xYzDl
 * @date 2018年1月23日 上午11:58:59
 * @description 事件类型
 */
public class EventType {
	// -------公用事件-------
	/**
	 * IO错误事件
	 */
	public static final String IO_ERROR = "IO_ERROR";
	/**
	 * 连接事件
	 */
	public static final String CONNECT = "CONNECT";
	/**
	 * 数据到达事件
	 */
	public static final String SOCKET_DATA = "SOCKET_DATA";
	/**
	 * 连接关闭事件
	 */
	public static final String CLOSE = "CLOSE";
}
