package ztest.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
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

	/**
	 * 定义缓存逻辑
	 */
	@Around("@annotation(ztest.cache.MultiCache)")
	public Object cache(ProceedingJoinPoint pjp) {
		Object result = null;
		Boolean cacheEnable = false;
		// 判断是否开启缓存
		if (!cacheEnable) {
			try {
				result = pjp.proceed();
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return result;
		}

		Method method = getMethod(pjp);
		MultiCache cacheable = method.getAnnotation(MultiCache.class);

		String fieldKey = parseKey(cacheable.fieldKey(), method, pjp.getArgs());

		// 获取方法的返回类型,让缓存可以返回正确的类型
		Class returnType = ((MethodSignature) pjp.getSignature()).getReturnType();

		// 使用redis 的hash进行存取，易于管理
		result = cacheBean.hget(cacheable.key(), fieldKey, returnType);

		if (result == null) {
			try {
				result = pjp.proceed();
				cacheBean.hset(cacheable.key(), fieldKey, result);
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
	public Method getMethod(ProceedingJoinPoint pjp) {
		// 获取参数的类型
		Object[] args = pjp.getArgs();
		Class[] argTypes = new Class[pjp.getArgs().length];
		for (int i = 0; i < args.length; i++) {
			argTypes[i] = args[i].getClass();
		}
		Method method = null;
		try {
			method = pjp.getTarget().getClass().getMethod(pjp.getSignature().getName(), argTypes);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
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