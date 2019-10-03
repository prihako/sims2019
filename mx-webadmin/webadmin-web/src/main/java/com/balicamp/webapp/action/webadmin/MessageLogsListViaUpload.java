/**
 * 
 */
package com.balicamp.webapp.action.webadmin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mx.AnalisaTransactionLogsDto;
import com.balicamp.model.mx.MessageLogsDto;
import com.balicamp.model.mx.MessageLogsParameter;
import com.balicamp.model.mx.Transactions;
import com.balicamp.service.MessageLogsManager;
import com.balicamp.util.CommonUtil;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.tapestry.component.ActionListenerWrapper;
import com.balicamp.webapp.tapestry.component.ExtendedGrid;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: MessageLogsList.java 505 2013-05-24 08:15:51Z rudi.sadria $
 */
public abstract class MessageLogsListViaUpload extends BasePageList implements IActionListener, PageBeginRenderListener {

	@InjectSpring("messageLogsManagerImpl")
	public abstract MessageLogsManager getMessageLogsManager();

	@Persist("client")
	public abstract String getEndpointCode();

	public abstract void setEndpointCode(String endpointCode);

	@Persist("client")
	public abstract String getTransactionId();

	public abstract void setTransactionId(String transId);

	public abstract String getMxRc();

	public abstract void setMxRc(String transId);

	public abstract String getChannelRc();

	public abstract void setChannelRc(String transId);

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

	public abstract String getKlienID();

	public abstract void setKlienID(String clientID);

	public abstract String getInvoiceNo();

	public abstract void setInvoiceNo(String invoiceNo);

	public abstract void setTransactionCode(Transactions code);

	public abstract Transactions getTransactionCode();

	public abstract String getTransactionType();

	public abstract void setTransactionType(String transactionType);

	public abstract Date getStartDate2();

	public abstract void setStartDate2(Date startDate);

	public abstract Date getEndDate2();

	public abstract void setEndDate2(Date endDate);

	public abstract void setTransactionLogList(List<AnalisaTransactionLogsDto> list);

	public abstract List<AnalisaTransactionLogsDto> getTransactionLogList();

	public abstract void setNotFirstLoad(boolean firsloadFlag);

	public abstract boolean isNotFirstLoad();

	private MessageLogsParameter parameter;

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		generateInqueryParameter();
		getGridData();

		if (!pageEvent.getRequestCycle().isRewinding()) {
			if (!isNotFirstLoad()) {
				setNotFirstLoad(true);
				if (getFields() == null) {
					setFields(new HashMap());
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public ExtendedGrid<MessageLogsDto> getGrid() {
		return (ExtendedGrid<MessageLogsDto>) getPage().getComponent("grid");
	}

	public int getGridRowCount() {
		if (getTransactionId() == null && getEndpointCode() == null && getStartDate() == null && getEndDate() == null
				&& getRawKey() == null)
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

		if (getTransactionId() == null && getEndpointCode() == null && getStartDate() == null && getEndDate() == null
				&& getRawKey() == null)
			return null;
		// if (getCacheTableData() == null) {
		List<MessageLogsDto> messageLogsDisplay = getMessageLogsManager().findMessageLogsByParameter(parameter, -1, -1);
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
		parameter.setEndpointCode(!CommonUtil.isEmpty(getEndpointCode()) ? getEndpointCode() : null);
		parameter.setEndpointCode(!CommonUtil.isEmpty(getEndpointCode()) ? getEndpointCode() : null);
		parameter.setStartDate(getStartDate() != null ? (getStartDate()) : null);
		parameter.setEndDate(getEndDate() != null ? (getEndDate()) : null);
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
				MessageLogsDetailViaUpload msgLog = (MessageLogsDetailViaUpload) getRequestCycle().getPage(
						"messageLogsDetailViaUpload");
				msgLog.setObjectParameter((MessageLogsDto) parameter);

				getFields().put("transactionLogListViaUpload",
						(List<AnalisaTransactionLogsDto>) getFields().get("transactionLogListViaUpload"));
				getFields().put("transactionIdViaUpload", getFields().get("transactionIdViaUpload"));

				msgLog.setTransactionId(getTransactionId());

				if (getFields().get("back") != null) {
					getFields().remove("back");
				}

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
			public void onAction(Object param) {
				if (getDelegate().getHasErrors())
					return;

				// if use checkbox, use this validation
				// if (getGrid().getSortedUniqueSet().size() == 0) {
				// addError(getDelegate(), (IFormComponent) null,
				// getText("messageLog.required"),
				// ValidationConstraint.REQUIRED);
				// return;
				// }

				if (param instanceof List<?>) {
					// List<MessageLogsDto> lines = new
					// ArrayList<MessageLogsDto>();
					// lines.addAll((ArrayList<MessageLogsDto>) parameter);

					List<MessageLogsDto> lines = getMessageLogsManager().findMessageLogsByParameter(parameter, -1, -1);
					// Collections.sort(lines);
					// String fileName =
					// getMessageLogsManager().exportToWord(lines);

					// String fileName =
					// getMessageLogsManager().exportToWord(lines,
					// getRequest().getRealPath("/"),
					// getUserLoginFromSession());
					String fileName = getMessageLogsManager().exportToWord(lines);
					System.out.println("FILE NAME WORD " + fileName);

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

	public void goBack(IRequestCycle cycle) {
		AnalisaTransactionLogsViaUploadInvoice analisaMessageLogsList = (AnalisaTransactionLogsViaUploadInvoice) cycle
				.getPage("analisaTransactionLogsViaUploadInvoice");

		getFields().put("TRANSACTION_LOGS_VIA_UPLOAD_INVOICE",
				(List<AnalisaTransactionLogsDto>) getTransactionLogList());
		getFields().put("viewLogs", "xxx");
		System.out.println("Size list " + ((List<AnalisaTransactionLogsDto>) getTransactionLogList()).size());

		cycle.activate(analisaMessageLogsList);

	}
}
