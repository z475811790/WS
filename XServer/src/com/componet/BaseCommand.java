package com.componet;

import org.springframework.context.ApplicationContext;

import com.infra.CommandData;

/**
 * @author xYzDl
 * @date 2018年8月12日 下午10:29:14
 * @description: 命令基类 命令模式命令具体实现类是以对应消息来命名LoginCMD对应消息为C_Login
 */
public abstract class BaseCommand {
	private static ApplicationContext context;

	public CommandData data;

	public BaseCommand() {
	}

	public static void initContext(ApplicationContext springContext) {
		context = springContext;
	}

	/**
	 * 从Spring容器中获取Action Bean
	 * 
	 * @param actionClass
	 * @return
	 */
	protected <T> T getAction(Class<T> actionClass) {
		return context.getBean(actionClass);
	}

	public void execute() throws Exception {
		if (data == null || data.msgBytes == null) {
			throw new Exception("消息数据对象为空！");
		}
	}

	public void dispose() {
		if (data == null)
			return;
		data.socketId = null;
		data.msgBytes = null;
	}

}