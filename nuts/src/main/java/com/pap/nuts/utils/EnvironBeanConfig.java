package com.pap.nuts.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import com.pap.nuts.modules.services.data.convert.DataRefiner;
import com.pap.nuts.modules.services.data.zip.ZipExtractService;
import com.pap.nuts.modules.services.threads.processes.VersionHandlerThread;

/**
 * This class handles the environ file, and the beans which use it.
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
