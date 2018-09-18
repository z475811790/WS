package com.infra;

/**
 * @author xYzDl
 * @date 2018年9月19日 上午12:08:18
 * @description 存放Socket的一个原始数据包
 */
public class SocketData {
	public String socketId;
	public byte[] dataBytes;

	public SocketData(String socketId, byte[] dataBytes) {
		this.socketId = socketId;
		this.dataBytes = dataBytes;
	}
}
