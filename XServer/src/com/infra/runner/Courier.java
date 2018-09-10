package com.infra.runner;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.core.App;
import com.core.event.XEvent;
import com.infra.DestinationData;
import com.infra.event.ModuleEvent;
import com.infra.net.NSocket;

/**
 * @author xYzDl
 * @date 2018年8月17日 下午10:57:42
 * @description 专门负责将消息发送出去
 */
public class Courier {
	private byte[] LOCK = new byte[0];

	private Queue<DestinationData> dataQueue = new LinkedList<>();

	private boolean runMark = true;// 线程停止标记,因为stop方法不建议使用,所以采用标记停止

	public Courier() {
		App.addModuleListener(ModuleEvent.SERVER_WORKER_SEND_SOCKET_MESSAGE, this::onSendSocketMsg);
		new MsgSender().start();
	}

	@SuppressWarnings("unchecked")
	private void onSendSocketMsg(XEvent xEvent) {
		synchronized (LOCK) {
			if (xEvent.data instanceof DestinationData) {
				dataQueue.offer((DestinationData) xEvent.data);
			} else if (xEvent.data instanceof List) {
				dataQueue.addAll((List<DestinationData>) xEvent.data);
			}
			LOCK.notify();
		}
	}

	public void dispose() {
		try {
			runMark = false;
			synchronized (LOCK) {
				LOCK.notify();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class MsgSender extends Thread {
		public MsgSender() {
			super("MsgSender");
			setDaemon(true);
		}

		@Override
		public void run() {
			try {
				DestinationData data;
				NSocket nSocket;
				while (runMark) {
					synchronized (LOCK) {
						data = dataQueue.poll();
						if (data == null) {
							LOCK.wait();
						} else {
							nSocket = NSocket.getSocket(data.socketId);
							// System.out.println("ou:" +
							// Hex.fromArray(data.msgByes));
							if (nSocket != null) {
								nSocket.writeDataPack(data.msgByes);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
