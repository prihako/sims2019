package com.balicamp.webapp.action.bankadmin.user.key;

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
import org.hibernate.criterion.Restrictions;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mx.MxParm;
import com.balicamp.service.GenericManager;
import com.balicamp.service.MxParmManager;
import com.balicamp.util.CommonUtil;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.tapestry.NewTableModel;

public abstract class ViewKey extends BasePageList implements PageBeginRenderListener, PageEndRenderListener {

	@InjectObject("spring:mxParmManager")
	public abstract MxParmManager getMxParmManager();

	@InjectPage("deleteKeyConfirmation")
	public abstract DeleteKeyConfirmation getDeleteKeyConfirmation();

	@InjectPage("addKeyInput")
	public abstract AddKeyInput getAddKeyInput();

	public abstract MxParm getSearchMxParm();

	public abstract void setSearchMxParm(MxParm searchMxParm);

	public abstract String getMasking();

	public abstract void setMasking(String masking);

	private Set<MxParm> selectedMxParm;

	public void setSelectedMxParm(Set<MxParm> selectedMxParm) {
		this.selectedMxParm = selectedMxParm;
	}

	public Set<MxParm> getSelectedMxParm() {
		return selectedMxParm;
	}

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		populateSessionPropertyValue();

		if (getMasking() == null)
			if (getRequest().getParameter("masking") != null)
				setMasking(getRequest().getParameter("masking"));
		if (getSearchMxParm() == null)
			setSearchMxParm(new MxParm());
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);

		// save last state to session
		syncSessionPropertyValue(new String[] { "searchMxParm" });
	}

	public IBasicTableModel getMxParmListTableModel() {
		return super.getTableModel();
	}

	public IBasicTableModel createTableModel() {
		SearchCriteria searchCriteria = SearchCriteria.createSearchCriteria("mxParm");

		if (!CommonUtil.isEmpty(getSearchMxParm().getIdentifier()))
			searchCriteria.addCriterion(Restrictions.ilike("identifier", getSearchMxParm().getIdentifier(),
					MatchMode.ANYWHERE));

		searchCriteria.addCriterion(Restrictions.ilike("identifier", getMasking(), MatchMode.START));

		return new NewTableModel<MxParm>((GenericManager) getMxParmManager(), searchCriteria, emptyOrderMaping);
	}

	public void checkSelect(IRequestCycle cycle) {
		if (cycle.isRewinding()) {
			MxParm mxParm = (MxParm) getLoopObject();
			if (mxParm != null && mxParm.getSelected()) {
				if (selectedMxParm == null) {
					setSelectedMxParm(new HashSet<MxParm>());
				}
				selectedMxParm.add(mxParm);
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
		AddKeyInput addKey = getAddKeyInput();
		MxParm mxParm = getMxParmManager().findById(id);
		addKey.setEntity(mxParm);
		return addKey;
	}

	public IPage doDelete() {
		if (selectedMxParm == null || selectedMxParm.size() == 0) {
			addError(getDelegate(), (IFormComponent) null, getText("common.message.validation.noDataSelected"),
					ValidationConstraint.CONSISTENCY);
			return null;
		}
		if (getDelegate().getHasErrors())
			return null;

		DeleteKeyConfirmation deleteKeyConfirmation = getDeleteKeyConfirmation();
		deleteKeyConfirmation.setMxParmToDelete(selectedMxParm);
		return deleteKeyConfirmation;
	}
}
