package com.pap.nuts.modules.session.beans;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.pap.nuts.modules.interfaces.DaoService;
import com.pap.nuts.modules.model.beans.Coordinate;
import com.pap.nuts.modules.model.beans.StopLocation;

@Repository
public class StopLocationDao extends JdbcDaoSupport implements DaoService<StopLocation> {
	
	@Autowired
	DataSource dataSource;
	
	@PostConstruct
	private void init(){
		setDataSource(dataSource);
	}
	
	@Override
	public void insert(StopLocation value) {}

	@Override
	public void update(StopLocation value) {}

	@Override
	public void execute(StopLocation value) {
		
	}

	@Override
	public StopLocation select() {
		return null;
	}

	@Override
	public StopLocation getAll() {
		return null;
	}


}
