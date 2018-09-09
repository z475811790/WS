package com.core.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.core.ByteArray;
import com.core.XML;
import com.core.event.IEventHandler;
import com.core.event.XEvent;

/**
 * @author xYzDl
 * @date 2018年1月24日 上午10:32:51
 * @description 加载单元类
 */
public class LoaderBean {
	private static final int PROGRESS_INTERVAL = 200;// 进度检查时间间隔

	private List<LoaderModel> _loadQueue = new ArrayList<>();
	private Map<String, Float> _progressMap = new HashMap<>();
	public IEventHandler loadCompleteHandler;
	public IEventHandler loadFailHandler;
	private static ConcurrentMap<String, ByteArray> _resourceMap = new ConcurrentHashMap<>();
	private long _lastRecordTime = 0;
	private byte[] LOCK = new byte[0];

	/**
	 * @param loadCompleteHandler
	 *            加载成功的回调
	 * @param loadFailHandler
	 *            加载失败的回调
	 */
	public LoaderBean(IEventHandler loadCompleteHandler, IEventHandler loadFailHandler) {
		this.loadCompleteHandler = loadCompleteHandler;
		this.loadFailHandler = loadFailHandler;
	}

	// ------START-事件注册区
	// ------END---事件注册区
	// ------START-公共方法区
	public void add(String url) {
		if (!hasUrl(url))
			_loadQueue.add(new LoaderModel(url));
	}

	public void start() {
		for (LoaderModel model : _loadQueue) {
			_progressMap.put(model.url, 0f);
			if (_resourceMap.get(model.url) != null) {
				model.state = LoaderModel.SUCCESS;
			} else {
				loadUrl(model);
			}
		}
		try {
			isLoadFinished();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param url
	 *            获取url的资源 不存在相应资源会返回null
	 * @return
	 */
	public static ByteArray getRes(String url) {
		return _resourceMap.get(url);
	}

	/**
	 * @param url
	 *            获取url的XML资源 不存在相应资源会返回null
	 * @return
	 */
	public static XML getResXML(String url) {
		return getRes(url) == null ? null : new XML(getRes(url));
	}

	// ------END---公共方法区
	// ------START-私有方法区
	private void loadUrl(LoaderModel model) {
		LoadThread t = new LoadThread(model);
		t.start();
	}

	private void isLoadFinished() {
		boolean mark = true;
		int success = 0;
		for (LoaderModel model : _loadQueue) {
			if (model.state == LoaderModel.INIT) {
				mark = false;
				break;
			} else if (model.state == LoaderModel.SUCCESS) {
				success++;
			}
		}
		int size = _loadQueue.size();
		System.out.println("总加载进度：" + String.format("%.2f%%", size == 0 ? 100 : getTotalProgress() / size * 100));
		if (mark) {
			try {
				if (success == _loadQueue.size()) {
					if (loadCompleteHandler != null)
						loadCompleteHandler.execute(null);
				} else {
					if (loadFailHandler != null)
						loadFailHandler.execute(new XEvent(_loadQueue));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private float getTotalProgress() {
		float sum = 0;
		for (float f : _progressMap.values()) {
			sum += f;
		}
		return sum;
	}

	private boolean hasUrl(String url) {
		boolean mark = false;
		for (LoaderModel model : _loadQueue) {
			if (model.url == url) {
				mark = true;
				break;
			}
		}
		return mark;
	}
	// ------END---私有方法区

	class LoadThread extends Thread {
		public LoaderModel model;

		public LoadThread(LoaderModel model) {
			super(model.url);
			this.model = model;
		}

		@Override
		public void run() {
			if (model == null || model.state == LoaderModel.FAIL)
				return;
			if (model.loader == null) {
				model.loader = new Loader();
			}
			model.loader.addListener(LoaderEvent.COMPLETE, this::onComplete);
			model.loader.addListener(LoaderEvent.IO_ERROR, this::onIOError);
			model.loader.addListener(LoaderEvent.PROGRESS, this::onProgress);

			while (model.state == LoaderModel.INIT && model.count <= 3) {
				model.loader.load(model.url);
			}
			dispose();
		}

		private void onComplete(XEvent xEvent) {
			synchronized (LOCK) {
				model.state = LoaderModel.SUCCESS;
				_resourceMap.put(model.url, (ByteArray) xEvent.data);
				isLoadFinished();
			}
		}

		private void onIOError(XEvent xEvent) {
			System.out.println(xEvent.data + " " + model.url + " 加载失败次数 " + model.count);
			if (model.count >= 3) {
				synchronized (LOCK) {
					System.out.println("加载文件失败已达最大次数 " + model.url);
					model.state = LoaderModel.FAIL;
					isLoadFinished();
				}
			}
			model.count++;
		}

		private void onProgress(XEvent xEvent) {
			_progressMap.put(model.url, (Float) xEvent.data);
			if (System.currentTimeMillis() - _lastRecordTime > PROGRESS_INTERVAL) {
				synchronized (LOCK) {
					if (System.currentTimeMillis() - _lastRecordTime > PROGRESS_INTERVAL) {
						System.out.println(
								"总加载进度：" + String.format("%.2f%%", getTotalProgress() / _progressMap.size() * 100));
					}
					_lastRecordTime = System.currentTimeMillis();
				}
			}
		}

		private void dispose() {
			model.loader.removeListener(LoaderEvent.COMPLETE);
			model.loader.removeListener(LoaderEvent.IO_ERROR);
			model.loader.removeListener(LoaderEvent.PROGRESS);
			model = null;
		}
	}

}
