package com.pap.nuts.modules.services.threads.processes;

import com.pap.nuts.modules.interfaces.AbstractProcessService;
import com.pap.nuts.modules.services.data.convert.DataRefiner;
import com.pap.nuts.modules.services.data.zip.ZipExtractService;

public class DataUpdateThread extends AbstractProcessService {

	@Override
	protected void logic() throws Exception {
		ZipExtractService.extractZipFile();
		DataRefiner refiner = new DataRefiner();
		refiner.createInsertFromFile();
	}

}
