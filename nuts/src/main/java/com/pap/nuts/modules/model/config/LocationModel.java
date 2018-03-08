package com.pap.nuts.modules.model.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.pap.nuts.modules.model.beans.Coordinate;
import com.pap.nuts.modules.model.beans.StopLocation;

@Configuration
public class LocationModel {
	
	@Bean
	@Scope("prototype")
	public StopLocation newStopLocation(){
		StopLocation location = new StopLocation();
		location.setStopCoordinate(new Coordinate());
		return location;
	}
	
	@Bean
	public StopLocation existsLocation(){
		StopLocation location = new StopLocation();
		location.setStopCoordinate(coordinate());
		return location;
	}
	
	@Bean
	public Coordinate coordinate(){
		return new Coordinate();
	}
	
}
