package com.pap.nuts.modules.interfaces;

import org.apache.log4j.Logger;

/**
 * This is a common implementation of the thread service which you can unify the process handling.
 * 
 * @author Pap Gergely
 *
 */
public abstract class AbstractThreadService implements Runnable{
	
	private final Logger LOGGER = Logger.getLogger(AbstractThreadService.class);
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
		while (!stop){
			try{
				logic();
			}catch(Exception e){
				LOGGER.error(e);
				stop = true;
			}
		}
	}
	
}
