package com.componet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xYzDl
 * @date 2018年9月19日 下午11:59:41
 * @description Service基类，负责批量插入和更新
 */
public abstract class BaseService {
	@Autowired
	protected SqlSessionTemplate sqlSessionTemplate;
	protected Set<Object> _insertSet = Collections.synchronizedSet(new HashSet<Object>());
	protected Set<Object> _updateSet = Collections.synchronizedSet(new HashSet<Object>());

	protected abstract String getDAO();

	synchronized public void synBatchInsert() {
		SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
		String op = getDAO() + "insert";
		int i = 1;
		for (Object entity : _insertSet) {
			session.insert(op, entity);
			if (i++ % 200 == 0)
				session.commit();
		}
		session.commit();
		session.close();
		_insertSet.clear();
	}

	synchronized public void synBatchUpdate() {
		SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
		String op = getDAO() + "update";
		int i = 1;
		for (Object entity : _updateSet) {
			session.update(op, entity);
			if (i++ % 600 == 0)
				session.commit();
		}
		session.commit();
		session.close();
		_updateSet.clear();
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
}
