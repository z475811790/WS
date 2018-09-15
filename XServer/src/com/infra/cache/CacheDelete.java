package com.infra.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xYzDl
 * @date 2018年9月14日 下午11:18:16
 * @description 删除缓存，key+第一个参数toString 作为实际的key，注意参数必须是引用类型，不能是值类型
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheDelete {

	/**
	 * Cache映射表名
	 * 
	 * @return
	 */
	String cache();

	/**
	 * 默认 Entity-
	 * 
	 * @return
	 */
	String preKey() default "Entity-";

}