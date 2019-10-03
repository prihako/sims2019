package com.balicamp.webapp.action.bankadmin.user;

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

import com.balicamp.model.user.Role;
import com.balicamp.service.user.RoleManager;
import com.balicamp.service.user.UserRoleManager;
import com.balicamp.webapp.action.AdminBasePage;

public abstract class UserEntryEdit extends AdminBasePage implements PageBeginRenderListener {

	public abstract void setRole(Role role);

	public abstract Role getRole();

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

	private BeanPropertySelectionModel roleModel = null;

	protected final Log log = LogFactory.getLog(UserEntryEdit.class);

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

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

	public BeanPropertySelectionModel getRoleModel() {
//		if (roleModel == null) {
			List<Role> roleList = getRoleManager().getEditableAdminRoleList();
//			Role branchMAYORA = getRoleManager().findByName("MAYORA");
//			roleList.add(branchMAYORA);
			roleModel = new BeanPropertySelectionModel(roleList, "name");
//		}

		return roleModel;
	}

	public IPage doSubmit(IRequestCycle cycle) {
		UserConfirm userConfirm = getUserConfirm();
		userConfirm.setUserId(getUserId());
		userConfirm.setRoleId(getRole().getId());
		userConfirm.setUserName(getUserName());
		userConfirm.setEmail(getEmail());
		userConfirm.setName(getName());
		userConfirm.setSearchUserId(getSearchUserId());
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
}
