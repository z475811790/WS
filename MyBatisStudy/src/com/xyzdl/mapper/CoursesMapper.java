package com.xyzdl.mapper;

import com.xyzdl.bean.CoursesBean;

public interface CoursesMapper {

	/**
	 * 根据id查询课程
	 * @param id
	 * @return
	 */
	public CoursesBean findCouById(int id);

	/**
	 * 要求查课时，将选课的学生一并查出
	 * @param id
	 * @return
	 */
	public CoursesBean findCouAndStu(int id);

}
