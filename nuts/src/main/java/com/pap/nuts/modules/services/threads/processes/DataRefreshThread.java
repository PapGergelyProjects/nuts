package com.pap.nuts.modules.services.threads.processes;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.pap.nuts.modules.interfaces.AbstractProcessService;

public class DataRefreshThread extends AbstractProcessService {
	
	private final Logger LOGGER = Logger.getLogger(DataRefreshThread.class);
	
	@Override
	protected void logic() throws Exception {
        Document doc = Jsoup.connect("http://bkk.hu/apps/gtfs/").get();
        Element caption = doc.getElementsByTag("caption").first();
        Element link = caption.getElementsByTag("a").first();
        String href = link.attr("abs:href");
        downloadFile(href);
	}
	
    private void downloadFile(String urlAddress) throws IOException{
        URL pack = new URL(urlAddress);
        LOGGER.info("Download file from: "+urlAddress);
        try(InputStream in = pack.openStream()){
            Files.copy(in, Paths.get("C:/temp_downloads/bkk_gtfs_new.zip"), StandardCopyOption.REPLACE_EXISTING);
        }catch(MalformedURLException m){
        	LOGGER.error(m);
        }
        LOGGER.info("Download finished!");
    }
	
}
