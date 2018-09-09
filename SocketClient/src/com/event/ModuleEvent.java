package com.event;

/**
 * @author xYzDl
 * @date 2018年3月5日 下午10:23:23
 * @description 模块消息
 */
public class ModuleEvent {
	/**
	 * SOCKET数据包头值为0事件
	 */
	public static final String SOCKET_DATA_PACKAGE_EMPTY = "SOCKET_DATA_PACKAGE_EMPTY";
	/**
	 * SOCKET数据体未完全到达
	 */
	public static final String SOCKET_DATA_PACKAGE_NOT_ENOUGH = "SOCKET_DATA_PACKAGE_NOT_ENOUGH";
	/**
	 * SOCKET关闭
	 */
	public static final String SOCKET_CLOSE = "SOCKET_CLOSE";
	/**
	 * 正常通信状态
	 */
	public static final String SOCKET_STATE_TO_NORMAL = "SOCKET_STATE_TO_NORMAL";
}
