package com.balicamp.webapp.action.bankadmin.bank;

import java.util.HashSet;
import java.util.Set;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.ValidationConstraint;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mx.Bank;
import com.balicamp.service.BankManager;
import com.balicamp.service.GenericManager;
import com.balicamp.util.CommonUtil;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.tapestry.NewTableModel;

public abstract class ViewBank extends BasePageList implements PageBeginRenderListener, PageEndRenderListener {

	@InjectObject("spring:bankManager")
	public abstract BankManager getBankManager();

	@InjectPage("deleteBankConfirmation")
	public abstract DeleteBankConfirmation getDeleteBankConfirmation();

	@InjectPage("addBankInput")
	public abstract AddBankInput getAddBankInput();

	public abstract Bank getSearchBank();

	public abstract void setSearchBank(Bank searchBank);

	private Set<Bank> selectedBank;

	public void setSelectedBank(Set<Bank> selectedBank) {
		this.selectedBank = selectedBank;
	}

	public Set<Bank> getSelectedBank() {
		return selectedBank;
	}

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		populateSessionPropertyValue();

		if (getSearchBank() == null)
			setSearchBank(new Bank());
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);

		// save last state to session
		syncSessionPropertyValue(new String[] { "searchBank" });
	}

	public IBasicTableModel getBankListTableModel() {
		return super.getTableModel();
	}

	public IBasicTableModel createTableModel() {
		SearchCriteria searchCriteria = SearchCriteria.createSearchCriteria("bank");

		if (!CommonUtil.isEmpty(getSearchBank().getBankCode()))
			searchCriteria.addCriterion(Restrictions.ilike("bankCode", getSearchBank().getBankCode(),
					MatchMode.ANYWHERE));
		if (!CommonUtil.isEmpty(getSearchBank().getBankName()))
			searchCriteria.addCriterion(Restrictions.ilike("bankName", getSearchBank().getBankName(),
					MatchMode.ANYWHERE));
		if (!CommonUtil.isEmpty(getSearchBank().getBinPrefix()))
			searchCriteria.addCriterion(Restrictions.ilike("binPrefix", getSearchBank().getBinPrefix(),
					MatchMode.ANYWHERE));
		searchCriteria.addOrder(Order.asc("bankName"));
		return new NewTableModel<Bank>((GenericManager) getBankManager(), searchCriteria, emptyOrderMaping);
	}

	public void checkSelect(IRequestCycle cycle) {
		if (cycle.isRewinding()) {
			Bank Bank = (Bank) getLoopObject();
			if (Bank != null && Bank.getSelected()) {
				if (selectedBank == null) {
					setSelectedBank(new HashSet<Bank>());
				}
				selectedBank.add(Bank);
			}
		}
	}

	/**
	 * Listener search button
	 * @return
	 */
	public IPage onSearch() {
		// do nothing just refresh
		return null;
	}

	public IPage onViewDetail(Long id) {
		AddBankInput addBank = getAddBankInput();
		Bank bank = getBankManager().findById(id);
		addBank.setEntity(bank);
		return addBank;
	}

	public IPage doDelete() {
		if (selectedBank == null || selectedBank.size() == 0) {
			addError(getDelegate(), (IFormComponent) null, getText("common.message.validation.noDataSelected"),
					ValidationConstraint.CONSISTENCY);
			return null;
		}
		if (getDelegate().getHasErrors())
			return null;

		DeleteBankConfirmation deleteBankConfirmation = getDeleteBankConfirmation();
		deleteBankConfirmation.setBankToDelete(selectedBank);
		return deleteBankConfirmation;
	}
}
