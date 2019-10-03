package com.balicamp.webapp.action.bankadmin.dayOff;

import java.util.HashSet;
import java.util.Set;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mx.DayOff;
import com.balicamp.service.DayOffManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.customerservice.Information;

public abstract class DeleteDayOffConfirmation extends AdminBasePage implements PageBeginRenderListener {

	@InjectObject("spring:dayOffManager")
	public abstract DayOffManager getDayOffManager();

	@InjectPage("information")
	public abstract Information getInformation();

	@InjectPage("viewDayOff")
	public abstract ViewDayOff getViewDayOff();

	public abstract void setDayOffToDelete(Set<DayOff> dayOffToDelete);

	public abstract Set<DayOff> getDayOffToDelete();

	public IPage doDelete() {
		if (getDayOffToDelete() == null)
			return null;

		for (DayOff dayOff : getDayOffToDelete()) {
			getDayOffManager().delete(dayOff);
		}

		Information userInformation = getInformation();
		userInformation.setInformationTitle(getText("userInformation.titleDayOffDelete"));
		userInformation.setInformationBody(getText("userInformation.informationDayOffDelete"));
		return userInformation;
	}

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		if (getDayOffToDelete() == null) {
			setDayOffToDelete(new HashSet<DayOff>());
		}
	}

	public IPage doCancel() {
		ViewDayOff viewDayOff = getViewDayOff();
		return viewDayOff;
	}
}
