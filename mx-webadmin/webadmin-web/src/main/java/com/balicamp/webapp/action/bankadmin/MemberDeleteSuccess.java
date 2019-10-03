package com.balicamp.webapp.action.bankadmin;

import java.util.Set;

import com.balicamp.model.user.UserDisplay;
import com.balicamp.webapp.action.AdminBasePage;

public abstract class MemberDeleteSuccess extends AdminBasePage {
	public abstract void setDeletedUser(Set<UserDisplay> deletedUser);

	public abstract Set<UserDisplay> getDeletedUser();

}
