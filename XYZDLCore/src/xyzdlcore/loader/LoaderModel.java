package xyzdlcore.loader;

/**
 * @author xYzDl
 * @date 2018年1月24日 上午10:46:44
 * @description 加载模型类
 */
public class LoaderModel {
	/**
	 * state 有三种状态 0：初始，1：加载成功，2：加载失败
	 */
	public static final int INIT = 0;
	public static final int SUCCESS = 1;
	public static final int FAIL = 2;

	public LoaderModel(String url) {
		this(url, null, 0, 1, 0f);
	}

	public LoaderModel(String url, Loader loader, int state, int count, float progress) {
		this.url = url;
		this.loader = loader;
		this.state = state;
		this.count = count;
		this.progress = progress;
	}

	public String url;
	public Loader loader;
	public int state;
	public int count;
	public float progress;
}
