package com.pap.nuts.services;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/start")
public class TestService {
	
	@GET
	@Path("/first")
	@Produces(MediaType.APPLICATION_JSON)
	public String getMsg(){
		String out = "Test page say: Hello World";
		
		return out;
	}
	
	@GET
	@Path("/{param}")
	public Response getResp(@PathParam("param") String msg){
		return Response.status(200).entity("REST say :"+msg).build();
	}
	
	@GET
	@Path("/")
	public String getPage(@Context ServletContext ctx){
		String ctxPath = ctx.getContextPath();
		
		return "<!DOCTYPE html>\n"
				+ "<html>\n"
				+ "  <head>\n"
				+ "    <meta charset=\"UTF-8\">\n"
				+ String.format("	   <link rel=\"shortcut icon\" type=\"image/png\" href=\"./%s/img/nuts.png\">\n", ctxPath)
				+ "    <title>Nuts project</title>\n"
				+ "  </head>\n"
				+ "<body>\n"
				+ "<p>Hello Rest <b>API<b></p>\n"
				+ "</body>\n"
				+ "</html>\n";
	}
}
