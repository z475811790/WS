package xyzdlcore.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import xyzdlcore.ByteArray;
import xyzdlcore.Dispatcher;
import xyzdlcore.event.IEventHandler;
import xyzdlcore.util.StringUtil;

/**
 * @author xYzDl
 * @date 2018年1月26日 上午11:01:35
 * @description 资源加载器类
 */
public class Loader {

	private static final int FILE_READ_BLOCK_SIZE = 1024;// 每次读取多少字节大小的文件块
	private static final int PROGRESS_VERIFY_COUNT = 5;// 进度检查间隔次数
	private static final int PROGRESS_VERIFY_TIME = 500;// 进度检查间隔时间

	private Dispatcher _dispatcher = new Dispatcher();
	private ByteArray _byteArray;
	private int _fileSize = 0;

	public Loader() {

	}

	public void load(String url) {
		FileInputStream fileInputStream = null;
		try {
			File file = new File(url);
			if (StringUtil.isNullOrWhiteSpaceOrEmpty(url)) {
				throw new FileNotFoundException(url);
			}
			if (!file.exists() || !file.isFile()) {
				throw new FileNotFoundException(url);
			}
			if (file.length() > Integer.MAX_VALUE) {
				throw new IOException(url + " 文件长度超过最大限度 " + Integer.MAX_VALUE);
			}
			_fileSize = (int) file.length();
			fileInputStream = new FileInputStream(file);
			_byteArray = new ByteArray(_fileSize);
			int times = _fileSize / FILE_READ_BLOCK_SIZE;
			int reminder = _fileSize % FILE_READ_BLOCK_SIZE;

			byte[] bs = new byte[FILE_READ_BLOCK_SIZE];
			long lastVerifyTime = System.currentTimeMillis();
			for (int i = 0; i < times; i++) {
				fileInputStream.read(bs);
				_byteArray.putBytes(bs);
				if ((i + 1) % PROGRESS_VERIFY_COUNT == 0) {
					if (System.currentTimeMillis() - lastVerifyTime > PROGRESS_VERIFY_TIME) {
						_dispatcher.dispatch(LoaderEvent.PROGRESS, (float) _byteArray.length() / _fileSize);
					}
				}
				// Thread.sleep(500);
			}
			if (reminder > 0) {
				bs = new byte[reminder];
				fileInputStream.read(bs);
				_byteArray.putBytes(bs);
			}
			_byteArray.position(0);
			_dispatcher.dispatch(LoaderEvent.PROGRESS, 1.0f);
			_dispatcher.dispatch(LoaderEvent.COMPLETE, _byteArray);
		} catch (Exception e) {
			_dispatcher.dispatch(LoaderEvent.IO_ERROR, e);
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param eventType
	 *            COMPLETE;IO_ERROR;PROGRESS
	 * @param listener
	 */
	public void addListener(String eventType, IEventHandler listener) {
		_dispatcher.add(eventType, listener, eventType);
	}

	public void removeListener(String eventType) {
		_dispatcher.remove(eventType, eventType);
	}
}
