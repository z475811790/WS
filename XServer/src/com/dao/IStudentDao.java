/**
 * Created by xYzDl Builder
 * Table: t_student
 */
package com.dao;

import java.util.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import com.entity.Student;

public interface IStudentDao {
	@Select("SELECT * FROM t_student WHERE id=#{id}")
	@Results({
			@Result(id = true, property = "id", column = "id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "userName", column = "user_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "sex", column = "sex", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "age", column = "age", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "birthday", column = "birthday", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
			@Result(property = "pro", column = "pro", javaType = Map.class, jdbcType = JdbcType.VARCHAR, typeHandler =typehandler.StringMapTypeHandler.class),
			@Result(property = "rewardInfo", column = "reward_info", javaType = List.class, jdbcType = JdbcType.VARCHAR, typeHandler =typehandler.StringListTypeHandler.class)
			})
	public Student selectById(int id);

	@Insert("INSERT INTO t_student (id, user_name, sex, age, birthday, pro, reward_info) VALUES (#{id}, #{userName}, #{sex}, #{age}, #{birthday}, #{pro, typeHandler=typehandler.StringMapTypeHandler}, #{rewardInfo, typeHandler=typehandler.StringListTypeHandler})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public int insert(Student student);

	@Update("UPDATE t_student SET user_name=#{userName}, sex=#{sex}, age=#{age}, birthday=#{birthday}, pro=#{pro, typeHandler=typehandler.StringMapTypeHandler}, reward_info=#{rewardInfo, typeHandler=typehandler.StringListTypeHandler} WHERE id=#{id}")
	public int update(Student student);

	@Delete("DELETE FROM t_student WHERE id=#{id}")
	public int deleteById(int id);
}