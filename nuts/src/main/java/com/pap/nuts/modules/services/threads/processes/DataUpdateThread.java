package com.pap.nuts.modules.services.threads.processes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

import com.pap.nuts.NutAppInitializer;
import com.pap.nuts.modules.interfaces.AbstractProcessService;
import com.pap.nuts.modules.interfaces.DaoService;
import com.pap.nuts.modules.model.beans.main.FeedVersion;
import com.pap.nuts.modules.services.data.utils.TableInsertValues;
import com.pap.nuts.modules.services.threads.utils.DataProcess;
import com.pap.nuts.modules.session.services.DataSourceDao;
import com.pap.nuts.modules.session.services.FeedVersionDao;

/**
 * This class handle the file extractor and refiner service
 *   
 * @author Pap Gergely
 *
 */
public class DataUpdateThread extends AbstractProcessService {

	private String tempFolder;
	private DataSourceDao<String> srvc;
	private DaoService<FeedVersion> feedVersDao;
	private List<String> tables;
	private List<Long> feedIds;
	
	private static final Logger LOGGER = Logger.getLogger(DataUpdateThread.class);
	
	public DataUpdateThread(){
	}
	
	@Override
	protected void logic() throws Exception {
		feedVersDao = NutAppInitializer.getContext().getBean(FeedVersionDao.class);
		LOGGER.info("Check updated versions...");
		List<FeedVersion> version = feedVersDao.getAll();
		if(version.stream().anyMatch(FeedVersion::isNewVersion)){// TODO: Temp sol, this now search to only the first occurrence, but later it must find the precise feed which is need to be update
			feedIds = version.stream().filter(pre -> pre.isNewVersion()).map(mp -> mp.getFeedId()).collect(Collectors.toList());
			checkFolderForContent();
		}
		if(feedIds != null){
			feedVersDao.execute("UPDATE feed_version SET new_version=false WHERE feed_id IN "+feedIds.stream().map(mp -> mp.toString()).collect(Collectors.joining(",", "(", ")")));
		}else{
			LOGGER.info("There is no new update!");
		}
	}
	
    private void checkFolderForContent() throws IOException{
    	srvc = NutAppInitializer.getContext().getBean(DataSourceDao.class);
    	File sourceFolder = new File(tempFolder);
        File[] fileList = sourceFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".zip") || name.toLowerCase().endsWith(".rar"));
        srvc.execute("SELECT clear_tables();");
        LOGGER.info("Tables cleared.");
        for(File file : fileList){
        	extractZipFile(file.getPath());
        	createInsertFromFile();
        	Files.delete(Paths.get(file.getPath()));
        }
    }
	
    private void extractZipFile(String zipPath) throws IOException{
    	LOGGER.info("Starting extracting files from zip...");
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath));
        ZipEntry zipEntry = zis.getNextEntry();
        byte[] buffer = new byte[1024];
        while(zipEntry != null){
            String fileName = zipEntry.getName();
            if(tables.contains(fileName.replace(".txt", ""))){
            	File feedFile = new File(tempFolder+fileName);
            	try(FileOutputStream f = new FileOutputStream(feedFile)){
            		int len;
            		while ((len = zis.read(buffer)) > 0) {
            			f.write(buffer, 0, len);
            		}
            	}catch(IOException e){
            		LOGGER.error("File extraction faild: "+e);
            	}
            }
            zipEntry = zis.getNextEntry();
        }
        LOGGER.info("File extraction done!");
        zis.closeEntry();
        zis.close();
    }
	
	private void createInsertFromFile() throws IOException{
        File[] listOfFiles = new File(tempFolder).listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        DataProcess.getFinishedTaskList().clear();
        Map<String, TableInsertValues> tableList = Arrays.asList(TableInsertValues.values()).stream().collect(Collectors.toMap(TableInsertValues::getTableName, v -> v));
        for (File file : listOfFiles) {
            List<String> fileContent = Files.readAllLines(Paths.get(tempFolder+file.getName()), Charset.forName("utf-8"));
            String tableName = file.getName().replace(".txt", "");
            LOGGER.info("Table next: "+tableName);
            TableInsertValues value = tableList.get(tableName);
            List<String> columns = value.getColNames(new ArrayList<>(Arrays.asList(fileContent.remove(0).split(","))));
            String joinedCols = columns.stream().collect(Collectors.joining(",","(",")"));
            while(0<fileContent.size()){
            	StringBuilder insert = new StringBuilder();
            	insert.append("INSERT INTO ").append(tableName).append(" ");
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
            		for (int j = 0; j < columns.size(); j++) {
            			String row = rows[j];
            			content.put(columns.get(j), row.isEmpty() ? "null" : row);
            		}
            		joinValues.add(value.getInsertValue(columns, content));
            		cnt--;
            		content = null;
            	}
            	insert.append(joinValues);
            	srvc.insert(insert.toString());
            }
            LOGGER.info(tableName+" insert done.");
            DataProcess.addToList(tableName+" insert done.");
            Files.delete(Paths.get(tempFolder+file.getName()));
        }
        srvc.execute("REFRESH MATERIALIZED VIEW static_stops;");
        LOGGER.info("static_stops view refreshed.");
        DataProcess.addToList("static_stops view refreshed.");
        srvc.execute("REFRESH MATERIALIZED VIEW static_stops_with_times;");
        LOGGER.info("static_stops_with_times view refreshed.");
        DataProcess.addToList("static_stops_with_times view refreshed.");
        DataProcess.addToList("Done!");
        DataProcess.getFinishedTaskList().clear();
	}
	
    private String replaceUselessComma(String rawStr){// Some GTFS file contains comma as an inner string separator, the problem with this, the comma is also the delimiter of the csv file...
        Matcher match = Pattern.compile("\"(?<line>[^\"]*)\"").matcher(rawStr);
        while(match.find()){
            String mtch = match.group("line");
            rawStr = rawStr.replace(mtch, mtch.replaceAll(",", ";"));
        }
        
        return rawStr;
    }
	
	public void setTempFolder(String tempFolder) {
		this.tempFolder = tempFolder;
	}

	public void setTables(String tables) {
		this.tables = Arrays.asList(tables.split(";")).stream().map(mp -> mp.trim()).collect(Collectors.toList());
	}
	
	
}
