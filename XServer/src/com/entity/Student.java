/**
 * Created by xYzDl Builder
 * Table: t_student
 */
package com.entity;

import java.util.Date;
import java.util.Map;
import java.util.List;

@SuppressWarnings("unused")
public class Student {
	private Integer id;
	private String userName;
	private String sex;
	private Integer age;
	private Date birthday;
	private Map<String, String> pro;
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