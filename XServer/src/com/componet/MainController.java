package com.componet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.core.App;
import com.core.Console;
import com.core.XTimer;
import com.core.event.XEvent;
import com.infra.Config;
import com.infra.event.ModuleEvent;
import com.infra.net.SessionContext;
import com.message.Main.S_SynHeartbeat;

/**
 * @author xYzDl
 * @date 2018年8月26日 下午1:41:49
 * @description 主要负责服务器上一些核心处理
 */
@Component
public class MainController {

	public MainController() {
		XTimer.add(this::onHeartbeat, Config.SYN_HEARTBEAT);
		initListeners();
	}

	protected void initListeners() {
		App.addModuleListener(ModuleEvent.SOCKET_CLOSE, this::onSocketClose);// 通信接口关闭事件,包括由IO错误,服务主动断开
	}

	private void onSocketClose(XEvent event) {
		Console.addMsg("Socket Closed@" + event.data.toString());
		Console.addMsg("The Left Client Num is " + SessionContext.numSession());
	}

	@Autowired
	private SocketOuter outer;

	private void onHeartbeat(XEvent xEvent) {
		S_SynHeartbeat.Builder builder = S_SynHeartbeat.newBuilder();
		long now = System.currentTimeMillis();
		builder.setServerTime(now);
		outer.sendSocketMessageToAll(builder.build());
	}
}
