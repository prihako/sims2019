package com.balicamp.webapp.tapestry.service;

import static com.balicamp.Constants.SystemParameter.Configuration.CONTEXT_PATH_REAL_DIR;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.LinkFactory;

import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.service.report.ReportManager;

public class ReportService implements IEngineService {

	private HttpServletRequest request;

	private HttpServletResponse response;

	private LinkFactory linkFactory;

	private ReportManager reportManager;

	private SystemParameterManager systemParameterManager;

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setLinkFactory(LinkFactory linkFactory) {
		this.linkFactory = linkFactory;
	}

	public void setReportManager(ReportManager reportManager) {
		this.reportManager = reportManager;
	}

	public void setSystemParameterManager(SystemParameterManager systemParameterManager) {
		this.systemParameterManager = systemParameterManager;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return "report";
	}

	/**
	 * {@inheritDoc}
	 */
	public void service(IRequestCycle cycle) throws IOException {
		HttpSession session = request.getSession();

		// read require parameter
		String name = cycle.getParameter("name");
		String fileType = cycle.getParameter("fileType");

		// prepare parameters
		Map<String, String> parameters = new HashMap<String, String>();
		Enumeration<String> parameterNames = request.getParameterNames();

		while (parameterNames.hasMoreElements()) {
			String parameterName = parameterNames.nextElement();
			parameters.put(parameterName, request.getParameter(parameterName));
		}

		if (fileType.equals("pdf")) {
			response.setContentType("application/pdf; charset=UTF-8");
			reportManager.generatePdfTmpFile(name, parameters, session, response.getOutputStream());
		} else if (fileType.equals("rtf")) {
			response.setContentType("application/rtf; charset=UTF-8");
			reportManager.generateRtf(name, parameters, session, response.getOutputStream());
		} else if (fileType.equals("xls")) {
			response.setContentType("application/xls; charset=UTF-8");
			reportManager.generateXlsTmpFile(name, parameters, session, response.getOutputStream());
		} else if (fileType.equals("csv")) {
			response.setContentType("application/csv; charset=UTF-8");
			reportManager.generateCsvTmpFile(name, parameters, session, response.getOutputStream());
		} else if (fileType.equals("txt")) {
			response.setContentType("application/txt; charset=UTF-8");
			reportManager.generateTxtTmpFile(name, parameters, session, response.getOutputStream());
		} else if (fileType.equals("odt")) {
			response.setContentType("application/vnd.oasis.opendocument.text; charset=UTF-8");
			reportManager.generateOdt(name, parameters, session, response.getOutputStream());
		} else if (fileType.equals("skn")) {
			response.setContentType("application/skn; charset=UTF-8");
			reportManager.generateSknTmpFile(name, parameters, session, response.getOutputStream());
		} else if (fileType.equals("rtgs")) {
			response.setContentType("application/rtgs; charset=UTF-8");
			reportManager.generateRtgsTmpFile(name, parameters, session, response.getOutputStream());
		} else if ("html".equalsIgnoreCase(fileType)) {
			response.setContentType("text/html; charset=UTF-8");
			reportManager.generateHtmlTmpFile(name, parameters, getDirPrintResource(), request.getContextPath()
					+ "/report-images?image=", request.getSession(), response.getOutputStream());
		} else {
			response.sendError(404);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public ILink getLink(boolean post, Object parameter) {
		Object[] parameterList = (Object[]) parameter;
		String name = (String) (parameterList)[0];
		String fileType = (String) (parameterList)[1];

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("name", name);
		parameters.put("fileType", fileType);

		for (int i = 2; i < parameterList.length; i++) {
			String tempSplit[] = ((String) parameterList[i]).split("=", 2);
			if (tempSplit.length == 2)
				parameters.put(tempSplit[0], tempSplit[1]);
		}

		return linkFactory.constructLink(this, post, parameters, false);
	}

	public String getDirPrintResource() {
		return getDirPrintBase() + "/resource/";
	}

	protected String getDirPrintBase() {
		String basePath = systemParameterManager.getStringConfiguration(CONTEXT_PATH_REAL_DIR, "");
		return basePath + File.separator + "WEB-INF" + File.separator + "report";
	}
}
