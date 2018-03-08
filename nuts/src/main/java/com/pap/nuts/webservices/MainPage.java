package com.pap.nuts.webservices;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.pap.nuts.NutAppInitializer;
import com.pap.nuts.modules.model.beans.Coordinate;
import com.pap.nuts.modules.session.beans.CoordinateDao;
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Coordinate postMethod(Coordinate post){
		System.err.println(post);
		CoordinateDao dao = NutAppInitializer.getContext().getBean(CoordinateDao.class); 
		Coordinate coord = dao.select();
		return coord;
	}
	
}
