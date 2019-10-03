package com.balicamp.webapp.action.priority;

import java.util.Set;
import java.util.logging.Logger;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mx.PriorityRouting;
import com.balicamp.service.priority.PriorityManager;
import com.balicamp.service.user.ApprovalManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.constant.PriorityConstant.ProcessFlag;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class PriorityDeleteConfirm extends AdminBasePage {

	protected final static Logger LOGGER = Logger.getLogger(PriorityDeleteConfirm.class.getName());

	@InjectPage("priorityList")
	public abstract PriorityList getPriorityList();

	@InjectPage("priorityCommonNotif")
	public abstract PriorityCommonNotif getPriorityCommonNotif();

	@InjectSpring("priorityManager")
	public abstract PriorityManager getPriorityManager();
	
	@InjectSpring("approvalManager")
	public abstract ApprovalManager getApprovalManager();

	public abstract void setSelectedData(Set<PriorityRouting> selectedData);

	public abstract Set<PriorityRouting> getSelectedData();

	public abstract void setPriority(PriorityRouting priority);

	public abstract PriorityRouting getPriority();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	/**
	 * save ke table approval
	 * @return
	 */
	public IPage doDelete() {
		if (getSelectedData() == null)
			return null;

//		boolean stat = getPriorityManager().deletePriorities(getSelectedData());
		
		boolean stat = getApprovalManager().deleteAllPriorities(getSelectedData(), getUserLoginFromSession(), ProcessFlag.DELETE);

		PriorityCommonNotif notif = getPriorityCommonNotif();

		notif.setTitle(getText("leftMenu.priority.crudParameter.list"));

		if (stat) {
			notif.setMsgnotif(getText("priority.message.notif.delete.success"));
		} else {
			notif.setMsgnotif(getText("priority.message.notif.delete.failed"));
		}
		return notif;
	}
}
