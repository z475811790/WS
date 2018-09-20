package typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.util.StringUtil;

/**
 * @author xYzDl
 * @date 2018年9月19日 下午11:49:12
 * @description 类似这种数据 1,2;3,4 以分号分组，转换成KV映射
 */
public class StringMapTypeHandler extends BaseTypeHandler<Map<String, String>> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Map<String, String> parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, StringUtil.map2String(parameter));
	}

	@Override
	public Map<String, String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return StringUtil.string2Map(rs.getString(columnName));
	}

	@Override
	public Map<String, String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return StringUtil.string2Map(rs.getString(columnIndex));
	}

	@Override
	public Map<String, String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return new HashMap<>();
	}

}
