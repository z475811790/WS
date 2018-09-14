package com.service.impl;

import java.util.*;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.componet.BaseService;
import com.entity.*;
import com.service.*;
//自定义部分在代码自动生成时不会被覆盖
//S
//E

@Service
public class StudentServiceImpl extends BaseService implements StudentService {

	private static String DAO = "com.dao.IStudentDao.";
	private int _autoIncrementValue = -1;// 用来记录当前自动增长Id的值，如果为-1，使用前就需要初始化为数据表对应当前自动增长的值

	@Override
	public String getDAO() {
		return DAO;
	}

	@Cacheable(value = "UserCache", key = "'Entity-' + #id")
	@Override
	public Student selectById(int id) {
		return sqlSessionTemplate.selectOne(DAO + "selectById", id);
	}

	@CachePut(value = "UserCache", key = "'Entity-' + #entity.id")
	@Override
	public Student insert(Student entity) {
		if (_autoIncrementValue == -1) {
			entity.setId(null);
			sqlSessionTemplate.insert(DAO + "insert", entity);
			_autoIncrementValue = entity.getId() + 1;
			return entity;
		}
		entity.setId(_autoIncrementValue++);
		_insertSet.add(entity);
		return entity;
	}

	/**
	 * @param entity
	 *            绝对不能是new出来的实例
	 * @return
	 */
	@CachePut(value = "UserCache", key = "'Entity-' + #entity.id")
	@Override
	public Student update(Student entity) {
		_updateSet.add(entity);
		return entity;
	}

	@Override
	@CacheEvict(value = "UserCache", key = "'Entity-' + #id")
	public int deleteById(int id) {
		return sqlSessionTemplate.update(DAO + "deleteById", id);
	}

	/**
	 * 执行全表操作前需要强制同步缓存到数据库，尽量不要有全表操作
	 */
	public void flushCache() {
		if (_insertSet.size() > 0)
			synBatchInsert();
		if (_updateSet.size() > 0)
			synBatchUpdate();
	}
	// 自定义部分在代码自动生成时不会被覆盖
	/* S */

	@Override
	public void testMethod() {
		Student s = selectById(1);
		s = selectById(1);
		s.setAge(3);
	}

	@Override
	public List<Student> selectByAgeRange(int age) {
		flushCache();
		return sqlSessionTemplate.selectList(DAO + "selectByAgeRange", age);
	}
	/* E */

}