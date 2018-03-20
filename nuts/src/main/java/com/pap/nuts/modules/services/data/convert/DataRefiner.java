package com.pap.nuts.modules.services.data.convert;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.core.env.Environment;

import com.pap.nuts.NutAppInitializer;
import com.pap.nuts.modules.services.data.utils.TableInsertValues;
import com.pap.nuts.modules.services.threads.utils.DataProcess;
import com.pap.nuts.modules.session.service.DataSourceService;

/**
 * This class is responsible for the gtfs data management.
 * When the files are ready, this process will read up one by one,
 * and create an insert script from it.
 * 
 * @author Pap Gergely
 *
 */
public class DataRefiner{
	
	private Environment env;
	private final Logger LOGGER = Logger.getLogger(DataRefiner.class);
	
	public DataRefiner(){}
	public DataRefiner(Environment env){
		this.env = env;
	}
	
	public void createInsertFromFile() throws IOException{
		String tempDirectory = env.getProperty("temp_directory");
        File[] listOfFiles = new File(tempDirectory).listFiles();
        DataSourceService srvc = NutAppInitializer.getContext().getBean(DataSourceService.class);
        srvc.execute("SELECT clear_tables();");
        DataProcess.getFinishedTaskList().clear();
        LOGGER.info("Tables cleared.");
        Map<String, TableInsertValues> tableList = Arrays.asList(TableInsertValues.values()).stream().collect(Collectors.toMap(k -> k.getTableName(), v -> v));
        for (File file : listOfFiles) {
            if(file.isFile() && file.getName().contains(".txt")){
                List<String> fileContent = Files.readAllLines(Paths.get(tempDirectory+file.getName()), Charset.forName("utf-8"));
                String tabelName = file.getName().replace(".txt", "");
                String[] columns = fileContent.remove(0).split(",");
                String joinedCols = Arrays.asList(columns).stream().collect(Collectors.joining(",","(",")"));
                TableInsertValues value = tableList.get(tabelName);
                while(0<fileContent.size()){
                	StringBuilder insert = new StringBuilder();
                	insert.append("INSERT INTO ").append(tabelName).append(" ");
                	insert.append(joinedCols).append(" VALUES ");
                	StringJoiner joinValues = new StringJoiner(",");
                	int cnt = 10000; // Optimal for inserts
                	while(0<cnt){
                		if(fileContent.size()==0){
                			break;
                		}
                		String raw = replaceUselessComma(fileContent.remove(0));
                		String[] rows = raw.endsWith(",") ? (raw+"null").replace("\"", "").split(",") : raw.replace("\"", "").split(",");//
                		Map<String, String> content = new HashMap<>();
                		for (int j = 0; j < columns.length; j++) {
                			String row = rows[j];
                			content.put(columns[j], row.isEmpty() ? "null" : row);
                		}
                		joinValues.add(value.getInsertValue(content));
                		cnt--;
                	}
                	insert.append(joinValues);
                	srvc.insert(insert.toString());
                }
                LOGGER.info(tabelName+" insert done.");
                DataProcess.addToList(tabelName+" insert done.");
                Files.delete(Paths.get(tempDirectory+file.getName()));
            }
        }
        Files.delete(Paths.get(tempDirectory+"/bkk_gtfs_new.zip"));
        srvc.execute("REFRESH MATERIALIZED VIEW static_stops;");
        LOGGER.info("static_stops view refreshed.");
        DataProcess.addToList("static_stops view refreshed.");
        srvc.execute("REFRESH MATERIALIZED VIEW static_stops_with_times;");
        LOGGER.info("static_stops_with_times view refreshed.");
        DataProcess.addToList("static_stops_with_times view refreshed.");
        DataProcess.addToList("Done!");
        DataProcess.getFinishedTaskList().clear();
	}
	
    private String replaceUselessComma(String rawStr){
        Matcher match = Pattern.compile("(\\\".*\\\")").matcher(rawStr);
        if(match.find()){
            String mtch = match.group(1);
            rawStr = rawStr.replace(mtch, mtch.replace(",", ";"));
        }
        
        return rawStr;
    }
	
}
