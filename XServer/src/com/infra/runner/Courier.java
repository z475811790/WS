package com.infra.runner;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.infra.SocketOutData;
import com.infra.net.NSocket;

/**
 * @author xYzDl
 * @date 2018年8月17日 下午10:57:42
 * @description 专门负责将消息发送出去
 */
public class Courier {
	private byte[] LOCK = new byte[0];

	private Queue<SocketOutData> dataQueue = new LinkedList<>();

	private boolean runMark = true;// 线程停止标记,因为stop方法不建议使用,所以采用标记停止

	public Courier() {
		new MsgSender().start();
	}

	public void onSendSocketMsg(SocketOutData data) {
		synchronized (LOCK) {
			dataQueue.offer(data);
			LOCK.notify();
		}
	}

	public void onSendSocketMsg(List<SocketOutData> datas) {
		synchronized (LOCK) {
			dataQueue.addAll(datas);
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
				SocketOutData data;
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
