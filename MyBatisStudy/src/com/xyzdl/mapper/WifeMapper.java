package com.xyzdl.mapper;

import com.xyzdl.bean.WifeBean;

public interface WifeMapper {
	/**
	 * 根据丈夫id查询妻子信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public WifeBean selectWifeByHusbandId(int id) throws Exception;

	/**
	 * 根据id查询妻子信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public WifeBean selectWifeById(int id) throws Exception;
}
