package com.pap.nuts.modules.services.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.pap.nuts.modules.interfaces.ThreadUtils;
import com.pap.nuts.modules.services.threads.utils.ThreadManufactorum;

public enum ThreadServiceHandler implements ThreadUtils {
	
	SCHEDULED{
		
		@Override
		public void execute(Runnable... processes) {
			if(scheduledService==null){
				scheduledService = Executors.newScheduledThreadPool(3, new ThreadManufactorum(Thread.NORM_PRIORITY));
			}
			for(Runnable process : processes){
				scheduledService.scheduleAtFixedRate(process, 0, 1, TimeUnit.MINUTES);
			}
		}
		
	},
	SHUTDOWN{

		@Override
		public void execute(Runnable... process){}

		@Override
		public void shutdown() {
			if(scheduledService==null){
				scheduledService.shutdown();
			}
		}
	};
	
	private static ScheduledExecutorService scheduledService;
	public abstract void execute(Runnable... process);
}
