package com.infra.net;

import java.util.HashMap;
import java.util.Map;

import com.core.App;
import com.core.ByteArray;
import com.core.Console;
import com.core.Dispatcher;
import com.core.event.IEventHandler;
import com.core.event.XEvent;
import com.infra.Config;
import com.infra.Stringbytes;
import com.infra.event.EventType;
import com.infra.event.ModuleEvent;

public class XServerSocket {
	private static final int DETERMINE_VERSION = 0; // 协议版本验证状态
	private static final int SEND_CHALLENGE = 1; // 发送验证状态
	private static final int RECEIVE_CHALLENGE = 2; // 接收码验证和客户端公钥状态
	private static final int NORMAL = 4; // 正常通信状态

	@SuppressWarnings("unused")
	private static final int HEAD_LEN = 4; // 数据包头,用来表示实际数据的长度,数据包去掉包头才是实际数据长度

	private Map<Integer, IEventHandler> _stateFunMap = new HashMap<>();// 状态处理方法字典
	private Dispatcher _dispatcher = new Dispatcher();
	private static XServerSocket _singleton;

	public static XServerSocket singleton() {
		if (_singleton == null)
			_singleton = new XServerSocket();
		return _singleton;
	}

	public XServerSocket() {
		_stateFunMap.put(DETERMINE_VERSION, this::sendVersion);
		_stateFunMap.put(SEND_CHALLENGE, this::sendChallenge);
		_stateFunMap.put(RECEIVE_CHALLENGE, this::receiveChallengeAndPublicKey);
		_stateFunMap.put(NORMAL, this::read);
		initListeners();
		App.addModuleListener(ModuleEvent.SERVER_WORKER_CREATE_AES_COMPLETE, this::onCreateAESComplete);
		// new SocketListener().start();
		Reactor r = new Reactor();
		r.dispatcher = _dispatcher;
	}

	protected void initListeners() {
		_dispatcher.add(EventType.CONNECT, this::onConnect, null);
		_dispatcher.add(EventType.CLOSE, this::onClose, null);
	}

	private void onConnect(XEvent xEvent) {
		NSocket xSession = (NSocket) xEvent.data;
		initSessionListeners(xSession);
		try {
			_stateFunMap.get(DETERMINE_VERSION).execute(new XEvent(xSession));
			App.dispatch(ModuleEvent.SERVER_SOCKET_CONNECT, xSession.getSocketId());
			Console.addMsg("Start to Verify...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void initSessionListeners(NSocket xSession) {
		_dispatcher.add(EventType.SOCKET_DATA, this::onClientSocketData, null);
		// xSession.addEventListener(EventType.CLOSE, this::onCScoketClose);
	}

	private void onClose(XEvent xEvent) {
		App.dispatch(ModuleEvent.SERVER_SOCKET_CLOSE, xEvent.data);
	}

	private void onCreateAESComplete(XEvent xEvent) {
		Stringbytes args = (Stringbytes) xEvent.data;
		NSocket session = SessionContext.getSessionBySocketId(args.string);
		if (session == null)
			return;
		session.writeDataPack(args.bs);
		session.state = NORMAL;
		App.dispatch(ModuleEvent.SOCKET_STATE_TO_NORMAL, SessionContext.numSession());
	}

	private void onClientSocketData(XEvent xEvent) {
		NSocket xSession = SessionContext.getSessionBySocketId(xEvent.data.toString());
		if (xSession == null)
			return;
		try {
			_stateFunMap.get(xSession.state).execute(new XEvent(xSession));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void onCScoketClose(XEvent xEvent) {
		SessionContext.deleteSession(SessionContext.getSessionBySocketId(xEvent.data.toString()));
		App.dispatch(ModuleEvent.SOCKET_CLOSE, xEvent.data.toString());
	}

	private void sendVersion(XEvent xEvent) {
		NSocket session = (NSocket) xEvent.data;
		session.writeInt(Config.COMMUNICATION_PROTOCOL_VERSION);
		session.state = SEND_CHALLENGE;

		Console.addMsg("sendVersion");
	}

	private void sendChallenge(XEvent xEvent) {
		NSocket session = (NSocket) xEvent.data;
		if (session.bytesAvailable() < 4)
			return;
		int ver = session.readHeadLen();
		if (ver > Config.COMMUNICATION_PROTOCOL_VERSION) {
			Console.addMsg(session.getSocketId() + "-" + "Client Version is " + ver + ". Newer than Server's.");
			SessionContext.deleteSession(session);
			return;
		}
		session.writeInt(Config.challenge);
		session.state = RECEIVE_CHALLENGE;

		Console.addMsg("sendChallenge");
	}

	private void receiveChallengeAndPublicKey(XEvent xEvent) {
		NSocket session = (NSocket) xEvent.data;
		byte[] buffer = readBytes(session);
		if (buffer == null)
			return;
		ByteArray byteArray = new ByteArray(buffer);

		int challenge = byteArray.getInt();
		// 验证码算法
		if (challenge != Config.challenge * 2) {
			Console.addMsg(session.getSocketId() + "-" + "Client Challenge is Wrong " + challenge + " Socket will be Closed.");
			SessionContext.deleteSession(session);
			return;
		}
		Console.addMsg("State Change to RECEIVE_PUB_KEY");

		App.dispatch(ModuleEvent.SERVER_WORKER_CRYPT_CREAT_AES, new Stringbytes(session.getSocketId(), byteArray.getAvailableBytes()));
	}

	private void read(XEvent xEvent) {
		NSocket session = (NSocket) xEvent.data;
		byte[] bs = readBytes(session);
		if (bs == null || bs.length == 0) {
			return;
		}
		// ******多线程派发事件******------第一步-服务端在多线程的情况下,该步骤之后就可以多线程读取访问socket(用专门的IO处理线程的话可以更高效)
		// ******多线程派发事件******------第二步-到这里表示客户端发来的一个完整的消息数据包已经接收到了,派发给解码线程(用可以调用硬件解码的线程解码消息可以更加高效)
		App.dispatch(ModuleEvent.SERVER_WORKER_CRYPT_DECRYPT, new Stringbytes(session.getSocketId(), bs));
		// ******多线程派发事件******------第三步-向一个根据重要性分组的线程安全消息队列加入消息--暂不实现
		// ******多线程派发事件******------第四步-一个线程专门从消息队列中循环取消息后派发消息
		// ******多线程派发事件******------第五步-消息派发器用多线程的方式去处理每一条消息,每个方法用一个线程去执行
	}

	private byte[] readBytes(NSocket session) {
		int bufferLen = session.len;
		if (bufferLen <= 0) {
			bufferLen = session.readDataPackHead();
			if (bufferLen <= 0) {
				App.dispatch(ModuleEvent.SOCKET_DATA_PACKAGE_EMPTY);
				return null;
			}
			session.len = bufferLen;
		}

		byte[] buffer = session.readDataPackBody(bufferLen);
		if (buffer == null)
			return null;
		session.len = 0;
		return buffer;
	}
}
