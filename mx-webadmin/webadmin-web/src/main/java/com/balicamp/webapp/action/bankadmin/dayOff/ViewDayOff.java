package com.balicamp.webapp.action.bankadmin.dayOff;

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
import com.balicamp.model.mx.DayOff;
import com.balicamp.service.DayOffManager;
import com.balicamp.service.GenericManager;
import com.balicamp.util.CommonUtil;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.tapestry.NewTableModel;

public abstract class ViewDayOff extends BasePageList implements PageBeginRenderListener, PageEndRenderListener {

	@InjectObject("spring:dayOffManager")
	public abstract DayOffManager getDayOffManager();

	@InjectPage("deleteDayOffConfirmation")
	public abstract DeleteDayOffConfirmation getDeleteDayOffConfirmation();

	@InjectPage("addDayOffInput")
	public abstract AddDayOffInput getAddDayOffInput();

	public abstract DayOff getSearchDayOff();

	public abstract void setSearchDayOff(DayOff searchDayOff);

	private Set<DayOff> selectedDayOff;

	public void setSelectedDayOff(Set<DayOff> selectedDayOff) {
		this.selectedDayOff = selectedDayOff;
	}

	public Set<DayOff> getSelectedDayOff() {
		return selectedDayOff;
	}

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		populateSessionPropertyValue();

		if (getSearchDayOff() == null)
			setSearchDayOff(new DayOff());
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);

		// save last state to session
		syncSessionPropertyValue(new String[] { "searchDayOff" });
	}

	public IBasicTableModel getDayOffListTableModel() {
		return super.getTableModel();
	}

	public IBasicTableModel createTableModel() {
		SearchCriteria searchCriteria = SearchCriteria.createSearchCriteria("dayOff");

		if (getSearchDayOff().getNonBusinessDate() != null)
			searchCriteria.addCriterion(Restrictions.eq("nonBusinessDate", getSearchDayOff().getNonBusinessDate()));
		if (!CommonUtil.isEmpty(getSearchDayOff().getDescription()))
			searchCriteria.addCriterion(Restrictions.ilike("description", getSearchDayOff().getDescription(),
					MatchMode.ANYWHERE));
		searchCriteria.addOrder(Order.asc("nonBusinessDate"));
		return new NewTableModel<DayOff>((GenericManager) getDayOffManager(), searchCriteria, emptyOrderMaping);
	}

	public void checkSelect(IRequestCycle cycle) {
		if (cycle.isRewinding()) {
			DayOff dayOff = (DayOff) getLoopObject();
			if (dayOff != null && dayOff.getSelected()) {
				if (selectedDayOff == null) {
					setSelectedDayOff(new HashSet<DayOff>());
				}
				selectedDayOff.add(dayOff);
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
		AddDayOffInput addDayOff = getAddDayOffInput();
		DayOff dayOff = getDayOffManager().findById(id);
		addDayOff.setEntity(dayOff);
		return addDayOff;
	}

	public IPage doDelete() {
		if (selectedDayOff == null || selectedDayOff.size() == 0) {
			addError(getDelegate(), (IFormComponent) null, getText("common.message.validation.noDataSelected"),
					ValidationConstraint.CONSISTENCY);
			return null;
		}
		if (getDelegate().getHasErrors())
			return null;

		DeleteDayOffConfirmation deleteDayOffConfirmation = getDeleteDayOffConfirmation();
		deleteDayOffConfirmation.setDayOffToDelete(selectedDayOff);
		return deleteDayOffConfirmation;
	}
}
