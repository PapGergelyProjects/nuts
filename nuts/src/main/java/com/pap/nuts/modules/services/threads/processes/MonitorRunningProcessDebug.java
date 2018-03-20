package com.pap.nuts.modules.services.threads.processes;

import org.apache.log4j.Logger;

import com.pap.nuts.modules.interfaces.AbstractProcessService;
import com.pap.nuts.modules.services.threads.utils.ThreadCache;

public class MonitorRunningProcessDebug extends AbstractProcessService {
	
	private final Logger LOGG = Logger.getLogger(MonitorRunningProcessDebug.class);
	
	@Override
	protected void logic() throws Exception {
		LOGG.info("Current threads in pool: "+ThreadCache.getAllThread().size());
	}

}
