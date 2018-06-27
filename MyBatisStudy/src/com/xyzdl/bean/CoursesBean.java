package com.xyzdl.bean;

import java.io.Serializable;
import java.util.List;

/**
 * manyTOmany
 * @author acer
 *
 */
public class CoursesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	// 使用 List<StudentBean>集合，是说明学习这门课程的所有学生
	private List<StudentBean> student;

	public CoursesBean() {
		super();
	}

	public CoursesBean(Integer id, String name, List<StudentBean> student) {
		super();
		this.id = id;
		this.name = name;
		this.student = student;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<StudentBean> getStudent() {
		return student;
	}

	public void setStudent(List<StudentBean> student) {
		this.student = student;
	}

	@Override
	public String toString() {
		return "CoursesBean [id=" + id + ", name=" + name + ", student=" + student + "]";
	}

}