/**
 * Created by xYzDl Builder
 * Table: t_user
 */
package dao;

import java.util.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import entity.User;

@SuppressWarnings("unused")
public interface IUserDao {
	@Select("SELECT * FROM t_user WHERE id=#{id}")
	@Results({
			@Result(id = true, property = "id", column = "id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "userName", column = "user_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "password", column = "password", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "age", column = "age", javaType = Integer.class, jdbcType = JdbcType.INTEGER)
			})
	public User selectById(int id);

	@Insert("INSERT INTO t_user (user_name, password, age) VALUES (#{userName}, #{password}, #{age})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public int insert(User user);

	@Update("UPDATE t_user SET user_name=#{userName}, password=#{password}, age=#{age} WHERE id=#{id}")
	public int update(User user);

	@Delete("DELETE FROM t_user WHERE id=#{id}")
	public int deleteById(int id);
}