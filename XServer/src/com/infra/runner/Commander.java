package com.infra.runner;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.componet.BaseCommand;
import com.infra.CommandData;
import com.infra.Config;
import com.infra.SocketInData;
import com.message.Message.MessageEnum.MessageId;

import xyzdlcore.util.XUtil;

/**
 * @author xYzDl
 * @date 2018年8月13日 下午11:15:30
 * @description 负责将执行解密后的命令
 */
public class Commander {
	private byte[] LOCK_COMMAND = new byte[0];
	private byte[] LOCK_EXECUTOR_POOL = new byte[0];
	private Map<Integer, Class<BaseCommand>> msgMap = new HashMap<>();

	private Queue<CommandData> dataQueue = new LinkedList<>();
	private Queue<Executor> executorPool = new LinkedList<>();
	private ExecutorService pool = Executors.newCachedThreadPool();

	private boolean runMark = true;// 线程停止标记,因为stop方法不建议使用,所以采用标记停止

	public Commander() {
		registerMsg();
		new CommandDispatcher().start();
	}

	@SuppressWarnings("unchecked")
	private void registerMsg() {
		try {
			// 注册需要处理的消息
			for (MessageId mid : MessageId.values()) {
				if (mid.toString().toUpperCase().startsWith("S"))
					continue;
				Class<?> klass = Class.forName(Config.COMMAND_PREFIX + mid.toString() + Config.COMMAND_SUFFIX);
				msgMap.put(mid.getNumber(), (Class<BaseCommand>) klass);
			}
		} catch (Exception e) {
			System.err.println("未定义对应的消息处理命令类！");
			e.printStackTrace();
		}
	}

	public void onDecryptComplete(SocketInData socketData) {
		int msgId = XUtil.bytesToInt(socketData.dataBytes);
		byte[] msgBytes = new byte[socketData.dataBytes.length - 4];
		System.arraycopy(socketData.dataBytes, 4, msgBytes, 0, msgBytes.length);
		// System.out.println("id:" + msgId);
		// System.out.println(Hex.fromArray(msgBytes));
		synchronized (LOCK_COMMAND) {
			dataQueue.offer(new CommandData(socketData.socketId, msgId, msgBytes));
			LOCK_COMMAND.notify();
		}
	}

	public void dispose() {
		try {
			runMark = false;
			synchronized (LOCK_COMMAND) {
				LOCK_COMMAND.notify();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class Executor implements Runnable {
		CommandData data;
		MessageId id;
		Class<BaseCommand> klass;

		@Override
		public void run() {
			try {
				if (data != null) {
					klass = msgMap.get(data.msgId);
					if (klass != null) {
						BaseCommand command = klass.newInstance();
						command.data = data;
						command.execute();
					} else {
						System.err.println("消息未注册：" + id);
					}
				}
			} catch (Exception e) {
				// TODO:通过消息派发方式将异常传回给客户端
				e.printStackTrace();
			} finally {
				synchronized (LOCK_EXECUTOR_POOL) {
					executorPool.offer(this);
				}
			}
		}
	}

	/**
	 * @author xYzDl
	 * @date 2018年8月13日 下午11:16:51
	 * @description 不在解密后直接派发，而多用一个线程和命令队列派发命令是为了更加灵活地处理命令的派发
	 */
	class CommandDispatcher extends Thread {
		public CommandDispatcher() {
			super("CommandDispatcher");
			setDaemon(true);
		}

		@Override
		public void run() {
			try {
				CommandData data;
				Executor executor;
				while (runMark) {
					synchronized (LOCK_COMMAND) {
						data = dataQueue.poll();
						if (data == null) {
							LOCK_COMMAND.wait();
						} else {
							synchronized (LOCK_EXECUTOR_POOL) {
								executor = executorPool.poll();
								if (executor == null)
									executor = new Executor();
								executor.data = data;
								pool.execute(executor);
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
