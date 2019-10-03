package com.balicamp.webapp.action.bankadmin;

import java.util.HashSet;
import java.util.Set;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.constant.ModelConstant;
import com.balicamp.model.user.Role;
import com.balicamp.model.user.User;
import com.balicamp.model.user.UserDisplay;
import com.balicamp.model.user.UserRoleId;
import com.balicamp.service.user.RoleManager;
import com.balicamp.service.user.UserManager;
import com.balicamp.service.user.UserRoleManager;
import com.balicamp.webapp.action.AdminBasePage;

public abstract class MemberDeleteConfirm extends AdminBasePage implements PageBeginRenderListener {
	@InjectObject("spring:userManager")
	public abstract UserManager getUserManager();

	@InjectObject("spring:userRoleManager")
	public abstract UserRoleManager getUserRoleManager();

	@InjectPage("memberDeleteSuccess")
	public abstract MemberDeleteSuccess getMemberDeleteSuccess();

	@InjectPage("memberList")
	public abstract MemberList getMemberList();

	public abstract void setMemberToDelete(Set<UserDisplay> memberToDelete);

	public abstract Set<UserDisplay> getMemberToDelete();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		if (getMemberToDelete() == null) {
			setMemberToDelete(new HashSet<UserDisplay>());
		}
		setMessage(getText("memberDeleteConfirm.info"));
	}

	@InjectObject("spring:roleManager")
	public abstract RoleManager getRoleManager();

	public IPage doDelete() {
		if (getMemberToDelete() == null)
			return null;
		Role role = getRoleManager().findById(ModelConstant.Role.ROLE_ID_CUSTOMER_WITH_MEMBER);
		for (UserDisplay userDisplay : getMemberToDelete()) {
			//if (userDisplay.getRoleId() != null) {
			User userChild = getUserManager().findById(userDisplay.getUserId());
			User userParent = getUserManager().findById(userChild.getUserParentId());
			//getUserRoleManager().deleteByUserId(userDisplay.getUserId());
			UserRoleId userRoleId = new UserRoleId(userParent, role);
			//UserRole userRole = new UserRole(userRoleId);			
			getUserRoleManager().deleteByRoleId(userRoleId.getRole().getId());
			userChild.setUserParentId(ModelConstant.User.STATUS_PARENT);
			getUserManager().save(userChild);
			//}						
		}
		MemberDeleteSuccess memberDeleteSuccess = getMemberDeleteSuccess();
		memberDeleteSuccess.setDeletedUser(getMemberToDelete());
		return memberDeleteSuccess;
	}

	public IPage doCancel() {
		MemberList memberList = getMemberList();
		return memberList;
	}

}
