package com.action.impl;

import org.springframework.stereotype.Component;

import com.action.MainAction;
import com.componet.BaseAction;
import com.infra.CommandData;

@Component
public class MainActionImpl extends BaseAction implements MainAction {

	public MainActionImpl() {
	}

	@Override
	public void C_Heartbeat(CommandData data) throws Exception {
		// System.out.println("C_HEARTBEAT:" + System.currentTimeMillis());
	}
}
