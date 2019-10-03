package com.balicamp.webapp.action.bankadmin.dayOff;

import java.util.Date;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;

import com.balicamp.model.mx.DayOff;
import com.balicamp.service.DayOffManager;
import com.balicamp.webapp.action.BasePageForm;
import com.balicamp.webapp.action.customerservice.Information;

public abstract class AddDayOffConfirmation extends BasePageForm<DayOff, Long> implements PageBeginRenderListener {

	@InjectObject("spring:dayOffManager")
	public abstract DayOffManager getDayOffManager();

	@InjectPage("addDayOffInput")
	public abstract AddDayOffInput getAddDayOffInput();

	@InjectPage("information")
	public abstract Information getInformation();

	public IPage onBack() {
		AddDayOffInput addDayOffInput = getAddDayOffInput();
		addDayOffInput.setEntity(getEntity());
		return addDayOffInput;
	}

	public IPage doButtonSave(IRequestCycle cycle) {
		boolean isNew = getEntity().getId() == null;

		getEntity().setUpdatedBy(getUserLoginFromSession());
		getEntity().setUpdatedDate(new Date());
		if (isNew) {
			getEntity().setCreatedBy(getUserLoginFromSession());
			getEntity().setCreatedDate(new Date());
		} else {

		}

		getDayOffManager().saveOrUpdate(getEntity());

		Information userInformation = getInformation();
		if (isNew) {
			userInformation.setInformationTitle(getText("userInformation.titleDayOffAdd"));
			userInformation.setInformationBody(getText("userInformation.informationDayOffAdd"));
		} else {
			userInformation.setInformationTitle(getText("userInformation.titleDayOffEdit"));
			userInformation.setInformationBody(getText("userInformation.informationDayOffEdit"));
		}
		return userInformation;
	}

	public IPage onButtonCancel() {
		AddDayOffInput addDayOffInput = getAddDayOffInput();
		addDayOffInput.setEntity(getEntity());
		return addDayOffInput;
	}

}
