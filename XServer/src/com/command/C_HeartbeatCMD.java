package com.command;

import com.action.MainAction;
import com.componet.BaseCommand;

public class C_HeartbeatCMD extends BaseCommand {

	public C_HeartbeatCMD() {
	}

	@Override
	public void execute() throws Exception {
		super.execute();

		MainAction action = getAction(MainAction.class);
		action.C_Heartbeat(data);

		dispose();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
