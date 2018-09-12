package com.componet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.entity.Student;

public abstract class BaseService {
	@Autowired
	protected SqlSessionTemplate sqlSessionTemplate;
	protected Set<Student> _insertSet = Collections.synchronizedSet(new HashSet<Student>());
	protected Set<Student> _updateSet = Collections.synchronizedSet(new HashSet<Student>());

	protected abstract String getDAO();

	public void synBatchInsert() {
		SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
		String op = getDAO() + "insert";
		int i = 1;
		for (Student student : _insertSet) {
			session.insert(op, student);
			if (i++ % 200 == 0)
				session.commit();
		}
		session.commit();
		session.close();
	}
}
