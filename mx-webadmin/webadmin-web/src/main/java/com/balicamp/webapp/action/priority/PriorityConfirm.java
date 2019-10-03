package com.balicamp.webapp.action.priority;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mx.PriorityRouting;
import com.balicamp.service.priority.PriorityManager;
import com.balicamp.service.user.ApprovalManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.constant.PriorityConstant.FromPage;
import com.balicamp.webapp.constant.PriorityConstant.ProcessFlag;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * <a href="mailto:snurma.wijayanti@gmail.com">antin</a>
 * @version 
 */
public abstract class PriorityConfirm extends AdminBasePage implements PageBeginRenderListener {

	protected final static Logger log = Logger.getLogger(PriorityConfirm.class.getName());

	public abstract void setErrormsg(String errormsg);

	@Persist("client")
	public abstract String getErrormsg();

	public abstract void setPriorityRouting(PriorityRouting priorityRouting);

	@Persist("client")
	public abstract PriorityRouting getPriorityRouting();

	public abstract void setTransactionCode(String transactionCode);

	@Persist("client")
	public abstract String getTransactionCode();

	public abstract void setProjectCode(String projectCode);

	@Persist("client")
	public abstract String getProjectCode();

	public abstract void setDescription(String description);

	@Persist("client")
	public abstract String getDescription();

	public abstract void setProductCode(String productCode);

	@Persist("client")
	public abstract String getProductCode();

	public abstract void setRoutingCode(String routingCode);

	@Persist("client")
	public abstract String getProductCodeExt();

	public abstract void setProductCodeExt(String productCodeExt);

	@Persist("client")
	public abstract String getFromPage();

	public abstract void setFromPage(String fromPage);

	@Persist("client")
	public abstract String getRoutingCode();

	@Persist("client")
	public abstract String getGlCode();

	public abstract void setGlCode(String glCode);

	@InjectPage("priorityEntry")
	public abstract PriorityEntry getPriorityEntry();

	@InjectPage("priorityList")
	public abstract PriorityList getPriorityList();

	@InjectPage("priorityCommonNotif")
	public abstract PriorityCommonNotif getPriorityCommonNotif();

	@InjectSpring("priorityManager")
	public abstract PriorityManager getPriorityManager();

	@InjectSpring("approvalManager")
	public abstract ApprovalManager getApprovalManager();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	public IPage onBack(IRequestCycle cycle) {

		if (getFromPage() != null && getFromPage().equals(FromPage.UPDATE)) {
			return getPriorityList();
		}
		return getPriorityEntry();
	}

	/**
	 * 
	 * @param cycle
	 * @return
	 */
	public IPage doSubmit(IRequestCycle cycle) {
		try {

			if (getDelegate().getHasErrors()) {
				return null;
			}
			// cekDoubleSubmit();
			if (getDelegate().getHasErrors()) {
				return null;
			}

			// save DB staging : t_approval
			PriorityRouting newdata = getEntityData();
			
			if(productCodeExist(newdata) && (getFromPage() == null || (getFromPage() != null && !getFromPage().equals(FromPage.UPDATE)))){
				setErrormsg(getText("priority.error.entry.productcode.exist"));
				return null;
			} 
			
			boolean stat = getApprovalManager().saveOrUpdatePriority(newdata, getUserLoginFromSession(), ProcessFlag.UPDATE);

			// contruct status page
			PriorityCommonNotif notif = getPriorityCommonNotif();
			notif.setTitle(getText("priority.title.confirm"));
			if (stat) {
				notif.setMsgnotif(getText("priority.message.notif.add.success"));
			} else {
				notif.setMsgnotif(getText("priority.message.notif.add.failed"));
			}
			return notif;
		} catch (Exception e) {
			log.log(Level.SEVERE, e.toString(), e);
		}
		return null;
	}
	
	private boolean productCodeExist(PriorityRouting newData){
		boolean stat = false;
		List<PriorityRouting> exist = getPriorityManager().findByProductCode(newData);
		if(exist != null && exist.size() > 0){
			stat = true;
		}
		return stat;
	}

	/**
	 * 
	 * @param newdata
	 * @return
	 */
	private PriorityRouting getEntityData() {
		PriorityRouting entity;

		if (getFromPage() != null && getFromPage().equals(FromPage.UPDATE)) {
			entity = getPriorityRouting();
		} else {
			entity = new PriorityRouting();
			entity.setId(getPriorityManager().getNextSeq());
			entity.setActiveStatus(true);
		}

		entity.setTransactionCode(getTransactionCode());
		entity.setProjectCode(getProjectCode());
		entity.setDescription(getDescription());
		entity.setProductCode(getProductCode());
		entity.setRoutingCode(getRoutingCode());
		entity.setProductCodeExt(getProductCodeExt());

		entity.setGlCode(getGlCode());
		return entity;
	}
}