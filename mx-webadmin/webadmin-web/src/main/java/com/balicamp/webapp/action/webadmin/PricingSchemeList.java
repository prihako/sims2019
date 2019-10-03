package com.balicamp.webapp.action.webadmin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;
import org.hibernate.criterion.Order;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mx.Endpoints;
import com.balicamp.model.mx.MerchantGroup;
import com.balicamp.model.mx.PriorityRouting;
import com.balicamp.model.mx.TransactionFee;
import com.balicamp.service.EndpointsManager;
import com.balicamp.service.TransactionFeeManager;
import com.balicamp.service.TransactionsManager;
import com.balicamp.service.merchant.MerchantManager;
import com.balicamp.service.priority.PriorityManager;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.constant.MerchantConstant.FromPage;
import com.balicamp.webapp.constant.PriorityConstant.Parameter;
import com.balicamp.webapp.constant.WebConstant;
import com.balicamp.webapp.model.priority.ObjectModel;
import com.balicamp.webapp.tapestry.GenericPropertySelectionModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class PricingSchemeList extends BasePageList implements PageBeginRenderListener {

	private static final Logger LOGGER = Logger.getLogger(PricingSchemeList.class.getSimpleName());

	/* filter search variable */

	public abstract void setKeys(String keys);

	@Persist("client")
	public abstract String getKeys();

	public abstract void setKeys2(String keys2);

	@Persist("client")
	public abstract String getKeys2();

//	private BeanPropertySelectionModel criteriaModel = null;
//
//	public BeanPropertySelectionModel getCriteriaModel() {
//		final List<ObjectModel> criteriaList = Parameter.getCriteria2();
//		criteriaModel = new BeanPropertySelectionModel(criteriaList, "obj");
//		return criteriaModel;
//	}
	
	public abstract void setCriteriaModel(BeanPropertySelectionModel criteriaModel);
	
	@Persist("client")
	public abstract BeanPropertySelectionModel getCriteriaModel();

	public abstract void setCriteria(ObjectModel criteria);

	@Persist("client")
	public abstract ObjectModel getCriteria();

	public abstract void setCriteria2(ObjectModel criteria2);

	public abstract ObjectModel getCriteria2();

	/* filter search variable */

	public abstract String getErrormsg();

	public abstract void setErrormsg(String errormsg);

	@InjectSpring("transactionFeeManagerImpl")
	public abstract TransactionFeeManager getTransactionFeeManager();

	@InjectSpring("endpointsManagerImpl")
	public abstract EndpointsManager getEndpointsManager();

	@InjectSpring("merchantManager")
	public abstract MerchantManager getMerchantManager();

	@InjectPage("transactionFeeNotFound")
	public abstract TransactionFeeNotFound getTransactionFeeNotFound();

	@InjectPage("transactionFeeConfirm")
	public abstract TransactionFeeConfirm getTransactionFeeDeleteConfirm();
	
	@InjectPage("pricingSchemeConfirm")
	public abstract PricingSchemeConfirm getPricingSchemeConfirm();
	
	@InjectPage("pricingSchemeDeleteConfirm")
	public abstract PricingSchemeDeleteConfirm getPricingSchemeDeleteConfirm();

	@InjectSpring("priorityManager")
	public abstract PriorityManager getPriorityManager();

	@InjectSpring("transactionsManagerImpl")
	public abstract TransactionsManager getTransactionsManager();

	@Persist("client")
	public abstract String getDescription();

	public abstract void setDescription(String description);

	// @Persist("client")
	// public abstract String getIdentifier();
	//
	// public abstract void setIdentifier(String identifier);

	@Persist("client")
	public abstract Endpoints getChannelCodeItem();

	public abstract void setChannelCodeItem(Endpoints channelItem);

	public abstract void setUniqueSet(Set<TransactionFee> selectedIds);

	public abstract Set<TransactionFee> getUniqueSet();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IPropertySelectionModel getChannelSelectionModel() {
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

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		if (getUniqueSet() == null)
			setUniqueSet(new HashSet<TransactionFee>());
		
		if(getCriteriaModel() == null){
			setCriteriaModel(constructCriteriaModel());
		}
		
		super.pageBeginRender(pageEvent);
	}

	private BeanPropertySelectionModel constructCriteriaModel() {
		final List<ObjectModel> criteriaList = Parameter.getCriteria2();
		BeanPropertySelectionModel criteriaModel = new BeanPropertySelectionModel(criteriaList, "obj");
		return criteriaModel;
	}

	public void setTableModel(IBasicTableModel model) {
	}

	@Override
	public IBasicTableModel createTableModel() {
		return new IBasicTableModel() {

			@Override
			public int getRowCount() {
				try {
					if (getChannelCodeItem() != null) {
						// if (getDescription() != null &&
						// !getDescription().equals("")) {
						// return
						// getPriorityManager().getPriorityCountForFee(getDescription());
						// // return
						// //
						// getTransactionFeeManager().findAllTxFeeRowCount(getIdentifier(),
						// // getChannelCodeItem().getCode(),
						// // getDescription());
						// } else {
						// return
						// getTransactionFeeManager().findAllTxFeeRowCount(getIdentifier(),
						// getChannelCodeItem().getCode());
						// }

						return getTransactionFeeManager().findAllTxFeeRowCount(getIdentifier(),
								getChannelCodeItem().getCode());
					}
					return 0;

				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}

			@Override
			public Iterator<TransactionFee> getCurrentPageRows(int nFirst, int nPageSize, ITableColumn objSortColumn,
					boolean bSortOrder) {
				try {
					if (getChannelCodeItem() != null) {
						List<TransactionFee> results = getTransactionFeeManager().findAllTxFee(getIdentifier(),
								getChannelCodeItem().getCode(), nFirst, nPageSize);

						int i = 1;
						for (TransactionFee trxFee : results) {
							trxFee.getId().setPriority(
									getPriorityManager().findByTransactionFeeCriteria(
											trxFee.getTransactions().getCode(), trxFee.getId().getProjectCodeTr(),
											trxFee.getId().getProductCodeTr()));
							trxFee.setNo(i);
							i++;
						}
						// List<TransactionFee> results =
						// getTransactionFeeManager().findAllTxFee(getIdentifier(),
						// getChannelCodeItem().getCode(), nFirst, nPageSize);

						Iterator<TransactionFee> rows = results.iterator();
						return rows;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return new ArrayList<TransactionFee>().iterator();
			}
		};
	}

	/**
	 * 
	 * @param priority
	 * @return
	 */
	private String packIdentifier(PriorityRouting priority) {
		String result = "";
		result = getMerchantGroup().getCode() + "#" + priority.getProjectCode() + "#" + priority.getProductCode();
		return result;
	}

	public void checkSelect(IRequestCycle cycle) {
		if (cycle.isRewinding()) {
			TransactionFee transactionFee = (TransactionFee) getLoopObject();
			if (transactionFee != null) {
				Set<TransactionFee> uniqueSet = getUniqueSet();
				if (transactionFee.getSelected()) {
					uniqueSet.add(transactionFee);
				} else {
					uniqueSet.remove(transactionFee);
				}
				setUniqueSet(uniqueSet);
			}
		}
	}

	/**
	 * Listener onSearch Button
	 * @return
	 */
	public IPage onSearch() {

		if (getChannelCodeItem() == null) {
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

		setRealRender(true);
		if (getTableModel().getRowCount() <= 0) {
			setErrormsg(getText("trxfee.message.error.search.notfound",
					new Object[] { getMerchantGroup().getCode(), getChannelCodeItem().getCode(), getDescription() }));
			// TransactionFeeNotFound tnF = getTransactionFeeNotFound();
			// tnF.setdName("Jenis Transaksi : " +
			// getChannelCodeItem().getConcatCodeName());
			// return tnF;
		}
		return null;
	}

	public IPage doEdit() {
		if (getDelegate().getHasErrors())
			return null;

		if (getUniqueSet().size() == 0) {
			addError(getDelegate(), (IFormComponent) null, getText("transactionFee.required"),
					ValidationConstraint.REQUIRED);
			return null;
		}
		
		PricingSchemeConfirm conf = getPricingSchemeConfirm();
		
		conf.setTransactionFee(getTrxFeeList(getUniqueSet()));
		conf.setFromPage(FromPage.UPDATE);
		
//		TransactionFeeConfirm transFeeDeleteConfirm = getTransactionFeeDeleteConfirm();
//		transFeeDeleteConfirm.setFromPage(FromPage.UPDATE);
//		prepareBeforeSubmit(transFeeDeleteConfirm);
//		transFeeDeleteConfirm.setStateMode(WebConstant.EDITOR_STATE_EDIT);
		return conf;
	}

	/**
	 * 
	 * @param uniqueSet
	 * @return
	 */
	private List<TransactionFee> getTrxFeeList(Set<TransactionFee> uniqueSet) {
		List<TransactionFee> list = new ArrayList<TransactionFee>();
		list.addAll(uniqueSet);
		return list;
	}

	public IPage doDelete() {
		if (getDelegate().getHasErrors())
			return null;

		if (getUniqueSet().size() == 0) {
			addError(getDelegate(), (IFormComponent) null, getText("transactionFee.required"),
					ValidationConstraint.REQUIRED);
			return null;
		}

		PricingSchemeDeleteConfirm conf = getPricingSchemeDeleteConfirm();
		
		conf.setTransactionFee(getTrxFeeList(getUniqueSet()));
		conf.setFromPage(FromPage.UPDATE);
		
//		TransactionFeeConfirm transFeeDeleteConfirm = getTransactionFeeDeleteConfirm();
//		transFeeDeleteConfirm.setFromPage(FromPage.UPDATE);
//		prepareBeforeSubmit(transFeeDeleteConfirm);
//		transFeeDeleteConfirm.setStateMode(WebConstant.EDITOR_STATE_DELETE);
		return conf;
	}

	protected void prepareBeforeSubmit(TransactionFeeConfirm transFeeDeleteConfirm) {
		transFeeDeleteConfirm.setTransactionFeeToConfirm(getUniqueSet());
		transFeeDeleteConfirm.setChannelCode(getChannelCodeItem());
		transFeeDeleteConfirm.setIdentifier(getIdentifier());
	}

	/**
	 * 
	 * @return
	 */
	private String getIdentifier() {
		StringBuilder result = new StringBuilder();
		if (getMerchantGroup() != null) {
			result.append(getMerchantGroup().getCode());
		}
		if (getCriteria() != null && getKeys() != null && !getKeys().equals("")) {
			result.append("%").append(getKeys());
		}

		return result.toString();
	}
}
