package com.pap.nuts.modules.session.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.pap.nuts.NutAppInitializer;
import com.pap.nuts.modules.interfaces.DaoService;
import com.pap.nuts.modules.model.beans.main.FeedVersion;

@Repository
public class FeedVersionDao extends JdbcDaoSupport implements DaoService<FeedVersion> {
	
	@Autowired
	private DataSource dataSource;
	
	@PostConstruct
	private void init(){
		setDataSource(dataSource);
	}
	
	@Override
	public void insert(FeedVersion value) {
		final String insert = "INSERT INTO feed_version(feed_id, title, latest_version, recent, new_version) VALUES(?, ?, ?, ?, ?);";
		this.getJdbcTemplate().update(insert, new Object[]{value.getFeedId(), value.getTitle(), Date.from(value.getLatestVersion().atStartOfDay(ZoneId.systemDefault()).toInstant()), true, true});
	}

	@Override
	public void update(FeedVersion value) {
		final String update="UPDATE feed_version SET feed_id=?, title=?, latest_version=?, recent=?, new_version=? WHERE id="+value.getId();
		this.getJdbcTemplate().update(update, new Object[]{value.getFeedId(), value.getTitle(), Date.from(value.getLatestVersion().atStartOfDay(ZoneId.systemDefault()).toInstant()), value.isRecent(), value.isNewVersion()});
	}
	
	@Override
	public void delete(FeedVersion value) {
		this.getJdbcTemplate().execute("DELETE FROM feed_version WHERE id="+value.getId());
	}

	@Override
	public void execute(String query) {
		this.getJdbcTemplate().execute(query);
	}

	@Override
	public FeedVersion select(long id) {
		final String select = "SELECT id, feed_id, title, latest_version, recent, new_version FROM feed_version WHERE feed_id="+id;
		RowMapper<FeedVersion> mapper = (rs, rows) -> {
			FeedVersion row = NutAppInitializer.getContext().getBean("feedVersion", FeedVersion.class);
			row.setId(rs.getLong("id"));
			row.setFeedId(rs.getLong("feed_id"));
			row.setTitle(rs.getString("title"));
			row.setLatestVersion(LocalDate.parse(rs.getString("latest_version"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			row.setRecent(rs.getBoolean("recent"));
			row.setNewVersion(rs.getBoolean("new_version"));
			return row;
		};
		
		return this.getJdbcTemplate().queryForObject(select, mapper);
	}

	@Override
	public List<FeedVersion> getAll() {
		List<Map<String, Object>> allFeed = this.getJdbcTemplate().queryForList("SELECT id, feed_id, title, latest_version, recent, new_version FROM feed_version");
		List<FeedVersion> res = new ArrayList<>();
		allFeed.forEach(feed -> {
			FeedVersion vers = NutAppInitializer.getContext().getBean("feedVersion", FeedVersion.class);
			vers.setId(Long.valueOf(feed.get("id").toString()));
			vers.setFeedId(Long.valueOf(feed.get("feed_id").toString()));
			vers.setTitle(feed.get("title").toString());
			vers.setLatestVersion(LocalDate.parse(feed.get("latest_version").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			vers.setRecent((boolean)feed.get("recent"));
			vers.setNewVersion((boolean)feed.get("new_version"));
			res.add(vers);
		});
		return res;
	}

}
