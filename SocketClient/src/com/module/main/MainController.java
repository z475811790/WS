package com.module.main;

import com.configuration.LanPack;
import com.event.ModuleEvent;
import com.google.protobuf.InvalidProtocolBufferException;
import com.message.Main.C_Heartbeat;
import com.message.Main.S_SynHeartbeat;
import com.message.Message.MessageEnum.MessageId;
import com.module.BaseController;
import com.xyzdl.XCTimer;

import xyzdlcore.App;
import xyzdlcore.Console;
import xyzdlcore.event.IEventHandler;
import xyzdlcore.event.XEvent;

/**
 * @author xYzDl
 * @date 2018年3月11日 上午12:19:27
 * @description 主控制器
 */
public class MainController extends BaseController {
	private static MainController _singleton;

	public static MainController singleton() {
		if (_singleton == null)
			_singleton = new MainController();
		return _singleton;
	}

	public MainController() {
		super();

		XCTimer.add(new IEventHandler() {
			@Override
			public void execute(XEvent xEvent) throws Exception {
				// System.out.println("serverTime:" + XCTimer.serverTime());
				// System.out.println("nowSysTime:" +
				// System.currentTimeMillis());
				C_Heartbeat.Builder builder = C_Heartbeat.newBuilder();
				sendSocketMessage(builder.build());
			}
		}, 5000);// 每隔一定时间向服务器发送一次心跳
	}

	@Override
	protected void initListeners() {
		super.initListeners();
		App.addModuleListener(ModuleEvent.SOCKET_STATE_TO_NORMAL, this::onSocketStateToNormal);// 通信连接验证成功变为正常通信状态后引发的事件
		App.addModuleListener(ModuleEvent.SOCKET_CLOSE, this::onSocketClose);// 通信接口关闭事件,包括由IO错误,服务主动断开
		App.addModuleListener(ModuleEvent.SOCKET_DATA_PACKAGE_EMPTY, this::onDataPackageEmpty); // 数据包为空事件
		App.addModuleListener(ModuleEvent.SOCKET_DATA_PACKAGE_NOT_ENOUGH, this::onDataPackageNotEnough);// 数据包未完全到达

		addSocketListener(MessageId.S_SynHeartbeat_VALUE, this::onSynHeartbeat);
	}

	private void onSocketStateToNormal(XEvent event) {
		Console.addMsg(LanPack.getLanVal("1008"));
	}

	private void onSocketClose(XEvent event) {
		Console.addMsg(event.data.toString());
	}

	private void onDataPackageEmpty(XEvent event) {
		System.out.println("DataPackage Length is 0, It Can't be Empty!");
	}

	private void onDataPackageNotEnough(XEvent event) {
		System.out.println("DataPackage Has not Arrived All!");
	}

	private void onSynHeartbeat(XEvent event) throws InvalidProtocolBufferException {
		S_SynHeartbeat s = S_SynHeartbeat.parseFrom((byte[]) event.data);
		// System.out.println("SYN_HEARTBEAT:" + s.getServerTime());
		// Console.addMsg("SYN_HEARTBEAT:" + s.getServerTime());
		XCTimer.synServerTime(s.getServerTime());
	}
}
