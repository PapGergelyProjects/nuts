package com.pap.nuts.modules.services.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.pap.nuts.modules.interfaces.ThreadUtils;
import com.pap.nuts.modules.services.threads.utils.ThreadManufactorum;

public enum ThreadServiceHandler implements ThreadUtils {
	
	FIXED {
		@Override
		public void process(Runnable... processes) {
			if(cachedThreadPool==null){
				cachedThreadPool = Executors.newCachedThreadPool(manufactorum);
			}
			for(Runnable process : processes){
				cachedThreadPool.execute(process);
			}
		}
	},
	SCHEDULED{
		@Override
		public void process(long timeQty, TimeUnit unit, Runnable... processes){
			if(scheduledService==null){
				scheduledService = Executors.newScheduledThreadPool(3, manufactorum);
			}
			for(Runnable process : processes){
				scheduledService.scheduleAtFixedRate(process, 0, timeQty, unit);
			}
		}
	},
	SHUTDOWN{
		@Override
		public void shutdown() {
			if(scheduledService!=null){
				scheduledService.shutdown();
			}
			if(cachedThreadPool!=null){
				cachedThreadPool.shutdown();
			}
		}
	};
	
	private static ThreadManufactorum manufactorum = new ThreadManufactorum(Thread.NORM_PRIORITY);
	private static ScheduledExecutorService scheduledService;
	private static ExecutorService cachedThreadPool;
}
