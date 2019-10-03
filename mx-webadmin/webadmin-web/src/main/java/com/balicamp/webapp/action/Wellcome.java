package com.balicamp.webapp.action;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.Constants;
import com.balicamp.model.constant.ModelConstant;
import com.balicamp.model.user.User;
import com.balicamp.service.user.UserManager;
import com.balicamp.util.DateFormatSymbolsIn;
import com.balicamp.util.SecurityContextUtil;
import com.balicamp.webapp.constant.WebConstant;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public abstract class Wellcome extends AdminBasePage implements
		PageBeginRenderListener {

	private String notification = "";
	private String MCHP = "Change Password";

	public abstract void setInformation(String information);

	public abstract String getInformation();

	public abstract void setUnreadMailCount(Integer unreadMailCount);

	public abstract Integer getUnreadMailCount();

	@InjectObject("spring:userManager")
	public abstract UserManager getUserManager();

	/*
	 * @InjectObject("spring:internalEmailManager") public abstract
	 * InternalEmailManager getInternalEmailManager();
	 */

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		if (isRealRender()) {
			String msg = pageEvent.getRequestCycle().getParameter("msg");
			if (msg != null) {
				setInformation(getText(msg));
			} else {
				setInformation("");
			}
		} else {
			if (getUnreadMailCount() == null) {
				setUnreadMailCount(0);
			}
		}
	}

	public User getUserLoginFromSession() {
		return super.getUserLoginFromSession();
	}

	public Date getLastLogin() {
		return (Date) getSession().getAttribute(
				Constants.HttpSessionAttribute.LAST_LOGIN_DATE);
	}

	@SuppressWarnings("unused")
	public boolean isShowActivationPageLink() {
		String userStatus = getUserLoginFromSession().getStatus();
		String userSecurityType = getUserLoginFromSession().getSecurityType();

		if (userStatus.equals(ModelConstant.User.STATUS_INQUIRY_ONLY))
			return true;

		return false;
	}

	public Format getDateFormatWellcome() {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm",
				new DateFormatSymbolsIn());
	}

	public String getUnreadMail() {
		if (SecurityContextUtil.getCurrentUser().getStatus()
				.equalsIgnoreCase(ModelConstant.User.STATUS_ADMIN))
			return getText("wellcome.unreadMailAdmin", " "
					+ getUnreadMailCount());
		return getText("wellcome.unreadMail", " " + getUnreadMailCount());
	}

	public boolean isCustomer() {
		return !SecurityContextUtil.getCurrentUser().getStatus()
				.equals(ModelConstant.User.STATUS_ADMIN);
	}

	public String getNotification() {
		User user = getUserLoginFromSession();
		if (user.isMustChangePassword()) {
			// return getText("login.passwordhint.change");
			return WebConstant.CONFIRM_PASSWORD_CHANGE;
		}
		return "";
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	public String getMCHP() {
		if (getUserLoginFromSession().isMustChangePassword())
			return MCHP;
		else
			return "";
	}

	public void setMCHP(String mCHP) {
		MCHP = mCHP;
	}

	public void gotoCHP(IRequestCycle cycle) {
		cycle.activate(cycle.getPage("changePassword"));
	}
}