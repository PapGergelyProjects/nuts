package com.pap.nuts.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppContext implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ServletContextListener.super.contextDestroyed(sce);
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("WEBAPP INIT!!!");
	}
	
}
