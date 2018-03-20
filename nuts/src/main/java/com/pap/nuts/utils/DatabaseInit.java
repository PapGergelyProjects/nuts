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

/**
 * This class responsible to create the DB structure, functions, views etc. from SQL files
 * 
 * @author Pap Gergely
 *
 */
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
		Resource arcPoints = resourceLoad.getResource("classpath:sql/arcpoints.sql");
		Resource pointsInRange = resourceLoad.getResource("classpath:sql/point_in_range.sql");
		Resource staticStops = resourceLoad.getResource("classpath:sql/m_view_static_stops.sql");
		Resource staticStopsTimes = resourceLoad.getResource("classpath:sql/m_view_static_stops_with_times.sql");
		Resource stopWithinRange = resourceLoad.getResource("classpath:sql/stops_within_radius.sql");
		Resource stopWithinRangeTime = resourceLoad.getResource("classpath:sql/stops_and_times_within_radius.sql");
		
		ResourceDatabasePopulator dataPop = new ResourceDatabasePopulator(initSchema, tableClear, arcPoints, pointsInRange, staticStops, staticStopsTimes, stopWithinRange, stopWithinRangeTime);
		dataPop.setSeparator("^;");
		DatabasePopulatorUtils.execute(dataPop, dataSource);
	}
}
