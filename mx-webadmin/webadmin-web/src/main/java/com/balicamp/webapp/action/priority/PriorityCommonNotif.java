package com.balicamp.webapp.action.priority;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.Wellcome;

public abstract class PriorityCommonNotif extends AdminBasePage implements PageBeginRenderListener {

	protected final static Log log = LogFactory.getLog(PriorityCommonNotif.class);

	public abstract void setTitle(String title);

	@Persist("client")
	public abstract String getMsgnotif();

	public abstract void setMsgnotif(String msgnotif);

	@Persist("client")
	public abstract String getTitle();

	@InjectPage("wellcome")
	public abstract Wellcome getWellcome();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	/**
	 * back ke halaman home
	 * @return
	 */
	public IPage doSubmit() {
		return getWellcome();
	}
}
