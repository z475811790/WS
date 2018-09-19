package xyzdlcore.util;

/**
 * @author xYzDl
 * @date 2018年8月19日 下午11:19:29
 * @description 时间工具类
 */
public class TimeUtil {

	/**
	 * @param time
	 *            传入的是UTC时间戳 需要加上时区
	 * @return例如12:05:30
	 */
	public static String toShortTime(String time) {
		Long d = Long.parseLong(time) + 8 * 3600 * 1000;
		d = d % (24 * 3600 * 1000);
		int h = (int) (d / 1000 / 3600);
		d = d % (3600 * 1000);
		int m = (int) (d / 1000 / 60);
		d = d % (60 * 1000);
		int s = (int) (d / 1000);
		return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
	}
}
