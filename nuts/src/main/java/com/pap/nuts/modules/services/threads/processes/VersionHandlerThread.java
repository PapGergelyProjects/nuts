package com.pap.nuts.modules.services.threads.processes;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.env.Environment;

import com.pap.nuts.modules.interfaces.AbstractProcessService;
import com.pap.nuts.modules.services.threads.ThreadServiceHandler;
import com.pap.nuts.modules.services.threads.utils.ThreadCache;

/**
 * 
 * @author Pap Gergely
 *
 */
public class VersionHandlerThread extends AbstractProcessService{
	
	private final Logger LOGGER = Logger.getLogger(VersionHandlerThread.class);
	private Environment env;
	private Predicate<Element> longVal = elem -> {
        try{
            Long.parseLong(elem.text());
        }catch(NumberFormatException ex){
            return true;
        }
        return false;
	};
	
	public VersionHandlerThread(){}
	
	public VersionHandlerThread(Environment env){
		this.env = env;
	}
	
	@Override
	protected void logic() throws Exception {
        Document doc = Jsoup.connect(env.getProperty("data_source_url")).get();
        List<List<String>> mainList = new ArrayList<>();
        Elements trElement = doc.getElementsByTag("tr");
        trElement.stream().forEach(e -> {
            List<String> resList = e.getElementsByTag("td")
                                    .stream()
                                    .filter(longVal)
                                    .map(map -> map.text())
                                    .collect(Collectors.toList());
            mainList.add(resList);
        });
        Map<String, LocalDateTime> fileList = mainList.stream().filter(pre -> !pre.isEmpty()).collect(Collectors.toMap(k -> k.get(0), v -> LocalDateTime.parse(v.get(1), DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm:ss"))));
        if(ThreadCache.getVersionCache().isEmpty()){
        	startDownload(doc);
        	ThreadCache.getVersionCache().putAll(fileList);
        	ThreadServiceHandler.FIXED.process(new DataUpdateThread());
        }else{
        	for(Map.Entry<String, LocalDateTime> pair : fileList.entrySet()){
        		String k = pair.getKey();
        		if(ThreadCache.getVersionCache().get(k).isEqual(pair.getValue())){
        			LOGGER.info(k+" is up to date.");
        		}else{
        			LOGGER.info(k+" is out of date.");
        			startDownload(doc);
        			ThreadCache.getVersionCache().putAll(fileList);
        			break;
        		}
        	}
        }
	}
	
	private void startDownload(Document doc) throws IOException{
        Element caption = doc.getElementsByTag("caption").first();
        Element link = caption.getElementsByTag("a").first();
        String href = link.attr("abs:href");
        downloadFile(href);
	}
	
    private void downloadFile(String urlAddress) throws IOException{
        URL pack = new URL(urlAddress.replace("http", "https"));
        LOGGER.info("Download file from: "+urlAddress);
        try(InputStream in = pack.openStream()){
            Files.copy(in, Paths.get(env.getProperty("temp_directory")+"/bkk_gtfs_new.zip"), StandardCopyOption.REPLACE_EXISTING);
        }catch(MalformedURLException m){
        	LOGGER.error(m);
        }
        LOGGER.info("Download finished!");
    }

}
