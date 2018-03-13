package com.pap.nuts.modules.model.beans;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class hold a stop location.
 * 
 * @author Pap Gergely
 *
 */
public class StopLocation {
	
	private String stopName;
	private String routeName;
	private Coordinate stopCoordinate;
	
	public String getStopName() {
		return stopName;
	}
	
	public void setStopName(String stopName) {
		this.stopName = stopName;
	}
	
	public String getRouteName() {
		return routeName;
	}
	
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	
	public Coordinate getStopCoordinate() {
		return stopCoordinate;
	}
	
	public void setStopCoordinate(Coordinate stopCoordinate) {
		this.stopCoordinate = stopCoordinate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 11;
		result = prime * result + ((routeName == null) ? 0 : routeName.hashCode());
		result = prime * result + ((stopCoordinate == null) ? 0 : stopCoordinate.hashCode());
		result = prime * result + ((stopName == null) ? 0 : stopName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StopLocation other = (StopLocation) obj;
		if (routeName == null) {
			if (other.routeName != null)
				return false;
		} else if (!routeName.equals(other.routeName))
			return false;
		if (stopCoordinate == null) {
			if (other.stopCoordinate != null)
				return false;
		} else if (!stopCoordinate.equals(other.stopCoordinate))
			return false;
		if (stopName == null) {
			if (other.stopName != null)
				return false;
		} else if (!stopName.equals(other.stopName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StopLocation[stopName=" + stopName + ", routeName=" + routeName + ", stopCoordinate=" + stopCoordinate+"]";
	}
	
	
}
