/**
 * 
 */
package com.balicamp.webapp.action.webadmin;

import java.text.Format;

import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mx.MessageLogsDto;
import com.balicamp.service.MessageLogsManager;
import com.balicamp.util.DateUtil;
import com.balicamp.webapp.action.AdminBasePage;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: MessageLogsDetails.java 508 2013-05-28 09:06:38Z gloria.patara $
 */
public abstract class MessageLogsDetails extends AdminBasePage implements PageBeginRenderListener {

	// keperluan untuk query
	public abstract void setObjectParameter(MessageLogsDto msgDisplay);

	public abstract MessageLogsDto getObjectParameter();

	public abstract void setTransactionId(String transId);

	public abstract void setTransactionCode(String transCode);

	public abstract void setTransactionDesc(String transDesc);

	public abstract void setEndpoint(String endpoint);

	public abstract void setTransactionDate(String transDate);

	public abstract void setResponseCode(String rc);

	public abstract void setResponseCodeDesc(String rcDesc);

	public abstract void setRawMessage(String raw);

	public abstract void setFormatedMessage(String fmsg);

	@InjectSpring("messageLogsManagerImpl")
	public abstract MessageLogsManager getMessageLogManager();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		drawDetails(getObjectParameter());
	}

	@Override
	public Format getDateFormat() {
		return super.getDateFormat();
	}

	private void drawDetails(MessageLogsDto objectParameter) {
		// set to property of page
		setTransactionId(objectParameter.getTransactionId());
		setTransactionCode(objectParameter.getTransactionCode());
		setTransactionDate(DateUtil.convertDateToString(objectParameter.getTransactionDateTime(),
				"dd MMM yyyy HH:mm:ss"));
		setTransactionDesc(objectParameter.getTransactionName());
		setEndpoint(objectParameter.getEndpointCode());
		setResponseCode(objectParameter.getRc());
		setResponseCodeDesc(objectParameter.getRcDesc());

		MessageLogsDto display = getMessageLogManager().getRawMessageLogs(objectParameter);
		setRawMessage(display.getRaw());
		setFormatedMessage(display.getFormatedMessage());
	}
}
