package com.balicamp.webapp.action.webadmin;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.hibernate.criterion.Order;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mx.Transactions;
import com.balicamp.model.mx.TransactionsDto;
import com.balicamp.service.TransactionsManager;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.tapestry.GenericPropertySelectionModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class MonitoringTransaksi extends BasePageList implements PageBeginRenderListener {

	@InjectSpring("transactionsManagerImpl")
	public abstract TransactionsManager getGridManager();

	public abstract void setChannelCode(String channelCode);

	public abstract String getChannelCode();

	public abstract Date getStartDate();

	public abstract void setStartDate(Date startDate);

	public abstract Date getEndDate();

	public abstract void setEndDate(Date endDate);

	public abstract void setSelectedTransactions(List<Transactions> transactions);

	public abstract List<Transactions> getSelectedTransactions();

	public abstract void setTableRow(Object row);

	public abstract Object getTableRow();

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	private GenericPropertySelectionModel selectionModel = null;

	public IPropertySelectionModel getSelectionModel() {
		if (selectionModel == null) {
			SearchCriteria sc = SearchCriteria.createSearchCriteria("transactions");
			sc.addOrder(Order.asc("name"));
			List transList = getGridManager().findByCriteria(sc, -1, -1);
			selectionModel = new GenericPropertySelectionModel(transList, null, null, false, false);
		}
		return selectionModel;
	}

	public IBasicTableModel getTableModel() {
		return new IBasicTableModel() {

			@Override
			public int getRowCount() {
				if (getSelectedTransactionCodes() == null && getSelectedTransactionNames() == null
						&& getChannelCode() == null && getStartDate() == null && getEndDate() == null)
					return 0;
				if (getCacheTableRowCount() <= 0) {
					setCacheTableRowCount(getGridManager().getRowCount(getSelectedTransactionCodes(),
							getSelectedTransactionNames(), getChannelCode(), getStartDate(), getEndDate()));
				}

				return getCacheTableRowCount();
			}

			@SuppressWarnings("unchecked")
			@Override
			public Iterator<TransactionsDto> getCurrentPageRows(int nFirst, int nPageSize, ITableColumn objSortColumn,
					boolean bSortOrder) {

				if (getSelectedTransactionCodes() == null && getSelectedTransactionNames() == null
						&& getChannelCode() == null && getStartDate() == null && getEndDate() == null)
					return new ArrayList<TransactionsDto>().iterator();

				if (getCacheTableData() == null) {
					List<TransactionsDto> data = getGridManager().findSummaryTransactions(getSelectedTransactionCodes(),
							getSelectedTransactionNames(), getChannelCode(), getStartDate(), getEndDate(), nFirst,
							nPageSize);
					setCacheTableData(data);
				}
				return (Iterator<TransactionsDto>) getCacheTableData().iterator();
			}
		};
	}

	protected List<String> getSelectedTransactionCodes() {
		if (getSelectedTransactions().size() <= 0)
			return null;
		List<String> codes = new ArrayList<String>();
		for (Transactions tx : getSelectedTransactions()) {
			codes.add(tx.getCode());
		}
		return codes;
	}

	public List<String> getSelectedTransactionNames() {
		if (getSelectedTransactions().size() <= 0)
			return null;
		List<String> names = new ArrayList<String>();
		for (Transactions tx : getSelectedTransactions()) {
			names.add(tx.getName());
		}
		return names;
	}

	public String getFormattedSelectedTransactionCodes() {
		if (getSelectedTransactionCodes().size() <= 0)
			return null;
		StringBuffer codes = new StringBuffer();
		for (String code : getSelectedTransactionCodes()) {
			codes.append(code + ", ");
		}
		return codes.toString().substring(0, codes.length() - 2);
	}

	public String getFormattedSelectedTransactionNames() {
		if (getSelectedTransactionNames().size() <= 0)
			return null;
		StringBuffer names = new StringBuffer();
		for (String name : getSelectedTransactionNames()) {
			names.append(name + ", ");
		}
		return names.toString().substring(0, names.length() - 2);
	}

}
