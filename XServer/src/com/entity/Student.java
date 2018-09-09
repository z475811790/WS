/**
 * Created by xYzDl Builder
 * Table: t_student
 */
package com.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.List;

public class Student implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1534564827894800883L;
	/** id编号 */
	private int id;
	/** 名字 */
	private String userName;
	/** 性别 */
	private String sex;
	/** 年龄 */
	private int age;
	/** 生日 */
	private Date birthday;
	/** 属性 1;3;5 */
	private Map<String, String> pro;
	/** 奖励信息 1;5 */
	private List<String> rewardInfo;

	public Student() {
	}

	public Student(int id, String userName, String sex, int age, Date birthday, Map<String, String> pro, List<String> rewardInfo) {
		this.id = id;
		this.userName = userName;
		this.sex = sex;
		this.age = age;
		this.birthday = birthday;
		this.pro = pro;
		this.rewardInfo = rewardInfo;
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Map<String, String> getPro() {
		return pro;
	}

	public void setPro(Map<String, String> pro) {
		this.pro = pro;
	}

	public List<String> getRewardInfo() {
		return rewardInfo;
	}

	public void setRewardInfo(List<String> rewardInfo) {
		this.rewardInfo = rewardInfo;
	}
}