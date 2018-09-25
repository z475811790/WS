package service;

import java.util.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import entity.*;
//自定义部分在代码自动生成时不会被覆盖
//S
//E

@SuppressWarnings("unused")
@Service
public class CreditServiceImpl implements CreditService{
	private static final String DAO = "dao.ICreditDao.";

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public Credit selectById(int id) {
		return sqlSessionTemplate.selectOne(DAO + "selectById", id);
	}

	public int insert(Credit entity) {
		return sqlSessionTemplate.insert(DAO + "insert", entity);
	}

	public int update(Credit entity) {
		return sqlSessionTemplate.update(DAO + "update", entity);
	}

	public int deleteById(int id) {
		return sqlSessionTemplate.update(DAO + "deleteById", id);
	}
	//自定义部分在代码自动生成时不会被覆盖
	/* S */
	/* E */
}