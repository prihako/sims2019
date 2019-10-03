/**
 * 
 */
package com.balicamp.webapp.action.webadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
 * @version $Id: TransactionFeeEntry.java 517 2013-06-25 10:13:25Z rudi.sadria $
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

	public abstract void setAllTransactions(List<TransactionModel> txs);

	@Persist("session")
	public abstract List<TransactionModel> getAllTransactions();

	public abstract Object getLoopObject();

	public abstract void setLoopObject(Object o);

	public abstract void setFeeChanges(HashMap<String, TransactionModel> model);

	public abstract HashMap<String, TransactionModel> getFeeChanges();

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
			setFeeChanges(new HashMap<String, TransactionModel>());

		if (getAllTransactions() == null) {
			List<Transactions> txs = getTransactionsManager().findTransactionsForFee(-1, -1);

			List<TransactionModel> cache = new ArrayList<TransactionModel>();
			for (Transactions tx : txs) {
				cache.add(new TransactionModel(tx.getName(), tx.getCode(), 0, tx));
			}
			setAllTransactions(cache);
		}
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
						List<TransactionModel> sessionTransaction = (List<TransactionModel>) getAllTransactions();
						int fee = 0;
						for (TransactionModel transactionModelCache : sessionTransaction) {
							if (tx.getCode().equals(transactionModelCache.getCode()))
								fee = transactionModelCache.getFee();
						}

						cache.add(new TransactionModel(tx.getName(), tx.getCode(), fee, tx));
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
			List<TransactionModel> cache = (List<TransactionModel>) getAllTransactions();
			if (transactionModel.getFee() > 0) {
				getFeeChanges().put(transactionModel.getCode(), transactionModel);
				for (TransactionModel transactionModelCache : cache) {
					if (transactionModel.getCode().equals(transactionModelCache.getCode()))
						transactionModelCache.setFee(transactionModel.getFee());
				}
			}
		}
	}

	public IPage doSave(IRequestCycle cycle) {

		StringBuffer trxNames = new StringBuffer();

		List<TransactionFee> validFee = new ArrayList<TransactionFee>();

		for (TransactionModel tm : getAllTransactions()) {
			if (tm.getFee() > 0) {
				trxNames.append(tm.getName() + ", ");
				TransactionFee tf = new TransactionFee();

				TransactionFeeId feeId = new TransactionFeeId();
				feeId.setIdentifier(getIdentifier());
				feeId.setFee(tm.getFee());
				feeId.setTransactionId(tm.getTransactions().getId());

				feeId.setChannelId(getChannel().getId());

				tf.setTransactions(tm.getTransactions());
				tf.setId(feeId);
				tf.setEndpoints(getChannel());
				validFee.add(tf);
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
		setAllTransactions(null);
		return confirm;
	}

	public IPage doReset(IRequestCycle cycle) {
		setAllTransactions(null);
		setTransactions(null);
		setCacheTableData(null);
		setFeeChanges(null);
		return this;
	}
}
