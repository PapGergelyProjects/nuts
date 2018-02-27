package com.pap.nuts.utils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.pap.nuts.modules.interfaces.DaoService;

@SpringBootApplication
@ComponentScan("com.pap.nuts.modules.session, com.pap.nuts.modules.session.service")
public class NutAppInitializer{
	
	private static ApplicationContext context;
	
	public static void initApp(){
		context = SpringApplication.run(NutAppInitializer.class);
	}
	
	public static <E,T extends DaoService<E>> T getService(Class<T> service){
		return context.getBean(service);
	}
	
}