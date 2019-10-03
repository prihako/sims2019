/**
 * 
 */
package com.balicamp.webapp.action.webadmin;

import java.util.HashMap;
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
import com.balicamp.model.user.Approval;
import com.balicamp.model.user.User;
import com.balicamp.service.TransactionFeeManager;
import com.balicamp.service.priority.PriorityManager;
import com.balicamp.service.user.ApprovalManager;
import com.balicamp.service.user.UserManager;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.action.priority.PriorityCommonNotif;
import com.balicamp.webapp.constant.PriorityConstant.ApprovalStatus;
import com.balicamp.webapp.constant.PriorityConstant.ProcessFlag;
import com.balicamp.webapp.constant.PriorityConstant.UserType;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * @author antin
 * 
 */
public abstract class PricingAppConfirm extends BasePageList implements
		PageBeginRenderListener {
	private static final Logger LOGGER = Logger
			.getLogger(PricingAppConfirm.class.getSimpleName());

	@InjectPage("priorityCommonNotif")
	public abstract PriorityCommonNotif getPriorityCommonNotif();

	@InjectPage("pricingAppList")
	public abstract PricingAppList getPricingAppList();	

	@InjectSpring("approvalManager")
	public abstract ApprovalManager getApprovalManager();
	
	@InjectSpring("transactionFeeManagerImpl")
	public abstract TransactionFeeManager getTransactionFeeManager();

	@InjectSpring("priorityManager")
	public abstract PriorityManager getPriorityManager();
	
	@InjectSpring("userManager")
	public abstract UserManager getUserManager();

	private List<TransactionFee> transactionFee;

	public void setTransactionFee(List<TransactionFee> txFee) {
		this.transactionFee = txFee;
	}

	public List<TransactionFee> getTransactionFee() {
		return transactionFee;
	}

	@Persist("client")
	public abstract List<Approval> getDataApproval();

	public abstract void setDataApproval(List<Approval> dataApproval);

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
	 * proses:
	 * 1. cek spv (apakah user berhak melakukan proses approval)
	 * 2. do Approve
	 * @return
	 */
	public IPage doApprove() {
		PriorityCommonNotif notif = getPriorityCommonNotif();
		notif.setTitle(getText("trxfee.title.app.notif"));
		
		if(!cekSpv()){
			notif.setMsgnotif(getText("trxfee.message.notif.app.spvfailed"));
			return notif;
		}
		
		if (getTransactionFee() == null && getDataApproval() == null)
			return null;

		boolean stat = doJob(getTransactionFee(), getDataApproval(),
				ApprovalStatus.APPROVE);	

		if (stat) {
			notif.setMsgnotif(getText("trxfee.message.notif.app.success"));
		} else {
			notif.setMsgnotif(getText("trxfee.message.notif.app.failed"));
		}
		return notif;
	}

	/**
	 * compare parent id from user whom created the approval data with user login
	 * @return
	 */
	private boolean cekSpv() {
		boolean stat = false;
		try {
			User spv = getUserLoginFromSession();
			
			if(spv.getUserParentId().compareTo(UserType.ROOT) == 0){
				stat = true;
			} else {
				for(Approval app:getDataApproval()){
					User userId = getUserManager().findById(app.getCreatedBy());
					if(userId.getUserParentId().compareTo(spv.getId()) == 0){
						stat = true;
					} else {
						stat = false;
						break;
					}
				}
			}			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return stat;
	}

	/**
	 * 
	 * @param transactionFee
	 * @param dataApproval
	 * @param approve
	 * @return
	 * step:
	 * 1. update/save data approval without commit
	 * 2. save ke table pricing
	 * 3. JIKA save pricing gagal >> rollback data approval; JIKA save pricing berhasil >> commit data approval
	 * 
	 */
	private boolean doJob(List<TransactionFee> transactionFee,
			List<Approval> dataApproval, int approve) {
		boolean stat = false;
		try {
			/*
			 * -- reminder rollback jika FAILED 1. UPDATE table t_approval 2.
			 * INSERT data ke table transaction_fee
			 */
			if (getApprovalManager().updateApprs(getDataApproval(),	getUserLoginFromSession(), ApprovalStatus.APPROVE)) {				
				stat = doSaveOrDeleteTrxFee();		
				
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return stat;
	}

	/**
	 * cek proces nya save or delete
	 * untuk memindahkan data musti di cek dia approval delete/save
	 * JIKA delete >> hapus dr table master  (tabel pricing)
	 * JIKA save/update >> pindahin/update table master (tabel pricing)
	 */
	private boolean doSaveOrDeleteTrxFee() {
		boolean stat = false;
		/**
		 * cek mau save atau delete
		 */
		boolean delete = false;
		for(Approval app:getDataApproval()){
			if(app.getProcessFlag() == ProcessFlag.DELETE){
				delete = true;
				break;
			}
		}
		
		if(delete){
			if(deleteTransactionFees()) {
				stat = getApprovalManager().commitEntity();
			} else {
				getApprovalManager().rollbackEntity();
			}
		} else {
			if (saveTransactionFees()) {
				stat = getApprovalManager().commitEntity();
			} else {
				getApprovalManager().rollbackEntity();
			}
		}		
		return stat;
	}
	
	/**
	 * clear enough
	 * @return
	 */
	private boolean deleteTransactionFees() {
		boolean stat = false;
		try {
			getTransactionFeeManager().deleteAll(getTransactionFee());
			stat = true;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return stat;
	}

	/**
	 * clear enough
	 * @return
	 */
	private boolean saveTransactionFees() {
		boolean stat = false;
		try {
			getTransactionFeeManager().saveOrUpdateAll(constructTrxFee());;
			stat = true;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return stat;
	}
	
	/**
	 * 
	 * @return
	 */
	private HashMap<TransactionFee, Boolean> constructTrxFee() {
		HashMap<TransactionFee, Boolean> data = new HashMap<TransactionFee, Boolean>();
		for(TransactionFee fee: getTransactionFee()){
			TransactionFee exist = getTransactionFeeManager().findTransactionFee(fee.getId().getTransactionId(), fee.getEndpoints().getCode(), fee.getId().getIdentifier());
			if(exist != null){
				data.put(fee, Boolean.TRUE);
			} else {
				data.put(fee, Boolean.FALSE);
			}
		}
		return data;
	}

	/**
	 * REJECT
	 * 
	 * @return
	 */
	public IPage doReject() {
		PriorityCommonNotif notif = getPriorityCommonNotif();

		notif.setTitle(getText("priority.title.app.notif"));
		
		if(!cekSpv()){
			notif.setMsgnotif(getText("trxfee.message.notif.app.spvfailed"));
			return notif;
		}
		
		if (getTransactionFee() == null && getDataApproval() == null)
			return null;

		boolean stat = doReject(getTransactionFee(), getDataApproval(),
				ApprovalStatus.REJECT);

		if (stat) {
			notif.setMsgnotif(getText("priority.message.notif.reject.success"));
		} else {
			notif.setMsgnotif(getText("priority.message.notif.reject.failed"));
		}
		return notif;
	}

	/**
	 * 
	 * @param transactionFee
	 * @param dataApproval
	 * @param reject
	 * @return
	 */
	private boolean doReject(List<TransactionFee> transactionFee,
			List<Approval> dataApproval, int reject) {
		boolean stat = false;
		try {
			/*
			 * 1. UPDATE table t_approval
			 */
			if (getApprovalManager().updateApprs(getDataApproval(),
					getUserLoginFromSession(), ApprovalStatus.REJECT)) {
				stat = getApprovalManager().commitEntity();
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return stat;
	}

	public IPage onBack() {
		return getPricingAppList();
	}
}
