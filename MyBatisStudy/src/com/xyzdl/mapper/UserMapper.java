package com.xyzdl.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xyzdl.bean.UserBean;

public interface UserMapper {
	/**
	 * 新增用戶
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int insertUser(@Param("user") UserBean user) throws Exception;

	/**
	 * 修改用戶
	 * @param user
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int updateUser(@Param("u") UserBean user, @Param("id") int id) throws Exception;

	/**
	 * 刪除用戶
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int deleteUser(int id) throws Exception;

	/**
	 * 根据id查询用户信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public UserBean selectUserById(int id) throws Exception;

	/**
	 * 查询所有的用户信息
	 * @return
	 * @throws Exception
	 */
	public List<UserBean> selectAllUser() throws Exception;

	/**
	 * 批量增加
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int batchInsertUser(@Param("users") List<UserBean> list) throws Exception;

	/**
	* 批量删除
	* @param list
	* @return
	* @throws Exception
	*/
	public int batchDeleteUser(@Param("list") List<Integer> list) throws Exception;

	/**
	* 分页查询数据
	* @param parma
	* @return
	* @throws Exception
	*/
	public List<UserBean> pagerUser(Map<String, Object> parmas) throws Exception;

	/**
	* 
	* 分页统计数据
	* @param parma
	* @return
	* @throws Exception
	*/
	public int countUser(Map<String, Object> parmas) throws Exception;

}