package com.balicamp.webapp.action.bankadmin.user;

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
import com.balicamp.model.common.MethodResponse;
import com.balicamp.model.user.Role;
import com.balicamp.service.IManager;
import com.balicamp.service.function.FunctionManager;
import com.balicamp.service.user.RoleManager;
import com.balicamp.util.CommonUtil;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.tapestry.GenericTableModel;

public abstract class UpdateRole extends BasePageList implements PageBeginRenderListener, PageEndRenderListener {

	private Set<Role> selectedRole;

	@InjectObject("spring:roleManager")
	public abstract RoleManager getRoleManager();

	@InjectObject("spring:functionManager")
	public abstract FunctionManager getFunctionManager();

	@InjectPage("addRole")
	public abstract AddRole getAddRole();

	@InjectPage("roleDeleteConfirm")
	public abstract RoleDeleteConfirm getRoleDeleteConfirm();

	public abstract Role getSearchRole();

	public abstract void setSearchRole(Role searchRole);

	public void setSelectedRole(Set<Role> selectedRole) {
		this.selectedRole = selectedRole;
	}

	public Set<Role> getSelectedRole() {
		return selectedRole;
	}

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		populateSessionPropertyValue();

		if (isRealRender()) {
			if (getSelectedRole() != null) {
				getSelectedRole().clear();
			}
		}
		if (getSearchRole() == null)
			setSearchRole(new Role());

	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);

		//save last state to session
		syncSessionPropertyValue(new String[] { "searchRole" });
	}

	public IBasicTableModel getRoleListTableModel() {
		return super.getTableModel();
	}

	public IBasicTableModel createTableModel() {
		SearchCriteria searchCriteria = SearchCriteria.createSearchCriteria("role");
		searchCriteria.addCriterion(Restrictions.eq("editable", 1));

		if (!CommonUtil.isEmpty(getSearchRole().getName()))
			searchCriteria.addCriterion(Restrictions.ilike("name", getSearchRole().getName(), MatchMode.ANYWHERE));

		return new GenericTableModel<Role>((IManager) getRoleManager(), searchCriteria, emptyOrderMaping);
	}

	public void checkSelect(IRequestCycle cycle) {
		if (cycle.isRewinding()) {
			Role role = (Role) getLoopObject();
			if (role != null && role.getSelected()) {
				if (selectedRole == null) {
					setSelectedRole(new HashSet<Role>());
				}
				selectedRole.add(role);
			}
		}
	}

	/**
	 * Listener search button
	 * @return
	 */
	public IPage onSearch() {
		//do nothing just refresh
		return null;
	}

	public IPage onViewDetail(Long id) {
		AddRole addRole = getAddRole();
		Role role = getRoleManager().findById(id);
		addRole.setEntity(role);
		return addRole;
	}

	public IPage doDelete() {
		if (selectedRole == null || selectedRole.size() == 0) {
			addError(getDelegate(), (IFormComponent) null, getText("common.message.validation.noDataSelected"),
					ValidationConstraint.CONSISTENCY);
			return null;
		}

		validateServerBeforeDelete();

		if (getDelegate().getHasErrors())
			return null;

		RoleDeleteConfirm roleDeleteConfirm = getRoleDeleteConfirm();
		roleDeleteConfirm.setRoleToDelete(selectedRole);
		return roleDeleteConfirm;
	}

	private void validateServerBeforeDelete() {
		for (Role tmpRole : selectedRole) {
			MethodResponse methodResponse = getRoleManager().cekDeleteReferencialIntegrity(tmpRole);
			if (!methodResponse.isStatus()) {
				addError(getDelegate(), (IFormComponent) null,
						getText(methodResponse.getMessageKey(), methodResponse.getMessageParam()),
						ValidationConstraint.CONSISTENCY);
				return;
			}
		}

	}

}
