package com.command;

import com.action.LoginAction;
import com.componet.BaseCommand;

public class C_LoginCMD extends BaseCommand {

	public C_LoginCMD() {
	}

	@Override
	public void execute() throws Exception {
		super.execute();
		
		LoginAction action = getAction(LoginAction.class);
		action.C_Login(data);
		
		dispose();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
