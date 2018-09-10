package com.service.impl;

import java.util.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.entity.*;
import com.service.*;
//自定义部分在代码自动生成时不会被覆盖
//S
//E

@SuppressWarnings("unused")
@Service
public class StudentServiceImpl implements StudentService {
	private static final String DAO = "com.dao.IStudentDao.";

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Cacheable(value = "UserCache", key = "#id")
	@Override
	public Student selectById(int id) {
		return sqlSessionTemplate.selectOne(DAO + "selectById", id);
	}

	@Override
	public int insert(Student entity) {
		return sqlSessionTemplate.insert(DAO + "insert", entity);
	}

	@Override
	public int update(Student entity) {
		return sqlSessionTemplate.update(DAO + "update", entity);
	}

	@Override
	public int deleteById(int id) {
		return sqlSessionTemplate.update(DAO + "deleteById", id);
	}
	// 自定义部分在代码自动生成时不会被覆盖
	/* S */

	@Override
	public void testMethod() {
		Student s = selectById(1);
		// Student s1 = selectById(1);
		// int id = insert(s);
		// deleteById(id);
	}

	@Override
	public List<Student> selectByAgeRange(int age) {
		return sqlSessionTemplate.selectList(DAO + "selectByAgeRange", age);
	}
	/* E */

}