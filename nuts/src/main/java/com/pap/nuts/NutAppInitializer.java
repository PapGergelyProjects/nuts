package com.pap.nuts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.pap.nuts.modules.interfaces.DaoService;

@SpringBootApplication
public class NutAppInitializer{
	
	private static ApplicationContext context;
	
	public static void initApp(){
		context = SpringApplication.run(NutAppInitializer.class);
	}
	
	public static ApplicationContext getContext(){
		return context;
	}
	
	public static <T extends DaoService<?>> T getService(Class<T> service){
		return context.getBean(service);
	}
	
}