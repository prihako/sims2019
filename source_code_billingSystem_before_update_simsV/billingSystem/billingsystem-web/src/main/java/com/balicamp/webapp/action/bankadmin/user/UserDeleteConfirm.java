package com.balicamp.webapp.action.bankadmin.user;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.model.user.User;
import com.balicamp.model.user.UserDisplay;
import com.balicamp.service.user.UserAdminGroupManager;
import com.balicamp.service.user.UserManager;
import com.balicamp.service.user.UserRoleManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;

public abstract class UserDeleteConfirm extends AdminBasePage implements PageBeginRenderListener {

	@InjectObject("spring:userManager")
	public abstract UserManager getUserManager();

	@InjectObject("spring:userRoleManager")
	public abstract UserRoleManager getUserRoleManager();

	@InjectObject("spring:userAdminGroupManager")
	public abstract UserAdminGroupManager getUserAdminGroupManager();

	@InjectPage("userList")
	public abstract UserList getUserList();

	@Persist("client")
	public abstract String getSearchUserId();

	public abstract void setSearchUserId(String searchUserId);

	public abstract void setUserToDelete(Set<UserDisplay> userToDelete);

	public abstract Set<UserDisplay> getUserToDelete();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		if (getUserToDelete() == null) {
			setUserToDelete(new HashSet<UserDisplay>());
		}
	}

	public IPage doDelete(IRequestCycle cycle) {
		Set<Long> userIdToDelete = new HashSet<Long>();
		if (getUserToDelete() == null)
			return null;
		UserManager userManager = getUserManager();

		for (UserDisplay userDisplay : getUserToDelete()) {
			if (userDisplay.getRoleId() != null) {
				getUserRoleManager().deleteByUserIdAndRoleId(userDisplay.getUserId(), userDisplay.getRoleId());
				userIdToDelete.add(userDisplay.getUserId());
			}
		}

		for (UserDisplay userDisplay : getUserToDelete()) {
			User u = userManager.findById(userDisplay.getUserId());
			if (u.getUserRoleSet().size() == 0) {
				if (u.getUserAdminGroupSet().size() > 0) {
					getUserAdminGroupManager().deleteByUserId(userDisplay.getUserId());
				}
				u.setEnabled(false);
				u.setInactiveDate(new Date());
				Long uId = u.getId();
				getUserManager().adminDeleteUser(getUserLoginFromSession(), uId, getRequest().getRemoteHost());
			}
		}

		//contruct status page
		InfoPageCommand infoPageCommand = new InfoPageCommand();
		infoPageCommand.setTitle(getText("userDeleteSuccess.deleteTitle"));
		infoPageCommand.addMessage(getText("userDeleteSuccess.deleteInformation"));
		infoPageCommand
				.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.finish"), "userList.html"));
		infoPageCommand
				.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.welcome"), "wellcome.html"));

		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);

		return infoPage;
	}

	public IPage doCancel() {
		UserList userList = getUserList();
		userList.setUserId(getSearchUserId());
		clearSession();
		return userList;
	}

	private void clearSession() {
		setSearchUserId(null);
	}
}
