package com.infra.cache;

public enum CacheSetType {
	/**
	 * 直接取第一个参数的toString值
	 */
	STR_VAL,
	/**
	 * 取实体的ID值，实体必须是第一个参数
	 */
	REF_ID
}
