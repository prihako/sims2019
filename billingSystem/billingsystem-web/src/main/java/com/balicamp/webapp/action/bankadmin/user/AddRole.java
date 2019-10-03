package com.balicamp.webapp.action.bankadmin.user;

import java.util.List;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.BeanPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;
import org.hibernate.criterion.Restrictions;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.function.Function;
import com.balicamp.model.user.Role;
import com.balicamp.service.function.FunctionManager;
import com.balicamp.service.user.RoleManager;
import com.balicamp.webapp.action.BasePageForm;

public abstract class AddRole extends BasePageForm<Role, Long> implements PageBeginRenderListener {

	public abstract List<Function> getSelectedFunction();

	public abstract void setSelectedFunction(List<Function> selectedFunction);

	@InjectObject("spring:roleManager")
	public abstract RoleManager getRoleManager();

	@InjectObject("spring:functionManager")
	public abstract FunctionManager getFunctionManager();

	@InjectPage("addRoleConfirmation")
	public abstract AddRoleConfirmation getAddRoleConfirmation();

	private BeanPropertySelectionModel functionModel;

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		initializedEntity(getRoleManager(), Role.class);

		if (isRealRender()) {
			if (getEntity().getId() != null) {
				List<Function> functionList = getFunctionManager().getBankAdminFunctionList();
				functionModel = new BeanPropertySelectionModel(functionList, "description");
				setSelectedFunction(getRoleManager().getFunctionList(getEntity().getId()));
			}
		}

	}

	public BeanPropertySelectionModel getFunctionModel() {
		if (functionModel == null) {
			List<Function> functionList = getFunctionManager().getBankAdminFunctionList();
			functionModel = new BeanPropertySelectionModel(functionList, "description");
		}
		return functionModel;
	}

	public IPage doButtonNext() {
		boolean isNew = getEntity().getId() == null;

		if (getSelectedFunction() == null || getSelectedFunction().size() == 0) {
			addError(getDelegate(), "functions", getText("addRole.functions.required"),
					ValidationConstraint.CONSISTENCY);
			return null;
		}
		/* check for existing name */
		RoleManager roleManager = getRoleManager();
		SearchCriteria searchCriteria = new SearchCriteria("role");
		searchCriteria.addCriterion(Restrictions.eq("name", getEntity().getName()));
		if (!isNew)
			searchCriteria.addCriterion(Restrictions.ne("id", getEntity().getId()));
		List<Role> roleList = (List<Role>) roleManager.findByCriteria(searchCriteria, -1, -1);
		if (roleList != null && roleList.size() > 0) {
			addError(getDelegate(), "roleName", getText("addRole.name.exist"), ValidationConstraint.CONSISTENCY);
		}

		if (getDelegate().getHasErrors()) {
			return null;
		}

		getEntity().setEditable(1);
		AddRoleConfirmation addRoleConfirmation = getAddRoleConfirmation();
		addRoleConfirmation.setEntity(getEntity());
		addRoleConfirmation.setSelectedFunction(getSelectedFunction());
		return addRoleConfirmation;
	}

}
