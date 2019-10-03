package com.balicamp.webapp.action.bankadmin.dayOff;

import java.util.List;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.valid.ValidationConstraint;
import org.hibernate.criterion.Restrictions;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mx.DayOff;
import com.balicamp.service.DayOffManager;
import com.balicamp.webapp.action.BasePageForm;

public abstract class AddDayOffInput extends BasePageForm<DayOff, Long> implements PageBeginRenderListener {

	@InjectObject("spring:dayOffManager")
	public abstract DayOffManager getDayOffManager();

	@InjectPage("addDayOffConfirmation")
	public abstract AddDayOffConfirmation getAddDayOffConfirmation();

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		initializedEntity(getDayOffManager(), DayOff.class);

		if (isRealRender()) {
			if (getEntity().getId() != null) {

			}
		}
	}

	public IPage doButtonNext() {
		boolean isNew = getEntity().getId() == null;

		if (getEntity().getDescription() == null) {
			addError(getDelegate(), "description", getText("dayOff.validation.description.required"),
					ValidationConstraint.CONSISTENCY);
		}
		if (getEntity().getNonBusinessDate() == null) {
			addError(getDelegate(), "nonBusinessDate", getText("dayOff.validation.nonBusinessDate.required"),
					ValidationConstraint.CONSISTENCY);
		}
		if (getDelegate().getHasErrors()) {
			return null;
		}

		/* check for existing name */
		SearchCriteria searchCriteria = new SearchCriteria("dayOff");
		searchCriteria.addCriterion(Restrictions.eq("nonBusinessDate", getEntity().getNonBusinessDate()));

		if (!isNew)
			searchCriteria.addCriterion(Restrictions.ne("id", getEntity().getId()));

		List<DayOff> bankList = (List<DayOff>) getDayOffManager().findByCriteria(searchCriteria, -1, -1);
		if (bankList != null && bankList.size() > 0) {
			addError(getDelegate(), "nonBusinessDate", getText("dayOff.validation.nonBusinessDate.exists"),
					ValidationConstraint.CONSISTENCY);
		}

		if (getDelegate().getHasErrors()) {
			return null;
		}

		AddDayOffConfirmation addDayOffConfirmation = getAddDayOffConfirmation();
		addDayOffConfirmation.setEntity(getEntity());
		return addDayOffConfirmation;
	}
}
