package com.infra.net;

import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.core.App;
import com.core.XTimer;
import com.core.event.IEventHandler;
import com.core.event.XEvent;
import com.core.util.XUtil;
import com.infra.Config;
import com.infra.event.ModuleEvent;

/**
 * @author xYzDl
 * @date 2018年3月16日 下午10:24:22
 * @description 会话容器
 */
public class SessionContext {
	private Map<String, NSocket> _sessionMap = new HashMap<>(); // 客户端会话字典
	private static SessionContext _singleton;

	public static SessionContext singleton() {
		if (_singleton == null)
			_singleton = new SessionContext();
		return _singleton;
	}

	public SessionContext() {
		XTimer.add(new IEventHandler() {
			// 定时查看Socket连接是否断开，指定时间间隔内如果没有数据传入说明已经断开
			@Override
			public void execute(XEvent xEvent) throws Exception {
				List<NSocket> list = new ArrayList<>();
				long now = System.currentTimeMillis();
				for (NSocket e : _sessionMap.values()) {
					if (now - e.lastRecordTime > Config.SESSION_CHECK_INTERVAL)
						list.add(e);
				}
				for (NSocket e : list) {
					String id = e.toString();
					deleteSession(e);
					App.dispatch(ModuleEvent.SOCKET_CLOSE, id);
				}
			}
		}, Config.SESSION_CHECK_INTERVAL);
	}

	public static NSocket getSessionBySocket(Socket socket) {
		return getSessionBySocketId(XUtil.getSocketId(socket));
	}

	public static NSocket getSessionBySocketId(String socketId) {
		return singleton()._sessionMap.get(socketId);
	}

	public static NSocket createSession(Socket socket) {
		// XSession s = new XSession(socket);
		// singleton()._sessionMap.put(XCommonUtil.getSocketId(socket), s);
		// return s;
		return null;
	}

	public static NSocket createSession(SocketChannel socketChannel) {
		NSocket s = new NSocket(socketChannel);
		singleton()._sessionMap.put(XUtil.getSocketId(socketChannel.socket()), s);
		return s;
	}

	public static void deleteSession(NSocket session) {
		if (session == null)
			return;
		String key = session.getSocketId();// XCommonUtil.getSocketIdX();
		if (singleton()._sessionMap.get(key) != null) {
			session.dispose();
			singleton()._sessionMap.remove(key);
		}
	}

	public static int numSession() {
		return singleton()._sessionMap.size();
	}
}
