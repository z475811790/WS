package xyzdlcore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import xyzdlcore.event.IEventHandler;
import xyzdlcore.event.XEvent;

/**
 * @author xYzDl
 * @date 2018年8月26日 上午10:58:19
 * @description 非常有用的一个定时器类 只在添加和移除时同步，其实添加移除都是经过优化的 一瞬间就执行完成，应该可以不用同步的，添加移除操作少
 *              同步处理应该没什么影响 重要的是反复执行方法和添加移除互不影响 不用同步
 *              虽然是借鉴别人的，但是有做移植到java上的修改和优化，可以说是很棒的了
 */
public class XTimer {
	private static final int FREQUENCE = 25; // 基本频率毫秒值
	private static final long SYSTEM_START_TIME = System.currentTimeMillis();// 系统开始启动的时间
	protected static long _nowTime;// 自动程序启动以来的相对时间
	private static int _num = 0;// 当前所有方法数量
	private static int _pos = -1;// 当前执行循环中方法的索引
	private static List<Node> _nodes = new ArrayList<>();
	private static Map<Object, Integer> _handlerIndexMap = new HashMap<>();
	private static Timer _internalTimer = new Timer();
	private static TimerTask _timerTask = new TimerTask() {
		@Override
		public void run() {
			_nowTime = System.currentTimeMillis() - SYSTEM_START_TIME;// 系统已运行时间
			_pos = _num - 1;
			Node n;

			while (_pos >= 0) {
				n = _nodes.get(_pos);
				_pos--;
				if (n.setTime + n.interval > _nowTime)
					continue;
				n.setTime = (n.setTime + n.interval * 2) > _nowTime ? (n.setTime + n.interval) : _nowTime;
				if (n.isOnce)
					remove(n);
				// 函数功能执行须放在remove判断后，避免在执行过程中改变tasks队列及池对象重用引起的判断问题
				// debug版弹出报错，非debug版catch住错误，避免影响后续执行
				try {
					n.handler.execute(n.event);
				} catch (Exception e) {
					System.err.println("SERIOUS ERROR!");
					e.printStackTrace();
				}
			}
		}
	};
	static {
		_internalTimer.schedule(_timerTask, 0, FREQUENCE);
	}

	/**
	 * @param handler
	 *            需要执行的回调
	 * @param interval
	 *            执行间隔时间
	 * @param isOnce
	 *            是否只执行一次
	 * @param data
	 *            传入参数
	 * @param mark
	 *            移除标记
	 */
	public synchronized static void add(IEventHandler handler, int interval, boolean isOnce, Object data, Object mark) {
		if (handler == null)
			return;
		if (mark != null && _handlerIndexMap.get(mark) != null)
			return;

		Node n;
		if (_num == _nodes.size()) {
			n = new Node();
			_nodes.add(n);
		} else {
			n = _nodes.get(_num);
		}

		// 因为需要修改索引位置，所以mark一定要赋值
		n.mark = mark == null ? handler : mark;
		_handlerIndexMap.put(n.mark, _num);
		n.handler = handler;
		n.setTime = _nowTime;
		n.interval = interval;
		n.isOnce = isOnce;
		n.event = new XEvent(data);
		_num++;
	}

	/**
	 * @param mark
	 */
	public synchronized static void remove(Object mark) {
		if (mark == null || _num < 1)
			return;
		Object key = mark;
		if (mark instanceof Node) // 如果走这里说明是只执行一次的回调，内部分调用的移除
			key = ((Node) mark).mark;
		Integer index = _handlerIndexMap.get(key);
		if (index == null || index > _num)
			return;
		Node n = _nodes.get(index);
		Node pn;
		int curr = index;// 需要移除的任务最终所在索引
		if (index <= _pos) {
			// 删除在列表中尚未执行的任务，将其与执行队列末位任务互换
			// 前移指针跳过删除的任务
			pn = _nodes.get(_pos);
			_handlerIndexMap.put(pn.mark, index);
			_handlerIndexMap.put(key, _pos);

			_nodes.set(index, pn);
			_nodes.set(_pos, n);
			curr = _pos;
			_pos--;
		}
		// 与任务列表尾节点互换完成删除
		pn = _nodes.get(_num - 1);
		_nodes.set(curr, pn);
		_nodes.set(_num - 1, n);
		_num--;
		_handlerIndexMap.put(pn.mark, curr);
		_handlerIndexMap.remove(key);
	}

	/**
	 * 无参数不移除需要一直循环执行
	 * 
	 * @param handler
	 *            需要执行的回调
	 * @param interval
	 *            执行间隔时间
	 */
	public static void add(IEventHandler handler, int interval) {
		add(handler, interval, false, null, null);
	}
}

class Node {
	public Object mark;// handler的标记
	public IEventHandler handler; // 回调方法
	public long setTime; // 设置时间
	public int interval; // 回调间隔
	public boolean isOnce; // 执行一次否
	public XEvent event; // 自定义数据

	public void invoke() throws Exception {
		if (handler == null)
			return;
		handler.execute(event);
	}
}
