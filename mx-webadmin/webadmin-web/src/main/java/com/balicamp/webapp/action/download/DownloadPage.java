package com.balicamp.webapp.action.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.webapp.action.BasePage;
import com.balicamp.webapp.common.DownloadSession;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class DownloadPage extends BasePage implements PageBeginRenderListener, PageEndRenderListener{
	
	private String successPage;
	private String errorPage;
	private String fileName;
	
	public abstract long getRefreshInterval();
	public abstract void setRefreshInterval(long refreshInterval);
	
	@InjectSpring("downloadSession")
	public abstract DownloadSession getDownloadSession();

	
	private boolean start = false;
	
	public void initPage(){
		start = false;
	}

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		setRefreshInterval(5);
		System.out.println("page begin render --> "+(new Date()).toString());
		System.out.println("file name : "+getFileName());
		
		if(start){
		  	if(null != getFileName() ){
				if(!getDownloadSession().exist(getFileName())){
					setRefreshInterval(5);
					download(getFileName());
				}/*else{
					nextEvent();
				}*/
			}
		}
	}		

	@Override
	public void pageEndRender(PageEvent event) {
		start = true;
		System.out.println("page end render");
	}
	

	private void nextEvent() {
		String status = getDownloadSession().status(getFileName());
		System.out.println("next Event --> status : "+status);
		
		if(DownloadSession.DOWNLOAD_SUCCESS.equals(status)){						
			System.out.println("redirect to success page : "+getSuccessPage());	
			
			getDownloadSession().unregister(getFileName());
			setFileName(null);
			
			ILink link  = getEngineService().getLink(false, getSuccessPage());
			getRequestCycle().sendRedirect(link.getAbsoluteURL());

		}else if(DownloadSession.DOWNLOAD_EXCEPTION.equals(status) ||				
				DownloadSession.DOWNLOAD_NOT_REGISTERED.equals(status) || 
				DownloadSession.DOWNLOAD_FILE_NOT_FOUND.equals(status)){
			
			System.out.println("redirect to error page : "+getErrorPage());
			
			getDownloadSession().unregister(getFileName());
			setFileName(null);
			
			ILink link  = getEngineService().getLink(false, getErrorPage());
			getRequestCycle().sendRedirect(link.getAbsoluteURL());
			
		}else if(DownloadSession.DOWNLOAD_SUCCESS.equals(status)){
			System.out.println("todo nothing");
		}
	}
	
	
	private void testDownload(String file){
		System.out.println("download : "+file);
		HttpServletResponse respon = getResponse();
		HttpServletRequest request = getRequest();
		
		try {
			String context = getBaseUrl();
			request.getRequestDispatcher("download?fileName="+file).forward(request, respon);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public String getSuccessPage() {
		return successPage;
	}
	public void setSuccessPage(String successPage) {
		this.successPage = successPage;
	}
	public String getErrorPage() {
		return errorPage;
	}
	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
			
}
