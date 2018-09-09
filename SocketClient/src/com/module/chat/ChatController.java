package com.module.chat;

import java.awt.event.KeyEvent;

import javax.swing.JTextField;

import com.configuration.LanPack;
import com.core.Console;
import com.core.event.XEvent;
import com.core.util.TimeUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.message.Chat.C_ChatMsg;
import com.message.Chat.C_LoginChat;
import com.message.Chat.S_ChatMsg;
import com.message.Chat.S_LoginChat;
import com.message.Login.C_Login;
import com.message.Login.S_Login;
import com.message.Message.MessageEnum.MessageId;
import com.module.BaseController;

/**
 * @author xYzDl
 * @date 2018年3月11日 上午12:21:30
 * @description 聊天控制器
 */
public class ChatController extends BaseController {
	private static ChatController _singleton;

	public static ChatController singleton() {
		if (_singleton == null)
			_singleton = new ChatController();
		return _singleton;
	}

	public ChatController() {
		super();
		Console.singleton().onInputHandler = this::onInput;
	}

	@Override
	protected void initListeners() {
		super.initListeners();
		addSocketListener(MessageId.S_ChatMsg_VALUE, this::onChatMsgBack);
		addSocketListener(MessageId.S_Login_VALUE, this::onLoginBack);
		addSocketListener(MessageId.S_LoginChat_VALUE, this::onLoginChatRoomBack);
	}

	// ------START-事件注册区
	private void onChatMsgBack(XEvent event) throws InvalidProtocolBufferException {
		S_ChatMsg s = S_ChatMsg.parseFrom((byte[]) event.data);
		Console.addMsg(s.getName() + "#" + TimeUtil.toShortTime(s.getDate()) + "#" + s.getContent());

	}

	private void onLoginBack(XEvent e) throws InvalidProtocolBufferException {
		S_Login s = S_Login.parseFrom((byte[]) e.data);
		if (!s.getIsSuccessful()) {
			Console.addMsg(LanPack.getLanVal("1003") + s.getMsg());
			return;
		}
		Console.addMsg(LanPack.getLanVal("1004"));
		userName = s.getAccount();
		Console.addMsg(LanPack.getLanVal("1005") + userName);
		Console.addMsg(LanPack.getLanVal("1006"));
	}

	private void onLoginChatRoomBack(XEvent e) throws InvalidProtocolBufferException {
		S_LoginChat s = S_LoginChat.parseFrom((byte[]) e.data);
		roomNumber = s.getRoomNumber();
	}

	private String userName = "";
	private int roomNumber = 0;

	private void onInput(XEvent e) {
		KeyEvent event = (KeyEvent) e.data;
		JTextField tf = (JTextField) event.getSource();
		if (event.getKeyCode() == KeyEvent.VK_ENTER) {
			if (tf.getText() == null || "".equals(tf.getText())) {
				return;
			}
			if ("".equals(userName)) {
				C_Login.Builder builder = C_Login.newBuilder();
				builder.setAccount(tf.getText());
				builder.setPassword("123456");
				sendSocketMessage(builder.build());
				tf.setText("");
				return;
			}
			if (roomNumber == 0) {
				int rn = Integer.parseInt(tf.getText());
				if (rn <= 0) {
					Console.addMsg(LanPack.getLanVal("1007"));
					return;
				}
				C_LoginChat.Builder builder = C_LoginChat.newBuilder();
				builder.setRoomNumber(rn);
				sendMsg(builder.build());
				tf.setText("");
				return;
			}
			C_ChatMsg.Builder builder = C_ChatMsg.newBuilder();
			builder.setName(userName);
			builder.setContent(tf.getText());
			sendMsg(builder.build());
			tf.setText("");
			// trace("C_Send_Time:"+TimeUtil.nowTimeNumber);
		}
	}

	// ------END---事件注册区
	// ------START-公共方法区
	// ------END---公共方法区
	// ------START-私有方法区
	private void sendMsg(Message message) {
		if ("".equals(userName)) {
			Console.addMsg(LanPack.getLanVal("1008"));
			return;
		}
		sendSocketMessage(message);
	}

	// ------END---私有方法区
}
