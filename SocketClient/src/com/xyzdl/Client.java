package com.xyzdl;

import java.util.Date;

import javax.swing.JFrame;

import com.configuration.DataConfig;
import com.configuration.GeneralConfig;
import com.configuration.LanPack;
import com.core.Console;
import com.core.event.XEvent;
import com.core.loader.LoaderBean;
import com.core.util.AssetUtil;
import com.module.ControllerRegister;

public class Client {

	public Client() {
		// 先把配置文件加载进来
		LoaderBean lb = new LoaderBean(this::onInitApp, this::onInitAppFailed);
		lb.add(AssetUtil.COMMON_CONFIG);
		lb.add(AssetUtil.GENERAL_CONFIG);
		lb.add(AssetUtil.LANGUAGE_PACKAGE);
		lb.add(AssetUtil.DATA_CONFIG);
		lb.start();
	}

	// ------START-事件注册区
	private void onInitApp(XEvent xEvent) {
		Config.initConf();// 第一:加载好核心配置文件后,初始化配置类
		initView();// 第二:初始化基本界面
		LanPack.singleton();// 第三:初始化语言包配置
		GeneralConfig.singleton();// 第四:初始化客户端配置
		DataConfig.singleton();// 第五步:初始化数据表配置
		ControllerRegister.singleton();
		// new TestCaseMain();
	}

	private void initView() {
		JFrame frame = new JFrame("Client");
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(Console.singleton());
		Console.addMsg("Welcome to Client " + new Date().toString());
		Console.addMsg("Version " + Config.CLIENT_VERSION);
		// Console.singleton().onInputHandler = Console.singleton()::onInput;23
		frame.setVisible(true);
	}

	private void onInitAppFailed(XEvent xEvent) {
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
				new Client();
			}
		});
	}

}
