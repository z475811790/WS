package test;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.core.crypt.AESCrypt;
import com.core.util.Hex;

public class TestCaseCrypto {

	public TestCaseCrypto() {
	}

	public static void main(String[] args) throws Exception {
		testCaseRSA();
		// testCaseAES();
		// testCaseAESCrypt();
//		testCaseRandomRSA();
	}

	public static void testCaseAES() throws Exception {
		Cipher enCipher = Cipher.getInstance("AES/ECB/NoPadding");
		Cipher deCipher = Cipher.getInstance("AES/ECB/NoPadding");
		byte[] keyBytes = Hex.toArray("360f0f0f0f0f0f0f0f0f0f0f0f0f0f0f").getAvailableBytes();
		System.out.println(Hex.fromArray(keyBytes));

		SecretKey key = new SecretKeySpec(Hex.toArray("422ca63bd8231d0c0e04c0221d9065a7").getAvailableBytes(), "AES");
		enCipher.init(Cipher.ENCRYPT_MODE, key);
		deCipher.init(Cipher.DECRYPT_MODE, key);
		byte[] enBytes = enCipher.doFinal(keyBytes);
		System.out.println(Hex.fromArray(enBytes));
		System.out.println(Hex.fromArray(deCipher.doFinal(enBytes)));
	}

	public static void testCaseAESCrypt() throws Exception {
		AESCrypt aes = new AESCrypt();
		aes.setSecretKey(Hex.toArray("422ca63bd8231d0c0e04c0221d9065a7").getAvailableBytes());
		byte[] bs = aes.encryptBytes(Hex.toArray("0000000a0a033132331206313233343536").getAvailableBytes());
		System.out.println(Hex.fromArray(bs));
	}

	public static void testCaseRandomRSA() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(512);

		KeyPair pair = generator.generateKeyPair();

		RSAPrivateKey privateKey = (RSAPrivateKey) pair.getPrivate();
		RSAPublicKey publicKey = (RSAPublicKey) pair.getPublic();
		
		System.out.println(privateKey.getPrivateExponent().toString(16));
		System.out.println(publicKey.getPublicExponent().toString(16));
		System.out.println(publicKey.getModulus().toString(16));
		
		Cipher encipher = Cipher.getInstance("RSA");// java默认"RSA"="RSA/ECB/PKCS1Padding"
		encipher.init(Cipher.ENCRYPT_MODE, publicKey);

		Cipher deCipher = Cipher.getInstance("RSA");
		deCipher.init(Cipher.DECRYPT_MODE, privateKey);

		byte[] en = encipher.doFinal(Hex.toArray("a1abcdef").getAvailableBytes());
		System.out.println("en:" + Hex.fromArray(en));
		System.out.println("de:" + Hex.fromArray(deCipher.doFinal(en)));
	}

	public static void testCaseRSA() throws Exception {
		String n = "";
		String d = "";
		String e = "";
		BigInteger bign = new BigInteger("d65e6d75aeda689cfab5efa15e134a7fa416765c568940ec93ab51c88be3581561ed258824fb1f366324cb6b412416452972f23737a816933fd3f156c00a0d9d", 16);
		BigInteger bigd = new BigInteger("b38a28b11cbe2e49f3acf74336907f9fc1e5524269f3d09d93dc33c5fc6b6f7407b141a12d1c2c6169d1fcb090a63072ad742d6eba45b326dffdd32b6e361281", 16);
		BigInteger bige = new BigInteger("10001", 16);// 65537

		// BigInteger bign = new
		// BigInteger("95701876885335270857822974167577168764621211406341574477817778908798408856077334510496515211568839843884498881589280440763139683446418982307428928523091367233376499779842840789220784202847513854967218444344438545354682865713417516385450114501727182277555013890267914809715178404671863643421619292274848317157");
		// BigInteger bigd = new
		// BigInteger("15118200884902819158506511612629910252530988627643229329521452996670429328272100404155979400725883072214721713247384231857130859555987849975263007110480563992945828011871526769689381461965107692102011772019212674436519765580328720044447875477151172925640047963361834004267745612848169871802590337012858580097",
		// 16);
		// BigInteger bige = new BigInteger("65573");// 65537

		RSAPublicKeySpec pubk = new RSAPublicKeySpec(bign, bige);
		RSAPrivateKeySpec prik = new RSAPrivateKeySpec(bign, bigd);

		// KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		// generator.initialize(512);

		// KeyPair pair = generator.generateKeyPair();

		RSAPrivateKey privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(prik);// pair.getPrivate();
		RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(pubk);// pair.getPublic();
		System.out.println(privateKey.getPrivateExponent().toString(16));
		System.out.println(publicKey.getPublicExponent().toString(16));
		System.out.println(publicKey.getModulus().toString(16));
		

		Cipher encipher = Cipher.getInstance("RSA");// java默认"RSA"="RSA/ECB/PKCS1Padding"
		encipher.init(Cipher.ENCRYPT_MODE, publicKey);

		Cipher deCipher = Cipher.getInstance("RSA");
		deCipher.init(Cipher.DECRYPT_MODE, privateKey);

		byte[] en = encipher.doFinal(Hex.toArray("a1abcdef").getAvailableBytes());
		System.out.println("en:" + Hex.fromArray(en));
		System.out.println("de:" + Hex.fromArray(deCipher.doFinal(en)));
	}
}
