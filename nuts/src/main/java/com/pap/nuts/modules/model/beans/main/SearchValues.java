package com.pap.nuts.modules.model.beans.main;

import com.pap.nuts.modules.interfaces.AbstractBean;

/**
 * This is an utility bean for the http communication.
 * The client side transmit data to server side with this class.
 * 
 * @author Pap Gergely
 *
 */
public class SearchValues extends AbstractBean{
	
	private Coordinate searchCoordinate;
	private double radius;
	
	public Coordinate getSearchCoordinate() {
		return searchCoordinate;
	}
	public void setSearchCoordinate(Coordinate searchCoordinate) {
		this.searchCoordinate = searchCoordinate;
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	@Override
	public String toString() {
		return "SearchValues [searchCoordinate=" + searchCoordinate + ", radius=" + radius + "]";
	}
}
