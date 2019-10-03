package com.balicamp.webapp.action.transaction.keyexchange;

import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.webapp.action.BasePage;

public abstract class KeyExchangeNotification extends BasePage implements PageBeginRenderListener {

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		setMessage(getText("keyExchange.message.success"));
	}
}
