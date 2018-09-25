/**
 * Created by xYzDl Builder
 * Table: t_user
 */
package entity;

import java.util.Date;
import java.util.Map;
import java.util.List;

@SuppressWarnings("unused")
public class User {
	private int id;
	private String userName;
	private String password;
	private int age;

	public User() {
	}

	public User(int id, String userName, String password, int age) {
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.age = age;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}