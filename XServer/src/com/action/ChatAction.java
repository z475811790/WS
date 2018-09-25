package com.action;

import com.infra.CommandData;

public interface ChatAction {
	void C_ChatMsg(CommandData data) throws Exception;

	void C_LoginChat(CommandData data) throws Exception;
}
