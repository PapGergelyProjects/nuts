package com.pap.nuts.utils;

import java.io.IOException;
import java.util.Scanner;
import java.util.StringJoiner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class ResourceReader {
	
	@Value("classpath:html/nuts.html")
	private Resource cert;
	
	@PostConstruct
	public void init(){
	}
	
	@Bean
	public String getHtmlPage(){
		StringJoiner join = new StringJoiner("\n");
		try {
			Scanner scn = new Scanner(cert.getInputStream());
			while(scn.hasNext()){
				join.add(scn.nextLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return join.toString();
	}
	
}
