package com.balicamp.webapp.action.webadmin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mx.AnalisaTransactionLogsDto;
import com.balicamp.service.TransactionLogsManager;
import com.balicamp.webapp.action.BasePageList;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class AnalisaTransactionLogs extends BasePageList implements PageBeginRenderListener {

	@InjectSpring("transactionLogsManagerImpl")
	public abstract TransactionLogsManager getGridManager();

	public abstract void setTransactionCode(String code);

	public abstract String getTransactionCode();

	public abstract void setTransactionName(String name);

	public abstract String getTransactionName();

	public abstract void setChannelCode(String channelCode);

	public abstract String getChannelCode();

	public abstract Date getStartDate();

	public abstract void setStartDate(Date startDate);

	public abstract Date getEndDate();

	public abstract void setEndDate(Date endDate);

	public abstract void setTransactionId(String id);

	public abstract String getTransactionId();

	public abstract AnalisaTransactionLogsDto getTableRow();

	public abstract void setTableRow(AnalisaTransactionLogsDto row);

	public abstract void setFormattedRaw(String formattedRaw);

	public abstract String getFormattedRaw();

	public abstract void setRawKey(String key);

	public abstract String getRawKey();

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	public void doSearch() {
		getTableModel();
	}

	public IBasicTableModel createTableModel() {
		return new IBasicTableModel() {

			@Override
			public int getRowCount() {
				if (getTransactionId() == null && getTransactionCode() == null && getTransactionName() == null
						&& getChannelCode() == null && getStartDate() == null && getEndDate() == null
						&& getRawKey() == null)
					return 0;
				if (getCacheTableRowCount() <= 0) {
					String rawKey = getRawKey();
					if (rawKey != null)
						rawKey = "%" + rawKey + "%";
					int rowCount = getGridManager().getRowCount(getTransactionId(), getTransactionCode(),
							getTransactionName(), getChannelCode(), getStartDate(), getEndDate(), rawKey).intValue();
					setCacheTableRowCount(rowCount);
				}
				return getCacheTableRowCount();
			}

			@SuppressWarnings("unchecked")
			@Override
			public Iterator<AnalisaTransactionLogsDto> getCurrentPageRows(int nFirst, int nPageSize,
					ITableColumn objSortColumn, boolean bSortOrder) {

				if (getTransactionId() == null && getTransactionCode() == null && getTransactionName() == null
						&& getChannelCode() == null && getStartDate() == null && getEndDate() == null
						&& getRawKey() == null)
					return new ArrayList<AnalisaTransactionLogsDto>().iterator();
				String rawKey = getRawKey();
				if (rawKey != null)
					rawKey = "%" + rawKey + "%";
				if (getCacheTableData() == null) {
					List<AnalisaTransactionLogsDto> data = getGridManager().findTransactions(getTransactionId(),
							getTransactionCode(), getTransactionName(), getChannelCode(), getStartDate(), getEndDate(),
							rawKey, nFirst, nPageSize);
					setCacheTableData(data);
				}
				return ((List<AnalisaTransactionLogsDto>) getCacheTableData()).iterator();
			}
		};
	}

	public IBasicTableModel getTableModel() {
		return super.getTableModel();
	}

	public IPage additionalLinkListener(IRequestCycle cycle, String trxId) {
		MessageLogsList messageLogsList = (MessageLogsList) cycle.getPage("messageLogsList");
		messageLogsList.setEditorState("visible");
		messageLogsList.setTransactionId(trxId);
		return messageLogsList;
	}

	public IPage viewRawData(IRequestCycle cycle, String trxId) {
		RawTransactionView rtv = (RawTransactionView) cycle.getPage("rawTransactionView");
		List<String> msgLogs = getGridManager().findMessageLogsByTxId(trxId);
		StringBuffer buffer = new StringBuffer();
		for (String raw : msgLogs) {
			StringReader stringReader = new StringReader(raw);
			BufferedReader reader = new BufferedReader(stringReader);
			String str;
			buffer.append("<table  border='0' class='data'>");
			try {
				while ((str = reader.readLine()) != null) {
					String[] line = str.split("[=]", 2);
					if (line != null && line.length > 1) {
						line[0] = line[0].replaceFirst("/data/|/info/|/custom/", "");
						line[0] = line[0].replaceFirst("/text().*$", "");
						buffer.append("<tr><td><b>" + line[0] + "</b></td><td>=</td><td>"
								+ line[1].replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</td></tr>");
					} else if (line != null && line.length == 1) {
						buffer.append("<tr><td colspan='3'><b>" + str + "</b></td></tr>");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			buffer.append("<table>");
		}
		rtv.setFormattedRaw(buffer.toString());
		return rtv;
	}

}
