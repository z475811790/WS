package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dao.StudentDaoImp;
import entity.Student;

@Service
public class StudentService {

	@Autowired
	private StudentDaoImp studentDaoImp;

	public void save(Student student) {
		studentDaoImp.save(student);
	}

	public Student getStudent(int id) {
		Student student = studentDaoImp.getStudent(id);
		return student;
	}
}