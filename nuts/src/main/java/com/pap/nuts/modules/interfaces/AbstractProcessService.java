package com.pap.nuts.modules.interfaces;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

public abstract class AbstractProcessService implements Runnable {

	private final Logger LOGGER = Logger.getLogger(AbstractProcessService.class);
	private boolean stop = false;
	protected abstract void logic() throws Exception;
	

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	@Override
	public void run(){
		try{
			logic();
		}catch(Exception e){
			StringJoiner join = new StringJoiner("\n");
			Arrays.asList(e.getStackTrace()).stream().forEach(el -> join.add(el.toString()));
			LOGGER.error(join.toString());
			stop = true;
		}
	}

}
