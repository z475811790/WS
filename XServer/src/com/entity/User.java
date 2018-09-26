/**
 * Created by xYzDl Builder
 * Table: t_user
 */
package com.entity;

import java.util.Date;
import java.util.Map;
import java.util.List;

@SuppressWarnings("unused")
public class User {
	private Integer userId;
	private String account;
	private String password;
	private Boolean isAdmin;
	private Date createDate;

	public User() {
	}

	public User(Integer userId, String account, String password, Boolean isAdmin, Date createDate) {
		this.userId = userId;
		this.account = account;
		this.password = password;
		this.isAdmin = isAdmin;
		this.createDate = createDate;
	}


	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}