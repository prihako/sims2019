package com.balicamp.webapp.action.bankadmin.user;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.BeanPropertySelectionModel;

import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.model.user.Role;
import com.balicamp.model.user.User;
import com.balicamp.model.user.UserRole;
import com.balicamp.model.user.UserRoleId;
import com.balicamp.service.user.RoleManager;
import com.balicamp.service.user.UserManager;
import com.balicamp.service.user.UserRoleManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;

public abstract class UserConfirm extends AdminBasePage implements
		PageBeginRenderListener {

	public abstract void setRole(Role role);

	public abstract Role getRole();

	public abstract void setUserId(Long userId);

	public abstract Long getUserId();

	public abstract void setUserName(String userName);

	public abstract String getUserName();

	public abstract void setName(String name);

	public abstract String getName();

	public abstract void setEmail(String email);

	public abstract String getEmail();

	public abstract void setRoleId(Long roleId);

	public abstract Long getRoleId();

	@Persist("client")
	public abstract String getSearchUserId();

	public abstract void setSearchUserId(String searchUserId);

	// @InjectObject("spring:triggerEmailSender")
	// public abstract JmsTriggerSenderImpl getTriggerEmailSender();

	@InjectPage("userEntry")
	public abstract UserEntry getUserEntry();

	@InjectPage("userEntryEdit")
	public abstract UserEntryEdit getUserEntryEdit();

	@InjectObject("spring:userRoleManager")
	public abstract UserRoleManager getUserRoleManager();

	@InjectObject("spring:roleManager")
	public abstract RoleManager getRoleManager();

	@InjectObject("spring:userManager")
	public abstract UserManager getUserManager();

	private BeanPropertySelectionModel roleModel = null;

	protected final Log log = LogFactory.getLog(UserEntryEdit.class);

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if (isRealRender()) {
			if (getRoleId() != null) {
				setRole(getRoleManager().findById(getRoleId()));
			}
		}
	}

	public BeanPropertySelectionModel getRoleModel() {
		if (roleModel == null) {
			List<Role> roleList = getRoleManager().getEditableAdminRoleList();
			roleModel = new BeanPropertySelectionModel(roleList, "name");
		}
		return roleModel;
	}

	public IPage doSubmit(IRequestCycle cycle) {
		InfoPageCommand infoPageCommand = new InfoPageCommand();

		if (getUserId() != null) { // edit

			User user = getUserManager().findById(getUserId());
			UserRoleId userRoleId = new UserRoleId(user, getRole());
			UserRole userRole = new UserRole(userRoleId);
			
			//To check, if user don't update role, we not need to save again in database, 
			//cause if we save with the save object hibernate will throw exception
			UserRole oldUserRole = getUserRoleManager().findByUser(user).get(0);
			if(!(oldUserRole.getUserRoleId().getRole().getName().equalsIgnoreCase(getRole().getName()))){
				getUserRoleManager().deleteByUserId(getUserId());
				getUserRoleManager().save(userRole);
			}
			
			// contruct status page
			infoPageCommand.setTitle(getText("userInformation.titleEdit"));
			infoPageCommand.addMessage(getText(
					"userInformation.informationEdit", new Object[] {
							getUserName(), getRole().getName() }));
			infoPageCommand
					.addInfoPageButton(new InfoPageCommand.InfoPageButton(
							getText("userProfileConfirm.button.finish"), "userList.html"));
			infoPageCommand
					.addInfoPageButton(new InfoPageCommand.InfoPageButton(
							getText("button.welcome"), "wellcome.html"));
			
			getUserManager().userAuditLog(initUserLoginFromDatabase(), getRequest().getRemoteHost(), getUserName(), false);

		} else { // add
			UserManager userManager = getUserManager();

			User user = new User();
			user.setUserName(getUserName());
			user.setUserFullName(getName());
			user.setEmail(getEmail());
			user.setUserParentId(new Long(0));
			// String plainPassword = userManager.generatePassword(user);
			// String plainPassword = userManager.encodeResetPassword(user);
			// sendEmail(user.getUsername(), plainPassword, user.getEmail());
			userManager.addNewAdmin(getUserName(), getName(), getEmail(), null,
					getRole());

			// contruct status page
			infoPageCommand.setTitle(getText("userInformation.title"));
			infoPageCommand.addMessage(getText("userInformation.information",
					new Object[] { getUserName(), getEmail() }));
			infoPageCommand
					.addInfoPageButton(new InfoPageCommand.InfoPageButton(
							getText("userProfileConfirm.button.finish"), "userEntry.html"));
			infoPageCommand
					.addInfoPageButton(new InfoPageCommand.InfoPageButton(
							getText("button.welcome"), "wellcome.html"));
			
			getUserManager().userAuditLog(initUserLoginFromDatabase(), getRequest().getRemoteHost(), getUserName(), true);

		}
		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);

		clearSession();

		return infoPage;
	}

	public IPage doCancel() {
		if (getUserId() != null) { // edit
			UserEntryEdit userEntryEdit = getUserEntryEdit();
			userEntryEdit.setUserId(getUserId());
			userEntryEdit.setRoleId(getRole().getId());
			userEntryEdit.setUserName(getUserName());
			userEntryEdit.setEmail(getEmail());
			userEntryEdit.setName(getName());
			userEntryEdit.setSearchUserId(getSearchUserId());
			clearSession();
			return userEntryEdit;
		} else {
			UserEntry userEntry = getUserEntry();
			userEntry.setRole(getRole());
			userEntry.setUserName(getUserName());
			userEntry.setEmail(getEmail());
			userEntry.setName(getName());
			return userEntry;
		}
	}

	private void clearSession() {
		setSearchUserId(null);
	}

}
