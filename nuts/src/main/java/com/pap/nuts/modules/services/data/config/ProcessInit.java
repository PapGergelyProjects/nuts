package com.pap.nuts.modules.services.data.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pap.nuts.modules.services.data.controllers.FeedLocations;

@Configuration
public class ProcessInit {
	
	
	@Bean
	public FeedLocations locations(){
		return new FeedLocations();
	}
	
}
