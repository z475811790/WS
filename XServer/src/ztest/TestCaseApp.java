package ztest;

import org.junit.Test;

import xyzdlcore.App;
import xyzdlcore.Dispatcher;
import xyzdlcore.event.XEvent;

public class TestCaseApp {
	@Test
	public void testApp() {
		String ns = new String("x");
		Dispatcher dispatcher = new Dispatcher();
		dispatcher.add("x", this::eve, null);
		dispatcher.add(ns, this::eve, null);
		
		
		dispatcher.dispatch("x", null);
		
	}

	public void eve(XEvent xEvent) {
		System.out.println("execute");
	}
}
