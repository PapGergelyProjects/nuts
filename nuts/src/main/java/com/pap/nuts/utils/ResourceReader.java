package com.pap.nuts.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.StringJoiner;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class ResourceReader {
	
	private final Logger LOGG = Logger.getLogger(ResourceReader.class);

	@Value("${google_api_key}")
	private String apiKey; 
	
	@Value("classpath:html/nutsAlter.html")
	private Resource mainPage;
	
	@Value("classpath:html/nuts_options.html")
	private Resource optionPage;
	
	@PostConstruct
	public void init(){
	}
	
	@Bean
	@Lazy
	public String getHtmlPage(){
		String pageWithKey = readFromStream(mainPage).replace("<api_key>", apiKey);
		return pageWithKey;
	}
	
	@Bean
	@Lazy
	public String getOptionHtmlPage(){
		return readFromStream(optionPage);
	}
	
	
	private String readFromStream(Resource resource){
		StringJoiner join = new StringJoiner("\n");
		try(BufferedReader bfr = new BufferedReader(new InputStreamReader(resource.getInputStream(), "UTF-8"))){
			while(bfr.ready()){
				join.add(bfr.readLine());
			}
		}catch(IOException e){
			LOGG.error(e);
		}
		
		return join.toString();
	}
	
}
