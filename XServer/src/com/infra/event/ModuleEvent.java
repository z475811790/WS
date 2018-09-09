package com.infra.event;

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
	/**
	 * 客户端连接服务器
	 */
	public static final String SERVER_SOCKET_CONNECT = "SERVER_SOCKET_CONNECT";
	/**
	 * 服务器通信接口关闭端连接服务器
	 */
	public static final String SERVER_SOCKET_CLOSE = "SERVER_SOCKET_CLOSE";
	/**
	 * AESWoker创建AESKey
	 */
	public static final String SERVER_WORKER_CRYPT_CREAT_AES = "SERVER_WORKER_CRYPT_CREAT_AES";
	/**
	 * AESWoker创建AESKey完成
	 */
	public static final String CREATE_AES_COMPLETE = "SERVER_WORKER_CREATE_AES_COMPLETE";
	/**
	 * AES解密消息
	 */
	public static final String SERVER_WORKER_CRYPT_DECRYPT = "SERVER_WORKER_CRYPT_DECRYPT";
	/**
	 * AES解密消息完成
	 */
	public static final String SERVER_WORKER_CRYPT_DECRYPT_COMPLETE = "SERVER_WORKER_CRYPT_DECRYPT_COMPLETE";
	/**
	 * AES加密消息
	 */
	public static final String SERVER_WORKER_CRYPT_ENCRYPT = "SERVER_WORKER_CRYPT_ENCRYPT";
	/**
	 * AES加密消息完成
	 */
	public static final String SERVER_WORKER_CRYPT_ENCRYPT_COMPLETE = "SERVER_WORKER_CRYPT_ENCRYPT_COMPLETE";
	/**
	 * 向客户端发送消息
	 */
	public static final String SERVER_WORKER_SEND_SOCKET_MESSAGE = "SERVER_WORKER_SEND_SOCKET_MESSAGE";
}
