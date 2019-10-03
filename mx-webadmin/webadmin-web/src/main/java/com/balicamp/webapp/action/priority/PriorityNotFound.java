package com.balicamp.webapp.action.priority;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.webapp.action.AdminBasePage;

public abstract class PriorityNotFound extends AdminBasePage implements PageBeginRenderListener {

	protected final static Log log = LogFactory.getLog(PriorityNotFound.class);

	public abstract void setMsgnotfound(String msgnotfound);

	public abstract String getMsgnotfound();

	@InjectPage("priorityList")
	public abstract PriorityList getPriorityList();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	/**
	 * 
	 * @param cycle
	 * @return
	 */
	public IPage onSubmit(IRequestCycle cycle) {
		return getPriorityList();
	}
}
