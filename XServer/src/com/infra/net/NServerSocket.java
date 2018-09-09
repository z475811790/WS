package com.infra.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.core.App;
import com.core.Console;
import com.infra.Config;
import com.infra.event.ModuleEvent;

/**
 * @author xYzDl
 * @date 2018年9月8日 下午7:56:38
 * @description 服务器Socket
 */
public class NServerSocket {

	public NServerSocket() {
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
			App.dispatch(ModuleEvent.SERVER_SOCKET_CLOSE, "Server Launch Failed!");
			// SessionContext.deleteSession(SessionContext.getSessionBySocketId(xEvent.data.toString()));
			// App.dispatch(ModuleEvent.SOCKET_CLOSE, xEvent.data.toString());
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
						serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
						socketChannel = serverSocketChannel.accept();
						NSocket nSocket = SessionContext.createSession(socketChannel);
						socketChannel.configureBlocking(false).register(subSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE).attach(nSocket);

						nSocket.sendVersion();
						Console.addMsg("Start to Verify...");
					}
					if (selectionKey.isConnectable()) {
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error - " + e.getMessage());
			e.printStackTrace();
		}
	}
}
