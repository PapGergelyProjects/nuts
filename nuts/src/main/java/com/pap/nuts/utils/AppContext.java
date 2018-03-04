package com.pap.nuts.utils;

import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.pap.nuts.NutAppInitializer;
import com.pap.nuts.modules.services.data.convert.DataRefiner;
import com.pap.nuts.modules.services.threads.ThreadServiceHandler;
import com.pap.nuts.modules.services.threads.processes.VersionHandlerThread;

public class AppContext implements ServletContextListener {
	
	private final Logger LOGGER = Logger.getLogger(AppContext.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ThreadServiceHandler.SHUTDOWN.shutdown();
		LOGGER.info("Nuts has been shutted down!");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOGGER.info("Nuts has been initialized!");
		NutAppInitializer.initApp();
		NutAppInitializer.getContext().getBean(DatabaseInit.class);
		ThreadServiceHandler.SCHEDULED.process(1, TimeUnit.HOURS, NutAppInitializer.getContext().getBean("versionOverwatch", VersionHandlerThread.class));
	}
	
}
