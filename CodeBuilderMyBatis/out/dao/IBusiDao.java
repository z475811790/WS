/**
 * Created by xYzDl Builder
 * Table: t_busi
 */
package dao;

import java.util.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import entity.Busi;

@SuppressWarnings("unused")
public interface IBusiDao {
	@Select("SELECT * FROM t_busi WHERE id=#{id}")
	@Results({
			@Result(id = true, property = "id", column = "id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "ps", column = "ps", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "busiDate", column = "busi_date", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
			@Result(property = "busiManId", column = "busi_man_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "busiType", column = "busi_type", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "money", column = "money", javaType = Double.class, jdbcType = JdbcType.DOUBLE),
			@Result(property = "shop", column = "shop", javaType = String.class, jdbcType = JdbcType.VARCHAR)
			})
	public Busi selectById(int id);

	@Insert("INSERT INTO t_busi (ps, busi_date, busi_man_id, busi_type, money, shop) VALUES (#{ps}, #{busiDate}, #{busiManId}, #{busiType}, #{money}, #{shop})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public int insert(Busi busi);

	@Update("UPDATE t_busi SET ps=#{ps}, busi_date=#{busiDate}, busi_man_id=#{busiManId}, busi_type=#{busiType}, money=#{money}, shop=#{shop} WHERE id=#{id}")
	public int update(Busi busi);

	@Delete("DELETE FROM t_busi WHERE id=#{id}")
	public int deleteById(int id);
}