package com.balicamp.webapp.action.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.service.priority.PriorityManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.Wellcome;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class NotificationPage extends AdminBasePage implements PageBeginRenderListener {

	protected final static Log log = LogFactory.getLog(NotificationPage.class);

	public abstract void setTitle(String title);

	@Persist("client")
	public abstract String getMsgnotif();

	public abstract void setMsgnotif(String msgnotif);

	@Persist("client")
	public abstract String getTitle();

	@InjectPage("wellcome")
	public abstract Wellcome getWellcome();

	@InjectSpring("priorityManager")
	public abstract PriorityManager getPriorityManager();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	/**
	 * back ke home
	 * @return
	 */
	public IPage doSubmit() {
		return getWellcome();
	}

}
