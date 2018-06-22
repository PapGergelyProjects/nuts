package com.pap.nuts.modules.services.threads.processes.config;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.pap.nuts.NutAppInitializer;
import com.pap.nuts.modules.services.threads.ThreadServiceHandler;
import com.pap.nuts.modules.services.threads.processes.DataUpdateThread;
import com.pap.nuts.modules.services.threads.processes.VersionHandlerThread;

/**
 * This class handle threads.
 * @author Pap Gergely
 *
 */
@Service
public class ThreadsInit {
	
	@Value("${update_delay_time}")
	private String delayTime;
	
	@Value("${update_restart_interval}")
	private String repeateTime;
	
	@Value("${transit_feed_key}")
	private String transitApiKey;
	
	@Value("${temp_directory}")
	private String tempFolder;
	
	@Value("${tables}")
	private String tables;
	
	@Bean
	public VersionHandlerThread versionOverwatch(){
		long initDelay = 0;
		long days = 1;
		if(!delayTime.isEmpty()){
			String[] delay = delayTime.split(":");
			LocalTime time = LocalTime.of(Integer.valueOf(delay[0]), Integer.valueOf(delay[1]), Integer.valueOf(delay[2]));
			initDelay = time.get(ChronoField.MILLI_OF_DAY);
		}
		if(!repeateTime.isEmpty()){
			days = Long.valueOf(repeateTime);
		}
		VersionHandlerThread vers = new VersionHandlerThread();
		vers.setTempFolder(tempFolder);
		vers.setTransitApiKey(transitApiKey);
		vers.setInitDelay(initDelay);
		vers.setDay(days);
		return vers;
	}
	
	@Bean
	public DataUpdateThread updateService(){
		DataUpdateThread update = new DataUpdateThread();
		update.setTempFolder(tempFolder);
		update.setTables(tables);
		return update;
	}
}
