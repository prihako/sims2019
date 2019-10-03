package com.balicamp.webapp.action.bankadmin.user;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import com.balicamp.model.user.UserDisplay;
import com.balicamp.model.user.UserRole;
import com.balicamp.model.user.UserRoleId;
import com.balicamp.service.user.RoleManager;
import com.balicamp.service.user.UserManager;
import com.balicamp.service.user.UserRoleManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.priority.PriorityCommonNotif;

public abstract class UserConfirm extends AdminBasePage implements PageBeginRenderListener {

	private static final Logger LOGGER = Logger.getLogger(UserConfirm.class.getSimpleName());

	public abstract void setRole(Role role);

	public abstract Role getRole();

	public abstract void setRoleOld(Role role);

	public abstract Role getRoleOld();

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

	public abstract void setRoleIdOld(Long roleId);

	public abstract Long getRoleIdOld();

	public abstract void setUserParent(UserDisplay userParent);

	public abstract UserDisplay getUserParent();

	public abstract UserRoleId getUserRoleId();

	public abstract void setUserRoleId(UserRoleId userRole);

	public abstract UserRole getUserRole();

	public abstract void setUserRole(UserRole userRole);

	@Persist("client")
	public abstract String getSearchUserId();

	public abstract void setSearchUserId(String searchUserId);

	// @InjectObject("spring:triggerEmailSender")
	// public abstract JmsTriggerSenderImpl getTriggerEmailSender();

	@InjectPage("userEntry")
	public abstract UserEntry getUserEntry();

	@InjectPage("userList")
	public abstract UserList getUserList();

	@InjectPage("priorityCommonNotif")
	public abstract PriorityCommonNotif getPriorityCommonNotif();

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
				// setRoleOld(getRoleManager().findById(getRoleIdOld()));
				UserRoleId userRoleId = new UserRoleId(getUserManager().findById(getUserId()), getRoleManager()
						.findById(getRoleId()));
				setUserRoleId(userRoleId);

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

		/**
		 * update table user update table role
		 */

		BigDecimal parentIdDefault = new BigDecimal(0);
		if (getUserId() != null) { // edit

			User user = getUserManager().findById(getUserId());
			user.setEmail(getEmail());
			user.setUserParentId(parentIdDefault.longValue());
			getUserManager().saveOrUpdate(user);

			PriorityCommonNotif notif = getPriorityCommonNotif();
			notif.setTitle(getText("userInformation.titleEdit"));

			if (updateRole(user)) {
				System.out.println("role object  -->" + getRole());

				notif.setMsgnotif(getText("userInformation.edit.success"));
				getUserManager().adminEditUser(getUserLoginFromSession(), user, getRequest().getRemoteHost());
			} else {

				notif.setMsgnotif(getText("userInformation.edit.failed"));
			}

			return notif;

			// // contruct status page
			// infoPageCommand.setTitle(getText("userInformation.titleEdit"));
			// infoPageCommand.addMessage(getText("userInformation.informationEdit",
			// new Object[] { getUserName(), getRole().getName() }));
			// infoPageCommand.addInfoPageButton(new
			// InfoPageCommand.InfoPageButton(getText("button.finish"),
			// "userList.html"));
			// infoPageCommand.addInfoPageButton(new
			// InfoPageCommand.InfoPageButton(getText("button.welcome"),
			// "wellcome.html"));

		} else { // add
			UserManager userManager = getUserManager();

			User user = new User();
			user.setUserName(getUserName());
			user.setUserFullName(getName());
			user.setEmail(getEmail());
			user.setUserParentId(parentIdDefault.longValue());
			// String plainPassword = userManager.generatePassword(user);
			// String plainPassword = userManager.encodeResetPassword(user);
			// sendEmail(user.getUsername(), plainPassword, user.getEmail());
			User usr = userManager.addNewAdmin(getUserName(), getName(), getEmail(), null, getRole(),
					parentIdDefault.longValue());

			// contruct status page

			PriorityCommonNotif notif = getPriorityCommonNotif();
			notif.setTitle(getText("userEntry.title"));

			if (usr != null) {
				notif.setMsgnotif(getText("userInformation.information", new Object[] { getUserName(), getEmail() }));
				getUserManager().adminEntryUser(getUserLoginFromSession(), user, getRequest().getRemoteHost());
			} else {
				notif.setMsgnotif(getText("userInformation.add.failed"));
			}

			return notif;

			// if(usr == null){
			// return null;
			// } else {
			// infoPageCommand.setTitle(getText("userInformation.title"));
			// infoPageCommand.addMessage(getText("userInformation.information",
			// new Object[] { getUserName(), getEmail() }));
			// infoPageCommand
			// .addInfoPageButton(new InfoPageCommand.InfoPageButton(
			// getText("button.finish"), "userEntry.html"));
			// infoPageCommand
			// .addInfoPageButton(new InfoPageCommand.InfoPageButton(
			// getText("button.welcome"), "wellcome.html"));
			//
			// InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
			// infoPage.setInfoPageCommand(infoPageCommand);
			//
			// clearSession();
			//
			// return infoPage;
			// }
		}
	}

	private boolean updateRole(User user) {
		boolean stat = false;
		try {

			if (!getRoleId().equals(getRoleIdOld())) {
				getUserRoleManager().deleteByUserId(getUserId());

				UserRoleId userRoleId = new UserRoleId(user, getRole());
				UserRole userRole = new UserRole(userRoleId);
				getUserRoleManager().save(userRole);

				stat = true;
			} else {

				stat = true;
			}

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return stat;
	}

	public IPage doCancel() {
		return getUserList();

	}

	private void clearSession() {
		setSearchUserId(null);
	}

}
