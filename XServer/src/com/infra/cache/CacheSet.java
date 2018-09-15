package com.infra.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xYzDl
 * @date 2018年9月14日 下午11:17:28
 * @description 将运行结果存入后返回结果，根据type来进一步确定实际的key，注意参数必须是引用类型，不能是值类型
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheSet {

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

	/**
	 * key的类型
	 * 
	 * @return
	 */
	CacheSetType type();

}
