package ztest;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import com.core.Dispatcher;
import com.core.event.XEvent;
import com.core.util.Hex;
import com.infra.Config;
import com.infra.event.EventType;
import com.infra.net.NServerSocket;
import com.infra.net.NSocket;

public class TestReactor {
	public boolean mark = true;

	@Before
	public void testBefore() throws Exception {
		Dispatcher dispatcher = new Dispatcher();
		dispatcher.add(EventType.CONNECT, this::onConnect, null);

		NServerSocket reactor = new NServerSocket();
	}

	private void onConnect(XEvent xEvent) {
		NSocket xSession = (NSocket) xEvent.data;
		// xSession.writeInt(Config.COMMUNICATION_PROTOCOL_VERSION);
	}


	@Test
	public void testConnect() throws Exception {
		Scanner scan = new Scanner(System.in);
		scan.nextLine();

		Socket socket = new Socket("127.0.0.1", 8080);// BIO 阻塞
		System.out.println("连接成功");

		// 下面这种写法，不用关闭客户端，服务器端也是可以收到的
		OutputStream os = socket.getOutputStream();

		Thread t = new Thread(new Runnable() {

			public void run() {
				try {
					while (mark) {
						String str = scan.nextLine();
						byte[] bs = Hex.toArray(str).getAvailableBytes();
						os.write(bs);
					}
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.setName("outer");
		t.start();
		byte[] buf = new byte[32];
		System.out.println("准备读取数据~~");

		while (mark) {
			try {
				int count = socket.getInputStream().read(buf); // 会阻塞
				if (count == -1)
					continue;
				System.out.println("方式一： 读取数据" + Hex.fromArray(buf) + " count = " + count);
				Thread.sleep(1 * 100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("client socket close");
		try {
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}