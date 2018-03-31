package com.pap.nuts.modules.model.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.pap.nuts.modules.model.beans.Coordinate;
import com.pap.nuts.modules.model.beans.FeedVersion;
import com.pap.nuts.modules.model.beans.StopLocation;

@Configuration
public class BeanConfig {
	
	@Bean
	@Scope("prototype")
	public StopLocation existsLocation(){
		StopLocation location = new StopLocation();
		location.setStopCoordinate(coordinate());
		return location;
	}
	
	@Bean
	@Scope("prototype")
	public Coordinate coordinate(){
		return new Coordinate();
	}
	
	@Bean
	@Scope("prototype")
	public FeedVersion feedVersion(){
		return new FeedVersion();
	}
	
}
