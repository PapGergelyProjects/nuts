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

import org.apache.log4j.Logger;

import com.pap.nuts.NutAppInitializer;
import com.pap.nuts.modules.model.beans.main.Coordinate;
import com.pap.nuts.modules.model.beans.main.SearchValues;
import com.pap.nuts.modules.model.beans.main.StopLocation;
import com.pap.nuts.modules.services.threads.utils.DataProcess;
import com.pap.nuts.modules.session.services.StopLocationDao;
import com.pap.nuts.utils.ResourceReader;

@Path("/")
public class MainPage {
	
	private final Logger LOGGER = Logger.getLogger(MainPage.class);
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getPage(@Context ServletContext ctx){
		ResourceReader file = NutAppInitializer.getContext().getBean(ResourceReader.class);
		return file.getHtmlPage();
	}
	
	
	@GET
	@Path("/server_stat")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getServerStatus(){
		return DataProcess.getFinishedTaskList();
	}
	
	@POST
	@Path("/stop_location")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public  Map<String, Map<Coordinate,List<StopLocation>>> locationWithStop(SearchValues post){
		LOGGER.info(post);
		Coordinate coord = post.getSearchCoordinate();
		Map<String, Map<Coordinate,List<StopLocation>>> stopLoc = NutAppInitializer.getContext().getBean(StopLocationDao.class).getAllStopWithinRadius(coord.getLatitude(), coord.getLongitude(), post.getRadius());
		
		return stopLoc;
	}
	
	
	@POST
	@Path("/stop_times")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Map<Coordinate,List<StopLocation>>> locationWithStopsAndTimes(SearchValues post){
		LOGGER.info(post);
		Coordinate coord = post.getSearchCoordinate();
		Map<String, Map<Coordinate,List<StopLocation>>> stopLocTimes = NutAppInitializer.getContext().getBean(StopLocationDao.class).getAllStopWithinRadiusWithTime(coord.getLatitude(), coord.getLongitude(), post.getRadius());
		
		return stopLocTimes;
	}
	
}
