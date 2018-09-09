package com.infra;

import java.util.Collection;

public class DestinationData {
	public String socketId;
	public Collection<String> socketIds;
	public byte[] msgByes;

	public DestinationData() {

	}

	public DestinationData(String socketId, byte[] msgByes) {
		super();
		this.socketId = socketId;
		this.msgByes = msgByes;
	}
}
