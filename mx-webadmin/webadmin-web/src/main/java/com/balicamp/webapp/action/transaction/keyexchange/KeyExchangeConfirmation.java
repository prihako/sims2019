package com.balicamp.webapp.action.transaction.keyexchange;

import java.util.HashMap;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.page.WaitingPageCommand;
import com.balicamp.webapp.action.BasePage;

public abstract class KeyExchangeConfirmation extends BasePage implements PageBeginRenderListener {

	@InjectPage("keyExchangeNotification")
	public abstract KeyExchangeNotification getNextPage();

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	public ILink onNext(IRequestCycle cycle) {
		WaitingPageCommand waitingPageCommand = new WaitingPageCommand();
		waitingPageCommand.setNextPage(getNextPage().getPageName());
		waitingPageCommand.setNextPageInitMethod("setModel");
		waitingPageCommand.setPreviousPage("keyExchangeInput");

		HashMap data = getData();

		data.put("channelMappingCode", "ATMB_KEY_EXCHANGE_WEB");

		ILink iLink = getWaitingPage().send(waitingPageCommand, data, cycle);

		//

		if (iLink == null)
			return getEngineService().getLink(false, getWaitingPage().getPageName());

		return iLink;
	}
}
