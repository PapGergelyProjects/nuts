package com.pap.nuts.modules.services.threads.utils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.apache.log4j.Logger;

/**
 * This class store the executed thread's name, which with can track the current tasks.
 * 
 * @author Pap Gergely
 *
 */
public class ThreadCache {
	
	private static final Logger LGG = Logger.getLogger(ThreadCache.class);
	private static volatile WeakHashMap<Thread, Void> threadCache = new WeakHashMap<>();
	private static final Map<String, LocalDateTime> VERSION_CACHE = new HashMap<>();
	
	public static void addToThreadCache(Thread thread){
		if(!threadCache.containsKey(thread)){
			LGG.debug(thread.getName()+" is added.");
			threadCache.put(thread, null);
		}else{
			LGG.error(thread.getName()+" is already added.");
		}
	}
	
	public static Set<Thread> getAllThread(){
		return threadCache.keySet();
	}

	public static Map<String, LocalDateTime> getVersionCache() {
		return VERSION_CACHE;
	}
}
