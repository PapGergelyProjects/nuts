package com.pap.nuts.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.pap.nuts.modules.services.data.convert.DataRefiner;
import com.pap.nuts.modules.services.data.zip.ZipExtractService;
import com.pap.nuts.modules.services.threads.processes.VersionHandlerThread;

@Configuration
@PropertySource("classpath:config.properties")
public class PropertiesFile {
		
	@Autowired
	private Environment env;
	
	@Bean
	public VersionHandlerThread versionOverwatch(){
		return new VersionHandlerThread(env);
	}
	
	@Bean
	public ZipExtractService extractor(){
		return new ZipExtractService(env);
	}
	
	@Bean
	public DataRefiner refiner(){
		return new DataRefiner(env);
	}
	
}
