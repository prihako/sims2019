package com.balicamp.webapp.action.common;

import org.acegisecurity.Authentication;
import org.acegisecurity.concurrent.SessionRegistry;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.Constants;
import com.balicamp.model.parameter.SystemParameterId;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.webapp.action.AdminBasePage;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 * @version $Id: AfterLogout.java 304 2013-02-11 08:55:58Z bagus.sugitayasa $
 */
public abstract class AfterLogout extends AdminBasePage implements PageBeginRenderListener {

	@InjectObject("spring:systemParameterManager")
	public abstract SystemParameterManager getSystemParameterManager();

	@InjectObject("spring:sessionRegistry")
	public abstract SessionRegistry getSessionRegistry();

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if (getPortalLink() == null)
			initPortalLink();

		// SecurityContextHolder.getContext().setAuthentication(null);
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null)
			return;

		Authentication authentication = context.getAuthentication();
		if (authentication == null)
			return;

		getSessionRegistry().removeSessionInformation(getSession().getId());
		// getAuthenticationProcessingFilter().do
	}

	public abstract String getPortalLink();

	public abstract void setPortalLink(String portalLink);

	public void initPortalLink() {
		String portalLink = getSystemParameterManager()
				.getStringValue(
						new SystemParameterId(Constants.SystemParameter.Portal.GROUP,
								Constants.SystemParameter.Portal.URL_HOME), "http://www.telkomsigma.co.id/");

		int index = portalLink.indexOf("?");
		if (index >= 0)
			portalLink = portalLink.substring(0, index);

		setPortalLink(portalLink);
	}

}
