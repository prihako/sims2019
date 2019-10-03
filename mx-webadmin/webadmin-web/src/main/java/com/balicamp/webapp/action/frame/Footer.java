package com.balicamp.webapp.action.frame;

import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.service.user.UserManager;
import com.balicamp.webapp.action.AdminBasePage;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public abstract class Footer extends AdminBasePage implements PageBeginRenderListener {

	@InjectObject("spring:userManager")
	public abstract UserManager getUserManager();

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}
}
