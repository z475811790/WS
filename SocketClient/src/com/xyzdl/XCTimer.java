package com.xyzdl;

import xyzdlcore.XTimer;

/**
 * @author xYzDl
 * @date 2018年8月26日 下午9:50:18
 * @description 添加一些客户端需要的功能
 */
public class XCTimer extends XTimer {
	private static final int TIME_OFFSET = 100;// 与服务器时间的偏移差值，毫秒
	private static long _synTime = 0;
	private static long _setTime = 0;

	public static void synServerTime(long time) {
		_synTime = time;
		_setTime = _nowTime;
	}

	/**
	 * 获取服务器时间 时间戳
	 */
	public static long serverTime() {
		return _nowTime - _setTime + _synTime + TIME_OFFSET;
	}
}
