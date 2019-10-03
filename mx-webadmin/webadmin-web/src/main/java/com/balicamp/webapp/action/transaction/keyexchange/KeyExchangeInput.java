package com.balicamp.webapp.action.transaction.keyexchange;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.webapp.action.BasePage;

public abstract class KeyExchangeInput extends BasePage implements PageBeginRenderListener {

	@InjectPage("keyExchangeConfirmation")
	public abstract KeyExchangeConfirmation getNextPage();

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	public IPage onNext(IRequestCycle cycle) {

		if (getDelegate().getHasErrors())
			return null;
		getNextPage().setPrevPage("keyExchangeInput.html");
		return getNextPage();
	}
}
