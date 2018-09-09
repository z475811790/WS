package com.command;

import com.action.ChatAction;
import com.componet.BaseCommand;

public class C_ChatMsgCMD extends BaseCommand {

	public C_ChatMsgCMD() {
	}

	@Override
	public void execute() throws Exception {
		super.execute();

		ChatAction action = getAction(ChatAction.class);
		action.C_ChatMsg(data);

		dispose();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
