package com.xyzdl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xyzdl.bean.KeyBean;

public interface KeyMapper {
	/**
	 * 批量添加钥匙
	 * @return
	 * 提倡 这样使用 @Param("keys")
	 */
	public int batchSaveKeys(@Param("keys") List<KeyBean> keys);
}