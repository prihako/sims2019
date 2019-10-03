package com.balicamp.webapp.action.download;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.webapp.action.AdminBasePage;

public abstract class DownloadPageUserManual extends AdminBasePage implements PageBeginRenderListener {
	
	@InjectObject("spring:systemParameterManager")
	public abstract SystemParameterManager getSystemParameterManager();
	
	public void doDownload(IRequestCycle cycle) throws IOException{
		
		String path =  getSystemParameterManager().findParamValueByParamName("postel.usermanual");
		File downloadFile = new File(path);
		
		if(downloadFile.exists()){
		
			String context = getBaseUrl();
			cycle.sendRedirect(context + "/download?usermanual=usermanual");
			
		}else{
			String errorMessage = getText("leftMenu.masterMaintenance.download.userManual.fileNotFound");
			addError(getDelegate(), "errorShadow", errorMessage, ValidationConstraint.CONSISTENCY);
			return;
		}
	}
	
}
