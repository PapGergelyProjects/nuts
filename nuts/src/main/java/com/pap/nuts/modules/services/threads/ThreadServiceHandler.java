package com.pap.nuts.modules.services.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.pap.nuts.modules.interfaces.ThreadUtils;
import com.pap.nuts.modules.services.threads.utils.ThreadCache;
import com.pap.nuts.modules.services.threads.utils.ThreadManufactorum;

/**
 * <p>
 * This enum is responsible for the threading.
 * With this class you can launch threads with different functionality,
 * for instance: fixed threads(run once), scheduled(run between the specified interval).
 * </p>
 * 
 * @author Pap Gergely
 *
 */
public enum ThreadServiceHandler implements ThreadUtils {
	
	FIXED {
		@Override
		public void process(String name, Runnable process) {
			if(cachedThreadPool==null){
				cachedThreadPool = Executors.newCachedThreadPool(manufactorum);
			}
			if(!ThreadCache.getAllThread().stream().anyMatch(pre -> pre.getName().equals(name))){
				manufactorum.setThreadName(name);
				cachedThreadPool.execute(process);
			}else{
				LOGG.warn("The thread "+name+" has already in thread pool.");
			}
		}
	},
	SCHEDULED{
		@Override
		public void process(long delayed, long timeQty, TimeUnit unit, String name, Runnable process){
			if(scheduledService==null){
				scheduledService = Executors.newScheduledThreadPool(3, manufactorum);
			}
			if(!ThreadCache.getAllThread().stream().anyMatch(pre -> pre.getName().equals(name))){
				manufactorum.setThreadName(name);
				scheduledService.scheduleAtFixedRate(process, delayed, timeQty, unit);
			}else{
				LOGG.warn("The thread "+name+" has already in thread pool.");
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
	private static Logger LOGG = Logger.getLogger(ThreadServiceHandler.class);
}
