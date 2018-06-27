package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dao.IStudentDao;
import entity.Student;

@Service
public class StudentService {

	@Autowired
	private IStudentDao iStudentDao;

	public void save(Student student) {
		iStudentDao.save(student);
	}

	public Student getStudent(int id) {
		Student student = iStudentDao.getStudent(id);
		return student;
	}
}