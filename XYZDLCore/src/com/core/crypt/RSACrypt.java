package com.core.crypt;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;

import com.core.util.XUtil;

/**
 * @author xYzDl
 * @date 2018年3月9日 下午11:21:58
 * @description RSA加密类
 */
public class RSACrypt {
	private static final BigInteger BIG_E = new BigInteger("10001", 16);// 65537

	private RSAPublicKey _publicKey;
	private RSAPrivateKey _privateKey;
	private Cipher _enCipher;
	private Cipher _deCipher;

	public RSACrypt() {
	}

	public RSACrypt(byte[] n, byte[] d) {
		BigInteger bign = XUtil.bytesToBigInteger(n);
		BigInteger bigd = XUtil.bytesToBigInteger(d);
		BigInteger bige = new BigInteger("10001", 16);// 65537
		RSAPublicKeySpec pubk = new RSAPublicKeySpec(bign, bige);
		RSAPrivateKeySpec prik = new RSAPrivateKeySpec(bign, bigd);

		try {
			_privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(prik);
			_publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(pubk);// pair.getPublic();
			_enCipher = Cipher.getInstance("RSA");
			_deCipher = Cipher.getInstance("RSA");
			_enCipher.init(Cipher.ENCRYPT_MODE, _publicKey);
			_deCipher.init(Cipher.DECRYPT_MODE, _privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void generateRandomKey() {
		try {
			KeyPairGenerator keyPairGenerator;
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(512);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			_publicKey = (RSAPublicKey) keyPair.getPublic();
			_privateKey = (RSAPrivateKey) keyPair.getPrivate();
			_enCipher = Cipher.getInstance("RSA");
			_deCipher = Cipher.getInstance("RSA");
			_enCipher.init(Cipher.ENCRYPT_MODE, _publicKey);
			_deCipher.init(Cipher.DECRYPT_MODE, _privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setPublicKey(byte[] pk) throws Exception {
		BigInteger bign = XUtil.bytesToBigInteger(pk);
		RSAPublicKeySpec pubk = new RSAPublicKeySpec(bign, BIG_E);
		_publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(pubk);
		_enCipher = Cipher.getInstance("RSA");
		_enCipher.init(Cipher.ENCRYPT_MODE, _publicKey);
	}

	public byte[] d() {
		if (_privateKey != null) {
			byte[] bs = _privateKey.getPrivateExponent().toByteArray(); // BigInteger为正数时有一位符号位(前8个字节)
			if (bs.length % 8 != 0)// BigInteger为正数时必去掉首位
				bs = Arrays.copyOfRange(bs, 1, bs.length);
			return bs;
		}
		return null;
	}

	public byte[] n() {
		if (_publicKey != null) {
			byte[] bs = _publicKey.getModulus().toByteArray(); // BigInteger为正数时有一位符号位(前8个字节)
			if (bs.length % 8 != 0)// BigInteger为正数时必去掉首位
				bs = Arrays.copyOfRange(bs, 1, bs.length);
			return bs;
		}
		return null;
	}

	public byte[] encrypt(byte[] bs) {
		if (_enCipher != null)
			try {
				return _enCipher.doFinal(bs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return bs;
	}

	public byte[] decrypt(byte[] bs) {
		if (_deCipher != null)
			try {
				return _deCipher.doFinal(bs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return bs;
	}
}
