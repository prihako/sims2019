package com.balicamp.webapp.action.customerservice;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.user.User;
import com.balicamp.webapp.action.BasePage;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: ConfirmUnblock.java 337 2013-02-19 09:16:01Z bagus.sugitayasa $
 */
public abstract class ConfirmUnblock extends BasePage implements PageBeginRenderListener {

	public abstract void setUserName(String userName);

	public abstract String getUserName();

	@InjectPage("unblockUser")
	public abstract UnblockUser getUnblockUser();

	public abstract User getUserToUnblock();

	public abstract void setUserToUnblock(User userToUnblock);

	@InjectPage("information")
	public abstract Information getInformation();

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if (isRealRender()) {
			if (getUserToUnblock() == null)
				setUserToUnblock((User) getUserManager().loadUserByUsername(getUserName()));
		}
	}

	public IPage doOk() {
		if (getDelegate().getHasErrors()) {
			return null;
		}

		cekDoubleSubmit();
		if (getDelegate().getHasErrors()) {
			return null;
		}

		if (getUserName() == null) {
			return getUnblockUser();
		}

		User user = (User) getUserManager().loadUserByUsername(getUserName());
		getUserManager().unblockUser(user);

		Information info = getInformation();
		info.setInformationTitle(getText("unblockUser.successTitle"));
		info.setInformationBody(getText("unblockUser.successBody", user.getUserFullName()));

		return info;
	}

	public IPage doCancel() {
		return getUnblockUser();
	}
}
