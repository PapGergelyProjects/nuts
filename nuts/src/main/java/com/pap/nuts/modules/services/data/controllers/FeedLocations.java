package com.pap.nuts.modules.services.data.controllers;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.google.gson.Gson;
import com.pap.nuts.NutAppInitializer;
import com.pap.nuts.modules.model.beans.main.FeedVersion;
import com.pap.nuts.modules.model.json.SelectedFeed;
import com.pap.nuts.modules.model.json.SwaggerFeed;
import com.pap.nuts.modules.model.json.SwaggerFeed.Feeds;
import com.pap.nuts.modules.model.json.SwaggerLocation;
import com.pap.nuts.modules.model.json.SwaggerLocation.Locations;
import com.pap.nuts.modules.services.data.utils.FeedUtils;
import com.pap.nuts.modules.services.data.utils.HttpRequestService;
import com.pap.nuts.modules.session.services.FeedVersionDao;

/**
 * This class is responsible to handle the location list functionality(e.g: save, load)
 * 
 * @author Pap Gergely
 *
 */
public class FeedLocations {
	
	@Value("${transit_feed_key}")
	private String transitApiKey;
	private Gson gson;
	private List<Feeds> feedList;
	List<Map<String, Object>> locationList = new ArrayList<>();
	
	private final Logger LGG = Logger.getLogger(FeedLocations.class);
	
	public FeedLocations(){
		gson = new Gson();
		feedList = new ArrayList<>();
		locationList = new ArrayList<>();
	}
	
	private List<Map<String, Object>> locations() throws IOException{
		if(locationList.size() == 0){
			String locationJson = HttpRequestService.instance.getRequest("http://api.transitfeeds.com/v1/getLocations?key="+transitApiKey);
			SwaggerLocation loc = gson.fromJson(locationJson, SwaggerLocation.class);
			Locations[] locationArray = loc.results == null ? new Locations[]{} : loc.results.locations;
			Set<Long> parentIds = Arrays.asList(locationArray).stream().map(mp -> mp.pid).collect(Collectors.toSet());
			List<Feeds> allFeed = getFeeds();
			for(Locations location : locationArray){
				if(!parentIds.contains(location.id)){
					Map<String, Object> bareLocations = new HashMap<>();
					bareLocations.put("id", String.valueOf(location.id));
					bareLocations.put("title", location.t);
					for(Feeds feed : allFeed){
						if(feed.l.id == location.id && feed.u.d != null){
							Map<String, String> subFeed = new HashMap<>();
							subFeed.put("title", feed.t);
							subFeed.put("latest", String.valueOf(feed.latest == null ? null : feed.latest.ts));
							bareLocations.put("sub_feed", subFeed);
							
							locationList.add(bareLocations);
							break;
						}
					}
				}
			}
		}
		return locationList;
	}
	
	private List<Feeds> getFeeds() throws IOException{
		if(feedList.size()==0){// TODO: It's a temporary solution, this will replace with a more sophisticated filter method which can manage the reload.
			feedList = FeedUtils.instance.getFeeds(transitApiKey);
		}
		
		return feedList;
	}
	
	public List<Map<String, Object>> locationDelegate(){
		List<Map<String, Object>> locs;
		try {
			locs = locations();
		} catch (IOException e) {
			LGG.error(e);
			locs = new ArrayList<>();
		}
		return locs;
	}
	
	/**
	 * When user hit the "Save location" button on the option surface.
	 * 
	 * @param feeds
	 */
	public void saveLocations(List<SelectedFeed> feeds){
		FeedVersionDao dao = NutAppInitializer.getContext().getBean(FeedVersionDao.class);
		List<FeedVersion> getAllStored = dao.getAll();
		List<Long> feedIds = getAllStored.stream().map(FeedVersion::getFeedId).collect(Collectors.toList());
		for(SelectedFeed feed : feeds){
			FeedVersion newVersion = NutAppInitializer.getContext().getBean("feedVersion",FeedVersion.class);
			newVersion.setFeedId(feed.id);
			newVersion.setTitle(feed.title);
			newVersion.setLatestVersion(Instant.ofEpochMilli(feed.latest*1000).atZone(ZoneId.systemDefault()).toLocalDate());
			if(feedIds.contains(feed.id)){
				dao.update(newVersion);
			}else{ 
				dao.insert(newVersion);
			}
			getAllStored.removeIf(pre -> pre.getFeedId() == feed.id);
		}
		
		getAllStored.forEach(id -> {
			FeedVersion deleteVersion = NutAppInitializer.getContext().getBean("feedVersion",FeedVersion.class);
			deleteVersion.setId(id.getId());
			dao.delete(deleteVersion);
		});
	}
	
}
