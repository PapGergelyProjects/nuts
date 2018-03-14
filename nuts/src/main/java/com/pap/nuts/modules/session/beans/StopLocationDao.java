package com.pap.nuts.modules.session.beans;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.pap.nuts.NutAppInitializer;
import com.pap.nuts.modules.interfaces.DaoService;
import com.pap.nuts.modules.model.beans.Coordinate;
import com.pap.nuts.modules.model.beans.StopLocation;

@Repository
public class StopLocationDao extends JdbcDaoSupport implements DaoService<StopLocation> {
	
	private DecimalFormat doubleFormat = new DecimalFormat(".#");
	
	@Autowired
	private DataSource dataSource;
	
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
	public List<StopLocation> getAll() {
		return null;
	}
	
	public Map<String, Map<Coordinate,List<StopLocation>>> getAllStopWithinRadius(double centerLat, double centerLon, double radius){
		final String sql = "SELECT * FROM stops_within_radius("+centerLat+","+centerLon+","+radius+") "
							+ "GROUP BY stop_name, stop_lat, stop_lon, route_name, stop_distance, stop_color, text_color "
							+ "ORDER BY route_name ";
		List<Map<String, Object>> resultSet = this.getJdbcTemplate().queryForList(sql);
		List<StopLocation> stopLocations = new ArrayList<>();
		resultSet.forEach(res -> {
			StopLocation location = NutAppInitializer.getContext().getBean("existsLocation",StopLocation.class);
			location.setStopName(String.valueOf(res.get("stop_name")));
			location.setRouteName(String.valueOf(res.get("route_name")));
			location.getStopCoordinate().setLatitude(Double.valueOf(res.get("stop_lat").toString()));
			location.getStopCoordinate().setLongitude(Double.valueOf(res.get("stop_lon").toString()));
			double dist = Double.valueOf(doubleFormat.format(Double.valueOf(res.get("stop_distance").toString())).replace(",", "."));
			location.setStopDistance(dist);
			location.setStopColor(res.get("stop_color").toString());
			location.setStopTextColor(res.get("text_color").toString());
			stopLocations.add(location);
		});
		
		Map<String, List<StopLocation>> stopNamegroup = stopLocations.stream().collect(Collectors.groupingBy(StopLocation::getStopName));
		Map<String, Map<Coordinate,List<StopLocation>>> coordGroup = new HashMap<>();
		stopNamegroup.forEach((k,v) -> {
			Map<Coordinate, List<StopLocation>> coordinateGroup = v.stream().collect(Collectors.groupingBy(StopLocation::getStopCoordinate));
			coordGroup.put(k, coordinateGroup);
		});
		
		return coordGroup;
	}

}
