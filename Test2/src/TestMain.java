public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s = "<p>123</p><p/><p/><p/><p>1245</p>".replaceAll("<p>", "");
		String[] ss = s.split("(</p>)|(<p/>)");
		System.out.println(ss.length);
	}
}
