/**
 * Created by xYzDl Builder
 * Table: t_student
 */
package com.entity;

import java.util.*;

public class Student {
	/**id编号*/
	private Integer id;
	/**名字*/
	private String userName;
	/**性别*/
	private String sex;
	/**年龄*/
	private Integer age;
	/**生日*/
	private Date birthday;
	/**属性 1;3;5*/
	private Map<String, String> pro;
	/**奖励信息 1;5*/
	private List<String> rewardInfo;

	public Student() {
	}

	public Student(Integer id, String userName, String sex, Integer age, Date birthday, Map<String, String> pro, List<String> rewardInfo) {
		this.id = id;
		this.userName = userName;
		this.sex = sex;
		this.age = age;
		this.birthday = birthday;
		this.pro = pro;
		this.rewardInfo = rewardInfo;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
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