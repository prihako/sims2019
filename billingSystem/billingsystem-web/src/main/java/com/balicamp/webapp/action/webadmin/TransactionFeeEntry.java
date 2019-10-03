/**
 * 
 */
package com.balicamp.webapp.action.webadmin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.hibernate.criterion.Order;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mx.Endpoints;
import com.balicamp.model.mx.TransactionFee;
import com.balicamp.model.mx.TransactionFeeId;
import com.balicamp.model.mx.TransactionModel;
import com.balicamp.model.mx.Transactions;
import com.balicamp.service.EndpointsManager;
import com.balicamp.service.TransactionFeeManager;
import com.balicamp.service.TransactionsManager;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.tapestry.GenericPropertySelectionModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionFeeEntry.java 508 2013-05-28 09:06:38Z gloria.patara $
 */
public abstract class TransactionFeeEntry extends BasePageList implements PageBeginRenderListener {

	@InjectSpring("transactionsManagerImpl")
	public abstract TransactionsManager getTransactionsManager();

	@InjectSpring("transactionFeeManagerImpl")
	public abstract TransactionFeeManager getTransactionFeeManager();

	@InjectSpring("endpointsManagerImpl")
	public abstract EndpointsManager getEndpointsManager();

	public abstract void setTransactions(List<Transactions> txs);

	public abstract List<Transactions> getTransactions();

	public abstract Object getLoopObject();

	public abstract void setLoopObject(Object o);

	public abstract void setFeeChanges(Set<TransactionModel> model);

	public abstract Set<TransactionModel> getFeeChanges();

	public abstract String getIdentifier();

	public abstract void setIdentifier(String identifier);

	public abstract void setChannel(Endpoints channelCode);

	@Persist("client")
	public abstract Endpoints getChannel();

	@SuppressWarnings("unchecked")
	public IPropertySelectionModel getChannelModel() {
		SearchCriteria scEnd = SearchCriteria.createSearchCriteria("endpoints");
		scEnd.addOrder(Order.asc("code"));
		List endpointList = getEndpointsManager().findByCriteria(scEnd, -1, -1);
		return new GenericPropertySelectionModel(endpointList, null, null, true);
	}

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		if (getFeeChanges() == null)
			setFeeChanges(new HashSet<TransactionModel>());
	}

	@Override
	public IBasicTableModel createTableModel() {
		return new IBasicTableModel() {

			@Override
			public int getRowCount() {
				if (getCacheTableRowCount() <= 0) {
					setCacheTableRowCount(getTransactionsManager().getTransactionCountForFee());
				}
				return getCacheTableRowCount();
			}

			@SuppressWarnings("unchecked")
			@Override
			public Iterator<TransactionModel> getCurrentPageRows(int nFirst, int nPageSize, ITableColumn objSortColumn,
					boolean bSortOrder) {
				if (getCacheTableData() == null) {
					List<Transactions> txs = getTransactionsManager().findTransactionsForFee(nFirst, nPageSize);
					setTransactions(txs);

					List<TransactionModel> cache = new ArrayList<TransactionModel>();
					for (Transactions tx : txs) {
						cache.add(new TransactionModel(tx.getName(), tx.getCode(), 0));
					}
					setCacheTableData(cache);
				}
				return (Iterator<TransactionModel>) getCacheTableData().iterator();
			}
		};
	}

	public IBasicTableModel getTableModel() {
		return createTableModel();
	}

	public void setTableModel(IBasicTableModel model) {
	}

	public void valueChanges(IRequestCycle cycle) {
		if (cycle.isRewinding()) {
			TransactionModel transactionModel = (TransactionModel) getLoopObject();
			if (transactionModel.getFee() > 0) {
				getFeeChanges().add(transactionModel);
			}
		}
	}

	public IPage doSave(IRequestCycle cycle) {

		StringBuffer trxNames = new StringBuffer();

		List<TransactionFee> validFee = new ArrayList<TransactionFee>();
		for (TransactionModel tm : getFeeChanges()) {
			if (tm.getFee() > 0) {
				for (Transactions tx : getTransactions()) {
					if (tm.getCode().equals(tx.getCode())) {
						trxNames.append(tx.getName() + ", ");
						TransactionFee tf = new TransactionFee();

						TransactionFeeId feeId = new TransactionFeeId();
						feeId.setIdentifier(getIdentifier());
						feeId.setFee(tm.getFee());
						feeId.setTransactionId(tx.getId());

						feeId.setChannelId(getChannel().getId());

						tf.setTransactions(tx);
						tf.setId(feeId);
						tf.setEndpoints(getChannel());
						validFee.add(tf);
						break;
					}
				}
			}
		}

		if (validFee.size() <= 0) {
			TransactionFeeEmptyRecord emptyRecord = (TransactionFeeEmptyRecord) cycle
					.getPage("transactionFeeEmptyRecord");
			return emptyRecord;
		}

		List<TransactionFee> exists = getTransactionFeeManager().isIdentifierExists(validFee);
		if (exists.size() > 0) {
			TransactionFeeAlreadyExists existsPage = (TransactionFeeAlreadyExists) getRequestCycle().getPage(
					"transactionFeeAlreadyExists");
			existsPage.setTransactionFee(exists);
			return existsPage;
		}

		TransactionFeeEntryConfirm confirm = (TransactionFeeEntryConfirm) getRequestCycle().getPage(
				"transactionFeeEntryConfirm");
		confirm.setTransactionFee(validFee);
		return confirm;
	}
}
