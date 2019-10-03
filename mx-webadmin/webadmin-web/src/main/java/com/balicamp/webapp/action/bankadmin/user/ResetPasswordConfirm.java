package com.balicamp.webapp.action.bankadmin.user;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.Constants;
import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.model.user.User;
import com.balicamp.service.user.UserManager;
import com.balicamp.webapp.action.BasePageForm;
import com.balicamp.webapp.action.common.InfoPage;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public abstract class ResetPasswordConfirm extends BasePageForm<User, Long> implements PageBeginRenderListener {

	@Persist("client")
	public abstract String getUserId();

	public abstract void setUserId(String userId);

	@InjectPage("userList")
	public abstract UserList getUserList();

	@InjectObject("spring:userManager")
	public abstract UserManager getUserManager();

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		//init entity

		/*
		 * FIXME: Statement ini masih dipakai, tapi bikin repot.
		 * Jadi saya comment dulu.
		 */
		initializedEntity(getUserManager(), User.class);
	}

	public ILink onReset(IRequestCycle cycle) {
		getSystemParameterManager().addConfiguration(Constants.SystemParameter.Configuration.CONTEXT_BASE_URL,
				getBaseUrl());

		User user = getEntity();
		user.setMustChangePassword(Boolean.TRUE);
		getUserManager().resetUserAdminPassword(getUserLoginFromSession(), user, getRequest().getRemoteHost());

		InfoPageCommand infoPageCommand = new InfoPageCommand();
		infoPageCommand.setTitle(getText("resetPasswordConfirm.result.title"));
		infoPageCommand.addMessage(getText("userInformation.information.reset", new String[] { getEntity()
				.getUserName() }));
		infoPageCommand
				.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.finish"), "userList.html"));
		infoPageCommand
				.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.welcome"), "wellcome.html"));

		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);

		return getEngineService().getLink(false, "infoPage");
	}

	public IPage onBack(IRequestCycle cycle) {
		getUserList().setUserId(getUserId());
		clearSession();
		return getUserList();
	}

	private void clearSession() {
		setUserId(null);
	}
}
