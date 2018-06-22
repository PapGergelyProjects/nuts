package com.pap.nuts.utils;

import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.pap.nuts.NutAppInitializer;
import com.pap.nuts.modules.services.threads.ThreadServiceHandler;
import com.pap.nuts.modules.services.threads.processes.DataUpdateThread;
import com.pap.nuts.modules.services.threads.processes.VersionHandlerThread;
import com.pap.nuts.modules.services.threads.processes.config.ThreadsInit;

/**
 * This is where everything begins, the appcontext launch the whole application, init Spring, and the data updater/downloader.
 * 
 * @author Pap Gergely
 *
 */
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
		VersionHandlerThread version = NutAppInitializer.getContext().getBean(ThreadsInit.class).versionOverwatch();
		DataUpdateThread update = NutAppInitializer.getContext().getBean(ThreadsInit.class).updateService();
		ThreadServiceHandler.SCHEDULED.process(version.getInitDelay(), version.getDay(), TimeUnit.DAYS, "version_overwatch", version);
		ThreadServiceHandler.SCHEDULED.process(1, 1, TimeUnit.MINUTES, "version_updater", update);
	}
	
}
