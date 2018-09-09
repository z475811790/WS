package ztest;

import com.core.XTimer;
import com.core.event.XEvent;

public class TestCaseXTimer {

	public TestCaseXTimer() {

	}

	public void tt(XEvent xEvent) {
		System.out.println("tt:" + System.currentTimeMillis() % 60000);
	}

	public void tt2(XEvent xEvent) {
		System.out.println("tt2:" + System.currentTimeMillis() % 60000);
	}

	public static void st(XEvent xEvent) {
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("st:" + System.currentTimeMillis() % 60000);
	}

	public static void main(String[] args) throws Exception {
		TestCaseXTimer x = new TestCaseXTimer();
		Object mark  = new Object();
		XTimer.add(x::tt, 500, false, null, mark);
		XTimer.add(TestCaseXTimer::st, 1000, true, null, null);
		Thread.sleep(1500);
		XTimer.remove(mark);
	}
}
