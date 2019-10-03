package com.balicamp.webapp.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.balicamp.dao.parameter.SystemParameterDao;
import com.balicamp.webapp.common.DownloadSession;

public class DownloaderServlet extends HttpServlet{
	
	private SystemParameterDao paramDao;
	
	private String reportPath = null; 
	
	private DownloadSession session = null;



	@Override
	public void init() throws ServletException {
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		
		if(null == session){
			session = context.getBean(DownloadSession.class);
		}
		
		if(null == paramDao){			
			paramDao = context.getBean(SystemParameterDao.class);
		}
		
//		reportPath = paramDao.findParamValueByParamName("mayora.download.temporary.path");
//		if(null == reportPath){
//			throw new RuntimeException("PARAMETER UNTUK REPORT PATH DG KEY mayora.download.temporary.path BELUM DI SET");
//		}
		
//		File reportDir = new File(reportPath);
//		if(!reportDir.exists()){
//			if(!reportDir.mkdir()){
//				throw new ServletException("ADA KESALAHAN PADA PATH REPORT PATH "+reportPath);
//			}
//		}
		
//		File tmpReport = new File(reportDir+"/"+"test.txt");
//		try {
//			if(!tmpReport.createNewFile()){
//				throw new RuntimeException("Tidak bisa menulis pada directory "+reportPath);
//			}else{
//				tmpReport.delete();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		if (req.getParameter("usermanual") != null)
			downloadUserManual(req, resp);
		else{
			downloadReport(req,resp);
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (req.getParameter("usermanual") != null)
			downloadUserManual(req, resp);
		else{
			downloadReport(req,resp);
		}
	}

	private void downloadReport(HttpServletRequest req, HttpServletResponse resp) {
		String fileName = null;		
		String qrys = req.getQueryString();
		String[] arrQry = qrys.split(";");
		if(null != arrQry){
			for (int i = 0; i < arrQry.length; i++) {
				String qry = arrQry[i];
				if(qry.contains("fileName")){
					fileName = arrQry[i].substring((arrQry[i].indexOf("="))+1);
					break;
				}				
			}
		}
		
		
		if(null != fileName && !session.exist(fileName)){
			System.out.println("download : "+reportPath+"/"+fileName);
			File file = new File(reportPath+"/"+fileName);
			if(!file.exists()){
				session.fileNotExist(fileName);
				return;
			}
			session.progress(fileName);
			resp.setContentType("application/octet-stream");
		    resp.setHeader("Content-disposition","attachment;filename="+fileName);
		    boolean statusDownload = true;
			try {
 
				FileInputStream fis = new FileInputStream(file);
				OutputStream os = resp.getOutputStream();

				int bit = 256;
				int i = 0;

				while ((bit) >= 0) {
					bit = fis.read();
					os.write(bit);
				}
				os.flush();
				os.close();
				fis.close();				

			} catch (IOException e) {
				session.failed(fileName);
				statusDownload = false;
			}catch (Exception e) {
				statusDownload = false;
				session.failed(fileName);
			}
			
			if(statusDownload){
				session.success(fileName);
			}
			
		}
				
	}
	
	private void downloadUserManual(HttpServletRequest req, HttpServletResponse resp) {
		String path = paramDao.findParamValueByParamName("postel.usermanual");
		String fileName = path.substring(path.lastIndexOf("/") + 1);
		
		if(null != path && !session.exist(fileName)){
			System.out.println("download user manual : "+ path);
			File file = new File(path);
			if(!file.exists()){
				session.fileNotExist(path);
				return;
			}
			session.progress(fileName);
			resp.setContentType("application/octet-stream");
		    resp.setHeader("Content-disposition","attachment;filename="+fileName);
		    boolean statusDownload = true;
			try {
 
				FileInputStream fis = new FileInputStream(file);
				OutputStream os = resp.getOutputStream();

				int bit = 256;
				int i = 0;

				while ((bit) >= 0) {
					bit = fis.read();
					os.write(bit);
				}
				os.flush();
				os.close();
				fis.close();				

			} catch (IOException e) {
				session.failed(path);
				statusDownload = false;
			}catch (Exception e) {
				statusDownload = false;
				session.failed(path);
			}
			
			if(statusDownload){
				session.success(path);
			}
			
		}
				
	}
}
