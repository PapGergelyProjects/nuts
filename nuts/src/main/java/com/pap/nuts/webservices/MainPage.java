package com.pap.nuts.webservices;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.pap.nuts.NutAppInitializer;
import com.pap.nuts.modules.model.beans.Coordinate;
import com.pap.nuts.modules.model.beans.SearchValues;
import com.pap.nuts.modules.model.beans.StopLocation;
import com.pap.nuts.modules.session.beans.CoordinateDao;
import com.pap.nuts.modules.session.beans.StopLocationDao;
import com.pap.nuts.utils.ResourceReader;

@Path("/")
public class MainPage {
	
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getPage(@Context ServletContext ctx){
		ResourceReader file = NutAppInitializer.getContext().getBean(ResourceReader.class);
		return file.getHtmlPage();
	}
	
	@POST
	@Path("/palce_stop")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public  Map<String, Map<Coordinate,List<StopLocation>>> locationWithStop(SearchValues post){
		System.err.println(post);
		Coordinate coord = post.getSearchCoordinate();
		Map<String, Map<Coordinate,List<StopLocation>>> stopLoc = NutAppInitializer.getContext().getBean(StopLocationDao.class).getAllStopWithinRadius(coord.getLatitude(), coord.getLongitude(), post.getRadius());
		return stopLoc;
	}
	
	
	@POST
	@Path("/stop_times")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String locationWithStopsAndTimes(SearchValues post){
		return "";
	}
	
}
