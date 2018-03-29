package com.pap.nuts.webservices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.pap.nuts.NutAppInitializer;
import com.pap.nuts.utils.ResourceReader;

@Path("/options")
public class OptionPage {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getPage(){
		ResourceReader file = NutAppInitializer.getContext().getBean(ResourceReader.class);
		return file.getOptionHtmlPage();
	}
	
}
