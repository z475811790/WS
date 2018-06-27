package dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import entity.Student;;

public interface IStudentDao {
	@Insert("insert into t_student1(id,name,sex,age) values(null,#{name},#{sex},#{age})")
	public void save(Student student);

	@Select("select * from t_student1 where id = #{id}")
	public Student getStudent(int id);
}
