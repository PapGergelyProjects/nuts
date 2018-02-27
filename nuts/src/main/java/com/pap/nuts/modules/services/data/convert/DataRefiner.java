package com.pap.nuts.modules.services.data.convert;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.pap.nuts.modules.services.data.utils.TableInsertValues;
import com.pap.nuts.modules.session.service.DataSourceService;
import com.pap.nuts.utils.NutAppInitializer;

public class DataRefiner{
	
	private final Logger LOGGER = Logger.getLogger(DataRefiner.class);
	
	public void createInsertFromFile() throws IOException{
        File[] listOfFiles = new File("E:/Development/temp_downloads/").listFiles();
        Map<String, TableInsertValues> tableList = Arrays.asList(TableInsertValues.values()).stream().collect(Collectors.toMap(k -> k.getTableName(), v -> v));
        for (File file : listOfFiles) {
            if(file.isFile() && file.getName().contains(".txt") && file.getName().equals("agency.txt")){
                List<String> fileContent = Files.readAllLines(Paths.get("E:/Development/temp_downloads/"+file.getName()), Charset.forName("utf-8"));
                String tabelName = file.getName().replace(".txt", "");
                String insert = "INSERT INTO bkk."+tabelName+" ";
                String[] columns = fileContent.remove(0).split(",");
                String joinedCols = Arrays.asList(columns).stream().collect(Collectors.joining(",","(",")"));
                insert+=joinedCols+" VALUES ";
                StringJoiner joinValues = new StringJoiner(",");
                TableInsertValues value = tableList.get(tabelName);
                for (int i = 0; i < fileContent.size(); i++) {
                    String raw = fileContent.remove(i);
                    raw = replaceUselessComma(raw);
                    String[] rows = raw.endsWith(",") ? (raw+"null").replace("\"", "").split(",") : raw.replace("\"", "").split(",");//
                    Map<String, String> content = new HashMap<>();
                    for (int j = 0; j < columns.length; j++) {
                        String row = rows[j];
                        content.put(columns[j], row.isEmpty() ? "null" : row);
                    }
                    joinValues.add(value.getInsertValue(content));
                }
                insert+=joinValues.toString();
                DataSourceService srvc = NutAppInitializer.getService(DataSourceService.class);
                srvc.insert(insert);
                LOGGER.info(tabelName+" insert done.");
            }
        }
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
