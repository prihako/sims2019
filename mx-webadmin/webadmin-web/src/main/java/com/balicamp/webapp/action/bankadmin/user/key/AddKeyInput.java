package com.balicamp.webapp.action.bankadmin.user.key;

import java.util.List;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.valid.ValidationConstraint;
import org.hibernate.criterion.Restrictions;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mx.MxParm;
import com.balicamp.service.MxParmManager;
import com.balicamp.webapp.action.BasePageForm;

public abstract class AddKeyInput extends BasePageForm<MxParm, Long> implements PageBeginRenderListener {

	@InjectObject("spring:mxParmManager")
	public abstract MxParmManager getMxParmManager();

	@InjectPage("addKeyConfirmation")
	public abstract AddKeyConfirmation getAddKeyConfirmation();

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		initializedEntity(getMxParmManager(), MxParm.class);

		if (isRealRender()) {
			if (getEntity().getId() != null) {

			}
		}

	}

	public IPage doButtonNext() {
		boolean isNew = getEntity().getId() == null;

		if (getEntity().getIdentifier() == null || getEntity().getValues() == null) {
			addError(getDelegate(), "identifier", getText("addKey.validation.identifier.required"),
					ValidationConstraint.CONSISTENCY);
		}
		if (getEntity().getValues() == null) {
			addError(getDelegate(), "values", getText("addKey.validation.values.required"),
					ValidationConstraint.CONSISTENCY);
		}
		if (getDelegate().getHasErrors()) {
			return null;
		}

		/* check for existing name */
		SearchCriteria searchCriteria = new SearchCriteria("mxParm");
		searchCriteria.addCriterion(Restrictions.eq("identifier", getEntity().getIdentifier()));
		if (!isNew)
			searchCriteria.addCriterion(Restrictions.ne("id", getEntity().getId()));

		List<MxParm> mxParmList = (List<MxParm>) getMxParmManager().findByCriteria(searchCriteria, -1, -1);
		if (mxParmList != null && mxParmList.size() > 0) {
			addError(getDelegate(), "identifier", getText("addKey.validation.identifier.exists"),
					ValidationConstraint.CONSISTENCY);
		}

		if (getDelegate().getHasErrors()) {
			return null;
		}

		AddKeyConfirmation addMxParmConfirmation = getAddKeyConfirmation();
		addMxParmConfirmation.setEntity(getEntity());
		return addMxParmConfirmation;
	}

}
