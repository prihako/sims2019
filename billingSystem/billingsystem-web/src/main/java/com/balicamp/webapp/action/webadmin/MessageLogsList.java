/**
 * 
 */
package com.balicamp.webapp.action.webadmin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mx.MessageLogsDto;
import com.balicamp.model.mx.MessageLogsParameter;
import com.balicamp.service.MessageLogsManager;
import com.balicamp.util.CommonUtil;
import com.balicamp.webapp.action.BasePageGrid;
import com.balicamp.webapp.tapestry.component.ActionListenerWrapper;
import com.balicamp.webapp.tapestry.component.ExtendedGrid;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: MessageLogsList.java 508 2013-05-28 09:06:38Z gloria.patara $
 */
public abstract class MessageLogsList extends BasePageGrid implements IActionListener, PageBeginRenderListener {

	@InjectSpring("messageLogsManagerImpl")
	public abstract MessageLogsManager getMessageLogsManager();

	@Persist("client")
	public abstract String getTransactionCode();

	public abstract void setTransactionCode(String transCode);

	@Persist("client")
	public abstract String getTransactionDesc();

	public abstract void setTransactionDesc(String transDesc);

	@Persist("client")
	public abstract String getEndpointCode();

	public abstract void setEndpointCode(String endpointCode);

	@Persist("client")
	public abstract String getTransactionId();

	public abstract void setTransactionId(String transId);

	@Persist("client")
	public abstract Date getStartDate();

	public abstract void setStartDate(Date startDate);

	@Persist("client")
	public abstract Date getEndDate();

	public abstract void setEndDate(Date endDate);

	public abstract String getEditorState();

	public abstract void setEditorState(String state);

	public abstract void setRawKey(String key);

	public abstract String getRawKey();

	private MessageLogsParameter parameter;

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		generateInqueryParameter();
		getGridData();
	}

	@SuppressWarnings("unchecked")
	public ExtendedGrid<MessageLogsDto> getGrid() {
		return (ExtendedGrid<MessageLogsDto>) getPage().getComponent("grid");
	}

	public int getGridRowCount() {
		if (getTransactionId() == null && getTransactionCode() == null && getTransactionDesc() == null
				&& getEndpointCode() == null && getStartDate() == null && getEndDate() == null && getRawKey() == null)
			return 0;

		if (getCacheTableRowCount() <= 0) {
			setCacheTableRowCount(getMessageLogsManager().getRowCountByParameter(parameter));
		}
		return getCacheTableRowCount();
	}

	public void setGridData(List<MessageLogsDto> lists) {
	}

	@SuppressWarnings("unchecked")
	public List<MessageLogsDto> getGridData() {

		if (getTransactionId() == null && getTransactionCode() == null && getTransactionDesc() == null
				&& getEndpointCode() == null && getStartDate() == null && getEndDate() == null && getRawKey() == null)
			return null;

		// if (getCacheTableData() == null) {
		List<MessageLogsDto> messageLogsDisplay = getMessageLogsManager().findMessageLogsByParameter(parameter,
				getFirstResult(), getMaxResults());
		setCacheTableData(messageLogsDisplay);
		// }

		List<MessageLogsDto> tableData = (List<MessageLogsDto>) getCacheTableData();
		// Collections.sort(tableData);

		return tableData;
	}

	private MessageLogsParameter generateInqueryParameter() {
		if (parameter == null)
			parameter = new MessageLogsParameter();

		parameter.setTransactionId(!CommonUtil.isEmpty(getTransactionId()) ? getTransactionId() : null);
		parameter.setTransactionCode(!CommonUtil.isEmpty(getTransactionCode()) ? getTransactionCode() : null);
		parameter.setTransactionDesc(!CommonUtil.isEmpty(getTransactionDesc()) ? getTransactionDesc() : null);
		parameter.setEndpointCode(!CommonUtil.isEmpty(getEndpointCode()) ? getEndpointCode() : null);
		parameter.setEndpointCode(!CommonUtil.isEmpty(getEndpointCode()) ? getEndpointCode() : null);
		parameter.setStartDate(getStartDate() != null ? getStartDate() : null);
		parameter.setEndDate(getEndDate() != null ? getEndDate() : null);
		parameter.setRawKey(!CommonUtil.isEmpty(getRawKey()) ? getRawKey() : null);
		return parameter;
	}

	/**
	 * Action view details
	 */
	public ActionListenerWrapper getAdditionalLinkListener() {
		return new ActionListenerWrapper() {
			@Override
			public void onAction(Object parameter) {
				MessageLogsDetail msgLog = (MessageLogsDetail) getRequestCycle().getPage("messageLogsDetail");
				msgLog.setObjectParameter((MessageLogsDto) parameter);
				getRequestCycle().activate(msgLog);
			}
		};
	}

	/**
	* Listener untuk export to word
	* @return {@link ActionListenerWrapper} 
	*/
	public ActionListenerWrapper getGridActionButtonListener() {
		return new ActionListenerWrapper() {
			@SuppressWarnings("unchecked")
			@Override
			public void onAction(Object parameter) {
				if (getDelegate().getHasErrors())
					return;

				if (getGrid().getSortedUniqueSet().size() == 0) {
					addError(getDelegate(), (IFormComponent) null, getText("messageLog.required"),
							ValidationConstraint.REQUIRED);
					return;
				}

				if (parameter instanceof List<?>) {
					List<MessageLogsDto> lines = new ArrayList<MessageLogsDto>();
					lines.addAll((ArrayList<MessageLogsDto>) parameter);
					// Collections.sort(lines);
					String fileName = getMessageLogsManager().exportToWord(lines);
					download(fileName);
				}
			}

		};
	}

	@SuppressWarnings("unchecked")
	public void onSearch() {
		generateInqueryParameter();
		List<MessageLogsDto> msgLogs = getGridData();
		if (msgLogs != null) {
			if (msgLogs.size() == 0) {
				MessageLogsNotFound msgNF = (MessageLogsNotFound) getRequestCycle().getPage("messageLogsNotFound");
				msgNF.setMsg(parameter.getConcatParameter());
				getRequestCycle().activate(msgNF);
			} else {
				((ExtendedGrid<MessageLogsDto>) getComponent("grid")).setGridData(msgLogs);
			}
		}
	}

	public void actionTriggered(IComponent component, IRequestCycle cycle) {
		if (component.getClientId().equals("searchButton"))
			onSearch();
	}

	public String getMethodName() {
		return null;
	}
}
