package com.xyzdl;

import java.util.Date;

import javax.swing.JFrame;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.componet.BaseCommand;
import com.core.Console;
import com.core.event.XEvent;
import com.core.loader.LoaderBean;
import com.infra.Config;
import com.infra.net.XServerSocket;
import com.infra.runner.Commander;
import com.infra.runner.Courier;
import com.infra.runner.Cryptor;

public class JServer {

	public JServer() {
		// 先把配置文件加载进来
		LoaderBean lb = new LoaderBean(this::onInitServer, this::onInitServerFailed);
		lb.add(Config.CONF_URL);
		lb.start();
	}

	private void initSpringContext() {
		// 一定要注意外部不要使用Spring容器中的类！！
		BaseCommand.initContext(new ClassPathXmlApplicationContext("applicationContext.xml"));
		Console.addMsg("SpringContext is Initialized Successfully");
	}

	// ------START-事件注册区
	private void onInitServer(XEvent xEvent) {
		// TODO Log4j
		Config.initConf();// 第一:加载好核心配置文件后,初始化配置类
		initView();// 第二:初始化基本界面
		initWorker();// 第三:初始化子线程
		initSpringContext();// 第四:初始化SpringContext
		XServerSocket.singleton();// 第五:初始化服务器通信接口
	}

	private void initView() {
		JFrame frame = new JFrame("JServer");
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(Console.singleton());
		Console.singleton().onInputHandler = Console.singleton()::onInput;
		Console.addMsg("Welcome to Server " + new Date().toString());
		Console.addMsg("Version " + Config.SERVER_VERSION);
		frame.setVisible(true);
	}

	private Cryptor cryptWorker;
	private Commander commander;
	private Courier courier;

	private void initWorker() {
		cryptWorker = new Cryptor();
		commander = new Commander();
		courier = new Courier();
	}

	private void onInitServerFailed(XEvent xEvent) {
		System.out.println("Run Failed");
	}

	// ------END---事件注册区
	// ------START-公共方法区
	// ------END---公共方法区
	// ------START-私有方法区
	// ------END---私有方法区

	public static void main(String[] args) {
		// 显示应用 GUI
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new JServer();
			}
		});
	}
}