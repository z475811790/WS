package ztest.aspect;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import ztest.cache.MultiCache;
import ztest.service.CacheBean;

@Component
@Aspect
public class CacheAspect {

	@Autowired
	public CacheBean cacheBean;

	@Pointcut("@annotation(ztest.cache.MultiCache)")
	public void cutPoint() {
	}

	// @Before("cutPoint()")
	// public void beforeSaveServiceNode(JoinPoint joinPoint) {
	// System.out.println(joinPoint);
	// }

	private static ConcurrentHashMap<String, MultiCache> annoMap = new ConcurrentHashMap<String, MultiCache>();

	/**
	 * 定义缓存逻辑
	 */
	@Around("@annotation(ztest.cache.MultiCache)")
	public Object cacheGet(ProceedingJoinPoint point) {
		String signature = point.getSignature().toString();

		MultiCache anno = annoMap.get(signature);
		if (anno == null) {
			Method method = getMethod(point);
			anno = method.getAnnotation(MultiCache.class);
			annoMap.put(signature, anno);
		}

		String cacheKey = null;
		switch (anno.keyType()) {
		case STR_VAL:
			cacheKey = anno.key() + point.getArgs()[0];
			break;
		case REF_ID:
			cacheKey = anno.key() + parseEntity(point.getArgs()[0]);
			break;
		}
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

	private String parseEntity(Object object) {
		if (object == null)
			return "";
		if (object instanceof Book)
			return ((Book) object).getId().toString();
		return "";
	}

	/**
	 * 获取被拦截方法对象
	 * 
	 * MethodSignature.getMethod() 获取的是顶层接口或者父类的方法对象 而缓存的注解在实现类的方法上
	 * 所以应该使用反射获取当前对象的方法对象
	 */
	public Method getMethod(ProceedingJoinPoint pjp) {
		// 获取参数的类型
		Object[] args = pjp.getArgs();
		Class[] argTypes = new Class[pjp.getArgs().length];
		for (int i = 0; i < args.length; i++) {
			argTypes[i] = args[i].getClass();
		}
		Method method = null;
		try {
			Object target = pjp.getTarget();
			Class klass = target.getClass();
			method = klass.getMethod(pjp.getSignature().getName(), argTypes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return method;
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