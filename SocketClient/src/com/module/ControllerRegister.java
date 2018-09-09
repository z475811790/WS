package com.module;

import com.module.chat.ChatController;
import com.module.main.MainController;

/**
 * @author xYzDl
 * @date 2018年2月28日 下午9:49:00
 * @description 控制器注册器
 */
public class ControllerRegister {

	private static ControllerRegister _singleton;

	public static ControllerRegister singleton() {
		if (_singleton == null)
			_singleton = new ControllerRegister();
		return _singleton;
	}

	/**
	 * 在主界面初始化完成之后开始注册事件
	 */
	public ControllerRegister() {
		// 控制器初始化的同时一定不要向服务发送请求,在所有控制器初始化完后再按一定顺序延时发送请求
		MainController.singleton();
		ChatController.singleton();
		initData();
	}

	/**
	 * 发送请求来初始化数据
	 */
	public void initData() {

	}
}
