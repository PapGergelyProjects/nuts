package com.pap.nuts.modules.model.beans;

import java.util.Arrays;

/**
 * This bean is hold a stop location.
 * The properties of the bean represents a valid location.
 * 
 * @author Pap Gergely
 *
 */
public class StopLocation {
	
	private String stopName;
	private String routeName;
	private double stopDistance;
	private String stopColor;
	private String stopTextColor;
	private String[] departureTime; // TODO: if needed, it can be LocalTime, but for now String is adequate.
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
	public double getStopDistance() {
		return stopDistance;
	}
	public void setStopDistance(double stopDistance) {
		this.stopDistance = stopDistance;
	}
	public String getStopColor() {
		return stopColor;
	}
	public void setStopColor(String stopColor) {
		this.stopColor = stopColor;
	}
	public String getStopTextColor() {
		return stopTextColor;
	}
	public void setStopTextColor(String stopTextColor) {
		this.stopTextColor = stopTextColor;
	}
	public String[] getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(String[] departureTime) {
		this.departureTime = departureTime;
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
		int result = 1;
		result = prime * result + Arrays.hashCode(departureTime);
		result = prime * result + ((routeName == null) ? 0 : routeName.hashCode());
		result = prime * result + ((stopColor == null) ? 0 : stopColor.hashCode());
		result = prime * result + ((stopCoordinate == null) ? 0 : stopCoordinate.hashCode());
		long temp;
		temp = Double.doubleToLongBits(stopDistance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((stopName == null) ? 0 : stopName.hashCode());
		result = prime * result + ((stopTextColor == null) ? 0 : stopTextColor.hashCode());
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
		if (!Arrays.equals(departureTime, other.departureTime))
			return false;
		if (routeName == null) {
			if (other.routeName != null)
				return false;
		} else if (!routeName.equals(other.routeName))
			return false;
		if (stopColor == null) {
			if (other.stopColor != null)
				return false;
		} else if (!stopColor.equals(other.stopColor))
			return false;
		if (stopCoordinate == null) {
			if (other.stopCoordinate != null)
				return false;
		} else if (!stopCoordinate.equals(other.stopCoordinate))
			return false;
		if (Double.doubleToLongBits(stopDistance) != Double.doubleToLongBits(other.stopDistance))
			return false;
		if (stopName == null) {
			if (other.stopName != null)
				return false;
		} else if (!stopName.equals(other.stopName))
			return false;
		if (stopTextColor == null) {
			if (other.stopTextColor != null)
				return false;
		} else if (!stopTextColor.equals(other.stopTextColor))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "StopLocation [stopName=" + stopName + ", routeName=" + routeName + ", stopDistance=" + stopDistance
				+ ", stopColor=" + stopColor + ", stopTextColor=" + stopTextColor + ", departureTime="
				+ Arrays.toString(departureTime) + ", stopCoordinate=" + stopCoordinate + "]";
	}
}
