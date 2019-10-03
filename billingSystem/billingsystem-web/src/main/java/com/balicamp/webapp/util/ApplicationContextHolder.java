package com.balicamp.webapp.util;

import org.springframework.context.ApplicationContext;

public class ApplicationContextHolder {

	public static ApplicationContext context = null ;
	
	public static Object getBean(String beanName){
		return context.getBean(beanName);
	}
	
	
}
