package com.pap.nuts.modules.services.threads.utils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class ThreadCache {
	
	private static volatile WeakHashMap<Thread, Void> threadCache = new WeakHashMap<>();
	private static final Map<String, LocalDateTime> VERSION_CACHE = new HashMap<>();
	
	public static void addToThreadCache(Thread thread){
		threadCache.put(thread, null);
	}
	
	public static Iterator<Thread> getAllThread(){
		return threadCache.keySet().iterator();
	}

	public static Map<String, LocalDateTime> getVersionCache() {
		return VERSION_CACHE;
	}
}
