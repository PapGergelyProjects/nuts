package com.pap.nuts.modules.services.data.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

/**
 * This class is responsible for the gtfs zip archive extraction.
 * 
 * @author Pap Gergely
 *
 */
public class ZipExtractService {
	
	private static final Logger LOGGER = Logger.getLogger(ZipExtractService.class);
	
	@Value("${temp_directory}")
	private String tempFolder;
	
	public ZipExtractService(){}
	
	
    public void extractZipFile() throws IOException{
    	LOGGER.info("Starting extracting files from zip...");
        ZipInputStream zis = new ZipInputStream(new FileInputStream(tempFolder+"/bkk_gtfs_new.zip"));
        ZipEntry zipEntry = zis.getNextEntry();
        byte[] buffer = new byte[1024];
        while(zipEntry != null){
            String fileName = zipEntry.getName();
            File bkkFile = new File(tempFolder+fileName);
            try(FileOutputStream f = new FileOutputStream(bkkFile)){
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    f.write(buffer, 0, len);
                }
            }catch(IOException e){
            	LOGGER.error("File extraction faild: "+e);
            }
            zipEntry = zis.getNextEntry();
        }
        LOGGER.info("File extraction done!");
        zis.closeEntry();
        zis.close();
    }
}
