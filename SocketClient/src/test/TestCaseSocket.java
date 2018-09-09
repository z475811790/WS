package test;

import com.core.event.XEvent;
import com.core.util.Hex;
import com.event.EventType;
//import com.net.XSocket;

public class TestCaseSocket {
//	public static XSocket xSocket;

	public TestCaseSocket() {
	}

	public static void main(String[] args) {
		try {
//			xSocket = new XSocket();
//			xSocket.addEventListener(EventType.CONNECT, TestCaseSocket::onConnect);
//			xSocket.addEventListener(EventType.SOCKET_DATA, TestCaseSocket::onSocketData);
//			xSocket.addEventListener(EventType.IO_ERROR, TestCaseSocket::onIOError);
//			xSocket.addEventListener(EventType.CLOSE, TestCaseSocket::onClose);
//			xSocket.connect("127.0.0.1", 5209);
//			xSocket.writeInt(7);
//			xSocket.flush();
			Thread.sleep(10000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void onConnect(XEvent e) {
		System.out.println("connected");
	}

	public static void onSocketData(XEvent e) {
		try {
//			System.out.println("C:" + xSocket.bytesAvailable());
//			System.out.println(xSocket);
//			if (xSocket.bytesAvailable() >= 4) {
//				System.out.println("C_DATA:" + Hex.fromArray(xSocket.readBytes(4)));
//				System.out.println("run");
//			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static void onIOError(XEvent e) {
		System.out.println("IOERROR");
	}

	public static void onClose(XEvent e) {
		System.out.println("CLOSE");
//		xSocket.dispose();
	}
}
