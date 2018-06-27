package com.xyzdl.service;

import org.apache.ibatis.session.SqlSession;

import com.xyzdl.bean.CoursesBean;
import com.xyzdl.bean.StudentBean;
import com.xyzdl.mapper.CoursesMapper;
import com.xyzdl.mapper.StudentMapper;
import com.xyzdl.util.DBUtil;

public class ManyToManyService {

	public static void main(String[] args) {

		findStudentByCourses();
		findCoursesByStudent();
	}

	private static void findCoursesByStudent() {
		SqlSession session = DBUtil.getSession();
		StudentMapper sm = session.getMapper(StudentMapper.class);
		StudentBean sb = sm.findStuAndCou(1);
		System.out.println(sb);

	}

	private static void findStudentByCourses() {
		SqlSession session = DBUtil.getSession();
		CoursesMapper cm = session.getMapper(CoursesMapper.class);
		CoursesBean cb = cm.findCouAndStu(2);
		System.out.println(cb);
	}

}