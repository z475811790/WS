package com.componet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.entity.User;

/**
 * @author xYzDl
 * @date 2018年8月14日 下午10:57:47
 * @description Socket与User的映射类
 */
@Component
public class SocketUserMap {

	public SocketUserMap() {
	}

	private Map<String, String> user2SocketId = new HashMap<>();
	private Map<String, User> socket2User = new HashMap<>();

	public Collection<String> getAllSocketIds() {
		return user2SocketId.values();
	}

	public String getSocketId(String account) {
		return user2SocketId.get(account);
	}

	public User getAccount(String socketId) {
		return socket2User.get(socketId);
	}

	public void addSocket(String socketId, User user) {
		socket2User.put(socketId, user);
		user2SocketId.put(user.getAccount(), socketId);
	}

	public void removeSocket(String socketId) {
		User u = socket2User.get(socketId);
		if (u != null)
			user2SocketId.remove(u.getAccount());
		socket2User.remove(socketId);
	}
}
