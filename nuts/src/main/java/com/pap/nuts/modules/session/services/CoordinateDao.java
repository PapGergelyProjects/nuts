package com.pap.nuts.modules.session.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	public void delete(Coordinate value) {
	}
	
	@Override
	public void execute(Coordinate value) {
	}

	@Override
	public Coordinate select(long id) {
		final String sql = "SELECT stop_lat, stop_lon FROM stops WHERE id="+id;
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
	public List<Coordinate> getAll() {
		
		return null;
	}
	
	public List<Coordinate> getRadiusCoordinates(double lat, double lon, int radius){
		final String sql = "SELECT * FROM stops_within_radius("+lat+","+lon+","+radius+")";
		List<Map<String, Object>> coords = this.getJdbcTemplate().queryForList(sql);
		List<Coordinate> result = new ArrayList<>();
		coords.forEach(crd -> {
			Coordinate crood = new Coordinate();
			crood.setLatitude(Double.valueOf(crd.get("stop_lat").toString()));
			crood.setLongitude(Double.valueOf(crd.get("stop_lon").toString()));
			
			result.add(crood);
		});
		
		return result;
	}
}
