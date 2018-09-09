package com.infra;

import com.core.CoreUtil;

/**
 * @author xYzDl
 * @date 2018年2月5日 下午10:49:10
 * @description 配置类 通过反射机制读取核心配置,key大小一定要一致Config中定义的变量一定要在配置文件中能找到对应定义
 */
public class Config {
	public static final String CONF_URL = "config/config.xml";
	public static final Boolean IS_SAME_AESKEY = false; // 是否所有AES都用的同一个
	public static final Boolean IS_DEBUG = true; // 调试模式否
	public static final String SERVER_VERSION = "1.0"; // 服务器版本
	public static final int COMMUNICATION_PROTOCOL_VERSION = 1;
	public static final String COMMAND_PREFIX = "com.command.";
	public static final String COMMAND_SUFFIX = "CMD";
	public static final int SESSION_CHECK_INTERVAL = 2000000;// 20000
																// Socket连接检查是否断开时间间隔，客户端一定要该时间内发消息给服务器
	public static final int SYN_HEARTBEAT = 15000;// 发送心跳和同步时间的间隔，毫秒

	public static int challenge = 0;
	public static String logFilePath = "";
	public static Boolean dataCompress = false; // 消息数据压缩否
	public static String host = "127.0.0.1";
	public static int port = 8080;

	public static void initConf() {
		CoreUtil.parseConf(Config.class, CONF_URL);
	}
}
