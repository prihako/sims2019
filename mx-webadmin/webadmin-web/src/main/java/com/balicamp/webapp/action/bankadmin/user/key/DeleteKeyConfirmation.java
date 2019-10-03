package com.balicamp.webapp.action.bankadmin.user.key;

import java.util.HashSet;
import java.util.Set;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mx.MxParm;
import com.balicamp.service.MxParmManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.customerservice.Information;

public abstract class DeleteKeyConfirmation extends AdminBasePage implements PageBeginRenderListener {

	@InjectObject("spring:mxParmManager")
	public abstract MxParmManager getMxParmManager();

	@InjectPage("information")
	public abstract Information getInformation();

	@InjectPage("viewKey")
	public abstract ViewKey getViewKey();

	public abstract void setMxParmToDelete(Set<MxParm> mxParmToDelete);

	public abstract Set<MxParm> getMxParmToDelete();

	public IPage doDelete() {
		if (getMxParmToDelete() == null)
			return null;

		for (MxParm mxParm : getMxParmToDelete()) {
			getMxParmManager().delete(mxParm);
		}

		Information userInformation = getInformation();
		userInformation.setInformationTitle(getText("userInformation.titleMxParmDelete"));
		userInformation.setInformationBody(getText("userInformation.informationMxParmDelete"));
		return userInformation;
	}

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		if (getMxParmToDelete() == null) {
			setMxParmToDelete(new HashSet<MxParm>());
		}
	}

	public IPage doCancel() {
		ViewKey userList = getViewKey();
		return userList;
	}
}
