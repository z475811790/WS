package com.service.impl;

import java.util.*;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.componet.BaseService;
import com.entity.*;
import com.service.*;
//自定义部分在代码自动生成时不会被覆盖
//S
//E

@SuppressWarnings("unused")
@Service
public class StudentServiceImpl extends BaseService implements StudentService {

	private static String DAO = "com.dao.IStudentDao.";

	@Override
	public String getDAO() {
		return DAO;
	}

	@Cacheable(value = "UserCache", key = "'Entity-Student-'+#id")
	@Override
	public Student selectById(int id) {
		return sqlSessionTemplate.selectOne(DAO + "selectById", id);
	}

	@CachePut(value = "UserCache", key = "'Entity-Student-'+#id")
	@Override
	public Student insert(Student entity) {
//		_insertSet.add(entity);
		sqlSessionTemplate.insert(DAO + "insert", entity);
		return entity;
	}

	@Cacheable(value = "UserCache", key = "'Entity-Student-'+#id")
	@Override
	public Student update(Student entity) {
		sqlSessionTemplate.update(DAO + "update", entity);
		return entity;
	}

	@Override
	public int deleteById(int id) {
		return sqlSessionTemplate.update(DAO + "deleteById", id);
	}
	// 自定义部分在代码自动生成时不会被覆盖
	/* S */

	@Override
	public void testMethod() {
		// SqlSession session =
		// sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH,
		// false);
		// Student s = selectById(1);
		// for (int i = 0; i < 3; i++) {
		// session.insert(DAO + "insert", s);
		// }
		// session.commit();

		Student s = selectById(1);
		
		s.setId(null);
		insert(s);
		System.out.println(s.getId());
//		_insertSet.add(s);
//		s = selectById(2);
//		_insertSet.add(s);
//		synBatchInsert();
	}

	@Override
	public List<Student> selectByAgeRange(int age) {
		return sqlSessionTemplate.selectList(DAO + "selectByAgeRange", age);
	}
	/* E */

}