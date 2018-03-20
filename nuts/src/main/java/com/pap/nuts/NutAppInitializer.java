package com.pap.nuts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * SpringBoot initializer.
 * 
 * @author Pap Gergely
 *
 */
@SpringBootApplication
public class NutAppInitializer{
	
	private static ApplicationContext context;
	
	public static void initApp(){
		context = SpringApplication.run(NutAppInitializer.class);
	}
	
	public static ApplicationContext getContext(){
		return context;
	}
	
}