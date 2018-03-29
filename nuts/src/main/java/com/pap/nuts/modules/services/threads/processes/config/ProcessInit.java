package com.pap.nuts.modules.services.threads.processes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pap.nuts.modules.services.data.convert.DataRefiner;
import com.pap.nuts.modules.services.data.zip.ZipExtractService;
import com.pap.nuts.modules.services.threads.processes.VersionHandlerThread;

@Configuration
public class ProcessInit {
	
	@Bean
	public VersionHandlerThread versionOverwatch(){
		return new VersionHandlerThread();
	}
	
	@Bean
	public ZipExtractService extractor(){
		return new ZipExtractService();
	}
	
	@Bean
	public DataRefiner refiner(){
		return new DataRefiner();
	}
	
}
