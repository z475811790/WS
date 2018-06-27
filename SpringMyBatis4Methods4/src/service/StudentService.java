package service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import entity.Student;

@Service
public class StudentService {

	/**
	*sqlSessionTemplate模板提供了sqlsession
	*/

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public void save(Student student) {
		sqlSessionTemplate.insert("dao.IStudentDao.save", student);
	}

	public Student getStudent(int id) {
		Student student = sqlSessionTemplate.selectOne("dao.IStudentDao.getStudent", id);
		return student;
	}

}