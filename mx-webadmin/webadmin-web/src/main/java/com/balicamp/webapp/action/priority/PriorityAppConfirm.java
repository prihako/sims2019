package com.balicamp.webapp.action.priority;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mx.PriorityRouting;
import com.balicamp.model.user.Approval;
import com.balicamp.model.user.User;
import com.balicamp.service.priority.PriorityManager;
import com.balicamp.service.user.ApprovalManager;
import com.balicamp.service.user.UserManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.constant.PriorityConstant.ApprovalStatus;
import com.balicamp.webapp.constant.PriorityConstant.ProcessFlag;
import com.balicamp.webapp.constant.PriorityConstant.UserType;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 *
 * @author snurma.wijayanti@gmail.com
 *
 */
public abstract class PriorityAppConfirm extends AdminBasePage {

	protected final static Logger LOGGER = Logger.getLogger(PriorityAppConfirm.class.getName());

	@InjectPage("priorityCommonNotif")
	public abstract PriorityCommonNotif getPriorityCommonNotif();

	@InjectPage("priorityAppList")
	public abstract PriorityAppList getPriorityAppList();

	@InjectSpring("approvalManager")
	public abstract ApprovalManager getApprovalManager();

	@InjectSpring("priorityManager")
	public abstract PriorityManager getPriorityManager();
	
	@InjectSpring("userManager")
	public abstract UserManager getUserManager();

	public abstract void setPriority(PriorityRouting priority);

	@Persist("client")
	public abstract PriorityRouting getPriority();

	public abstract void setDataApp(Approval appData);

	@Persist("client")
	public abstract Approval getDataApp();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	/**
	 *
	 * @return
	 */
	public IPage doApprove() {
		PriorityCommonNotif notif = getPriorityCommonNotif();

		notif.setTitle(getText("priority.title.app.notif"));
		
		if(!cekSpv()){
			notif.setMsgnotif(getText("trxfee.message.notif.app.spvfailed"));
			return notif;
		}
		
		if (getPriority() == null && getDataApp() == null)
			return null;

		boolean stat = doJob(getPriority(), getDataApp(), ApprovalStatus.APPROVE);

		if (stat) {
			notif.setMsgnotif(getText("priority.message.notif.app.success"));
		} else {
			notif.setMsgnotif(getText("priority.message.notif.app.failed"));
		}
		return notif;
	}

	/**
	 *
	 * @param priority
	 * @param dataApp
	 * @param approve
	 * @return
	 */
	private boolean doJob(PriorityRouting priority, Approval dataApp,
			int approve) {
		boolean stat = false;
		try {
			/* -- reminder rollback jika FAILED
			 * 1. UPDATE table t_approval
			 * 2. INSERT data ke table priority_routing
			 * */
			if(getApprovalManager().updateApprs(getDataApp(), getUserLoginFromSession(), ApprovalStatus.APPROVE)){
				if(doSaveOrDelete()){
					stat = getApprovalManager().commitEntity();
				} else {
					getApprovalManager().rollbackEntity();
				}
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(),  e);
		}
		return stat;
	}

	/**
	 * cek 
	 * @return
	 */
	private boolean doSaveOrDelete() {
		boolean stat = false;
		boolean delete = false;
		
		if(getDataApp().getProcessFlag() == ProcessFlag.DELETE){ //delete
			delete = true;
		}
		
		if(delete){
			Set<PriorityRouting> data = new TreeSet<PriorityRouting>();
			data.add(getPriority());
			stat = getPriorityManager().deletePriorities(data);
		} else {
			stat = getPriorityManager().saveOrUpdatePriority(getPriority(),isNewData());
		}		
		return stat;
	}

	/**
	 *
	 * @return
	 */
	private boolean isNewData() {
		boolean stat = true;
		if(getPriorityManager().findById(getPriority().getId()) != null){
			stat = false;
		}
		return stat;
	}

	/**
	 * REJECT
	 * @return
	 */
	public IPage doReject() {
		PriorityCommonNotif notif = getPriorityCommonNotif();

		notif.setTitle(getText("priority.title.app.notif"));

		if(!cekSpv()){
			notif.setMsgnotif(getText("trxfee.message.notif.app.spvfailed"));
			return notif;
		}
		
		if (getPriority() == null && getDataApp() == null)
			return null;

		boolean stat = doReject(getPriority(), getDataApp(), ApprovalStatus.REJECT);

		if (stat) {
			notif.setMsgnotif(getText("priority.message.notif.reject.success"));
		} else {
			notif.setMsgnotif(getText("priority.message.notif.reject.failed"));
		}
		return notif;
	}

	/**
	 *
	 * @param priority
	 * @param dataApp
	 * @param reject
	 * @return
	 */
	private boolean doReject(PriorityRouting priority, Approval dataApp,
			int reject) {
		boolean stat = false;
		try {
			/* 
			 * 1. UPDATE table t_approval
			 *
			 * */
			if(getApprovalManager().updateApprs(getDataApp(), getUserLoginFromSession(), ApprovalStatus.REJECT)){
				stat = getApprovalManager().commitEntity();
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(),  e);
		}
		return stat;
	}

	public IPage onBack() {
		return getPriorityAppList();
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean cekSpv() {
		boolean stat = false;
		try {
			User spv = getUserLoginFromSession();
			
			if(spv.getUserParentId().compareTo(UserType.ROOT) == 0){
				stat = true;
			} else {
				User userId = getUserManager().findById(getDataApp().getCreatedBy());
				if(userId.getUserParentId().compareTo(spv.getId()) == 0){
					stat = true;
				} 
			}			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return stat;
	}
}
