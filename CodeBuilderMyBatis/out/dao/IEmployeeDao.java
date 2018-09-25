/**
 * Created by xYzDl Builder
 * Table: t_employee
 */
package dao;

import java.util.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import entity.Employee;

@SuppressWarnings("unused")
public interface IEmployeeDao {
	@Select("SELECT * FROM t_employee WHERE id=#{id}")
	@Results({
			@Result(id = true, property = "id", column = "id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "password", column = "password", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "birth", column = "birth", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
			@Result(property = "empName", column = "emp_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "gender", column = "gender", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "isManager", column = "is_manager", javaType = Boolean.class, jdbcType = JdbcType.BIT),
			@Result(property = "phone", column = "phone", javaType = String.class, jdbcType = JdbcType.VARCHAR)
			})
	public Employee selectById(int id);

	@Insert("INSERT INTO t_employee (password, birth, emp_name, gender, is_manager, phone) VALUES (#{password}, #{birth}, #{empName}, #{gender}, #{isManager}, #{phone})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public int insert(Employee employee);

	@Update("UPDATE t_employee SET password=#{password}, birth=#{birth}, emp_name=#{empName}, gender=#{gender}, is_manager=#{isManager}, phone=#{phone} WHERE id=#{id}")
	public int update(Employee employee);

	@Delete("DELETE FROM t_employee WHERE id=#{id}")
	public int deleteById(int id);
}