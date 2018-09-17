package com.componet;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infra.cache.CacheDelete;
import com.infra.cache.CacheGet;
import com.infra.cache.CacheSet;
import com.util.EntityUtil;

/**
 * @author xYzDl
 * @date 2018年9月15日 下午11:34:42
 * @description 缓存切面
 */
@Component
@Aspect
public class CacheAspect {

	@Autowired
	private EntityCacher cacher;

	// 根据函数签名存放对应的缓存注解
	private static ConcurrentHashMap<String, CacheGet> cacheGetMap = new ConcurrentHashMap<>();

	@Around("@annotation(com.infra.cache.CacheGet)")
	private Object cacheGet(ProceedingJoinPoint point) {
		String signature = point.getSignature().toString();
		CacheGet anno = cacheGetMap.get(signature);
		if (anno == null) {
			Method method = getMethod(point);
			anno = method.getAnnotation(CacheGet.class);
			cacheGetMap.put(signature, anno);
		}
		String cacheKey = anno.preKey() + point.getArgs()[0].toString();
		Object result = cacher.get(anno.cache(), cacheKey);
		if (result == null) {
			try {
				result = point.proceed();
				if (result == null)
					return result;
				cacher.set(anno.cache(), cacheKey, result);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 获取被拦截方法对象
	 * 
	 * MethodSignature.getMethod() 获取的是顶层接口或者父类的方法对象 而缓存的注解在实现类的方法上
	 * 所以应该使用反射获取当前对象的方法对象
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Method getMethod(JoinPoint point) {
		// 获取参数的类型
		Object[] args = point.getArgs();
		Class[] argTypes = new Class[point.getArgs().length];
		for (int i = 0; i < args.length; i++) {
			argTypes[i] = args[i].getClass();
		}
		Method method = null;
		try {
			Object target = point.getTarget();
			Class klass = target.getClass();
			method = klass.getMethod(point.getSignature().getName(), argTypes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return method;
	}

	// 根据函数签名存放对应的缓存注解
	private static ConcurrentHashMap<String, CacheSet> cacheSetMap = new ConcurrentHashMap<>();

	// 声明rtv时指定的类型会限制目标方法必须返回指定类型的值或没有返回值
	// 此处将rtv的类型声明为Object，意味着对目标方法的返回值不加限制
	@AfterReturning(pointcut = "@annotation(com.infra.cache.CacheSet)", returning = "rtv")
	private void cacheSet(JoinPoint point, Object rtv) {
		String signature = point.getSignature().toString();
		CacheSet anno = cacheSetMap.get(signature);
		if (anno == null) {
			Method method = getMethod(point);
			anno = method.getAnnotation(CacheSet.class);
			cacheSetMap.put(signature, anno);
		}
		String cacheKey = null;
		switch (anno.type()) {
		case STR_VAL:
			cacheKey = anno.preKey() + point.getArgs()[0].toString();
			break;
		case REF_ID:
			cacheKey = anno.preKey() + EntityUtil.getId(point.getArgs()[0]);
			break;
		}
		if (cacheKey == null)
			return;
		cacher.set(anno.cache(), cacheKey, rtv);
	}

	// 根据函数签名存放对应的缓存注解
	private static ConcurrentHashMap<String, CacheDelete> cacheDeleteMap = new ConcurrentHashMap<>();

	@AfterReturning(pointcut = "@annotation(com.infra.cache.CacheDelete)")
	private void cacheDelete(JoinPoint point) {
		String signature = point.getSignature().toString();

		CacheDelete anno = cacheDeleteMap.get(signature);
		if (anno == null) {
			Method method = getMethod(point);
			anno = method.getAnnotation(CacheDelete.class);
			cacheDeleteMap.put(signature, anno);
		}

		String cacheKey = anno.preKey() + point.getArgs()[0].toString();
		cacher.del(anno.cache(), cacheKey);
	}

	/**
	 * 获取缓存的key key 定义在注解上，支持SPEL表达式 解析比较消耗性能，所以做参考备用
	 * 
	 * @param
	 * @return
	 */
	// private String parseKey(String key, Method method, Object[] args) {

	// 获取被拦截方法参数名列表(使用Spring支持类库)
	// LocalVariableTableParameterNameDiscoverer u = new
	// LocalVariableTableParameterNameDiscoverer();
	// String[] paraNameArr = u.getParameterNames(method);

	// 使用SPEL进行key的解析
	// ExpressionParser parser = new SpelExpressionParser();
	// SPEL上下文
	// StandardEvaluationContext context = new StandardEvaluationContext();
	// 把方法参数放入SPEL上下文中
	// for (int i = 0; i < paraNameArr.length; i++) {
	// context.setVariable(paraNameArr[i], args[i]);
	// }
	// return parser.parseExpression(key).getValue(context, String.class);
	// }
}