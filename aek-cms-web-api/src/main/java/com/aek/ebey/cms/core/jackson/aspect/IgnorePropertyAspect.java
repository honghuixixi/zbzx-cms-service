package com.aek.ebey.cms.core.jackson.aspect;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import com.aek.ebey.cms.core.jackson.annotation.IgnoreProperties;
import com.aek.ebey.cms.core.jackson.impl.FilterPropertyHandler;
import com.aek.ebey.cms.core.jackson.impl.JavassistFilterPropertyHandler;

@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class IgnorePropertyAspect {

	private static final Logger logger = LogManager.getLogger(IgnorePropertyAspect.class);

	public IgnorePropertyAspect() {
		super();
	}

	@Pointcut("@annotation(com.aek.ebey.cms.core.jackson.annotation.IgnoreProperties)")
	private void anyMethod() {

	}

	@Around("anyMethod()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		Object returnVal = pjp.proceed(); // 返回源结果
		try {
			FilterPropertyHandler filterPropertyHandler = new JavassistFilterPropertyHandler(true);
			Method method = ((MethodSignature) pjp.getSignature()).getMethod();
			// 方法标注过滤的才进行过滤
			if (method.isAnnotationPresent(IgnoreProperties.class)) {
				returnVal = filterPropertyHandler.filterProperties(method, returnVal);
			}
		} catch (Exception e) {
			logger.error("filter json error.", e);
		}
		return returnVal;
	}

	// @AfterThrowing(pointcut = "anyMethod()", throwing = "e")
	// public void doAfterThrowing(Exception e) {
	// logger.error("filter json exception.", e);
	// }
}