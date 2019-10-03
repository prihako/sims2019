package com.balicamp.webapp.action.customerservice;

import java.util.Date;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.constant.ModelConstant;
import com.balicamp.model.user.User;
import com.balicamp.service.user.UserManager;
import com.balicamp.webapp.action.BasePage;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: ConfirmBlock.java 345 2013-02-21 06:34:33Z bagus.sugitayasa $
 */
public abstract class ConfirmBlock extends BasePage implements PageBeginRenderListener {

	public abstract void setUserName(String userName);

	public abstract String getUserName();

	@InjectPage("blockUser")
	public abstract BlockUser getBlockUser();

	public abstract User getUserToBlock();

	public abstract void setUserToBlock(User userToBlock);

	@InjectPage("information")
	public abstract Information getInformation();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if (isRealRender()) {
			if (getUserToBlock() == null)
				setUserToBlock((User) getUserManager().loadUserByUsername(getUserName()));
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
			return getBlockUser();
		}

		UserManager userManager = getUserManager();

		User user = (User) getUserManager().loadUserByUsername(getUserName());
		userManager.loadUserProperties(user, true);

		user.getUserPropertiesObject().setPreviousStatus(user.getStatus());
		user.getUserPropertiesObject().setBlockReason(ModelConstant.User.BLOCK_BY_ADMIN);
		userManager.saveUserProperties(user, false, false);

		user.setStatus(ModelConstant.User.STATUS_BLOCKED);
//		user.setAccountLocked(true);
		user.setBlockTime(new Date());
		user.setBlockInterval(-1);

		userManager.saveOrUpdate(user);

		// save to audit log
		userManager.csBlockUser(getUserLoginFromSession(), user, getRequest().getRemoteHost());

		Information info = getInformation();
		info.setInformationTitle(getText("blockUser.successTitle"));
		info.setInformationBody(getText("blockUser.successBody", user.getUserFullName()));

		return info;
	}

	public IPage doCancel() {
		return getBlockUser();
	}
}
