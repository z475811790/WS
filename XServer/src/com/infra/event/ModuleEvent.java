package com.infra.event;

/**
 * @author xYzDl
 * @date 2018年3月5日 下午10:23:23
 * @description 模块消息
 */
public class ModuleEvent {
	/**
	 * 客户端SOCKET关闭
	 */
	public static final String SOCKET_CLOSE = "SOCKET_CLOSE";
	/**
	 * 服务器通信接口关闭
	 */
	public static final String SERVER_SOCKET_CLOSE = "SERVER_SOCKET_CLOSE";
	/**
	 * 正常通信状态
	 */
	public static final String SOCKET_STATE_TO_NORMAL = "SOCKET_STATE_TO_NORMAL";
	/**
	 * 客户端连接服务器
	 */
	public static final String SERVER_SOCKET_CONNECT = "SERVER_SOCKET_CONNECT";
}
