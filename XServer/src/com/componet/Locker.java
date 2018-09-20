package com.componet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class Locker {
	private Map<Integer, Map<String, Object>> playerSelfLockMap = new ConcurrentHashMap<>();

	public Object getPlayerLockByType(Integer playerID, String lockType) {
		Map<String, Object> lockCol = playerSelfLockMap.get(playerID);
		if (lockCol == null) {
			lockCol = new ConcurrentHashMap<String, Object>();
			playerSelfLockMap.put(playerID, lockCol);
		}
		Object lock = lockCol.get(lockType);
		if (lock == null) {
			lock = new Object();
			lockCol.put(lockType, lock);
		}
		return lock;
	}
}
