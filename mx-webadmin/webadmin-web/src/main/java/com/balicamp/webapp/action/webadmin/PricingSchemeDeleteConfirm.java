package com.balicamp.webapp.action.webadmin;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mx.TransactionFee;
import com.balicamp.service.TransactionFeeManager;
import com.balicamp.service.priority.PriorityManager;
import com.balicamp.service.user.ApprovalManager;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.action.priority.PriorityCommonNotif;
import com.balicamp.webapp.constant.PriorityConstant.ProcessFlag;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class PricingSchemeDeleteConfirm extends BasePageList implements PageBeginRenderListener  {

	private static final Logger LOGGER = Logger
			.getLogger(PricingSchemeDeleteConfirm.class.getSimpleName());
	
	public abstract void setMsgNotif(String msgNotif);

	@Persist("client")
	public abstract String getMsgNotif();
	

	public abstract void setFromPage(String fromPage);

	@Persist("client")
	public abstract String getFromPage();

	@InjectPage("priorityCommonNotif")
	public abstract PriorityCommonNotif getPriorityCommonNotif();
	
	@InjectPage("pricingSchemeList")
	public abstract PricingSchemeList getPricingSchemeList();

	@InjectPage("pricingSchemeEntry")
	public abstract PricingSchemeEntry getPricingSchemeEntry();

	@InjectSpring("transactionFeeManagerImpl")
	public abstract TransactionFeeManager getTransactionFeeManager();

	@InjectSpring("approvalManager")
	public abstract ApprovalManager getApprovalManager();
	
	@InjectSpring("priorityManager")
	public abstract PriorityManager getPriorityManager();

	private List<TransactionFee> transactionFee;

	public void setTransactionFee(List<TransactionFee> txFee) {
		this.transactionFee = txFee;
	}

	public List<TransactionFee> getTransactionFee() {
		return transactionFee;
	}

	@Override
	public abstract Object getLoopObject();

	@Override
	public abstract void setLoopObject(Object object);

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		setMsgNotif(getText("trxfee.message.conf.delete"));
	}

	/**
	 *
	 * @return
	 */
	@Override
	public IBasicTableModel getTableModel() {
		return new IBasicTableModel() {

			@Override
			public int getRowCount() {
				return getTransactionFee().size();
			}

			@Override
			public Iterator<TransactionFee> getCurrentPageRows(int nFirst,
					int nPageSize, ITableColumn objSortColumn,
					boolean bSortOrder) {
				return getTransactionFee().iterator();
			}
		};
	}

	public void setTableModel(IBasicTableModel model) {
	}

	/**
	 * save ke table t_approval
	 *
	 * @return
	 */
	public IPage doSubmit() {
		try {
			if (getDelegate().getHasErrors()) {
				return null;
			}
			// cekDoubleSubmit();
			if (getDelegate().getHasErrors()) {
				return null;
			}

			boolean stat = getApprovalManager().saveOrUpdatePricing(
					constructTransactionFee(), getUserLoginFromSession(), ProcessFlag.DELETE);

			// contruct status page
			PriorityCommonNotif notif = getPriorityCommonNotif();
			notif.setTitle(getText("trxfee.title.notif"));
			if (stat) {
				notif.setMsgnotif(getText("trxfee.message.notif.del.success"));
			} else {
				notif.setMsgnotif(getText("trxfee.message.notif.del.failed"));
			}
			return notif;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return null;

	}

	/**
	 * 
	 * @return
	 */
	private List<TransactionFee> constructTransactionFee() {
		for(TransactionFee trxFee : getTransactionFee()){
			trxFee.getId().setPriority(getPriorityManager().findByTransactionFeeCriteria(
					trxFee.getTransactions().getCode(), trxFee.getId().getProjectCodeTr(),
					trxFee.getId().getProductCodeTr()));
		}
		return getTransactionFee();
	}

	/**
	 *
	 * @return
	 */
	public IPage doCancel(){
		return  getPricingSchemeList();
	}
}
