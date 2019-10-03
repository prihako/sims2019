package com.balicamp.webapp.action.customerservice;

import static com.balicamp.webapp.constant.WebConstant.User.STATUS_BLOCKED;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.user.User;
import com.balicamp.service.user.UserManager;
import com.balicamp.webapp.action.BasePage;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: UnblockUser.java 345 2013-02-21 06:34:33Z bagus.sugitayasa $
 */
public abstract class UnblockUser extends BasePage {

	@InjectObject("spring:userManager")
	public abstract UserManager getUserManager();

	public abstract String getUserId();

	public abstract void setUserId(String userId);

	@InjectPage("information")
	public abstract Information getInformation();

	@InjectPage("confirmUnblock")
	public abstract ConfirmUnblock getConfirmUnblock();

	public IPage doUnblock() {
		if (getDelegate().getHasErrors()) {
			return null;
		}

		User user = null;
		if (getUserId() == null) {
			setUserId("");
		}

		try {
			if (getUserId().equals("")) {
				addError(getDelegate(), (IFormComponent) null, getText("unblockUser.userId.required"),
						ValidationConstraint.CONSISTENCY);
			} else {
				user = (User) getUserManager().loadUserByUsername(getUserId());

				if (user == null) {
					addError(getDelegate(), (IFormComponent) null, getText("unblockUser.userId.invalid"),
							ValidationConstraint.CONSISTENCY);
				} /*
				 * else if (user.getStatus().equals(STATUS_ADMIN)) {
				 * addError(getDelegate(), (IFormComponent) null,
				 * getText("unblockUser.userId.exceptAdmin"),
				 * ValidationConstraint.CONSISTENCY);
				 * }
				 */else if (!user.getStatus().equals(STATUS_BLOCKED)) {
					addError(getDelegate(), (IFormComponent) null, getText("unblockUser.userId.already"),
							ValidationConstraint.CONSISTENCY);
				}
			}
		} catch (Exception e) {
			addError(getDelegate(), (IFormComponent) null, getText("unblockUser.invalidUserId"),
					ValidationConstraint.CONSISTENCY);
		}

		if (getDelegate().getHasErrors()) {
			return null;
		}

		ConfirmUnblock confirmUnblock = getConfirmUnblock();
		confirmUnblock.setUserName(user.getUserName());

		return confirmUnblock;
	}
}
