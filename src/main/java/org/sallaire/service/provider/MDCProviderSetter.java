package org.sallaire.service.provider;

import org.apache.log4j.MDC;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MDCProviderSetter {

	@Before("execution(* org.sallaire.service.provider.IProvider.*(..)) and !execution(* org.sallaire.service.provider.IProvider.getId())")
	public void preProvider(JoinPoint joinPoint) {
		MDC.put(IProvider.PROVIDER, ((IProvider) joinPoint.getTarget()).getId());
	}

	@After("execution(* org.sallaire.service.provider.IProvider.*(..)) and !execution(* org.sallaire.service.provider.IProvider.getId())")
	public void postProvider(JoinPoint joinPoint) {
		MDC.remove(IProvider.PROVIDER);
	}
}
