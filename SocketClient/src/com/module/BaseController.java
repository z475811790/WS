package com.module;

import com.google.protobuf.Message;
import com.xyzdl.CSocket;

import xyzdlcore.event.IEventHandler;

/**
 * @author xYzDl
 * @date 2018年2月28日 下午9:53:01
 * @description MVC控制器基类
 */
public class BaseController {
	private static CSocket _socket = new CSocket();

	public BaseController() {
		initListeners();
	}

	protected void initListeners() {

	}

	protected void sendSocketMessage(Message msg) {
		_socket.sendProtoMessage(msg);
	}

	protected void addSocketListener(int msgId, IEventHandler listener) {
		_socket.addSocketMsgListener(msgId, listener);
	}

	protected void removeSocketListener(int msgId) {
		_socket.removeSocketMsgListener(msgId);
	}
}
