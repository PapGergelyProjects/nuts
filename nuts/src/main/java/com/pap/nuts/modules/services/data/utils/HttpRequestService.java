package com.pap.nuts.modules.services.data.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

public class HttpRequestService {
	
	private final Logger LOGGER = Logger.getLogger(HttpRequestService.class);
	
	private HttpRequestService(){}
	
	public static HttpRequestService instance = new HttpRequestService();
	
	public String getRequest(String urlAddress) throws IOException{
		URL url = new URL(urlAddress);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		
		StringBuilder sb = new StringBuilder();
		try(BufferedReader bfr = new BufferedReader(new InputStreamReader(conn.getInputStream()))){
			while(bfr.ready()){
				sb.append(bfr.readLine());
			}
		}catch(IOException e){
			LOGGER.error(e);
		}
		
		return sb.toString();
	}
	
}
