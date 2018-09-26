package com.service.impl;

import java.util.*;
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
public class UserServiceImpl extends BaseService implements UserService {
	private static final String DAO = "com.dao.IUserDao.";
	private int _autoIncrementValue = -1;// 用来记录当前自动增长Id的值，如果为-1，使用前就需要初始化为数据表对应当前自动增长的值

	@Override
	public String getDAO() {
		return DAO;
	}

	@CacheGet(cache = "UserCache")
	@Override
	public User selectById(Integer id) {
		return sqlSessionTemplate.selectOne(DAO + "selectById", id);
	}

	@CacheSet(cache = "UserCache", type = CacheSetType.REF_ID)
	@Override
	public User insert(User entity) {
		if (_autoIncrementValue == -1) {
			entity.setUserId(null);
			sqlSessionTemplate.insert(DAO + "insert", entity);
			_autoIncrementValue = entity.getUserId() + 1;
			return entity;
		}
		entity.setUserId(_autoIncrementValue++);
		_insertSet.add(entity);
		return entity;
	}

	/**
	 * @param entity
	 *            绝对不能是new出来的实例
	 * @return
	 */
	@CacheSet(cache = "UserCache", type = CacheSetType.REF_ID)
	@Override
	public User update(User entity) {
		_updateSet.add(entity);
		return entity;
	}

	@CacheDelete(cache = "UserCache")
	@Override
	public int deleteById(Integer id) {
		return sqlSessionTemplate.update(DAO + "deleteById", id);
	}
	// 自定义部分在代码自动生成时不会被覆盖
	/* S */
	/* E */
}