package com.pap.nuts.modules.session.beans;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.pap.nuts.modules.interfaces.DaoService;
import com.pap.nuts.modules.model.beans.Coordinate;

@Repository
public class CoordinateDao extends JdbcDaoSupport implements DaoService<Coordinate> {
	
	@Autowired
	DataSource dataSource;
	
	@PostConstruct
	private void init(){
		setDataSource(dataSource);
	}
	
	@Override
	public void insert(Coordinate value) {
	}

	@Override
	public void update(Coordinate value) {
	}

	@Override
	public void execute(Coordinate value) {
	}

	@Override
	public Coordinate select() {
		final String sql = "SELECT stop_lat, stop_lon FROM stops WHERE id=1";
		RowMapper<Coordinate> mapper = new RowMapper<Coordinate>(){
			@Override
			public Coordinate mapRow(ResultSet rs, int rowNum) throws SQLException {
				Coordinate coor = new Coordinate();
				coor.setLatitude(rs.getDouble("stop_lat"));
				coor.setLongitude(rs.getDouble("stop_lon"));
				return coor;
			}
		};
		return this.getJdbcTemplate().queryForObject(sql, mapper);
	}

	@Override
	public Coordinate getAll() {
		return null;
	}

}
