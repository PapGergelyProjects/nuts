package com.pap.nuts.modules.services.threads;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


public class SimpleThreadFactory implements ThreadFactory {
	
	private int priority;
	private volatile AtomicInteger cnt;
	
	public SimpleThreadFactory(int priority){
		this.priority = priority;
		cnt = new AtomicInteger(0);
	}
	
	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r);
		thread.setName("simple_thread_"+cnt.incrementAndGet());
		thread.setPriority(priority);
		thread.setDaemon(true);
		
		ThreadService.addToThreadCache(thread);
		
		return thread;
	}

}
