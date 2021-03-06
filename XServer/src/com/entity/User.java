/**
 * Created by xYzDl Builder
 * Table: t_user
 */
package com.entity;

import java.util.*;

public class User {
	/**主键*/
	private Integer userId;
	/**账号*/
	private String account;
	/**密码*/
	private String password;
	/**是否为管理员*/
	private Boolean isAdmin;
	/**创建时间*/
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