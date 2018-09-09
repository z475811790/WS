package com.command;

import com.action.ChatAction;
import com.componet.BaseCommand;

public class C_LoginChatCMD extends BaseCommand {

	public C_LoginChatCMD() {
	}

	@Override
	public void execute() throws Exception {
		super.execute();
		
		ChatAction action = getAction(ChatAction.class);
		action.C_LoginChat(data);
		
		dispose();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
