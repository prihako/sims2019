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
import com.balicamp.webapp.constant.PriorityConstant.FromPage;
import com.balicamp.webapp.constant.PriorityConstant.ProcessFlag;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * 
 * @author snurma.wijayanti
 * 
 */
public abstract class PricingSchemeConfirm extends BasePageList implements
		PageBeginRenderListener {

	private static final Logger LOGGER = Logger
			.getLogger(PricingSchemeConfirm.class.getSimpleName());

	public abstract void setFromPage(String fromPage);

	@Persist("client")
	public abstract String getFromPage();

	@InjectPage("priorityCommonNotif")
	public abstract PriorityCommonNotif getPriorityCommonNotif();

	@InjectPage("pricingSchemeEntry")
	public abstract PricingSchemeEntry getPricingSchemeEntry();

	@InjectPage("pricingSchemeList")
	public abstract PricingSchemeList getPricingSchemeList();

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
					constructTransactionFee(), getUserLoginFromSession(), ProcessFlag.UPDATE);

			// contruct status page
			PriorityCommonNotif notif = getPriorityCommonNotif();
			notif.setTitle(getText("trxfee.title.notif"));
			if (stat) {
				if (getFromPage() != null
						&& getFromPage().equals(FromPage.UPDATE)) {
					notif.setMsgnotif(getText("trxfee.message.notif.edit.success"));
				} else {
					notif.setMsgnotif(getText("trxfee.message.notif.add.success"));
				}
			} else {
				if (getFromPage() != null
						&& getFromPage().equals(FromPage.UPDATE)) {
					notif.setMsgnotif(getText("trxfee.message.notif.edit.failed"));
				} else {
					notif.setMsgnotif(getText("trxfee.message.notif.add.failed"));
				}
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
		for (TransactionFee trxFee : getTransactionFee()) {
			trxFee.getId().setPriority(
					getPriorityManager().findByTransactionFeeCriteria(
							trxFee.getTransactions().getCode(),
							trxFee.getId().getProjectCodeTr(),
							trxFee.getId().getProductCodeTr()));
		}
		return getTransactionFee();
	}

	/**
	 * 
	 * @return
	 */
	public IPage doCancel() {
		if (getFromPage() != null && getFromPage().equals(FromPage.UPDATE)) {
			return getPricingSchemeList();
		} else {
			return getPricingSchemeEntry();
		}
	}
}
