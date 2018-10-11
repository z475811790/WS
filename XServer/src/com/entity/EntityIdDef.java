package com.entity;

/**
 * @author xYzDl
 * @description 记录获取实体id的方法
 */
public class EntityIdDef {

	public static Integer getId(Object entity) {
		if (entity instanceof Student)
			return ((Student) entity).getId();
		if (entity instanceof User)
			return ((User) entity).getUserId();

		return null;
	}
}