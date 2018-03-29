package com.pap.nuts.modules.session.services;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.pap.nuts.modules.interfaces.DaoService;

@Repository
public class DataSourceDao<T> extends JdbcDaoSupport implements DaoService<String>{
	
	@Autowired
	DataSource dataSource;
	
	@PostConstruct
	private void init(){
		setDataSource(dataSource);
	}
	
	@Override
	public void insert(String value) {
		this.getJdbcTemplate().execute(value);
	}

	@Override
	public void update(String value) {}

	@Override
	public String select() {
		return null;
	}

	@Override
	public List<String> getAll() {
		return null;
	}

	@Override
	public void execute(String value) {
		this.getJdbcTemplate().execute(value);
	}

}
