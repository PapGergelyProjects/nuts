package com.pap.nuts.modules.services.threads;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.pap.nuts.modules.interfaces.AbstractProcessService;
import com.pap.nuts.modules.interfaces.AbstractThreadService;

/**
 * 
 * @author Pap Gergely
 *
 */
public class ThVersionMonitoring extends AbstractProcessService{

	@Override
	protected void logic() throws Exception {
        Document doc = Jsoup.connect("http://bkk.hu/apps/gtfs/").get();
//        System.out.println(doc.title());
//        Element caption = doc.getElementsByTag("caption").first();
//        Element link = caption.getElementsByTag("a").first();
//        String href = link.attr("abs:href");
//        System.out.println(href);
		
        List<List<String>> mainList = new ArrayList<>();
        Elements trElement = doc.getElementsByTag("tr");
        trElement.stream().forEach(e -> {
            List<String> resList = e.getElementsByTag("td")
                                    .stream()
                                    .filter(pre -> {
                                        try{
                                            Long.parseLong(pre.text());
                                        }catch(NumberFormatException ex){
                                            return true;
                                        }
                                        return false;
                                    }).map(map -> map.text()).collect(Collectors.toList());
                                    mainList.add(resList);
        });
        Map<String, LocalDateTime> fileList = mainList.stream().filter(pre -> !pre.isEmpty()).collect(Collectors.toMap(k -> k.get(0), v -> LocalDateTime.parse(v.get(1), DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm:ss"))));
        fileList.forEach((k,v) -> {
            System.out.println("Fálj neve: "+k+", Időpont: "+v);
        });
		
	}

}
