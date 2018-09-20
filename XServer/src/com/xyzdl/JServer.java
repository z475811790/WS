package com.xyzdl;

import java.util.Date;
import java.util.Map;

import javax.swing.JFrame;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.componet.BaseCommand;
import com.componet.BaseService;
import com.infra.Config;
import com.infra.net.NServerSocket;

import xyzdlcore.App;
import xyzdlcore.Console;
import xyzdlcore.XTimer;
import xyzdlcore.event.IEventHandler;
import xyzdlcore.event.XEvent;
import xyzdlcore.loader.LoaderBean;

public class JServer {

	public JServer() {
		// 先把配置文件加载进来
		LoaderBean lb = new LoaderBean(this::onInitServer, this::onInitServerFailed);
		lb.add(Config.CONF_URL);
		lb.start();
	}

	// ------START-事件注册区
	private void onInitServer(XEvent xEvent) {
		// TODO Log4j
		Config.initConf();// 第一:加载好核心配置文件后,初始化配置类
		initView();// 第二:初始化基本界面
		initSpringContext();// 第三:初始化SpringContext
		new NServerSocket();// 第四:初始化服务器通信接口
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

	private void initSpringContext() {
		// 一定要注意外部不要使用Spring容器中的类！！
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		App.springContext = context;
		BaseCommand.initContext(context);
		Console.addMsg("SpringContext is Initialized Successfully");
		Map<String, BaseService> map = context.getBeansOfType(BaseService.class);
		XTimer.add(new IEventHandler() {

			@Override
			public void execute(XEvent xEvent) throws Exception {
				// TODO 定时任务属于具体业务，应该放到专门的地方，因为比较耗时，所以用quatrz
				map.values().forEach(e -> {
					e.synBatchInsert();
					e.synBatchUpdate();
				});
			}
		}, Config.SYN_DB_INSERT_UPDATE);
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