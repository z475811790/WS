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
public class EmployeeServiceImpl implements EmployeeService{
	private static final String DAO = "dao.IEmployeeDao.";

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public Employee selectById(int id) {
		return sqlSessionTemplate.selectOne(DAO + "selectById", id);
	}

	public int insert(Employee entity) {
		return sqlSessionTemplate.insert(DAO + "insert", entity);
	}

	public int update(Employee entity) {
		return sqlSessionTemplate.update(DAO + "update", entity);
	}

	public int deleteById(int id) {
		return sqlSessionTemplate.update(DAO + "deleteById", id);
	}
	//自定义部分在代码自动生成时不会被覆盖
	/* S */
	/* E */
}