package com.pap.nuts.modules.services.data.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

/**
 * 
 * @author Pap Gergely
 *
 */
public class ZipExtractService {
	
	private static final Logger LOGGER = Logger.getLogger(ZipExtractService.class);
	
    public static void extractZipFile() throws IOException{
    	LOGGER.info("Starting extracting files from zip...");
        ZipInputStream zis = new ZipInputStream(new FileInputStream("E:/Development/temp_downloads/bkk_gtfs_new.zip"));
        ZipEntry zipEntry = zis.getNextEntry();
        byte[] buffer = new byte[1024];
        while(zipEntry != null){
            String fileName = zipEntry.getName();
            File bkkFile = new File("E:/Development/temp_downloads/"+fileName);
            try(FileOutputStream f = new FileOutputStream(bkkFile)){
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    f.write(buffer, 0, len);// Shity solution...
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
