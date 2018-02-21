package com.pap.nuts.modules.services.threads.utils;

import java.util.Iterator;
import java.util.WeakHashMap;

public class ThreadCache {
	
	private static volatile WeakHashMap<Thread, Void> threadCache = new WeakHashMap<>();
	
	public static void addToThreadCache(Thread thread){
		threadCache.put(thread, null);
	}
	
	public static Iterator<Thread> getAllThread(){
		return threadCache.keySet().iterator();
	}
	
}
