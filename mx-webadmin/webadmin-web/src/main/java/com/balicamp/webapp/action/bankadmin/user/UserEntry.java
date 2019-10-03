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

	// public abstract void setUserParent(UserDisplay userParent);
	//
	// public abstract UserDisplay getUserParent();

	@InjectObject("spring:roleManager")
	public abstract RoleManager getRoleManager();

	@InjectObject("spring:userManager")
	public abstract UserManager getUserManager();

	@InjectPage("userConfirm")
	public abstract UserConfirm getUserConfirm();

	private BeanPropertySelectionModel roleModel = null;
	private BeanPropertySelectionModel userParentModel = null;

	public BeanPropertySelectionModel getRoleModel() {
		List<Role> roleList = getRoleManager().getEditableAdminRoleList();
		roleModel = new BeanPropertySelectionModel(roleList, "description");
		return roleModel;
	}

	// public BeanPropertySelectionModel getUserParentModel() {
	// SearchCriteria criteria = new SearchCriteria("user");
	//
	// criteria.addOrder(Order.asc("userName").ignoreCase());
	//
	// List<UserDisplay> userList = new ArrayList<UserDisplay>();
	// List<UserDisplay> userListFromDatabase =
	// getUserManager().findUser(criteria, -1, -1);
	//
	// UserDisplay root = new UserDisplay();
	// root.setUserId(0l);
	// root.setUserName("ROOT");
	// root.setName("ROOT");
	//
	// userList.add(root);
	// userList.addAll(userListFromDatabase);
	//
	// userParentModel = new BeanPropertySelectionModel(userList, "userName");
	// return userParentModel;
	// }

	public abstract List<String> getEmailDomainList();

	public abstract void setEmailDomainList(List<String> emailDomainList);

	public abstract List<User> getAllUserList();

	public abstract void setAllUserList(List<User> list);

	public void initEmailDomainList() {

		if (getEmailDomainList() == null) {
			setEmailDomainList(new ArrayList<String>());
			String emailDomainConfig = getSystemParameterManager().getStringValue(
					new SystemParameterId(Constants.SystemParameter.Mail.GROUP,
							Constants.SystemParameter.Mail.USER_ADMIN_DOMAIN_LIST),
					Constants.SystemParameter.Mail.USER_ADMIN_DOMAIN_LIST_DEFAULT);

			String[] emailDomainConfigSplited = emailDomainConfig.split(";");
			for (String tmpString : emailDomainConfigSplited) {
				if (!CommonUtil.isEmpty(tmpString)) {

					// getEmailDomainList().add(Constants.SystemParameter.Mail.USER_ADMIN_DOMAIN_LIST_DEFAULT);

					getEmailDomainList().add(tmpString);
				}
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

		setAllUserList(getUserManager().findAllUser());

	}

	@SuppressWarnings("unchecked")
	public IPage doSubmit() {
		if (getDelegate().getHasErrors())
			return null;

		serverValidate();

		for (User user : getAllUserList()) {
			if (user.getEmail().equals(getEmail())) {
				addError(getDelegate(), "email", "Email sudah digunakan", ValidationConstraint.CONSISTENCY);
				return null;
			}
		}
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

		System.out.println("hahaha user name " + getUserName());

		UserConfirm userConfirm = getUserConfirm();
		userConfirm.setUserId(null);
		userConfirm.setRoleId(getRole().getId());
		userConfirm.setUserName(getUserName());
		userConfirm.setName(getName());
		userConfirm.setEmail(getEmail());
		// userConfirm.setUserParent(getUse);
		return userConfirm;
	}

	private final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private final String USER_ID_PATTER = "^[a-zA-Z0-9]*$";

	public void serverValidate() {
		if (getUserName() == null) {
			addError(getDelegate(), (IFormComponent) getComponent("userId"), getText("userEntry.userId.required"),
					ValidationConstraint.CONSISTENCY);
			return;
		}

		if (!getUserName().matches(USER_ID_PATTER)) {
			addError(getDelegate(), (IFormComponent) getComponent("userId"), getText("userEntry.userId.pattern"),
					ValidationConstraint.CONSISTENCY);
			return;
		}
		if (getUserName().length() < 3) {
			addError(getDelegate(), (IFormComponent) getComponent("userId"), getText("userEntry.userId.min.length"),
					ValidationConstraint.CONSISTENCY);
			return;
		}
		if (getUserName().length() > 10) {
			addError(getDelegate(), (IFormComponent) getComponent("userId"), getText("userEntry.userId.max.length"),
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

		if (!getEmail().matches(EMAIL_PATTERN)) {
			addError(getDelegate(), (IFormComponent) getComponent("email"), getText("userEntry.email.format"),
					ValidationConstraint.CONSISTENCY);
			return;
		}
	}
}
