package com.pap.nuts.modules.session.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pap.nuts.modules.interfaces.DaoService;
import com.pap.nuts.modules.session.DataSourceDao;

@Service
public class DataSourceService implements DaoService<String>{
	
	@Autowired
	DataSourceDao<String> dataSource;

	@Override
	public void insert(String value) {
		dataSource.insert(value);
	}

	@Override
	public void update(String value) {}

	@Override
	public String select() {
		return null;
	}

	@Override
	public String getAll() {
		return null;
	}

	@Override
	public void execute(String value) {
		dataSource.execute(value);
	}
	
}
