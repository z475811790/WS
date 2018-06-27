package com.xyzdl.mapper;

import com.xyzdl.bean.StudentBean;

public interface StudentMapper {
	/**
	 * 根据id值查询学生信息
	 * @param id
	 * @return
	 */
	public StudentBean findStuById(int id);

	/**
	 * 要求查询学生时，将学生选择的课程查出
	 * @param id
	 * @return
	 */
	public StudentBean findStuAndCou(int id);

}