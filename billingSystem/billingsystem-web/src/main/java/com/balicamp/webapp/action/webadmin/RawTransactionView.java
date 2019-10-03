package com.balicamp.webapp.action.webadmin;

import org.apache.tapestry.event.PageEvent;

import com.balicamp.webapp.action.BasePage;

public abstract class RawTransactionView extends BasePage {

	public abstract String getFormattedRaw();

	public abstract void setFormattedRaw(String raw);

	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
	}

}
