package com.action.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.action.LoginAction;
import com.componet.BaseAction;
import com.entity.User;
import com.infra.CommandData;
import com.message.Login.C_Login;
import com.message.Login.S_Login;
import com.service.StudentService;

import xyzdlcore.Console;

@Component
public class LoginActionImpl extends BaseAction implements LoginAction {

	@Autowired
	StudentService studentService;

	public LoginActionImpl() {
	}

	@Override
	public void C_Login(CommandData data) throws Exception {
		C_Login c = C_Login.parseFrom(data.msgBytes);
		User u = new User();
		u.setAccount(c.getAccount());
		u.setPassword("123");

		System.out.println("xYz");
		// User u = ServiceContext.singleton.loginService.login(c.account,
		// c.password);
		socketUserMap.addSocket(data.socketId, u);
		S_Login.Builder builder = S_Login.newBuilder();
		builder.setAccount(c.getAccount());
		builder.setIsSuccessful(true);
		builder.setMsg("登录成功");

		Console.addMsg(c.getAccount() + " 登录成功");
		outer.sendSocketMessage(builder.build(), data.socketId);
	}
}
