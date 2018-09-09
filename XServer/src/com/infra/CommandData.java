package com.infra;

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
