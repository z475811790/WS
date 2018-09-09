package test;

import java.nio.ByteBuffer;

import com.configuration.DataConfig;
import com.core.ByteArray;
import com.core.util.Hex;
import com.google.protobuf.Message;
import com.message.Info.C_INFO;
import com.message.Message.MessageEnum.MessageId;

public class TestCaseMain {
	public static void main(String[] args) {
		ByteArray byteArray = new ByteArray(4);
		byteArray.putByte((byte) 1);
		byteArray.putByte((byte) 2);
		byteArray.position(0);
		byteArray.putBytes(new byte[] { 3, 4, 5 });
	}

	public TestCaseMain() {
		// testCaseProtobuf();
		// testCaseWrite();
		// testCaseGenMsg();
//		System.out.println("start to parse");
//		DataConfig config = DataConfig.singleton();
//		System.out.println(config+"config parse completed");
	}

	public void testCaseProtobuf() {
		C_INFO.Builder builder = C_INFO.newBuilder();
		builder.setContent("xyzdl");
		C_INFO msg = builder.build();
		byte[] bs = msg.toByteArray();
		System.out.println(bs.length);

		try {
			C_INFO info = C_INFO.parseFrom(bs);
			System.out.println(info.getContent());
			Message message = C_INFO.parseFrom(bs);
			int id = MessageId.C_ChatMsg.getNumber();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testCaseWrite() {
		C_INFO.Builder builder = C_INFO.newBuilder();
		builder.setContent("xyzdsdgdsl");
		C_INFO msg = builder.build();
		ByteArray byteArray = new ByteArray(msg.toByteArray());

		System.out.println(msg.getClass().getSimpleName());
		System.out.println(MessageId.valueOf("C_ChatMsg").getNumber());
		System.out.println(msg.getSerializedSize());
		System.out.println(msg.toByteArray().length);
		try {
			C_INFO info = C_INFO.parseFrom(byteArray.getBytes(byteArray.bytesAvailable()));
			System.out.println(info.getContent());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testCaseGenMsg() {
		C_INFO.Builder builder = C_INFO.newBuilder();
		builder.setContent("xyzdsdg肖梦帆dsl");
		C_INFO msg = builder.build();
		ByteArray transitBytes = new ByteArray(msg.getSerializedSize() + 4);// 消息字节长度加消息id的总长度
		try {
			transitBytes.putInt(MessageId.valueOf(msg.getClass().getSimpleName()).getNumber());
			transitBytes.putBytes(msg.toByteArray());
			System.out.println(Hex.fromArray(transitBytes));

			System.out.println(transitBytes.getInt());
			// System.out.println(Hex.fromArray(transitBytes.getAvailableBytes()));
			C_INFO info = C_INFO.parseFrom(transitBytes.getAvailableBytes());
			System.out.println(info.getContent());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
