package ztest.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xYzDl
 * @date 2018年9月14日 下午11:14:42
 * @description 如果缓存为空，将运行结果存入后返回结果，存在缓存则不运行代码，key+第一个参数toString
 *              作为实际的key，注意参数必须是引用类型，不能是值类型
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheGet {

	String cache();

	String preKey();

}