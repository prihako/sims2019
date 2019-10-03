package id.co.sigma.mx.project.ftpreconcile.process;

import id.co.sigma.mx.util.PropertiesUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class FileHelper {
	Logger logger = Logger.getLogger("logger");
	private Properties fileProperties = null;
	private String webdavDir = null;

	private String localDir = null;
	
	public FileHelper(String settingProperties){
		fileProperties = PropertiesUtil.fromFile(settingProperties);
		webdavDir = fileProperties.getProperty("file.mt940.path.location");
		localDir =  fileProperties.getProperty("file.local.mt940.path.location");
	}
	
	public boolean isEnableToAccess() {
		boolean result = true;
		try {
			String testFile = webdavDir + "/" + "test.txt";
			File dir = new File(webdavDir);
			File file = new File(testFile);
			if(!dir.exists()){
				dir.mkdir();
			}
			if(file.createNewFile()){
				file.delete();
			}else{
				logger.info("Directory : " + webdavDir + " cannont be accessed");
			}
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
			logger.fatal(e);
		}
		
		return result;
	}
	
	public void retrieve(String source, String destination){
		File src = new File(source);
		File dst = new File(destination);
		
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
		    inputChannel = new FileInputStream(src).getChannel();
		    outputChannel = new FileOutputStream(dst).getChannel();
		    outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		    
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		    try {
				inputChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    try {
				outputChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    
		    if(src.delete()){
		    	logger.info("file : " + src + " successfully download and delete from webdav folder");;
		    }else{
		    	logger.info("file : " + src + " successfully download and but cannot be deleted from webdav folder");;
		    }
		}
	}
	
	public Object[] listFileNames(String filePath) {
		File folder = new File(filePath);
		List<String> listFiles =  new ArrayList<String>();
		
	    for (File fileEntry : folder.listFiles()) {
		    listFiles.add(fileEntry.getName());
	    }
	    
	    if(listFiles.size() == 0){
	    	return null;
	    }
	    
		return listFiles.toArray();
	}
	
	public String getWebdavDir() {
		return webdavDir;
	}

	public String getLocalDir() {
		return localDir;
	}
}
