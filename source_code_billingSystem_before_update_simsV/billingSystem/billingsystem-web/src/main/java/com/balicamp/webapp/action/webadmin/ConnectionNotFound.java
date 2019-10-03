package com.balicamp.webapp.action.webadmin;

import org.apache.tapestry.IRequestCycle;

import com.balicamp.webapp.action.AdminBasePage;

public abstract class ConnectionNotFound extends AdminBasePage {

	private String connection;

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	public void onSubmit(IRequestCycle cycle) {
		cycle.activate(cycle.getPage("viewConnectionList"));
	}


}
