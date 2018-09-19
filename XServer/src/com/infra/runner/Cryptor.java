package com.infra.runner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.infra.Config;
import com.infra.SocketOutData;
import com.infra.SocketInData;
import com.infra.net.NSocket;

import xyzdlcore.Console;
import xyzdlcore.crypt.AESCrypt;
import xyzdlcore.crypt.RSACrypt;
import xyzdlcore.util.Hex;

/**
 * @author xYzDl
 * @date 2018年4月8日 下午9:31:21
 * @description 专门负责加解密的线程
 */
public class Cryptor {
	private byte[] LOCK_CREATE_AES = new byte[0];
	private byte[] LOCK_DECRYPTOR_POOL = new byte[0];
	private byte[] LOCK_ENCRYPTOR_POOL = new byte[0];

	private Map<String, AESCrypt> _keyMap = new HashMap<>();
	private Queue<SocketInData> createKeyMsgQueue = new LinkedList<>();
	private Queue<Decryptor> decryptorPool = new LinkedList<>();
	private ExecutorService deExePool = Executors.newCachedThreadPool();
	private Queue<Encryptor> encryptorPool = new LinkedList<>();
	private ExecutorService enExePool = Executors.newCachedThreadPool();
	private Commander commander = new Commander();
	private Courier courier = new Courier();

	private boolean runMark = true;// 线程停止标记,因为stop方法不建议使用,所以采用标记停止

	public Cryptor() {
		new AESCreator().start();
	}

	public void createAES(SocketInData socketData) {
		synchronized (LOCK_CREATE_AES) {
			createKeyMsgQueue.offer(socketData);
			LOCK_CREATE_AES.notify();
		}
	}

	public void decrypt(SocketInData socketData) {
		Decryptor decryptor;
		synchronized (LOCK_DECRYPTOR_POOL) {
			decryptor = decryptorPool.poll();
			if (decryptor == null)
				decryptor = new Decryptor();
			decryptor.sbs = socketData;
			deExePool.execute(decryptor);
		}
	}

	public void encrypt(SocketOutData destinationData) {
		Encryptor encryptor;
		synchronized (LOCK_ENCRYPTOR_POOL) {
			encryptor = encryptorPool.poll();
			if (encryptor == null)
				encryptor = new Encryptor();
			encryptor.des = destinationData;
			enExePool.execute(encryptor);
		}
	}

	public void dispose() {
		try {
			runMark = false;
			synchronized (LOCK_CREATE_AES) {
				LOCK_CREATE_AES.notify();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class Decryptor implements Runnable {
		public SocketInData sbs;

		@Override
		public void run() {
			try {
				if (sbs != null) {
					if (_keyMap.get(sbs.socketId) == null)
						return;
					// System.out.println("en:" + Hex.fromArray(sbs.bs));
					// long start = System.currentTimeMillis();
					// System.out.println("len:" + sbs.bs.length);
					byte[] bs = (_keyMap.get(sbs.socketId)).decryptBytes(sbs.dataBytes);
					// System.out.println(System.currentTimeMillis() - start);
					sbs.dataBytes = bs;
					// System.out.println("de:" + Hex.fromArray(bs));
					commander.onDecryptComplete(sbs);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				synchronized (LOCK_DECRYPTOR_POOL) {
					sbs = null;
					decryptorPool.offer(this);
				}
			}
		}
	}

	class Encryptor implements Runnable {
		public SocketOutData des;

		@Override
		public void run() {
			try {
				if (des != null) {
					if (des.socketId != null && _keyMap.get(des.socketId) != null) {
						courier.onSendSocketMsg(genData(des.socketId, des.msgByes));
					}
					if (des.socketIds != null && des.socketIds.size() > 0) {
						List<SocketOutData> list = new ArrayList<>();
						if (Config.IS_SAME_AESKEY) {
							// TODO:AESKEY是同一个的情况
						} else {
							for (String socketId : des.socketIds) {
								if (_keyMap.get(socketId) != null)
									list.add(genData(socketId, des.msgByes));
							}
						}
						courier.onSendSocketMsg(list);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				synchronized (LOCK_DECRYPTOR_POOL) {
					encryptorPool.offer(this);
				}
			}
		}

		private SocketOutData genData(String sosketId, byte[] bs) {
			// System.out.println("beEn:"+Hex.fromArray(bs));
			byte[] enbs = _keyMap.get(sosketId).encryptBytes(bs);
			return new SocketOutData(sosketId, enbs);
		}
	}

	class AESCreator extends Thread {
		public AESCreator() {
			super("AESCreator");
			setDaemon(true);
		}

		@Override
		public void run() {
			try {
				SocketInData args;
				String socketId;
				byte[] pk;
				AESCrypt aes;
				RSACrypt rsa;
				while (runMark) {
					synchronized (LOCK_CREATE_AES) {
						args = createKeyMsgQueue.poll();
						if (args == null) {
							LOCK_CREATE_AES.wait();
						} else {
							socketId = args.socketId;
							pk = args.dataBytes;
							aes = new AESCrypt();
							aes.generateRandomKey();
							_keyMap.put(socketId, aes);
							Console.addMsg("sn:" + Hex.fromArray(pk));
							rsa = new RSACrypt();
							rsa.setPublicKey(pk);
							Console.addMsg("s-sk:" + Hex.fromArray(aes.getSecretKey()));
							byte[] encrypted = rsa.encrypt(aes.getSecretKey());
							Console.addMsg("afterEn:" + Hex.fromArray(encrypted));
							NSocket.createAESComplete(new SocketInData(socketId, encrypted));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
