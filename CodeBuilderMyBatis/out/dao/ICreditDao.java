/**
 * Created by xYzDl Builder
 * Table: t_credit
 */
package dao;

import java.util.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import entity.Credit;

@SuppressWarnings("unused")
public interface ICreditDao {
	@Select("SELECT * FROM t_credit WHERE id=#{id}")
	@Results({
			@Result(id = true, property = "id", column = "id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "customer", column = "customer", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "isCheckout", column = "is_checkout", javaType = Boolean.class, jdbcType = JdbcType.BIT)
			})
	public Credit selectById(int id);

	@Insert("INSERT INTO t_credit (customer, is_checkout) VALUES (#{customer}, #{isCheckout})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public int insert(Credit credit);

	@Update("UPDATE t_credit SET customer=#{customer}, is_checkout=#{isCheckout} WHERE id=#{id}")
	public int update(Credit credit);

	@Delete("DELETE FROM t_credit WHERE id=#{id}")
	public int deleteById(int id);
}