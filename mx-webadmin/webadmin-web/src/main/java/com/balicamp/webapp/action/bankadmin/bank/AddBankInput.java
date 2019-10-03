package com.balicamp.webapp.action.bankadmin.bank;

import java.util.List;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;
import org.hibernate.criterion.Restrictions;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mx.Bank;
import com.balicamp.service.BankManager;
import com.balicamp.webapp.action.BasePageForm;
import com.balicamp.webapp.tapestry.BooleanPropertySelectionModel;

public abstract class AddBankInput extends BasePageForm<Bank, Long> implements PageBeginRenderListener {

	@InjectObject("spring:bankManager")
	public abstract BankManager getBankManager();

	@InjectPage("addBankConfirmation")
	public abstract AddBankConfirmation getAddBankConfirmation();

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		initializedEntity(getBankManager(), Bank.class);

		if (isRealRender()) {
			if (getEntity().getId() != null) {

			}
		}

		if (getIsAltoOption() == null)
			setIsAltoOption(new BooleanPropertySelectionModel());
		if (getIsAtmbOption() == null)
			setIsAtmbOption(new BooleanPropertySelectionModel());
		if (getPriorityOption() == null) {
			setPriorityOption(new StringPropertySelectionModel(new String[] { "alto", "atmb" }));
		}
	}

	public IPage doButtonNext() {
		boolean isNew = getEntity().getId() == null;

		if (getEntity().getBankCode() == null) {
			addError(getDelegate(), "bankCode", getText("addBank.validation.bankCode.required"),
					ValidationConstraint.CONSISTENCY);
		}
		if (getEntity().getBankName() == null) {
			addError(getDelegate(), "bankName", getText("addBank.validation.bankName.required"),
					ValidationConstraint.CONSISTENCY);
		}
		if (getEntity().getBinPrefix() == null) {
			addError(getDelegate(), "binPrefix", getText("addBank.validation.binPrefix.required"),
					ValidationConstraint.CONSISTENCY);
		}
		if (getEntity().getIsAlto() == false && getEntity().getPriority().equalsIgnoreCase("alto")) {
			addError(getDelegate(), "priority", getText("addBank.validation.priority.inactive.alto"),
					ValidationConstraint.CONSISTENCY);
		}
		if (getEntity().getIsAtmb() == false && getEntity().getPriority().equalsIgnoreCase("atmb")) {
			addError(getDelegate(), "priority", getText("addBank.validation.priority.inactive.atmb"),
					ValidationConstraint.CONSISTENCY);
		}
		if (getDelegate().getHasErrors()) {
			return null;
		}

		/* check for existing name */
		SearchCriteria searchCriteria = new SearchCriteria("bank");
		searchCriteria.addCriterion(Restrictions.eq("bankCode", getEntity().getBankCode()));
		if (!isNew)
			searchCriteria.addCriterion(Restrictions.ne("id", getEntity().getId()));

		List<Bank> bankList = (List<Bank>) getBankManager().findByCriteria(searchCriteria, -1, -1);
		if (bankList != null && bankList.size() > 0) {
			addError(getDelegate(), "bankCode", getText("addBank.validation.bankCode.exists"),
					ValidationConstraint.CONSISTENCY);
		}

		String bin[] = getEntity().getBinPrefix().split(",");
		for (int i = 0; i < bin.length; i++) {
			searchCriteria = new SearchCriteria("bank");
			searchCriteria.addCriterion(Restrictions.ilike("binPrefix", "%" + bin[i] + "%"));

			if (!isNew)
				searchCriteria.addCriterion(Restrictions.ne("id", getEntity().getId()));
			List<Bank> prefixList = getBankManager().findByCriteria(searchCriteria, -1, -1);

			if (prefixList != null && prefixList.size() > 0) {
				addError(getDelegate(), "binPrefix", getText("addBank.validation.binPrefix.exists", bin[i]),
						ValidationConstraint.CONSISTENCY);
			}
		}

		if (getDelegate().getHasErrors()) {
			return null;
		}

		AddBankConfirmation addBankConfirmation = getAddBankConfirmation();
		addBankConfirmation.setEntity(getEntity());
		return addBankConfirmation;
	}

	public abstract BooleanPropertySelectionModel getIsAltoOption();

	public abstract void setIsAltoOption(BooleanPropertySelectionModel isAltoOption);

	public abstract BooleanPropertySelectionModel getIsAtmbOption();

	public abstract void setIsAtmbOption(BooleanPropertySelectionModel isAtmbOption);

	public abstract StringPropertySelectionModel getPriorityOption();

	public abstract void setPriorityOption(StringPropertySelectionModel priorityOption);
}
