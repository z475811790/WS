package typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.util.CommonUtil;

public class StringListTypeHandler extends BaseTypeHandler<List<String>> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, CommonUtil.list2String(parameter));
	}

	@Override
	public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return CommonUtil.string2List(rs.getString(columnName));
	}

	@Override
	public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return CommonUtil.string2List(rs.getString(columnIndex));
	}

	@Override
	public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return new ArrayList<>();
	}

}
