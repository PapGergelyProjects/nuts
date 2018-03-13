package com.pap.nuts.modules.services.threads.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is an factory class for the thread handling.
 * 
 * @author Pap Gergely
 *
 */
public class ThreadManufactorum implements ThreadFactory {
	
	private int priority;
	private volatile AtomicInteger cnt;
	
	public ThreadManufactorum(int priority){
		this.priority = priority;
		cnt = new AtomicInteger(0);
	}
	
	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r);
		thread.setName("simple_thread_"+cnt.incrementAndGet());
		thread.setPriority(priority);
		thread.setDaemon(true);
		
		ThreadCache.addToThreadCache(thread);
		
		return thread;
	}

}
