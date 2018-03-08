package com.pap.nuts.modules.model.beans;

import org.springframework.beans.factory.annotation.Autowired;

public class StopLocation {
	
	private String stopName;
	private String arrivalTime;
	private Coordinate stopCoordinate;
	
	public String getStopName() {
		return stopName;
	}
	
	public void setStopName(String stopName) {
		this.stopName = stopName;
	}
	
	public String getArrivalTime() {
		return arrivalTime;
	}
	
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	public Coordinate getStopCoordinate() {
		return stopCoordinate;
	}
	
	public void setStopCoordinate(Coordinate stopCoordinate) {
		this.stopCoordinate = stopCoordinate;
	}

	@Override
	public String toString() {
		return "Location {stopName=" + stopName + ", arrivalTime=" + arrivalTime + ", stopCoordinate=" + stopCoordinate+"}";
	}
	
}
