package com.balicamp.webapp.action.bankadmin.user.key;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;

import com.balicamp.model.mx.MxParm;
import com.balicamp.service.MxParmManager;
import com.balicamp.webapp.action.BasePageForm;
import com.balicamp.webapp.action.customerservice.Information;

public abstract class AddKeyConfirmation extends BasePageForm<MxParm, Long> implements PageBeginRenderListener {

	@InjectObject("spring:mxParmManager")
	public abstract MxParmManager getMxParmManager();

	@InjectPage("addMxParmInput")
	public abstract AddKeyInput getAddKeyInput();

	@InjectPage("information")
	public abstract Information getInformation();

	public IPage onBack() {
		AddKeyInput addMxParmInput = getAddKeyInput();
		addMxParmInput.setEntity(getEntity());
		return addMxParmInput;
	}

	public IPage doButtonSave(IRequestCycle cycle) {
		boolean isNew = getEntity().getId() == null;

		getMxParmManager().saveOrUpdate(getEntity());

		Information userInformation = getInformation();
		if (isNew) {
			userInformation.setInformationTitle(getText("userInformation.titleMxParmAdd"));
			userInformation.setInformationBody(getText("userInformation.informationMxParmAdd"));
		} else {
			userInformation.setInformationTitle(getText("userInformation.titleMxParmEdit"));
			userInformation.setInformationBody(getText("userInformation.informationMxParmEdit"));
		}
		return userInformation;
	}

	public IPage onButtonCancel() {
		AddKeyInput addMxParmInput = getAddKeyInput();
		addMxParmInput.setEntity(getEntity());
		return addMxParmInput;
	}

}
