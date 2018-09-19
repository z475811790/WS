package com.infra;

import java.util.Collection;

/**
 * @author xYzDl
 * @date 2018年9月19日 下午11:55:48
 * @description 存放需要输出的数据
 */
public class SocketOutData {
	public String socketId;
	public Collection<String> socketIds;
	public byte[] msgByes;

	public SocketOutData() {

	}

	public SocketOutData(String socketId, byte[] msgByes) {
		super();
		this.socketId = socketId;
		this.msgByes = msgByes;
	}
}
