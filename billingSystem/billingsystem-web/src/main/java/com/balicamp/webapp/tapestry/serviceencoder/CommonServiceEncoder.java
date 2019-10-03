package com.balicamp.webapp.tapestry.serviceencoder;

import java.util.Date;

import org.apache.tapestry.engine.ServiceEncoder;
import org.apache.tapestry.engine.ServiceEncoding;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * URL encoder for reconcile service
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class CommonServiceEncoder implements ServiceEncoder {
	// private static DateFormat concatDateFormat = new
	// SimpleDateFormat("yyyyMMddHHmmss");
	private static DateTimeFormatter transactionDateFormat = DateTimeFormat.forPattern("yyyyMMddHHmmss");

	public void encode(ServiceEncoding encoding) {
		String service = encoding.getParameterValue("service");

		if (service.equals("report")) {
			String name = encoding.getParameterValue("name");
			String fileType = encoding.getParameterValue("fileType");

			StringBuffer downloadFileName = new StringBuffer(name.replaceAll("\\s+", "_"));
			downloadFileName.append('.');
			downloadFileName.append(fileType);

			encoding.setServletPath("/report/" + downloadFileName.toString());
			encoding.setParameterValue("service", null);
			encoding.setParameterValue("name", null);
			encoding.setParameterValue("fileType", null);
			return;
		}

		if (!service.equals("common"))
			return;

		String fileName = encoding.getParameterValue("fileName");
		if (fileName == null)
			fileName = transactionDateFormat.print(new Date().getTime());

		String fileType = encoding.getParameterValue("fileType");

		StringBuffer downloadFileName = new StringBuffer(fileName.replaceAll("\\s+", "_"));
		downloadFileName.append('.');
		if (fileType != null)
			downloadFileName.append(fileType);

		encoding.setServletPath(encoding.getParameterValue("folderpath") + downloadFileName.toString());
		encoding.setParameterValue("service", null);
	}

	public void decode(ServiceEncoding encoding) {
		String path = encoding.getServletPath();
		String pathInfo = encoding.getPathInfo();

		if (path.equals("/report")) {
			pathInfo = pathInfo.replaceAll("/", "");
			String pathInfoSplitted[] = pathInfo.split("[.]");
			String name = pathInfoSplitted[0];
			String fileType = pathInfoSplitted[1];

			encoding.setServletPath("/app");
			encoding.setParameterValue("service", "report");
			encoding.setParameterValue("name", name);
			encoding.setParameterValue("fileType", fileType);
			return;
		}

		if (!path.equals("/commonservice"))
			return;

		encoding.setServletPath("/app");
		encoding.setParameterValue("service", "commonservice");
	}

}