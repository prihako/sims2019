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

public class ImageService implements IEngineService{
	private HttpServletResponse response;
	private LinkFactory linkFactory;
	
	public void setResponse(HttpServletResponse response) {
		this.response = response;
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

	@Override
	public String getName() {
		return "image";
	}

	@Override
	public void service(IRequestCycle cycle) throws IOException {
		String imageId = (cycle.getParameter("imageId"));
		byte imageData[] = ImageDB.loadImage(imageId);
		response.setHeader("Content-disposition", "attachment; filename="+imageId);
		response.setContentType("application/pdf");
		
		try{
			OutputStream out = response.getOutputStream();
			out.write(imageData);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}