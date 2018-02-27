package com.pap.nuts.modules.interfaces;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public abstract class DaoLayer<T> extends JdbcDaoSupport implements DaoLayerParts<T> {
	
	@Autowired
	DataSource dataSource;
	
	@PostConstruct
	private void init(){
		setDataSource(dataSource);
	}
	
}
