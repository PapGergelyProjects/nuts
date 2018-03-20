package com.pap.nuts.modules.services.threads.utils;

import java.util.concurrent.ThreadFactory;

/**
 * This is an factory class for the thread handling.
 * 
 * @author Pap Gergely
 *
 */
public class ThreadManufactorum implements ThreadFactory {
	
	private int priority;
	private String threadName;
	
	public ThreadManufactorum(int priority){
		this.priority = priority;
	}
	
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r);
		thread.setName(threadName);
		thread.setPriority(priority);
		thread.setDaemon(true);
		
		ThreadCache.addToThreadCache(thread);
		
		return thread;
	}

}
