package com.balicamp.webapp.action.bankadmin.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;

import com.balicamp.model.function.Function;
import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.model.user.Role;
import com.balicamp.service.function.FunctionRoleManager;
import com.balicamp.service.user.RoleManager;
import com.balicamp.service.user.UserManager;
import com.balicamp.webapp.action.BasePageForm;
import com.balicamp.webapp.action.common.InfoPage;

public abstract class AddRoleConfirmation extends BasePageForm<Role, Long> {

	@InjectPage("addRole")
	public abstract AddRole getAddRole();

	@InjectObject("spring:roleManager")
	public abstract RoleManager getRoleManager();

	@InjectObject("spring:functionRoleManager")
	public abstract FunctionRoleManager getFunctionRoleManager();

	@InjectObject("spring:userManager")
	public abstract UserManager getUserManager();

	public abstract List<Function> getSelectedFunction();

	public abstract void setSelectedFunction(List<Function> selectedFunction);

	public IPage onBack() {
		AddRole addRole = getAddRole();
		addRole.setEntity(getEntity());
		addRole.setSelectedFunction(getSelectedFunction());
		return addRole;
	}

	public IPage doButtonSave(IRequestCycle cycle) {
		boolean isNew = getEntity().getId() == null;

		if (isNew) {
			getEntity().setEditable(1);
			getEntity().setRtype("A");

			getRoleManager().save(getEntity());
		}

		Set<Long> functionIdSet = new HashSet<Long>();
		for (Function function : getSelectedFunction()) {
			functionIdSet.add(function.getId());
		}

		//FIXME:
		getFunctionRoleManager().saveRoleAndFunctions(getEntity(), functionIdSet);
		getRoleManager().updateDataRole(getEntity());

		// construct status page
		InfoPageCommand infoPageCommand = new InfoPageCommand();

		if (isNew) {
//			 save to audit log
			getUserManager().adminEntryRole(getUserLoginFromSession(), getEntity(), getRequest().getRemoteHost());

			infoPageCommand.setTitle(getText("userInformation.titleRoleAdd"));
			infoPageCommand.addMessage(getText("userInformation.informationRoleAdd"));

			infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.finish"),
					"addRole.html"));
		} else {
			// save to audit log
			getUserManager().adminEditRole(getUserLoginFromSession(), getEntity(), getRequest().getRemoteHost());

			infoPageCommand.setTitle(getText("userInformation.titleRoleEdit"));
			infoPageCommand.addMessage(getText("userInformation.informationRoleEdit"));

			infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.finish"),
					"updateRole.html"));
		}
		infoPageCommand
				.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.welcome"), "wellcome.html"));

		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);

		return infoPage;
	}

	public IPage onButtonCancel() {
		AddRole addRole = getAddRole();
		addRole.setEntity(getEntity());
		addRole.setSelectedFunction(getSelectedFunction());
		return addRole;
	}

}
