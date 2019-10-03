package com.balicamp.webapp.action.common;

import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.webapp.action.BasePage;

public abstract class InfoPage extends BasePage implements PageBeginRenderListener {

	@Persist("session")
	public abstract InfoPageCommand getInfoPageCommand();

	public abstract void setInfoPageCommand(InfoPageCommand infoPageCommand);

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	public boolean isLastMessage() {
		return getLoopIndex() + 1 == getInfoPageCommand().getMessageList().size();
	}

	public boolean isTableMessage() {
		if (getInfoPageCommand() != null && getInfoPageCommand().getMessageTableList() != null &&
				getInfoPageCommand().getMessageTableList().size() > 0) {
			return true;
		}

		return false;
	}

}
