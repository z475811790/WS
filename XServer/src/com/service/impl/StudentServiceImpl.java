package com.service.impl;

import java.util.*;

import org.springframework.stereotype.Service;

import com.componet.BaseService;
import com.entity.*;
import com.infra.cache.*;
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

	@CacheGet(cache = "StudentCache")
	@Override
	public Student selectById(Integer id) {
		return sqlSessionTemplate.selectOne(DAO + "selectById", id);
	}

	@CacheSet(cache = "StudentCache", type = CacheSetType.REF_ID)
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
	@CacheSet(cache = "StudentCache", type = CacheSetType.REF_ID)
	@Override
	public Student update(Student entity) {
		_updateSet.add(entity);
		return entity;
	}

	@Override
	@CacheDelete(cache = "StudentCache")
	public int deleteById(Integer id) {
		return sqlSessionTemplate.update(DAO + "deleteById", id);
	}

	/**
	 * 执行全表操作前需要强制同步缓存到数据库，尽量不要有全表操作，即使有也要注意优化操作，注意要先插入数据再更新
	 * 
	 * @return 插入和更新总数
	 */
	public int flushCache() {
		int numInsert = _insertSet.size();
		int numUpdate = _updateSet.size();
		if (numInsert > 0)
			synBatchInsert();
		if (numUpdate > 0)
			synBatchUpdate();
		return numInsert + numUpdate;
	}
	// 自定义部分在代码自动生成时不会被覆盖
	/* S */

	@Override
	public List<Student> selectByAgeRange(int age) {
		flushCache();
		return sqlSessionTemplate.selectList(DAO + "selectByAgeRange", age);
	}
	/* E */

}