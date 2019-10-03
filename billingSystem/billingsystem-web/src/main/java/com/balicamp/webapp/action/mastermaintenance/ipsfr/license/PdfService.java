package com.balicamp.webapp.action.mastermaintenance.ipsfr.license;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebResponse;

public class PdfService implements IEngineService{
	private LinkFactory linkFactory;
	private static byte[] pdf;
	
	public static byte[] getPdf() {
		return pdf;
	}

	public static void setPdf(byte[] pdf) {
		PdfService.pdf = pdf;
	}
	
	public void setLinkFactory(LinkFactory linkFactory) {
		this.linkFactory = linkFactory;
	}

	@Override
	public ILink getLink(boolean post, Object parameter) {
		String imageId = (String)((Object[])parameter)[0];
		Map parameters = new HashMap();
		parameters.put("imageId", imageId);
		return linkFactory.constructLink(this, post, parameters, false);
	}

//	This is used for service name in URL
	@Override
	public String getName() {
		
		return "pdf";
	}

	@Override
	public void service(IRequestCycle cycle) throws IOException {
		String imageId = (cycle.getParameter("imageId"));
		byte imageData[] = pdf;
		
		//If you want client to download directly use HttpServletResponse, otherwise use WebResponse
		WebResponse response = cycle.getInfrastructure().getResponse();
		
		try{
			OutputStream out = response.getOutputStream(new ContentType("application/pdf"));
			out.write(imageData);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}