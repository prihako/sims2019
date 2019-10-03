package com.balicamp.webapp.action.bankadmin.bank;

import java.util.HashSet;
import java.util.Set;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mx.Bank;
import com.balicamp.service.BankManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.customerservice.Information;

public abstract class DeleteBankConfirmation extends AdminBasePage implements PageBeginRenderListener {

	@InjectObject("spring:bankManager")
	public abstract BankManager getBankManager();

	@InjectPage("information")
	public abstract Information getInformation();

	@InjectPage("viewBank")
	public abstract ViewBank getViewBank();

	public abstract void setBankToDelete(Set<Bank> bankToDelete);

	public abstract Set<Bank> getBankToDelete();

	public IPage doDelete() {
		if (getBankToDelete() == null)
			return null;

		for (Bank bank : getBankToDelete()) {
			getBankManager().delete(bank);
		}

		Information userInformation = getInformation();
		userInformation.setInformationTitle(getText("userInformation.titleBankDelete"));
		userInformation.setInformationBody(getText("userInformation.informationBankDelete"));
		return userInformation;
	}

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		if (getBankToDelete() == null) {
			setBankToDelete(new HashSet<Bank>());
		}
	}

	public IPage doCancel() {
		ViewBank viewBank = getViewBank();
		return viewBank;
	}
}
