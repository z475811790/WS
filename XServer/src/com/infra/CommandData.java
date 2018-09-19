package com.infra;

/**
 * @author xYzDl
 * @date 2018年9月19日 下午11:56:35
 * @description 命令数据
 */
public class CommandData {
	public String socketId;
	public int msgId;
	public byte[] msgBytes;

	public CommandData(String socketId, int msgId, byte[] msgBytes) {
		super();
		this.socketId = socketId;
		this.msgId = msgId;
		this.msgBytes = msgBytes;
	}
}
