package com.xyzdl;

import com.core.CoreUtil;
import com.core.util.AssetUtil;

/**
 * @author xYzDl
 * @date 2018年2月5日 下午10:49:10
 * @description 配置类 通过反射机制读取核心配置,key大小一定要一致Config中定义的变量一定要在配置文件中能找到对应定义
 */
public class Config {
	public static final Boolean IS_DEBUG = true; // 调试模式否
	public static final String CLIENT_VERSION = "1.0"; // 客户端版本
	public static final int COMMUNICATION_PROTOCOL_VERSION = 1;
	public static int challenge = 0;
	public static String logFilePath = "";
	public static Boolean dataCompress = true; // 消息数据压缩否
	public static String host = "";
	public static int port = 0;

	public static void initConf() {
		CoreUtil.parseConf(Config.class, AssetUtil.COMMON_CONFIG);
	}
}
