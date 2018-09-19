package com.util;

import com.entity.Student;

/**
 * @author xYzDl
 * @date 2018年9月19日 下午11:53:07
 * @description 表实体处理相关工具
 */
public class EntityUtil {
	public static Integer getId(Object entity) {
		if (entity instanceof Student)
			return ((Student) entity).getId();
		return null;
	}
}
