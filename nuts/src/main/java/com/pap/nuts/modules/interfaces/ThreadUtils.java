package com.pap.nuts.modules.interfaces;

import java.util.concurrent.TimeUnit;

public interface ThreadUtils {
	public default void process(String name, Runnable processes){};
	public default void process(long delayed, long timeQty, TimeUnit unit, Runnable... processes){};
	public default void process(long delayed, long timeQty, TimeUnit unit, String name, Runnable processes){};
	public default void shutdown(){};
}
