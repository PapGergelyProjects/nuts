package com.pap.nuts.utils;

import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInit {
	
	@Autowired
	private ResourceLoader resourceLoad;
	
	@Autowired
	private DataSource dataSource;
	
	@PostConstruct
	public void init(){
		Resource initSchema = resourceLoad.getResource("classpath:sql/create_schema_with_tables.sql");
		Resource tableClear = resourceLoad.getResource("classpath:sql/clear_tables.sql");
		
		ResourceDatabasePopulator dataPop = new ResourceDatabasePopulator(initSchema, tableClear);
		dataPop.setSeparator("^;");
		DatabasePopulatorUtils.execute(dataPop, dataSource);
	}
}
