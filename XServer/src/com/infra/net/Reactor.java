package com.infra.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.core.Console;
import com.core.Dispatcher;
import com.core.util.XUtil;
import com.infra.Config;
import com.infra.event.EventType;

/**
 * @author xYzDl
 * @date 2018年9月8日 下午7:56:38
 * @description 处理网络IO的反应器
 */
public class Reactor {

	public Dispatcher dispatcher;

	public Reactor() {
		try {
			// 创建通道和选择器
			ServerSocketChannel channel = ServerSocketChannel.open();
			Selector mainSelector = Selector.open();
			Selector subSelector = Selector.open();

			channel.socket().bind(new InetSocketAddress(Config.host, Config.port));
			// 设置通道非阻塞 绑定选择器
			channel.configureBlocking(false).register(mainSelector, SelectionKey.OP_ACCEPT).attach("SocketListener");
			Console.addMsg("Bind to: " + Config.host + ":" + Config.port);

			Thread t = new Thread(new Runnable() {
				public void run() {
					subListener(subSelector);
				}
			}, "Reactor-Sub");
			t.setDaemon(true);
			t.start();

			t = new Thread(new Runnable() {
				public void run() {
					acceptSocket(mainSelector, subSelector);
				}
			}, "Reactor-Main");
			t.setDaemon(true);
			t.start();

		} catch (Exception e) {
			e.printStackTrace();
			dispatcher.dispatch(EventType.CLOSE, "Server Launch Failed!");
		}
	}

	private void subListener(Selector selector) {
		while (true) {
			SocketChannel clientChannel = null;
			try {
				int readyChannels = selector.selectNow();
				if (readyChannels > 0) {
					Set<SelectionKey> readyKeys = selector.selectedKeys();
					Iterator<SelectionKey> iterator = readyKeys.iterator();
					while (iterator.hasNext()) {
						SelectionKey readyKey = iterator.next();
						iterator.remove();
						NSocket nSocket = (NSocket) readyKey.attachment();
						if (readyKey.isReadable()) {// 读数据
							clientChannel = (SocketChannel) readyKey.channel();
							nSocket.readChannel();
							if (nSocket.bytesAvailable() > 0)
								dispatcher.dispatch(EventType.SOCKET_DATA, XUtil.getSocketId(clientChannel.socket()));
						}
						if (readyKey.isWritable()) {// 写数据
							nSocket.flush();
						}
					}
				}
				Thread.sleep(50);
			} catch (Exception e) {
				try {
					clientChannel.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
	}

	private void acceptSocket(Selector mainSelector, Selector subSelector) {
		try {
			Set<SelectionKey> readySelectionKey;
			Iterator<SelectionKey> it;
			SelectionKey selectionKey;
			ServerSocketChannel serverSocketChannel;
			SocketChannel socketChannel;
			while (true) {
				Thread.sleep(1 * 200);
				mainSelector.select(); // 阻塞 直到有就绪事件为止
				readySelectionKey = mainSelector.selectedKeys();
				it = readySelectionKey.iterator();
				while (it.hasNext()) {
					// 必须removed 否则会继续存在，下一次循环还会进来,
					// 注意removed 的位置，针对一个.next() remove一次
					selectionKey = it.next();
					it.remove();
					if (selectionKey.isAcceptable()) {
						System.out.println(selectionKey.attachment() + " - 接受请求事件");
						serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
						socketChannel = serverSocketChannel.accept();
						NSocket xSession = SessionContext.createSession(socketChannel);
						socketChannel.configureBlocking(false).register(subSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE).attach(xSession);

						dispatcher.dispatch(EventType.CONNECT, xSession);
						System.out.println(selectionKey.attachment() + " - 已连接");
					}
					if (selectionKey.isConnectable()) {
						System.out.println(selectionKey.attachment() + " - 连接事件");
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error - " + e.getMessage());
			e.printStackTrace();
		}
	}
}
