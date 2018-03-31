package com.pap.nuts.modules.services.threads.processes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.google.gson.Gson;
import com.pap.nuts.NutAppInitializer;
import com.pap.nuts.modules.interfaces.AbstractProcessService;
import com.pap.nuts.modules.interfaces.DaoService;
import com.pap.nuts.modules.model.beans.FeedVersion;
import com.pap.nuts.modules.model.json.SwaggerFeed;
import com.pap.nuts.modules.model.json.SwaggerFeed.FeedURL;
import com.pap.nuts.modules.model.json.SwaggerFeed.Feeds;
import com.pap.nuts.modules.model.json.SwaggerFeed.Latest;
import com.pap.nuts.modules.model.json.SwaggerFeed.Location;
import com.pap.nuts.modules.session.services.FeedVersionDao;

/**
 * Check the gtfs content version and proceed the download when it's needed.
 * When new version is ready, this process will download the zip archive and store the latest versions of the data.
 * 
 * @author Pap Gergely
 *
 */
public class VersionHandlerThread extends AbstractProcessService{
	
	private final Logger LOGGER = Logger.getLogger(VersionHandlerThread.class);
	
	@Value("${transit_feed_key}")
	private String transitApiKey;
	
	@Value("${temp_directory}")
	private String tempFolder;
	
	private Gson gson = new Gson();
	
	@Override
	protected void logic() throws Exception {
		List<String> links = checkSelectedFeed();
		for (String link : links) {
			String[] fileUrl = link.split("/");
			downloadFile(link,fileUrl[fileUrl.length-1]);
		}
	}
	
	private List<String> checkSelectedFeed() throws IOException{
		List<String> downloadLinks = new ArrayList<>();
		DaoService<FeedVersion> dbSrv = NutAppInitializer.getContext().getBean(FeedVersionDao.class);
		List<FeedVersion> feedVers = dbSrv.getAll();
		for (FeedVersion feedVersion : feedVers) {
			String urlAddress = String.format("http://api.transitfeeds.com/v1/getFeeds?key=%sf&location=%d&descendants=1&page=1&limit=1000", transitApiKey, feedVersion.getFeedId());
			URL url = new URL(urlAddress);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			SwaggerFeed feed = getTransitFeedJson(conn);
			Feeds[] feeds = feed.results.feeds;
			for (Feeds fd : feeds) {
				Location location = fd.l;
				FeedURL feedLink = fd.u;
				Latest latest = fd.latest;
				LocalDate verDate = Instant.ofEpochMilli(latest.ts).atZone(ZoneId.systemDefault()).toLocalDate();
				if(feedVersion.getLatestVersion().isBefore(verDate)){
					long feedId = location.id;
					String title = fd.t;
					String link = feedLink.d;
					downloadLinks.add(link);
					
					FeedVersion newVersion = NutAppInitializer.getContext().getBean("feedVersion", FeedVersion.class);
					newVersion.setId(feedVersion.getId());
					newVersion.setFeedId(feedId);
					newVersion.setTitle(title);
					newVersion.setLatestVersion(verDate);
					dbSrv.update(newVersion);
				}
			}
		}
		return downloadLinks;
	}
	
	private SwaggerFeed getTransitFeedJson(HttpURLConnection conn){
		StringBuilder sb = new StringBuilder();
		try(BufferedReader bfr = new BufferedReader(new InputStreamReader(conn.getInputStream()))){
			while(bfr.ready()){
				sb.append(bfr.readLine());
			}
		}catch(IOException e){
			LOGGER.error(e);
		}
		
		return Optional.ofNullable(gson.fromJson(sb.toString(), SwaggerFeed.class)).orElse(new SwaggerFeed());
	}
	
    private void downloadFile(String urlAddress, String archiveName) throws IOException{
        URL pack = new URL(urlAddress);//.replace("http", "https")
        LOGGER.info("Download file from: "+urlAddress);
        try(InputStream in = pack.openStream()){
            Files.copy(in, Paths.get(tempFolder+"/"+archiveName), StandardCopyOption.REPLACE_EXISTING);
        }catch(MalformedURLException m){
        	LOGGER.error(m);
        }
        LOGGER.info("Download finished!");
    }
	
}
