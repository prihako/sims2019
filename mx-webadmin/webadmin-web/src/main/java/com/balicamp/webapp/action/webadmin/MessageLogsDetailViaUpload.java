/**
 * 
 */
package com.balicamp.webapp.action.webadmin;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mx.AnalisaTransactionLogsDto;
import com.balicamp.model.mx.MessageLogsDto;
import com.balicamp.service.MessageLogsManager;
import com.balicamp.util.DateUtil;
import com.balicamp.webapp.action.AdminBasePage;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: MessageLogsDetail.java 505 2013-05-24 08:15:51Z rudi.sadria $
 */
public abstract class MessageLogsDetailViaUpload extends AdminBasePage implements PageBeginRenderListener {

	// keperluan untuk query
	public abstract void setObjectParameter(MessageLogsDto msgDisplay);

	public abstract MessageLogsDto getObjectParameter();

	public abstract void setTransactionId(String transId);

	public abstract String getTransactionId();

	public abstract String getMxRc();

	public abstract void setMxRc(String transId);

	public abstract String getChannelRc();

	public abstract void setChannelRc(String transId);

	public abstract void setEndpointCode(String endpoint);

	public abstract void setConversionDate(String conversionDate);

	public abstract void setMappingName(String mappingName);

	public abstract void setResponseCode(String rc);

	public abstract void setResponseCodeDesc(String rcDesc);

	public abstract boolean getIsRequest();

	public abstract void setIsRequest(boolean isRequest);

	public abstract void setRawMessage(String raw);

	public abstract void setFormatedMessage(String fmsg);

	public abstract String getFormatedMessage();

	public abstract void setNotFirstLoad(boolean firsloadFlag);

	public abstract boolean isNotFirstLoad();

	@InjectSpring("messageLogsManagerImpl")
	public abstract MessageLogsManager getMessageLogManager();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if (!pageEvent.getRequestCycle().isRewinding()) {
			if (!isNotFirstLoad()) {
				setNotFirstLoad(true);
				drawDetails(getObjectParameter());
				if (getFields() == null) {
					setFields(new HashMap());
				}
			}
		}

	}

	private void drawDetails(MessageLogsDto objectParameter) {

		Object messageLogs = getMessageLogManager().getMessageLogsData(objectParameter.getTransactionId(),
				objectParameter.getEndpointCode(), objectParameter.isRequest());
		// set to property of page
		setTransactionId(objectParameter.getTransactionId());
		setConversionDate(DateUtil.convertDateToString(objectParameter.getConversionTime(), "dd MMM yyyy HH:mm:ss"));
		setEndpointCode(objectParameter.getEndpointCode());
		// setResponseCode(objectParameter.getRc());

		System.out.println("Message Log DETAIL IS REQUEST " + objectParameter.isRequest());
		setIsRequest(objectParameter.isRequest());

		Properties prop = new Properties();
		try {
			prop.load(new StringReader((String) messageLogs));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!objectParameter.getEndpointCode().equals("bils0") && !objectParameter.isRequest()) {

			// setResponseCode(getChannelRc());
			System.out.println("COBA BACA ERROR CODE " + prop.getProperty("errorCode"));
			System.out.println("COBA BACA STATUS DESC " + prop.getProperty("statusDescription"));

			setResponseCode(prop.getProperty("errorCode"));
			// setResponseCodeDesc(prop.getProperty("statusDescription"));
			setResponseCodeDesc(objectParameter.getRcDesc());

		} else if (objectParameter.getEndpointCode().equals("bils0") && !objectParameter.isRequest()) {

			setResponseCode(objectParameter.getRc());
			setResponseCodeDesc(objectParameter.getRcDesc());

		}
		setMappingName(objectParameter.getMappingName());

		// MessageLogsDto display =
		// getMessageLogManager().getRawMessageLogs(objectParameter);

		System.out.println("MESSAGE LOGS RAW DETAIL -- >>" + objectParameter.getRaw());

		if (objectParameter.getRaw() == null) {

			setRawMessage((String) messageLogs);

		} else {
			setRawMessage(objectParameter.getRaw());

		}

		System.out.println("MESSAGE LOGS  DETAIL OBJECT PARAAMETER VALUE  -- > " + objectParameter);

		objectParameter = getMessageLogManager().getRawMessageLogs(objectParameter, true);

		System.out.println("MESSAGE LOGS RAW DETAIL111 -- >>" + objectParameter.getRaw());

		setFormatedMessage(objectParameter.getFormatedMessage());
	}

	public void goBack(IRequestCycle cycle) {
		MessageLogsListViaUpload messageLogsList = (MessageLogsListViaUpload) cycle.getPage("messageLogsListViaUpload");
		messageLogsList.setEditorState("visible");

		messageLogsList.setTransactionLogList((List<AnalisaTransactionLogsDto>) getFields().get(
				"transactionLogListViaUpload"));
		messageLogsList.setTransactionId((String) getFields().get("transactionIdViaUpload"));

		getFields().put("back", "back");

		cycle.activate(messageLogsList);
	}
}
