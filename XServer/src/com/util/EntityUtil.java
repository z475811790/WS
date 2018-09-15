package com.util;

import com.entity.Student;

public class EntityUtil {
	public static Integer getId(Object entity) {
		if (entity instanceof Student)
			return ((Student) entity).getId();
		return null;
	}
}
