package xyzdlcore.crypt;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import xyzdlcore.ByteArray;
import xyzdlcore.util.Hex;

/**
 * @author xYzDl
 * @date 2018年3月8日 下午11:04:49
 * @description AES加密类
 */
public class AESCrypt {
	private static final int BLOLEN = 16; // 块大小
	private SecretKey _secretKey;
	private Cipher _enCipher;
	private Cipher _deCipher;

	public AESCrypt() {
	}

	public void setSecretKey(byte[] key) {
		if (key == null)
			return;
		if (key.length != 16 && key.length != 24 && key.length != 32)
			throw new Error("AES Key's Length is Wrong");
		_secretKey = new SecretKeySpec(key, "AES");
		genCipher();
	}

	public byte[] getSecretKey() {
		if (_secretKey != null)
			return _secretKey.getEncoded();
		return null;
	}

	/**
	 * 随机生成一个AESKey
	 */
	public void generateRandomKey() {
		Random random = new Random();
		byte[] keybs = Hex.toArray("940600e35a3393eac3aa731090d49764").getAvailableBytes();// new
																							// byte[16];//
		// random.nextBytes(keybs);
		_secretKey = new SecretKeySpec(keybs, "AES");
		genCipher();
	}

	private void genCipher() {
		try {
			_enCipher = Cipher.getInstance("AES/ECB/NoPadding");
			_deCipher = Cipher.getInstance("AES/ECB/NoPadding");
			_enCipher.init(Cipher.ENCRYPT_MODE, _secretKey);
			_deCipher.init(Cipher.DECRYPT_MODE, _secretKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private byte[] encrypt(byte[] bs) throws Exception {
		return _enCipher.doFinal(bs);
	}

	private byte[] decrypt(byte[] bs) throws Exception {
		return _deCipher.doFinal(bs);
	}

	private Map<Integer, ByteArray> _enByteArrayMap = new HashMap<>();// 加密时专用

	/**
	 * 输入的bytes长度一定要大于0，否则加密就没有意义
	 */
	public byte[] encryptBytes(byte[] bs) {
		if (bs == null || bs.length == 0)
			return bs;
		int round = (bs.length - 1) / BLOLEN + 1;
		int needNum = (BLOLEN - bs.length % BLOLEN) % BLOLEN;// 长度小于16字节的块填充到16字节的需要量
		boolean mark = needNum == 0; // 是否是块大小的整数倍
		int realLen = mark ? round * BLOLEN + 1 : round * BLOLEN;

		ByteArray byteArray = getByteArray(realLen, _enByteArrayMap);// _enByteArrayMap.get(realLen);
		synchronized (byteArray) {
			byteArray.putBytes(bs);
			for (int i = 0; i < needNum; i++) {
				byteArray.putByte((byte) needNum);
			}
			try {
				for (int i = 0; i < round; i++) {
					encryptBlock(byteArray, i);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (mark)
				byteArray.putByte((byte) 0xff);
			bs = byteArray.getAvailableBytes();
		}
		return bs;
	}

	/**
	 * 加密数据块
	 * 
	 * @throws Exception
	 */

	private void encryptBlock(ByteArray bytes, int blockIndex) throws Exception {
		byte[] block = bytes.getBytes(BLOLEN);
		block = encrypt(block);
		bytes.position(blockIndex * BLOLEN);
		bytes.putBytes(block);
	}

	private Map<Integer, ByteArray> _deByteArrayMap = new HashMap<>();// 解密时专用

	/**
	 * 解密,返回一组新数据
	 */
	public byte[] decryptBytes(byte[] bs) {
		if (bs == null || bs.length == 0 || bs.length % BLOLEN > 1)
			return bs;// 需要解密的数据长度只可能是整数倍或者余1
		int round = (bs.length - 2) / BLOLEN + 1;
		int i = 0;
		try {
			ByteArray decryptedBytes = getByteArray(round * BLOLEN, _deByteArrayMap);
			synchronized (decryptedBytes) {
				if (bs.length % BLOLEN != 0) {
					decryptedBytes.putBytes(bs, round * BLOLEN);
					for (i = 0; i < round; i++) {
						decryptBlock(decryptedBytes, i);
					}
					bs = decryptedBytes.getAvailableBytes();
				} else {
					decryptedBytes.putBytes(bs);
					for (i = 0; i < round; i++) {
						decryptBlock(decryptedBytes, i);
					}
					decryptedBytes.position(decryptedBytes.length() - 1);
					int needNum = decryptedBytes.getByte();
					bs = decryptedBytes.getBytes(round * BLOLEN - needNum);
				}
			}
			return bs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bs;
	}

	/**
	 * 解密数据块
	 * 
	 * @throws Exception
	 */
	private void decryptBlock(ByteArray bytes, int blockIndex) throws Exception {
		byte[] block = bytes.getBytes(BLOLEN);
		block = decrypt(block);
		bytes.position(blockIndex * BLOLEN);
		bytes.putBytes(block);
	}

	private ByteArray getByteArray(int length, Map<Integer, ByteArray> map) {
		ByteArray array = map.get(length);
		if (array == null) {
			array = new ByteArray(length);
			map.put(length, array);
		}
		array.clear();
		return array;
	}
}
