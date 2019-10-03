package com.balicamp.webapp.tapestry.serviceencoder;

import org.apache.tapestry.engine.ServiceEncoder;
import org.apache.tapestry.engine.ServiceEncoding;

public class ReportServiceEncoder implements ServiceEncoder {

	public void encode(ServiceEncoding encoding) {
		String service = encoding.getParameterValue("service");
		if (!service.equals("report")) {
			return;
		}
		String name = encoding.getParameterValue("name");
		String fileType = encoding.getParameterValue("fileType");

		StringBuffer downloadFileName = new StringBuffer(name.replaceAll("\\s+", "_"));
		downloadFileName.append('.');
		downloadFileName.append(fileType);

		encoding.setServletPath("/report/" + downloadFileName.toString());
		encoding.setParameterValue("service", null);
		encoding.setParameterValue("name", null);
		encoding.setParameterValue("fileType", null);
	}

	public void decode(ServiceEncoding encoding) {
		String path = encoding.getServletPath();
		String pathInfo = encoding.getPathInfo();
		if (!path.equals("/report")) {
			return;
		}

		pathInfo = pathInfo.replaceAll("/", "");
		String pathInfoSplitted[] = pathInfo.split("[.]");
		String name = pathInfoSplitted[0];
		String fileType = pathInfoSplitted[1];

		encoding.setServletPath("/app");
		encoding.setParameterValue("service", "report");
		encoding.setParameterValue("name", name);
		encoding.setParameterValue("fileType", fileType);
	}
}
