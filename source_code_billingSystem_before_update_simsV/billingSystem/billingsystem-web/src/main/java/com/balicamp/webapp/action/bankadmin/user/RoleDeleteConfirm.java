package com.balicamp.webapp.action.bankadmin.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;

import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.model.user.Role;
import com.balicamp.service.function.FunctionRoleManager;
import com.balicamp.service.user.RoleManager;
import com.balicamp.service.user.UserManager;
import com.balicamp.service.user.UserRoleManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;

public abstract class RoleDeleteConfirm extends AdminBasePage {

	public abstract String getUserIdToDelete();

	public abstract void setUserIdToDelete(String userIdToDelete);

	@InjectObject("spring:roleManager")
	public abstract RoleManager getRoleManager();

	@InjectObject("spring:userRoleManager")
	public abstract UserRoleManager getUserRoleManager();

	@InjectObject("spring:functionRoleManager")
	public abstract FunctionRoleManager getFunctionRoleManager();

	@InjectPage("infoPage")
	public abstract InfoPage getInfoPage();

	@InjectObject("spring:userManager")
	public abstract UserManager getUserManager();

	public abstract void setRoleToDelete(Set<Role> roleToDelete);

	public abstract Set<Role> getRoleToDelete();

	public IPage doDelete() {
		if (getRoleToDelete() == null)
			return null;

		for (Role role : getRoleToDelete()) {
			Long roleId = role.getId();
			getFunctionRoleManager().deleteByRoleId(role.getId());
			getUserRoleManager().deleteByRoleId(role.getId());
			getRoleManager().deleteById(role.getId());

			// save to audit log
			//FIXME
			//getUserManager().adminDeleteRole(getUserLoginFromSession(), roleId, getRequest().getRemoteHost());
		}

		InfoPageCommand infoPageCommand = new InfoPageCommand();
		infoPageCommand.setTitle(getText("roleDeleteConfirm.success.title"));
		List<String> messageList = new ArrayList<String>();
		messageList.add(getText("roleDeleteConfirm.success.message"));
		infoPageCommand.setMessageList(messageList);

		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.finish"),
				"updateRole.html"));
		infoPageCommand
				.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.welcome"), "wellcome.html"));

		InfoPage infoPage = getInfoPage();
		infoPage.setInfoPageCommand(infoPageCommand);
		return infoPage;
	}

}
