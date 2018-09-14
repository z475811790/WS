package ztest.aspect;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import ztest.cache.CacheDelete;
import ztest.cache.CacheGet;
import ztest.cache.CacheSet;
import ztest.service.CacheBean;

@Component
@Aspect
public class CacheAspect {

	@Autowired
	public CacheBean cacheBean;

	// @Pointcut("@annotation(ztest.cache.CacheSet)")
	// public void cutPoint() {
	// }

	private static ConcurrentHashMap<String, CacheSet> cacheSetMap = new ConcurrentHashMap<>();

	// 声明rtv时指定的类型会限制目标方法必须返回指定类型的值或没有返回值
	// 此处将rtv的类型声明为Object，意味着对目标方法的返回值不加限制
	@AfterReturning(pointcut = "@annotation(ztest.cache.CacheSet)", returning = "rtv")
	public void cacheSet(JoinPoint point, Object rtv) {
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
			cacheKey = anno.preKey() + parseEntity(point.getArgs()[0]);
			break;
		}
		if (cacheKey == null)
			return;
		cacheBean.hset(anno.cache(), cacheKey, rtv);
	}

	private String parseEntity(Object object) {
		if (object instanceof Book)
			return ((Book) object).getId().toString();
		return "";
	}

	private static ConcurrentHashMap<String, CacheGet> cacheGetMap = new ConcurrentHashMap<>();

	/**
	 * 定义缓存逻辑
	 */
	@Around("@annotation(ztest.cache.CacheGet)")
	public Object cacheGet(ProceedingJoinPoint point) {
		String signature = point.getSignature().toString();

		CacheGet anno = cacheGetMap.get(signature);
		if (anno == null) {
			Method method = getMethod(point);
			anno = method.getAnnotation(CacheGet.class);
			cacheGetMap.put(signature, anno);
		}
		String cacheKey = anno.preKey() + point.getArgs()[0].toString();
		Object result = cacheBean.hget(anno.cache(), cacheKey);
		if (result == null) {
			try {
				result = point.proceed();
				cacheBean.hset(anno.cache(), cacheKey, result);
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

	private static ConcurrentHashMap<String, CacheDelete> cacheDeleteMap = new ConcurrentHashMap<>();

	@AfterReturning(pointcut = "@annotation(ztest.cache.CacheDelete)")
	public void cacheDelete(JoinPoint point) {
		String signature = point.getSignature().toString();

		CacheDelete anno = cacheDeleteMap.get(signature);
		if (anno == null) {
			Method method = getMethod(point);
			anno = method.getAnnotation(CacheDelete.class);
			cacheDeleteMap.put(signature, anno);
		}

		String cacheKey = anno.preKey() + point.getArgs()[0].toString();
		cacheBean.hdel(anno.cache(), cacheKey);
	}

	/**
	 * 获取缓存的key key 定义在注解上，支持SPEL表达式
	 * 
	 * @param pjp
	 * @return
	 */
	private String parseKey(String key, Method method, Object[] args) {

		// 获取被拦截方法参数名列表(使用Spring支持类库)
		LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
		String[] paraNameArr = u.getParameterNames(method);

		// 使用SPEL进行key的解析
		ExpressionParser parser = new SpelExpressionParser();
		// SPEL上下文
		StandardEvaluationContext context = new StandardEvaluationContext();
		// 把方法参数放入SPEL上下文中
		for (int i = 0; i < paraNameArr.length; i++) {
			context.setVariable(paraNameArr[i], args[i]);
		}
		return parser.parseExpression(key).getValue(context, String.class);
	}
}