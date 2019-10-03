package com.balicamp.webapp.action.webadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.BeanPropertySelectionModel;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.hibernate.criterion.Order;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mx.Endpoints;
import com.balicamp.model.mx.MerchantGroup;
import com.balicamp.model.mx.PriorityRouting;
import com.balicamp.model.mx.TransactionFee;
import com.balicamp.model.mx.TransactionFeeId;
import com.balicamp.model.mx.TransactionFeeModel;
import com.balicamp.model.mx.Transactions;
import com.balicamp.service.EndpointsManager;
import com.balicamp.service.TransactionFeeManager;
import com.balicamp.service.TransactionsManager;
import com.balicamp.service.merchant.MerchantManager;
import com.balicamp.service.priority.PriorityManager;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.tapestry.GenericPropertySelectionModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 *
 * @author snurma.wijayanti
 *
 */
public abstract class PricingSchemeEntry extends BasePageList implements PageBeginRenderListener {

	private static Logger LOGGER = Logger.getLogger(PricingSchemeEntry.class.getSimpleName());

	public abstract void setErrormsg(String errormsg);

	public abstract String getErrormsg();

	@InjectSpring("priorityManager")
	public abstract PriorityManager getPriorityManager();

	@InjectSpring("transactionsManagerImpl")
	public abstract TransactionsManager getTransactionsManager();

	@InjectSpring("transactionFeeManagerImpl")
	public abstract TransactionFeeManager getTransactionFeeManager();

	@InjectSpring("endpointsManagerImpl")
	public abstract EndpointsManager getEndpointsManager();

	@InjectSpring("merchantManager")
	public abstract MerchantManager getMerchantManager();

	public abstract void setTransactions(List<Transactions> txs);

	public abstract List<Transactions> getTransactions();

	public abstract void setAllTransactions(List<TransactionFeeModel> txs);

	@Persist("client")
	public abstract List<TransactionFeeModel> getAllTransactions();

	@Override
	public abstract Object getLoopObject();

	@Override
	public abstract void setLoopObject(Object o);

	public abstract void setFeeChanges(HashMap<String, TransactionFeeModel> model);

	public abstract HashMap<String, TransactionFeeModel> getFeeChanges();

	public abstract String getIdentifier();

	public abstract void setIdentifier(String identifier);

	public abstract void setPriorities(List<PriorityRouting> priorities);

	@Persist("client")
	public abstract List<PriorityRouting> getPriorities();

	public abstract void setCategory(SearchCriteria category);

	@Persist("client")
	public abstract SearchCriteria getCategory();

	public abstract void setRows(int rows);

	@Persist("client")
	public abstract int getRows();

	public abstract void setChannel(Endpoints channelCode);

	@Persist("client")
	public abstract Endpoints getChannel();

	@InjectPage("pricingSchemeConfirm")
	public abstract PricingSchemeConfirm getPricingSchemeConfirm();

	@SuppressWarnings("unchecked")
	public IPropertySelectionModel getChannelModel() {
		SearchCriteria scEnd = SearchCriteria.createSearchCriteria("endpoints");
		scEnd.addOrder(Order.asc("code"));
		List endpointList = getEndpointsManager().findByCriteria(scEnd, -1, -1);
		return new GenericPropertySelectionModel(endpointList, null, null, true);
	}

	private BeanPropertySelectionModel merchantGroupModel = null;

	public BeanPropertySelectionModel getMerchantGroupModel() {
		final List<MerchantGroup> criteriaList = getMerchantManager().findAllMerchantGroup();
		merchantGroupModel = new BeanPropertySelectionModel(criteriaList, "descTr");
		return merchantGroupModel;
	}

	public abstract void setMerchantGroup(MerchantGroup merchantGroup);

	@Persist("client")
	public abstract MerchantGroup getMerchantGroup();

	/**
	 * first inisiate
	 */
	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		if (getFeeChanges() == null)
			setFeeChanges(new HashMap<String, TransactionFeeModel>());

		if (getAllTransactions() == null) {
			setAllTransactions(getDataGridForTransactions());
		}
	}

	/**
	 *
	 * @return
	 */
	private List<TransactionFeeModel> getDataGridForTransactions() {
		List<PriorityRouting> priorities = getPriorities();

		List<TransactionFeeModel> list = new ArrayList<TransactionFeeModel>();
		int i = 1;
		for (PriorityRouting priority : priorities) {
			TransactionFeeModel fee = new TransactionFeeModel();
			fee.setPriority(priority);

			Transactions trx = getTransactionsManager().findTransactionsByCode(priority.getTransactionCode());

			/*
			 * trxCode trxName projectCode productCode fee;
			 */

			fee.setTransaction(trx);
			if (trx != null) {
				// LOGGER.log(Level.INFO, trx.getCode() + " - " +
				// trx.getName());
				fee.setTrxCode(trx.getCode());
			} else {
				fee.setTrxCode(priority.getTransactionCode());
			}

			fee.setTrxName(priority.getDescription());

			fee.setProjectCode(priority.getProjectCode());
			fee.setProductCode(priority.getProductCode());

			if (fee.getFee() == null) {
				fee.setFee(new Long(0));
			}

			fee.setNo(i);

			list.add(fee);
			i++;
		}
		return list;
	}

	@Override
	public IBasicTableModel createTableModel() {
		return new IBasicTableModel() {

			@Override
			public int getRowCount() {
				if (getCacheTableRowCount() <= 0) {
					setCacheTableRowCount(getRows());
				}
				return getCacheTableRowCount();
			}

			@SuppressWarnings("unchecked")
			@Override
			public Iterator<TransactionFeeModel> getCurrentPageRows(int nFirst, int nPageSize,
					ITableColumn objSortColumn, boolean bSortOrder) {

				if (getCacheTableData() == null) {
					List<PriorityRouting> priorities = getPriorityManager().findPriority(getCategory(), nFirst,
							nPageSize);

					List<TransactionFeeModel> list = new ArrayList<TransactionFeeModel>();
					int i = 1;
					for (PriorityRouting priority : priorities) {
						TransactionFeeModel fee = new TransactionFeeModel();
						Transactions trx = getTransactionsManager().findTransactionsByCode(
								priority.getTransactionCode());

						/*
						 * trxCode trxName projectCode productCode fee;
						 */

						fee.setTransaction(trx);
						if (trx != null) {
							fee.setTrxCode(trx.getCode());
						} else {
							fee.setTrxCode(priority.getTransactionCode());
						}
						fee.setTrxName(priority.getDescription());
						fee.setProjectCode(priority.getProjectCode());
						fee.setProductCode(priority.getProductCode());

						if (fee.getFee() == null) {
							fee.setFee(new Long(0));
						}
						fee.setNo(i);
						list.add(fee);
						i++;
					}
					setCacheTableData(list);
				}
				return (Iterator<TransactionFeeModel>) getCacheTableData().iterator();
			}
		};
	}

	@Override
	public IBasicTableModel getTableModel() {
		return createTableModel();
	}

	public void setTableModel(IBasicTableModel model) {
	}

	public void valueChanges(IRequestCycle cycle) {
		if (cycle.isRewinding()) {
			TransactionFeeModel transactionModel = (TransactionFeeModel) getLoopObject();
			List<TransactionFeeModel> cache = getAllTransactions();

			if (transactionModel.getFee() > 0) {
				getFeeChanges().put(transactionModel.getTrxCode(), transactionModel);

				for (TransactionFeeModel transactionModelCache : cache) {
					if (transactionModel.getTrxCode().equals(transactionModelCache.getTrxCode())
							&& transactionModel.getProductCode().equals(transactionModelCache.getProductCode())
							&& transactionModel.getTrxName().equals(transactionModelCache.getTrxName())) {
						transactionModelCache.setFee(transactionModel.getFee());
					}
				}
			}
		}

	}

	/**
	 * save ke table t_approval
	 * @param cycle
	 * @return
	 */
	public IPage doSave(IRequestCycle cycle) {

		if (getChannel() == null) {
			setErrormsg(getText("trxfee.message.error.add.channel.notnull"));
			return null;
		} else {
			setErrormsg(null);
		}

		if (getMerchantGroup() == null) {
			setErrormsg(getText("trxfee.message.error.add.merchant.notnull"));
			return null;
		} else {
			setErrormsg(null);
		}

		StringBuffer trxNames = new StringBuffer();

		List<TransactionFee> validFee = new ArrayList<TransactionFee>();

		for (TransactionFeeModel tm : getAllTransactions()) {
			if (tm.getFee() > 0) {

				trxNames.append(tm.getTrxName() + ", ");
				TransactionFee tf = new TransactionFee();

				TransactionFeeId feeId = new TransactionFeeId();
				feeId.setIdentifier(packIdentifier(tm));
				feeId.setFee(tm.getFee().intValue());

				if (tm.getTransaction() != null) {
					feeId.setTransactionId(tm.getTransaction().getId());
					tf.setTransactions(tm.getTransaction());
				}

				feeId.setChannelId(getChannel().getId());

				tf.setId(feeId);
				tf.setEndpoints(getChannel());
				validFee.add(tf);
				LOGGER.info(tf.getId().getFee() + " - " + tf.getId().getProductCodeTr() + " - " + validFee.size());
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

		PricingSchemeConfirm confirm = getPricingSchemeConfirm();
		confirm.setTransactionFee(validFee);
		setAllTransactions(null);
		return confirm;
	}

	/**
	 * merchant_group|projectcode|product_code
	 * @param tm
	 * @return
	 */
	private String packIdentifier(TransactionFeeModel tm) {
		return getMerchantGroup().getCode().trim() + "#" + tm.getProjectCode() + "#" + tm.getProductCode();
	}

	public IPage doReset(IRequestCycle cycle) {
		setAllTransactions(null);
		setTransactions(null);
		setCacheTableData(null);
		setFeeChanges(null);
		return this;
	}
}