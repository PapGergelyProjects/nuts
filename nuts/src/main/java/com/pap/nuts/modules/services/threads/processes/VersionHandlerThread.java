package com.pap.nuts.modules.services.threads.processes;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.pap.nuts.NutAppInitializer;
import com.pap.nuts.modules.interfaces.AbstractProcessService;
import com.pap.nuts.modules.interfaces.DaoService;
import com.pap.nuts.modules.model.beans.main.FeedVersion;
import com.pap.nuts.modules.model.json.SwaggerFeed.FeedURL;
import com.pap.nuts.modules.model.json.SwaggerFeed.Feeds;
import com.pap.nuts.modules.model.json.SwaggerFeed.Latest;
import com.pap.nuts.modules.model.json.SwaggerFeed.Location;
import com.pap.nuts.modules.services.data.utils.FeedUtils;
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
	private DaoService<FeedVersion> dbSrv;
	
	private String transitApiKey;
	private String tempFolder;
	private long initDelay;
	private long day;
	private long actFeedId;
	
	public VersionHandlerThread(){}

	public void setTransitApiKey(String transitApiKey) {
		this.transitApiKey = transitApiKey;
	}

	public void setTempFolder(String tempFolder) {
		this.tempFolder = tempFolder;
	}

	public long getInitDelay() {
		return initDelay;
	}

	public void setInitDelay(long initDelay) {
		this.initDelay = initDelay;
	}

	public long getDay() {
		return day;
	}

	public void setDay(long day) {
		this.day = day;
	}

	@Override
	protected void logic() throws Exception {
		dbSrv = NutAppInitializer.getContext().getBean(FeedVersionDao.class);
		Map<String, Long> links = checkSelectedFeed();
		for (Map.Entry<String, Long> pair : links.entrySet()) {
			String[] fileUrl = pair.getKey().split("/");
			downloadFile(pair.getKey(),fileUrl[fileUrl.length-1]);
			dbSrv.execute("UPDATE feed_version SET new_version=true WHERE feed_id="+pair.getValue());
		}
	}
	
	private Map<String, Long> checkSelectedFeed() throws IOException{
		Map<String, Long> downloadLinks = new HashMap<>();
		List<FeedVersion> feedVers = dbSrv.getAll();
		LOGGER.info("Check feed versions...");
		for (FeedVersion feedVersion : feedVers) {
			Feeds allFeed = FeedUtils.instance.getFeeds(transitApiKey, feedVersion.getFeedId());
			Location location = allFeed.l;
			FeedURL feedLink = allFeed.u;
			Latest latest = allFeed.latest;
			LocalDate verDate = Instant.ofEpochMilli(latest.ts*1000).atZone(ZoneId.systemDefault()).toLocalDate();
			if(feedVersion.getFeedId() == allFeed.l.id && (feedVersion.getLatestVersion().isBefore(verDate) || feedVersion.isRecent())){
				long feedId = location.id;
				String title = allFeed.t;
				String link = feedLink.d;
				downloadLinks.put(link, feedId);
				
				FeedVersion newVersion = NutAppInitializer.getContext().getBean("feedVersion", FeedVersion.class);
				newVersion.setId(feedVersion.getId());
				newVersion.setFeedId(feedId);
				newVersion.setTitle(title);
				newVersion.setLatestVersion(verDate);
				newVersion.setRecent(false);
				dbSrv.update(newVersion);
				LOGGER.info("New version founded.");
			}
		}
		return downloadLinks;
	}
	
    private void downloadFile(String urlAddress, String archiveName) throws IOException{
    	HttpURLConnection conn = establishConnection(urlAddress);
        LOGGER.info("Download file from: "+urlAddress);
        try(InputStream in = conn.getInputStream()){
        	String fileName = getFileName(conn);
        	String uri = tempFolder+"/"+(fileName.isEmpty() ? archiveName : fileName);
            Files.copy(in, Paths.get(uri+".tmp"), StandardCopyOption.REPLACE_EXISTING);
            renameFile(uri+".tmp", uri);
        }catch(MalformedURLException m){
        	LOGGER.error(m);
        }
        LOGGER.info("Download finished!");
    }
    
    private HttpURLConnection establishConnection(String urlAddress) throws IOException{
    	URL pack = null;
    	HttpURLConnection conn = null;
    	try{
    		pack = new URL(urlAddress.replace("http", "https"));
    		conn = (HttpsURLConnection)pack.openConnection();
    		conn.getResponseCode();
    	}catch(ConnectException | MalformedURLException e){
    		try{
    			LOGGER.warn("HTTPS connection cannot be established!");
    			LOGGER.warn("Try to establish HTTP connection...");
    			pack = new URL(urlAddress);
    			conn = (HttpURLConnection)pack.openConnection();
    			conn.getResponseCode();
    			LOGGER.warn("HTTP connection established.");
    		}catch(ConnectException | MalformedURLException ex){
    			LOGGER.error("Cannot establish any kind of connection: "+ex);
    		}
    	}
    	
    	return conn;
    }
    
    private String getFileName(HttpURLConnection conn){
    	List<String> contents = conn.getHeaderFields().get("Content-Disposition");
    	if(contents != null && !contents.isEmpty()){
    		String dispos = contents.get(0);
    		Matcher match = Pattern.compile("(?<filename>filename\\=\\\".*\\\")").matcher(dispos);
    		while(match.find()){
    			String[] fileWithExtension = match.group("filename").split("\\=");
    			return fileWithExtension[1].replaceAll("\"", "");
    		}
    		
    	}
    	return "";
    }
    
    private void renameFile(String path, String newName) throws IOException{
    	Path source = Paths.get(path);
    	Files.move(source, source.resolveSibling(newName), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
    }
	
}
