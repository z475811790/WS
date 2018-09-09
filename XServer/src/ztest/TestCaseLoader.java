package ztest;

import com.core.ByteArray;
import com.core.event.IEventHandler;
import com.core.event.XEvent;
import com.core.loader.Loader;
import com.core.loader.LoaderEvent;

public class TestCaseLoader {

	public TestCaseLoader() {
	}

	public static void main(String[] args) {
		// testLoader();
		testLoaderBean();
		System.exit(0);
	}

	public static void testLoader() {
		Loader loader = new Loader();
		loader.addListener(LoaderEvent.IO_ERROR, new IEventHandler() {

			@Override
			public void execute(XEvent xEvent) {
				System.out.println(xEvent.data.toString());
			}
		});
		loader.addListener(LoaderEvent.COMPLETE, new IEventHandler() {

			@Override
			public void execute(XEvent xEvent) {
				ByteArray byteArray = (ByteArray) xEvent.data;
				System.out.println("bytes length:" + byteArray.length());
				System.out.println("Load Complete");
			}
		});
		loader.addListener(LoaderEvent.PROGRESS, new IEventHandler() {

			@Override
			public void execute(XEvent xEvent) {
				System.out.println(String.format("%.2f%%", (float) xEvent.data * 100));
			}
		});
		loader.load("config/config.xml");
	}

	public static void testLoaderBean() {
		// LoaderBean lb = new LoaderBean();
		//
		// lb.add("config/12.rar");
		// lb.add("config/config1.xml");
		// lb.loadCompleteHandler = new IEventHandler() {
		//
		// @Override
		// public void execute(XEvent xEvent) throws Exception {
		// ByteArray bytes;
		// bytes = LoaderBean.getRes("config/12.rar");
		// System.out.println(bytes.length());
		// bytes = LoaderBean.getRes("config/configDemo12.xml");
		// System.out.println(bytes.length());
		//
		// System.out.println("OVER");
		// }
		// };
		// lb.loadFailHandler = new IEventHandler() {
		//
		// @Override
		// public void execute(XEvent xEvent) throws Exception {
		// System.out.println("failed");
		// }
		// };
		// lb.start();
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
