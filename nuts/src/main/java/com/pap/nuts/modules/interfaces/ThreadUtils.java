package com.pap.nuts.modules.interfaces;

import java.util.concurrent.TimeUnit;

public interface ThreadUtils {
	public default void process(Runnable... processes){};
	public default void process(long timeQty, TimeUnit unit, Runnable... processes){};
	public default void shutdown(){};
}
