package com.pap.nuts.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * This class handles the environ file, and those beans are which use it.
 * 
 * @author Pap Gergely
 *
 */
@Configuration
@PropertySource("classpath:config.properties")
public class EnvironBeanConfig {
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propConfig(){
		return new PropertySourcesPlaceholderConfigurer();
	}
	
}
