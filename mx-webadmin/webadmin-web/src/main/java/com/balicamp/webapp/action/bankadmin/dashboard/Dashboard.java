package com.balicamp.webapp.action.bankadmin.dashboard;

import java.util.Date;

import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.webapp.action.AdminBasePage;

public abstract class Dashboard extends AdminBasePage implements PageBeginRenderListener {

	// public abstract void setDashboardCurrentTime(Long dashboardCurrentTime);
	//
	// public abstract Long getDashboardCurrentTime();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		// Date date = new Date();
		// Long time = date.getTime();
		// setDashboardCurrentTime(time);
	}

	public Long getDashboardCurrentTime() {
		Date date = new Date();
		Long time = date.getTime();
		return time;
	}
}
