package ztest;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.core.ByteArray;
import com.core.util.Hex;

public class TestCaseByteArray {

	public TestCaseByteArray() {
	}

	public static void main(String[] args) {
		ByteArray b1 = new ByteArray();
		ByteArray b2 = new ByteArray();

		b1.putInt(0x12a46f8);

		b1.getBytes(b2);
		// System.out.println(Hex.fromArray(b2));

		byte[] bs = new byte[4];

		System.out.println(Hex.fromArray(bs));

		System.exit(0);
	}

}
