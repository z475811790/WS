package com.service.impl;

import java.util.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.componet.BaseService;
import com.entity.*;
import com.exception.*;
import com.infra.cache.*;
import com.service.*;
//自定义部分在代码自动生成时不会被覆盖
//S
//E

@Service
public class StudentServiceImpl extends BaseService implements StudentService{
	private static final String DAO = "com.dao.IStudentDao.";
	private int _autoIncrementValue = -1;// 用来记录当前自动增长Id的值，如果为-1，使用前就需要初始化为数据表对应当前自动增长的值

	@Override
	public String getDAO() {
		return DAO;
	}
	
	@CacheGet(cache = "StudentCache")
	@Override
	public Student selectById(int id) {
		return sqlSessionTemplate.selectOne(DAO + "selectById", id);
	}

	@CacheSet(cache = "StudentCache", type = CacheSetType.REF_ID)
	@Override
	public int insert(Student entity) {
		return sqlSessionTemplate.insert(DAO + "insert", entity);
	}

	/**
	 * @param entity
	 *            绝对不能是new出来的实例
	 * @return
	 */
	@CacheSet(cache = "StudentCache", type = CacheSetType.REF_ID)
	@Override
	public int update(Student entity) {
		return sqlSessionTemplate.update(DAO + "update", entity);
	}

	@CacheDelete(cache = "StudentCache")
	@Override
	public int deleteById(int id) {
		return sqlSessionTemplate.update(DAO + "deleteById", id);
	}
	//自定义部分在代码自动生成时不会被覆盖
	/* S */

	@Override
	public List<Student> selectByAgeRange(int age) {
		flushCache();
		return sqlSessionTemplate.selectList(DAO + "selectByAgeRange", age);
	}
	/* E */
}