package com.action;

import com.infra.CommandData;

public interface ChatAction {
	public void C_ChatMsg(CommandData data) throws Exception;

	public void C_LoginChat(CommandData data) throws Exception;
}
