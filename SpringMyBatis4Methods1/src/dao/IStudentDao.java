package dao;

import entity.Student;;

public interface IStudentDao {
	public void save(Student student);

	public Student getStudent(int id);
}
