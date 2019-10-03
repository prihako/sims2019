package com.balicamp.webapp.action.bankadmin.user;

import static com.balicamp.webapp.constant.WebConstant.User.STATUS_BLOCKED;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.user.User;
import com.balicamp.service.user.UserManager;
import com.balicamp.util.CommonUtil;
import com.balicamp.webapp.action.AdminBasePage;

public abstract class ChangePassword extends AdminBasePage implements PageBeginRenderListener {

	private String password;
	private String confirmPassword;
	private String oldPassword;

	@InjectObject("spring:userManager")
	public abstract UserManager getUserManager();

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		setPassword("");
		setOldPassword("");
		setConfirmPassword("");
		super.pageBeginRender(pageEvent);
	}

	public void doSubmit(IRequestCycle cycle) {
		User userLogin = getUserLoginFromSession();

		validatePassword();
		if (getDelegate().getHasErrors()) {
			return;
		}

		if (userLogin.isMustChangePassword() || !userLogin.getStatus().equals(STATUS_BLOCKED)) {
			userLogin.setAccountLocked(Boolean.FALSE);
			userLogin.setMustChangePassword(Boolean.FALSE);
			userLogin.setCredentialExpired(Boolean.FALSE);
			getUserManager().changePassword(userLogin, confirmPassword);
			
			//Save to audit log 
			getUserManager().changePasswordAuditLog(initUserLoginFromDatabase(), getRequest().getRemoteHost());
			
			cycle.activate(cycle.getPage("changePasswordConfirm"));
		}
	}

	/**
	 * validate password
	 */
	private void validatePassword() {

		if (CommonUtil.isEmpty(getOldPassword(), true)) {
			addError(getDelegate(), "oldPassword", getText("changePassword.oldPassword.message.isEmpty"),
					ValidationConstraint.CONSISTENCY);

			return;
		}

		boolean oldPasswordValid = CommonUtil.validatePassword(getUserLoginFromSession().getPassword(),
				getUserManager().encodePassword(getUserLoginFromSession(), getOldPassword()));

		// validate old password
		if (oldPasswordValid) {

			// cannot empty new password
			if (CommonUtil.isEmpty(getPassword(), true)) {
				addError(getDelegate(), "password", getText("changePassword.message.isEmpty"),
						ValidationConstraint.CONSISTENCY);

				return;
			}

			// cannot empty confirm password
			if (CommonUtil.isEmpty(getConfirmPassword(), true)) {
				addError(getDelegate(), "confirmPassword", getText("changePassword.message.isEmpty"),
						ValidationConstraint.CONSISTENCY);

				return;
			}

			if (!CommonUtil.validatePassword(getPassword(), getConfirmPassword())) {
				addError(getDelegate(), "password", getText("userInformation.information.confirmPasswordNotValid"),
						ValidationConstraint.CONSISTENCY);
			}

		} else {
			addError(getDelegate(), "oldPassword", getText("userInformation.information.confirmPasswordNotValid"),
					ValidationConstraint.CONSISTENCY);
		}

	}

}
