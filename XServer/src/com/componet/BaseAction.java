package com.componet;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xYzDl
 * @date 2018年9月1日 下午9:05:47
 * @description Action的每一个方法对应一条消息
 */
public class BaseAction {

	@Autowired
	public SocketOuter outer;
	@Autowired
	public SocketUserMap socketUserMap;

	public BaseAction() {
	}
}
