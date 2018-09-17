package com.componet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

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
}
