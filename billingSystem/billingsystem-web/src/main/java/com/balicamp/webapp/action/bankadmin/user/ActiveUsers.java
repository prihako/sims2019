package com.balicamp.webapp.action.bankadmin.user;

import java.util.Map;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.Constants;
import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.model.parameter.SystemParameterId;
import com.balicamp.model.user.User;
import com.balicamp.service.rpc.RemoteSystemControl;
import com.balicamp.service.user.UserManager;
import com.balicamp.webapp.action.BasePage;
import com.balicamp.webapp.action.common.InfoPage;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: ActiveUsers.java 436 2013-04-24 11:32:33Z rudi.sadria $
 */
public abstract class ActiveUsers extends BasePage implements PageBeginRenderListener, PageEndRenderListener {

	@InjectSpring("systemControls")
	public abstract Map<String, RemoteSystemControl> getSystemControls();

	@InjectSpring("userManager")
	public abstract UserManager getUserManager();

	@InjectPage("infoPage")
	public abstract InfoPage getInfoPage();

	public abstract IPropertySelectionModel getServerSelectionModel();

	public abstract void setServerSelectionModel(IPropertySelectionModel serverSelectionModel);

	public int getPageSize() {
		return getSystemParameterManager().getIntValue(
				new SystemParameterId(Constants.SystemParameter.Page.GROUP,
						Constants.SystemParameter.Page.TABLE_PAGESIZE), 20);
	}

	public void initServerSelectionModel() {
		if (getServerSelectionModel() != null) {
			return;
		}
		setServerSelectionModel(new StringPropertySelectionModel(getSystemControls().keySet().toArray(new String[0])));
	}

	@Persist("session")
	public abstract String getSelectedServer();

	public abstract void setSelectedServer(String selectedServer);

	@Persist("session")
	public abstract String getSearchUserName();

	public abstract void setSearchUserName(String searchUserName);

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		populateSessionPropertyValue();

		initServerSelectionModel();

		if (getSelectedServer() == null) {
			setSelectedServer(getServerSelectionModel().getValue(0));
		}

		if (isRealRender()) {
			try {
				getActiveUsers();
			} catch (Throwable t) {
				log.error("unable to retrieve active users", t);

				addRequestCache("activeUsers", new Object[0]);
				addError(getDelegate(), (IFormComponent) null, getText("activeUsers.error.message"),
						ValidationConstraint.CONSISTENCY);
			}
		}
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);

		syncSessionPropertyValue(new String[] { "selectedServer" });
	}

	public Object[] getActiveUsers() {
		return (Object[]) getRequestCacheValue("activeUsers", "initActiveUsers");
	}

	public Object[] initActiveUsers() {
		RemoteSystemControl remote = getSystemControls().get(getSelectedServer());
		if (remote == null) {
			return new Object[0];
		}

		return remote.getPrincipals(getSearchUserName());
	}

	public User getUserDetail() {
		return getUserManager().findByUserName((String) getLoopObject());
	}

	public String getUserFullName() {
		User user = getUserDetail();
		if (user == null) {
			return "";
		}

		return user.getUserFullName();
	}

	public IPage onKick(IRequestCycle cycle, String userName, String server) {
		RemoteSystemControl remote = getSystemControls().get(server);
		if (remote == null) {
			return null;
		}

		try {
			remote.forceLogout(userName);
		} catch (Throwable t) {
			log.error("unable to forced logout user " + userName + ", server " + server + ".", t);

			addError(getDelegate(), (IFormComponent) null, getText("activeUsers.error.onKick.message"),
					ValidationConstraint.CONSISTENCY);
			return null;
		}

		User user = getUserManager().findByUserName(userName);
		if (user != null) {
			getUserManager().kickUser(getUserLoginFromSession(), user, getRequest().getRemoteHost());
		}

		InfoPage infoPage = getInfoPage();
		InfoPageCommand infoPageCommand = new InfoPageCommand();
		infoPageCommand.setTitle(getText("activeUsers.success.title"));
		infoPageCommand.addMessage(getText("activeUsers.success.message", userName));
		infoPageCommand.setPrevPage(getPageName());
		infoPage.setInfoPageCommand(infoPageCommand);

		return infoPage;
	}

	public void onOk(IRequestCycle cycle) {
		serverValidate();
	}

	private void serverValidate() {
		if (getSelectedServer() == null) {
			addError(getDelegate(), (IFormComponent) null, getText("common.message.validation.required"),
					ValidationConstraint.CONSISTENCY);
			return;
		}
	}
}
