package com.xyzdl.bean;

import java.io.Serializable;
import java.util.List;

/**
 * manyTOmany
 * @author acer
 *
 */
public class StudentBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private List<CoursesBean> courses;

	public StudentBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StudentBean(Integer id, String name, List<CoursesBean> courses) {
		super();
		this.id = id;
		this.name = name;
		this.courses = courses;
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

	public List<CoursesBean> getCourses() {
		return courses;
	}

	public void setCourses(List<CoursesBean> courses) {
		this.courses = courses;
	}

	@Override
	public String toString() {
		return "StudentBean [id=" + id + ", name=" + name + ", courses=" + courses + "]";
	}

}