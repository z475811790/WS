/**
 * Created by xYzDl Builder
 * Table: t_user
 */
package com.dao;

import java.util.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import com.entity.User;

public interface IUserDao {
	@Select("SELECT * FROM t_user WHERE user_id=#{userId}")
	@Results({
			@Result(id = true, property = "userId", column = "user_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "account", column = "account", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "password", column = "password", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "isAdmin", column = "is_admin", javaType = Boolean.class, jdbcType = JdbcType.BIT),
			@Result(property = "createDate", column = "create_date", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP)
			})
	public User selectById(int userId);

	@Insert("INSERT INTO t_user (user_id, account, password, is_admin, create_date) VALUES (#{userId}, #{account}, #{password}, #{isAdmin}, #{createDate})")
	@Options(useGeneratedKeys = true, keyProperty = "userId")
	public int insert(User user);

	@Update("UPDATE t_user SET account=#{account}, password=#{password}, is_admin=#{isAdmin}, create_date=#{createDate} WHERE user_id=#{userId}")
	public int update(User user);

	@Delete("DELETE FROM t_user WHERE user_id=#{userId}")
	public int deleteById(int userId);
}