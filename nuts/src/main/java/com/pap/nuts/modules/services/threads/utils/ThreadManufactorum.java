package com.pap.nuts.modules.services.threads.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This is an factory class for the thread handling.
 * 
 * @author Pap Gergely
 *
 */
public class ThreadManufactorum implements ThreadFactory {
	
	private int priority;
	private String threadName;
	private AtomicLong cnt;
	
	public ThreadManufactorum(int priority){
		this.priority = priority;
		cnt = new AtomicLong(0);
	}
	
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r);
		thread.setName("process_"+cnt.getAndIncrement());
		thread.setPriority(priority);
		thread.setDaemon(true);
		
		ThreadCache.addToThreadCache(thread);
		
		return thread;
	}

}
