package com.action.impl;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.action.ChatAction;
import com.componet.BaseAction;
import com.infra.CommandData;
import com.message.Chat.C_ChatMsg;
import com.message.Chat.C_LoginChat;
import com.message.Chat.S_ChatMsg;
import com.message.Chat.S_LoginChat;

@Component
public class ChatActionImpl extends BaseAction implements ChatAction {
	@Override
	public void C_ChatMsg(CommandData data) throws Exception {
		C_ChatMsg c = C_ChatMsg.parseFrom(data.msgBytes);

		S_ChatMsg.Builder builder = S_ChatMsg.newBuilder();
		builder.setContent(c.getContent());
		builder.setName(c.getName());
		builder.setDate(new Date().getTime() + "");

		outer.sendSocketMessageToAll(builder.build());
	}

	public void C_LoginChat(CommandData data) throws Exception {
		C_LoginChat c = C_LoginChat.parseFrom(data.msgBytes);

//		System.out.println("ChatRoom:" + c.getRoomNumber());
		S_LoginChat.Builder builder = S_LoginChat.newBuilder();
		builder.setRoomNumber(4758);
		outer.sendSocketMessage(builder.build(), data.socketId);

		S_ChatMsg.Builder b = S_ChatMsg.newBuilder();
		b.setContent(socketUserMap.getAccount(data.socketId).getAccount() + " 进入房间 4758");
		b.setName("系统");
		b.setDate(new Date().getTime() + "");
		outer.sendSocketMessageToAll(b.build());
	}
}
