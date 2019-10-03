package com.balicamp.webapp.tapestry.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.LinkFactory;
import org.springframework.web.bind.ServletRequestUtils;

import com.balicamp.webapp.util.RequestUtil;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class CommonService implements IEngineService {

	private HttpServletRequest request;
	private HttpServletResponse response;

	private LinkFactory linkFactory;

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return "common";
	}

	/**
	 * {@inheritDoc}
	 */
	public void service(IRequestCycle cycle) throws IOException {

		Date nowDate = new Date();

		Date startDate = RequestUtil.getDate(request, "startDate", nowDate);
		Date endDate = RequestUtil.getDate(request, "endDate", nowDate);
		String responseCode = ServletRequestUtils.getStringParameter(request, "responseCode", "");

		response.setContentType("plain/text");
		OutputStream outputStream = response.getOutputStream();

		// smsLogSeraManager.writeReconcileFile(outputStream, startDate,
		// endDate, responseCode);

		outputStream.close();
	}

	/**
	 * {@inheritDoc}
	 */
	public ILink getLink(boolean post, Object parameter) {
		Object[] parameterList = (Object[]) parameter;

		Map<String, Object> parameters = new HashMap<String, Object>();
		for (int i = 0; i < parameterList.length; i++) {
			String tempSplit[] = ((String) parameterList[i]).split("=", 2);
			if (tempSplit.length == 2)
				parameters.put(tempSplit[0], tempSplit[1]);
		}

		return linkFactory.constructLink(this, post, parameters, false);
	}

	// setter
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setLinkFactory(LinkFactory linkFactory) {
		this.linkFactory = linkFactory;
	}

}
