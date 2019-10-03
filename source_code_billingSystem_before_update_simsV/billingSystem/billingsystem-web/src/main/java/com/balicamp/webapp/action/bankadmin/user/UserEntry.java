package com.balicamp.webapp.action.bankadmin.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.BeanPropertySelectionModel;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.ValidationConstraint;
import org.hibernate.criterion.Restrictions;

import com.balicamp.Constants;
import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.parameter.SystemParameterId;
import com.balicamp.model.user.Role;
import com.balicamp.model.user.User;
import com.balicamp.service.user.RoleManager;
import com.balicamp.service.user.UserManager;
import com.balicamp.util.CommonUtil;
import com.balicamp.util.StringUtil;
import com.balicamp.webapp.action.AdminBasePage;

public abstract class UserEntry extends AdminBasePage implements PageBeginRenderListener {

	protected final Log log = LogFactory.getLog(UserEntry.class);

	public abstract void setUserName(String userName);

	public abstract String getUserName();

	public abstract void setEmail(String email);

	public abstract String getEmail();

	public abstract void setName(String name);

	public abstract String getName();

	public abstract void setRole(Role role);

	public abstract Role getRole();

	@InjectObject("spring:roleManager")
	public abstract RoleManager getRoleManager();

	@InjectObject("spring:userManager")
	public abstract UserManager getUserManager();

	@InjectPage("userConfirm")
	public abstract UserConfirm getUserConfirm();

	private BeanPropertySelectionModel roleModel = null;

	public BeanPropertySelectionModel getRoleModel() {
		// if (roleModel == null) {
		List<Role> roleList = getRoleManager().getEditableAdminRoleList();
		roleModel = new BeanPropertySelectionModel(roleList, "name");
		// }
		return roleModel;
	}

	public abstract List<String> getEmailDomainList();

	public abstract void setEmailDomainList(List<String> emailDomainList);

	public void initEmailDomainList() {
		if (getEmailDomainList() == null) {
			setEmailDomainList(new ArrayList<String>());
			String emailDomainConfig = getSystemParameterManager().getStringValue(
					new SystemParameterId(Constants.SystemParameter.Mail.GROUP,
							Constants.SystemParameter.Mail.USER_ADMIN_DOMAIN_LIST),
					Constants.SystemParameter.Mail.USER_ADMIN_DOMAIN_LIST_DEFAULT);
			String[] emailDomainConfigSplited = emailDomainConfig.split(";");
			for (String tmpString : emailDomainConfigSplited) {
				if (!CommonUtil.isEmpty(tmpString))
					getEmailDomainList().add(tmpString);
			}

			if (Constants.DEVELOPMENT_MODE) {
				getEmailDomainList().add("balicamp.com");
			}

		}
	}

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		initEmailDomainList();
	}

	@SuppressWarnings("unchecked")
	public IPage doSubmit() {
		if (getDelegate().getHasErrors())
			return null;

		serverValidate();

		if (getDelegate().getHasErrors())
			return null;

		// cek email pattern
		if (getEmailDomainList().size() > 0) {
			String emailDomain = StringUtil.getEmailDomain(getEmail());
			boolean emailMatch = false;
			for (String cekEmailDomain : getEmailDomainList()) {
				if (emailDomain.equalsIgnoreCase(cekEmailDomain)) {
					emailMatch = true;
					break;
				}
			}
			if (!emailMatch) {
				addError(getDelegate(), "email", getText("userEntry.validation.emailDomainNotMatch"),
						ValidationConstraint.EMAIL_FORMAT);
				return null;
			}
		}

		// check user already exists
		UserManager userManager = getUserManager();
		SearchCriteria searchCriteria = new SearchCriteria("user");
		searchCriteria.addCriterion(Restrictions.eq("userName", getUserName()));
		searchCriteria.addCriterion(Restrictions.eq("enabled", true));
		List<User> userList = (List<User>) userManager.findByCriteria(searchCriteria, -1, -1);
		if (userList != null && userList.size() > 0) {
			addError(getDelegate(), (IFormComponent) null, getText("userEntry.userId.exist"),
					ValidationConstraint.CONSISTENCY);
		}

		if (getDelegate().getHasErrors())
			return null;

		UserConfirm userConfirm = getUserConfirm();
		userConfirm.setUserId(null);
		userConfirm.setRoleId(getRole().getId());
		userConfirm.setUserName(getUserName());
		userConfirm.setName(getName());
		userConfirm.setEmail(getEmail());
		return userConfirm;
	}

	public void serverValidate() {
		if (getUserName() == null) {
			addError(getDelegate(), (IFormComponent) getComponent("userId"), getText("userEntry.userId.required"),
					ValidationConstraint.CONSISTENCY);
			return;
		}
		if (getName() == null) {
			addError(getDelegate(), (IFormComponent) getComponent("name"), getText("userEntry.name.required"),
					ValidationConstraint.CONSISTENCY);
			return;
		}
		if (getEmail() == null) {
			addError(getDelegate(), (IFormComponent) getComponent("email"), getText("userEntry.email.required"),
					ValidationConstraint.CONSISTENCY);
			return;
		}
		if (!getEmail().matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
			addError(getDelegate(), (IFormComponent) getComponent("email"), getText("userEntry.email.format"),
					ValidationConstraint.CONSISTENCY);
			return;
		}
	}
}
