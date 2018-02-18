package com.pap.nuts.services;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path("/login")
public class LoginService {
	
	@GET
	@Path("/")
	public String getPage(@Context ServletContext ctx){
		String ctxPath = ctx.getContextPath();
		
		return "<!DOCTYPE html>\n"
				+ "<html>\n"
				+ "  <head>\n"
				+ "    <meta charset=\"UTF-8\">\n"
				+ String.format("	   <link rel=\"shortcut icon\" type=\"image/png\" href=\"%s/img/nuts.png\">\n", ctxPath)
				+ "    <title>Nuts project</title>\n"
				+ "  </head>\n"
				+ "<body>\n"
				+ "<p>Hello Rest <b>API Login<b></p>\n"
				+ "</body>\n"
				+ "</html>\n";
	}
	
}
