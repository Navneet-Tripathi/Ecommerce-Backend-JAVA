package com.navneet.ecommerce.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class ResponseTimeTracker {
	private Logger logger = LoggerFactory.getLogger(ResponseTimeTracker.class);
	
	@Around("@within(com.navneet.ecommerce.annotations.TrackResponceTime)")
	public Object trackTime(ProceedingJoinPoint point) throws Throwable {
		long startTime = System.currentTimeMillis();
		Object object = point.proceed();
		long endTime = System.currentTimeMillis();
		logger.info("Method name : "+ point.getSignature() +" response time : "+ (endTime - startTime) +"ms.");
		return object;
	}

}
