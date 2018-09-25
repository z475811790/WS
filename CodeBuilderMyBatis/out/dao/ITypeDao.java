/**
 * Created by xYzDl Builder
 * Table: t_type
 */
package dao;

import java.util.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import entity.Type;

@SuppressWarnings("unused")
public interface ITypeDao {
	@Select("SELECT * FROM t_type WHERE id=#{id}")
	@Results({
			@Result(id = true, property = "id", column = "id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "balanceType", column = "balance_type", javaType = Boolean.class, jdbcType = JdbcType.BIT),
			@Result(property = "isForManager", column = "is_for_manager", javaType = Boolean.class, jdbcType = JdbcType.BIT),
			@Result(property = "typeName", column = "type_name", javaType = String.class, jdbcType = JdbcType.VARCHAR)
			})
	public Type selectById(int id);

	@Insert("INSERT INTO t_type (balance_type, is_for_manager, type_name) VALUES (#{balanceType}, #{isForManager}, #{typeName})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public int insert(Type type);

	@Update("UPDATE t_type SET balance_type=#{balanceType}, is_for_manager=#{isForManager}, type_name=#{typeName} WHERE id=#{id}")
	public int update(Type type);

	@Delete("DELETE FROM t_type WHERE id=#{id}")
	public int deleteById(int id);
}