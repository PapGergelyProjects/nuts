package com.pap.nuts.modules.model.json;

/**
 * This class represents the getLocation json from swagger.
 * This is similar to the SwaggerFeed.
 * 
 * @author Pap Gergely
 *
 */
public class SwaggerLocation {
	
	public String status;
	public long ts;
	public Results results;
	
	public class Results{
		public Locations[] locations;
	}
	
	public class Locations{
		public long id;
		public long pid;
		public String t;
		public String n;
		public double lat;
		public double lng;
	}
}
