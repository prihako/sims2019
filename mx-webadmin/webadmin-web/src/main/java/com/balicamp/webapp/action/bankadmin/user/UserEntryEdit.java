package com.balicamp.webapp.action.bankadmin.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.BeanPropertySelectionModel;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.Constants;
import com.balicamp.model.parameter.SystemParameterId;
import com.balicamp.model.user.Role;
import com.balicamp.model.user.UserDisplay;
import com.balicamp.service.user.RoleManager;
import com.balicamp.service.user.UserRoleManager;
import com.balicamp.util.CommonUtil;
import com.balicamp.util.StringUtil;
import com.balicamp.webapp.action.AdminBasePage;

public abstract class UserEntryEdit extends AdminBasePage implements PageBeginRenderListener {

	private final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public abstract void setRole(Role role);

	public abstract Role getRole();

	public abstract void setRoleOld(Role role);

	public abstract Role getRoleOld();

	public abstract void setUserId(Long userId);

	public abstract Long getUserId();

	public abstract void setUserName(String userName);

	public abstract String getUserName();

	public abstract void setEmail(String email);

	public abstract String getEmail();

	public abstract void setName(String name);

	public abstract String getName();

	public abstract void setRoleId(Long roleId);

	public abstract Long getRoleId();

	public abstract void setRoleIdOld(Long roleId);

	public abstract Long getRoleIdOld();

	@Persist("client")
	public abstract String getSearchUserId();

	public abstract void setSearchUserId(String searchUserId);

	@InjectPage("userConfirm")
	public abstract UserConfirm getUserConfirm();

	@InjectPage("userList")
	public abstract UserList getUserList();

	@InjectObject("spring:userRoleManager")
	public abstract UserRoleManager getUserRoleManager();

	@InjectObject("spring:roleManager")
	public abstract RoleManager getRoleManager();

	// private BeanPropertySelectionModel roleModel = null;

	@Persist("client")
	public abstract BeanPropertySelectionModel getRoleModel();

	public abstract void setRoleModel(BeanPropertySelectionModel roleModel);

	protected final Log log = LogFactory.getLog(UserEntryEdit.class);

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

		if (isRealRender()) {
			if (getRoleId() != null) {
				for (int i = 0; i < getRoleModel().getOptionCount(); i++) {
					Role role = (Role) getRoleModel().getOption(i);
					if (role.getId().equals(getRoleId())) {
						setRole(role);
						break;
					}
				}
			}
		}
	}

	// public BeanPropertySelectionModel getRoleModel() {
	// if (roleModel == null) {
	// List<Role> roleList = getRoleManager().getEditableAdminRoleList();
	// Role branchMAYORA = getRoleManager().findByName("MAYORA");
	// roleList.add(branchMAYORA);
	// roleModel = new BeanPropertySelectionModel(roleList, "name");
	// }
	//
	// return roleModel;
	// }

	public IPage doSubmit(IRequestCycle cycle) {
		if (getEmail() == null) {
			addError(getDelegate(), (IFormComponent) getComponent("errorShadow"), getText("userEntry.email.required"),
					ValidationConstraint.CONSISTENCY);
			return null;
		} else if (!getEmail().matches(EMAIL_PATTERN)) {
			addError(getDelegate(), (IFormComponent) getComponent("errorShadow"), getText("userEntry.email.format"),
					ValidationConstraint.CONSISTENCY);
			return null;
		}

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
				addError(getDelegate(), "errorShadow", getText("userEntry.validation.emailDomainNotMatch"),
						ValidationConstraint.EMAIL_FORMAT);
				return null;
			}
		}

		UserConfirm userConfirm = getUserConfirm();
		userConfirm.setUserId(getUserId());
		userConfirm.setRoleIdOld(getRoleOld().getId());
		userConfirm.setRoleId(getRole().getId());

		System.out.println("getEmail() : " + getEmail());

		userConfirm.setUserName(getUserName());
		userConfirm.setEmail(getEmail());
		userConfirm.setName(getName());
		userConfirm.setSearchUserId(getSearchUserId());
		// userConfirm.setUserParent(getUserParent());
		return userConfirm;
	}

	public IPage onBack(IRequestCycle cycle) {
		getUserList().setUserId(getSearchUserId());
		clearSession();
		return getUserList();
	}

	private void clearSession() {
		setSearchUserId(null);
	}

	public void serverValidate() {
		if (getEmail() == null) {
			System.out.println("email kosong");
			addError(getDelegate(), (IFormComponent) getComponent("errorShadow"), getText("userEntry.email.required"),
					ValidationConstraint.CONSISTENCY);
			return;
		} else if (!getEmail().matches(EMAIL_PATTERN)) {
			System.out.println("email pattern salah");
			addError(getDelegate(), (IFormComponent) getComponent("errorShadow"), getText("userEntry.email.format"),
					ValidationConstraint.CONSISTENCY);
			return;
		}
	}

	// private BeanPropertySelectionModel userParentModel = null;

	// public BeanPropertySelectionModel getUserParentModel() {
	// SearchCriteria criteria = new SearchCriteria("user");
	// criteria.addOrder(Order.asc("userName").ignoreCase());
	//
	// List<UserDisplay> userList = new ArrayList<UserDisplay>();
	//
	// List<UserDisplay> userListFromDatabase = getUserManager().findUser(new
	// SearchCriteria("user"), -1, -1);
	//
	// UserDisplay root = new UserDisplay();
	// root.setUserId(0l);
	// root.setUserName("ROOT");
	// root.setName("ROOT");
	//
	// userList.add(root);
	// userList.addAll(userListFromDatabase);
	//
	// BeanPropertySelectionModel userParentModel = new
	// BeanPropertySelectionModel(userList, "userName");
	// return userParentModel;
	// }
	@Persist("client")
	public abstract BeanPropertySelectionModel getUserParentModel();

	public abstract void setUserParentModel(BeanPropertySelectionModel userParentModel);

	public abstract void setUserParent(UserDisplay userParent);

	@Persist("client")
	public abstract UserDisplay getUserParent();
}
