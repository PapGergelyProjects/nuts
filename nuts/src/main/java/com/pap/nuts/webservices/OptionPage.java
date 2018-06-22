package com.pap.nuts.webservices;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.pap.nuts.NutAppInitializer;
import com.pap.nuts.modules.model.json.SelectedFeed;
import com.pap.nuts.modules.services.data.controllers.FeedLocations;
import com.pap.nuts.utils.ResourceReader;

@Path("/options")
public class OptionPage {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getPage(){
		ResourceReader file = NutAppInitializer.getContext().getBean(ResourceReader.class);
		return file.getOptionHtmlPage();
	}
	
	@GET
	@Path("/locations")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Map<String, Object>> getLocations(){
		return NutAppInitializer.getContext().getBean("locations", FeedLocations.class).locationDelegate();
	}
	
	
	@POST
	@Path("/save_location")
	@Consumes(MediaType.APPLICATION_JSON)
	public Void postLocation(List<SelectedFeed> feeds){
		System.out.println(feeds);
		NutAppInitializer.getContext().getBean("locations", FeedLocations.class).saveLocations(feeds);
		return null;
	}
}
