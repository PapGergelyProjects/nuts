package com.pap.nuts.modules.services.threads.processes;

import com.pap.nuts.NutAppInitializer;
import com.pap.nuts.modules.interfaces.AbstractProcessService;
import com.pap.nuts.modules.services.data.convert.DataRefiner;
import com.pap.nuts.modules.services.data.zip.ZipExtractService;

public class DataUpdateThread extends AbstractProcessService {

	@Override
	protected void logic() throws Exception {
		NutAppInitializer.getContext().getBean("extractor", ZipExtractService.class).extractZipFile();
		NutAppInitializer.getContext().getBean("refiner", DataRefiner.class).createInsertFromFile();
	}

}
