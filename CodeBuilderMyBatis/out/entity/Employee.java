/**
 * Created by xYzDl Builder
 * Table: t_employee
 */
package entity;

import java.util.Date;
import java.util.Map;
import java.util.List;

@SuppressWarnings("unused")
public class Employee {
	private int id;
	private String password;
	private Date birth;
	private String empName;
	private String gender;
	private boolean isManager;
	private String phone;

	public Employee() {
	}

	public Employee(int id, String password, Date birth, String empName, String gender, boolean isManager, String phone) {
		this.id = id;
		this.password = password;
		this.birth = birth;
		this.empName = empName;
		this.gender = gender;
		this.isManager = isManager;
		this.phone = phone;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public boolean getIsManager() {
		return isManager;
	}

	public void setIsManager(boolean isManager) {
		this.isManager = isManager;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}