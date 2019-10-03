/**
 * 
 */
package com.balicamp.webapp.action.parameter;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.parameter.SystemParameter;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.util.CommonUtil;
import com.balicamp.webapp.action.AdminBasePage;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TambahDirectParameter.java 337 2013-02-19 09:16:01Z bagus.sugitayasa $
 */
public abstract class TambahDirectParameter extends AdminBasePage implements PageBeginRenderListener {
	public static final String MODE_ADD = "add";

	public static final String MODE_UPDATE = "edit";

	public static final boolean LOCKED = false;

	public static final long PARENT_ID = 51;

	// untuk Locked
	@Persist("client")
	public abstract boolean getLocked();

	public abstract void setLocked(boolean locked);

	@InjectSpring("systemParameterManager")
	public abstract SystemParameterManager getSystemParameterManager();

	@InjectPage("parameterCrudDirectConfirm")
	public abstract ParameterCrudDirectConfirm getParameterCrudDirectConfirm();

	@InjectPage("tambahParameter")
	public abstract TambahDirectParameter getTambahDirectParameter();

	@InjectPage("parameterCrudDirectList")
	public abstract ParameterCrudDirectList getParameterCrudDirectList();

	@Persist("client")
	public abstract SystemParameter getSystemParameter();

	public abstract void setSystemParameter(SystemParameter parameter);

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
		// IRequestCycle cycle = pageEvent.getRequestCycle();
	}

	/**
	 * @param cycle
	 * @return
	 */
	public IPage onNext(IRequestCycle cycle) {
		if (getDelegate().getHasErrors()) {
			return null;
		}

		// cekDoubleSubmit();
		if (getDelegate().getHasErrors()) {
			return null;
		}
		// serverValidate();
		if (getDelegate().getHasErrors()) {
			return null;
		}

		ParameterCrudDirectConfirm nextPage = getParameterCrudDirectConfirm();
		// get field 'type' & cek type of value entered by user
		SystemParameter systemParameter = getSystemParameterManager().findByParamName(getParameterName());
		String type = systemParameter.getType();
		if (type != null) {

			boolean valid = CommonUtil.isNumeric(getParameterValue());
			if (valid) {

				nextPage.setParameterValue(getParameterValue());
				nextPage.setMode(MODE_UPDATE.equals(getMode()) ? MODE_UPDATE : MODE_ADD);
				nextPage.setParameter(getSystemParameter());
				nextPage.setParameterName(getParameterName());
				nextPage.setActionType(MODE_UPDATE.equals(getMode()) ? MODE_UPDATE : MODE_ADD);
				nextPage.setLocked(getLocked());

				return nextPage;

			} else {

				addError(getDelegate(), "codeField", getText("crudparameter.error.formatNotValid"),
						ValidationConstraint.CONSISTENCY);
				return null;
			}

		} else {
			nextPage.setParameterValue(getParameterValue());
			nextPage.setMode(MODE_UPDATE.equals(getMode()) ? MODE_UPDATE : MODE_ADD);
			nextPage.setParameter(getSystemParameter());
			nextPage.setParameterName(getParameterName());
			nextPage.setActionType(MODE_UPDATE.equals(getMode()) ? MODE_UPDATE : MODE_ADD);
			nextPage.setLocked(getLocked());

			return nextPage;
		}

	}

	public IPage onBack(IRequestCycle cycle) {
		IPage nextPage = null;
		// FIXME:action type "ACTION_TYPE_DELETE"
		if (getActionType().equals(MODE_ADD)) {
			nextPage = getParameterCrudDirectList();
		} else {
			getTambahDirectParameter().setSystemParameter(getSystemParameter());
			nextPage = getTambahDirectParameter();
		}

		clearSession();
		return nextPage;
	}

	protected void clearSession() {
		setSystemParameter(null);
		setActionType(null);
	}
}
