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
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;
import org.hibernate.criterion.Order;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mx.Endpoints;
import com.balicamp.model.mx.TransactionFee;
import com.balicamp.service.EndpointsManager;
import com.balicamp.service.TransactionFeeManager;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.constant.WebConstant;
import com.balicamp.webapp.tapestry.GenericPropertySelectionModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionFeeList.java 505 2013-05-24 08:15:51Z rudi.sadria $
 */
public abstract class TransactionFeeList extends BasePageList implements PageBeginRenderListener {

	@InjectSpring("transactionFeeManagerImpl")
	public abstract TransactionFeeManager getTransactionFeeManager();

	@InjectSpring("endpointsManagerImpl")
	public abstract EndpointsManager getEndpointsManager();

	@InjectPage("transactionFeeNotFound")
	public abstract TransactionFeeNotFound getTransactionFeeNotFound();

	@InjectPage("transactionFeeConfirm")
	public abstract TransactionFeeConfirm getTransactionFeeDeleteConfirm();

	@Persist("client")
	public abstract String getIdentifier();

	public abstract void setIdentifier(String identifier);

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

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		if (getUniqueSet() == null)
			setUniqueSet(new HashSet<TransactionFee>());
		/*
		 * 3 Maret 2014
		 * set default just for ATMB 
		 * */
		 setIdentifier("ATMB");		
		super.pageBeginRender(pageEvent);
	}

	public void setTableModel(IBasicTableModel model) {
	}

	@Override
	public IBasicTableModel createTableModel() {
		return new IBasicTableModel() {

			@Override
			public int getRowCount() {
				try {
					if(getChannelCodeItem() == null){
						return getTransactionFeeManager().findAllTxFeeRowCount(getIdentifier(),
								null);
					} else {
						return getTransactionFeeManager().findAllTxFeeRowCount(getIdentifier(),
								getChannelCodeItem().getCode());
					}					
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}

			@Override
			public Iterator<TransactionFee> getCurrentPageRows(int nFirst, int nPageSize, ITableColumn objSortColumn,
					boolean bSortOrder) {
				try {
					if(getChannelCodeItem() == null){
						Iterator<TransactionFee> rows = getTransactionFeeManager().findAllTxFee(getIdentifier(),
								null, nFirst, nPageSize).iterator();
						return rows;
					} else {
						Iterator<TransactionFee> rows = getTransactionFeeManager().findAllTxFee(getIdentifier(),
								getChannelCodeItem().getCode(), nFirst, nPageSize).iterator();
						return rows;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return new ArrayList<TransactionFee>().iterator();
				}
			}
		};

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
		setRealRender(true);
		if (getTableModel().getRowCount() <= 0) {
			TransactionFeeNotFound tnF = getTransactionFeeNotFound();
			tnF.setdName("Jenis Transaksi : " + getChannelCodeItem().getConcatCodeName());
			return tnF;
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
		TransactionFeeConfirm transFeeDeleteConfirm = getTransactionFeeDeleteConfirm();
		prepareBeforeSubmit(transFeeDeleteConfirm);
		transFeeDeleteConfirm.setStateMode(WebConstant.EDITOR_STATE_EDIT);
		return transFeeDeleteConfirm;
	}

	public IPage doDelete() {
		if (getDelegate().getHasErrors())
			return null;

		if (getUniqueSet().size() == 0) {
			addError(getDelegate(), (IFormComponent) null, getText("transactionFee.required"),
					ValidationConstraint.REQUIRED);
			return null;
		}

		TransactionFeeConfirm transFeeDeleteConfirm = getTransactionFeeDeleteConfirm();
		prepareBeforeSubmit(transFeeDeleteConfirm);
		transFeeDeleteConfirm.setStateMode(WebConstant.EDITOR_STATE_DELETE);
		return transFeeDeleteConfirm;
	}

	protected void prepareBeforeSubmit(TransactionFeeConfirm transFeeDeleteConfirm) {
		transFeeDeleteConfirm.setTransactionFeeToConfirm(getUniqueSet());
		transFeeDeleteConfirm.setChannelCode(getChannelCodeItem());
		transFeeDeleteConfirm.setIdentifier(getIdentifier());
	}
}
