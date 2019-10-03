package com.balicamp.webapp.action.bankadmin.bank;

import java.util.Date;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;

import com.balicamp.model.mx.Bank;
import com.balicamp.service.BankManager;
import com.balicamp.webapp.action.BasePageForm;
import com.balicamp.webapp.action.customerservice.Information;

public abstract class AddBankConfirmation extends BasePageForm<Bank, Long> implements PageBeginRenderListener {

	@InjectObject("spring:bankManager")
	public abstract BankManager getBankManager();

	@InjectPage("addBankInput")
	public abstract AddBankInput getAddBankInput();

	@InjectPage("information")
	public abstract Information getInformation();

	public IPage onBack() {
		AddBankInput addBankInput = getAddBankInput();
		addBankInput.setEntity(getEntity());
		return addBankInput;
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

		getBankManager().saveOrUpdate(getEntity());

		Information userInformation = getInformation();
		if (isNew) {
			userInformation.setInformationTitle(getText("userInformation.titleBankAdd"));
			userInformation.setInformationBody(getText("userInformation.informationBankAdd"));
		} else {
			userInformation.setInformationTitle(getText("userInformation.titleBankEdit"));
			userInformation.setInformationBody(getText("userInformation.informationBankEdit"));
		}
		return userInformation;
	}

	public IPage onButtonCancel() {
		AddBankInput addBankInput = getAddBankInput();
		addBankInput.setEntity(getEntity());
		return addBankInput;
	}

}
