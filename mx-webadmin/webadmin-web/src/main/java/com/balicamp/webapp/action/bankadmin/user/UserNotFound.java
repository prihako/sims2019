package com.balicamp.webapp.action.bankadmin.user;

import org.apache.tapestry.IRequestCycle;

import com.balicamp.webapp.action.AdminBasePage;

public abstract class UserNotFound extends AdminBasePage {
	
	private String uName;
	
	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public void onSubmit(IRequestCycle cycle) {
		cycle.activate(cycle.getPage("userList"));
	}

}
