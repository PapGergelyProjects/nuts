package com.pap.nuts.webservices;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import com.pap.nuts.utils.WebPageDefaults;

@Path("/login")
public class LoginService {
	
	@GET
	@Path("/")
	public String getPage(@Context ServletContext ctx){
		return WebPageDefaults.LOGIN.getPage("", ctx.getContextPath());
	}
	
}
