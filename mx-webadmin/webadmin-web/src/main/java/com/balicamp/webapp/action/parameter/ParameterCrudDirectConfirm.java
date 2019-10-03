/**
 * 
 */
package com.balicamp.webapp.action.parameter;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.constant.ModelConstant;
import com.balicamp.model.constant.ModelConstant.PendingChange;
import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.model.parameter.SystemParameter;
import com.balicamp.model.user.Role;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: ParameterCrudDirectConfirm.java 337 2013-02-19 09:16:01Z bagus.sugitayasa $
 */
public abstract class ParameterCrudDirectConfirm extends AdminBasePage implements PageBeginRenderListener {

	public static final String MODE_ADD = "add";

	public static final String MODE_UPDATE = "edit";

	public static final String MODE_DELETE = "delete";

	@Persist("client")
	public abstract boolean getLocked();

	public abstract void setLocked(boolean locked);

	@InjectSpring("systemParameterManager")
	public abstract SystemParameterManager getSystemParameterManager();

	@InjectPage("tambahParameter")
	public abstract TambahDirectParameter getTambahDirectParameter();

	@InjectPage("parameterCrudDirectList")
	public abstract ParameterCrudDirectList getParameterCrudDirectList();

	@Persist("client")
	public abstract SystemParameter getParameter();

	public abstract void setParameter(SystemParameter parameter);

	@Persist("client")
	public abstract String getParameterName();

	public abstract void setParameterName(String parameterName);

	@Persist("client")
	public abstract String getParameterValue();

	public abstract void setParameterValue(String parameterValue);

	@Persist("client")
	public abstract String getMode();

	public abstract void setMode(String mode);

	@Persist("client")
	public abstract String getActionType();

	public abstract void setActionType(String actionType);

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	public IPage onBack(IRequestCycle cycle) {
		IPage nextPage = null;
		if (getActionType().equals(ModelConstant.PendingChange.ACTION_TYPE_DELETE)) {
			nextPage = getParameterCrudDirectList();
		} else if (getActionType().equals(ModelConstant.PendingChange.ACTION_TYPE_EDIT)) {
			nextPage = getParameterCrudDirectList();
		} else {
			nextPage = getTambahDirectParameter();
		}
		return nextPage;
	}

	@SuppressWarnings("unused")
	public ILink onNext(IRequestCycle cycle) {
		if (getDelegate().getHasErrors()) {
			return null;
		}
		// cekDoubleSubmit();
		if (getDelegate().getHasErrors()) {
			return null;
		}

		Role role1 = new Role();
		// FIXME:sesuaikan Role
		role1.setId((long) 1);
		Role role2 = new Role();
		// FIXME:sesuaikan Role
		// role2.setId((long) 31);

		// contruct status page
		InfoPageCommand infoPageCommand = new InfoPageCommand();
		if (getActionType().equals(PendingChange.ACTION_TYPE_ADD)) {
			// FIXME: save ke DB
			// getSystemParameterManager().saveOrUpdate(getParameter());

			infoPageCommand.setTitle(getText("crudprovider.title.notification.add"));
			infoPageCommand.addMessage(getText("crudprovider.result.direct.add.message"));
			infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.finish"),
					"tambahProvider.html"));
		} else {
			if (getActionType().equals(PendingChange.ACTION_TYPE_EDIT)) {
				// FIXME: save ke DB
				setParameter(getSystemParameterManager().findByParamName(getParameterName())); // getSystemParameterByName(getParameterName()));
				getParameter().setParamValue(getParameterValue());
				getSystemParameterManager().updateSystemParameter(getParameter());

				// write to audit log
				getUserManager().adminEditParameter(getUserLoginFromSession(), getParameter(),
						getRequest().getRemoteHost());

				infoPageCommand.setTitle(getText("crudparameter.title.notification.edit"));
				infoPageCommand.addMessage(getText("crudparameter.result.direct.edit.message"));
			}
			infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.finish"),
					"parameterCrudDirectList.html"));
		}

		infoPageCommand
				.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.welcome"), "wellcome.html"));

		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);

		return getEngineService().getLink(false, "infoPage");
	}
}
