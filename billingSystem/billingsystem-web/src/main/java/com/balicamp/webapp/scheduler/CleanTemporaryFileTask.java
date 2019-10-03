package com.balicamp.webapp.scheduler;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.balicamp.dao.parameter.SystemParameterDao;

public class CleanTemporaryFileTask {
	
	private final Log log = LogFactory.getLog(CleanTemporaryFileTask.class);
	
	@Autowired
	private SystemParameterDao paramDao;
	
	private String filePath;
		
	public void runCleaner(){
		log.info("@@@@@@@ Cleaning temporary file task Run...... ");
		if(null == filePath){
			filePath = paramDao.findParamValueByParamName("mayora.download.temporary.path");
		}
		
		File file = new File(filePath);
		if(!file.exists()){
			log.warn("@@@@@@@ Temporari File Tidak ditemukan..... ");
			return;
		}
		
		if(file.isFile()){
			log.info("@@@@@@@ Delete file  "+filePath);
			file.delete();
			return;
		}
		
		if(file.isDirectory()){
			File[] lstFile = file.listFiles();
			log.info("@@@@@@@ Delete all file in directory "+filePath);
			for (File f : lstFile) {
				f.delete();
			}
		}
		
	}

}
