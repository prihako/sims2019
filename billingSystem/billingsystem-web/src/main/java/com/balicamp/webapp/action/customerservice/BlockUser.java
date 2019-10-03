package com.balicamp.webapp.action.customerservice;

import static com.balicamp.webapp.constant.WebConstant.User.STATUS_BLOCKED;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.user.User;
import com.balicamp.service.user.UserManager;
import com.balicamp.webapp.action.BasePage;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: BlockUser.java 345 2013-02-21 06:34:33Z bagus.sugitayasa $
 */
public abstract class BlockUser extends BasePage {

	@InjectSpring("userManager")
	public abstract UserManager getUserManager();

	public abstract String getUserId();

	public abstract void setUserId(String userId);

	@InjectPage("information")
	public abstract Information getInformation();

	@InjectPage("confirmBlock")
	public abstract ConfirmBlock getConfirmBlock();

	public IPage doBlock() {
		if (getDelegate().getHasErrors()) {
			return null;
		}

		User user = null;
		if (getUserId() == null) {
			setUserId("");
		}

		try {
			if (getUserId().equals("")) {
				addError(getDelegate(), (IFormComponent) null, getText("blockUser.userId.required"),
						ValidationConstraint.CONSISTENCY);
			} else {
				user = (User) getUserManager().loadUserByUsername(getUserId());

				if (user == null) {
					addError(getDelegate(), (IFormComponent) null, getText("blockUser.userId.invalid"),
							ValidationConstraint.CONSISTENCY);
				} /*
				 * else if (user.getStatus().equals(STATUS_ADMIN)) {
				 * addError(getDelegate(), (IFormComponent) null,
				 * getText("blockUser.userId.exceptAdmin"),
				 * ValidationConstraint.CONSISTENCY);
				 * }
				 */else if (user.getStatus().equals(STATUS_BLOCKED)) {
					addError(getDelegate(), (IFormComponent) null, getText("blockUser.userId.already"),
							ValidationConstraint.CONSISTENCY);
				}else if(user.getUsername().equalsIgnoreCase(getUserLoginFromDatabase().getUsername())){
					addError(getDelegate(), (IFormComponent) null, getText("blockUser.userId.active"),
							ValidationConstraint.CONSISTENCY);
				}
			}
		} catch (Exception e) {
			addError(getDelegate(), (IFormComponent) null, getText("blockUser.invalidUserId"),
					ValidationConstraint.CONSISTENCY);
		}

		if (getDelegate().getHasErrors()) {
			return null;
		}

		ConfirmBlock confirmBlock = getConfirmBlock();
		confirmBlock.setUserName(user.getUserName());
		return confirmBlock;
	}
}
