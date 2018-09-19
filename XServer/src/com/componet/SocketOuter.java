package com.componet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.protobuf.Message;
import com.infra.SocketOutData;
import com.infra.net.NSocket;
import com.message.Message.MessageEnum.MessageId;

@Component
public class SocketOuter {
	@Autowired
	private SocketUserMap socketMap;

	public SocketOuter() {
	}

	/**
	 * 给外部使用的向所有端发消息
	 */
	public void sendSocketMessageToAll(Message msg) {
		SocketOutData des = new SocketOutData();
		des.socketIds = socketMap.getAllSocketIds();
		des.msgByes = packMsg(msg);
		NSocket.sendMsgData(des);
	}

	/**
	 * @param msg
	 *            消息
	 * @param toType
	 *            目的地类型
	 */
	public void sendSocketMessage(Message msg, String desId) {
		SocketOutData des = new SocketOutData();
		des.socketId = desId;
		des.msgByes = packMsg(msg);
		NSocket.sendMsgData(des);
	}

	private byte[] packMsg(Message msg) {
		byte[] bs = new byte[msg.getSerializedSize() + 4];
		intToBytes(MessageId.valueOf(msg.getClass().getSimpleName()).getNumber(), bs);
		System.arraycopy(msg.toByteArray(), 0, bs, 4, bs.length - 4);
		return bs;
	}

	private void intToBytes(int id, byte[] bs) {
		bs[0] = (byte) (id >> 24 & 0xFF);
		bs[1] = (byte) (id >> 16 & 0xFF);
		bs[2] = (byte) (id >> 8 & 0xFF);
		bs[3] = (byte) (id & 0xFF);
	}
}
