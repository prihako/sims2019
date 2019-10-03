package com.balicamp.webapp.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

public class DownloadSession implements InitializingBean{
 
	public static final String DOWNLOAD_IN_PROGRESS = "DOWNLOAD_IN_PROGRESS";	
	public static final String DOWNLOAD_NOT_REGISTERED = "DOWNLOAD_FILE_NOT_REGISTERED";
	public static final String DOWNLOAD_EXCEPTION = "DOWNLOAD_EXCEPTION";
	public static final String DOWNLOAD_SUCCESS = "DOWNLOAD_SUCCESS";
	public static final String DOWNLOAD_FILE_NOT_FOUND = "DOWNLOAD_FILE_NOT_FOUND";
	
	private Map<String, String> sessionMap;  
	
	private static final Log log = LogFactory.getLog(DownloadSession.class);

	
	@Override
	public void afterPropertiesSet() throws Exception {
		sessionMap = new HashMap<String, String>();
	}


	public boolean exist(String fileName) {
		return (sessionMap.get(fileName)== null?false:true);
		
	}

	public void progress(String fileName) {
		if(!exist(fileName)){
			sessionMap.put(fileName, DOWNLOAD_IN_PROGRESS);
		}
	}

	public void failed(String fileName) {
		setStatus(fileName, DOWNLOAD_EXCEPTION);
	}


	public void success(String fileName) {
		setStatus(fileName, DOWNLOAD_SUCCESS);
	}
	
	private void setStatus(String fileName, String status){
		if(exist(fileName)){
			sessionMap.remove(fileName);
			sessionMap.put(fileName, status);
		}else{
			sessionMap.put(fileName, DOWNLOAD_NOT_REGISTERED);
		}
	}


	public String status(String fileName) {		
		if(!exist(fileName)){
			return DOWNLOAD_NOT_REGISTERED;
		}
		return sessionMap.get(fileName);
	}


	public void unregister(String fileName) {
		if(exist(fileName)){
			sessionMap.remove(fileName);
		}
	}


	public void fileNotExist(String fileName) {
		setStatus(fileName, DOWNLOAD_FILE_NOT_FOUND);
	}



}
