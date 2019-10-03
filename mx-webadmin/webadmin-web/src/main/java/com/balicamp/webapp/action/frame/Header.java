package com.balicamp.webapp.action.frame;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.service.user.UserManager;
import com.balicamp.util.DateFormatSymbolsIn;
import com.balicamp.util.SecurityContextUtil;
import com.balicamp.webapp.action.AdminBasePage;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public abstract class Header extends AdminBasePage implements PageBeginRenderListener {

	@InjectObject("spring:userManager")
	public abstract UserManager getUserManager();

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	public Format getDateFormatWellcome() {
		return new SimpleDateFormat(getText("pattern.date.wellcome"), new DateFormatSymbolsIn());
	}

	public long getTimeInMilis() {
		Calendar cal = Calendar.getInstance();
		return cal.getTimeInMillis() + 1000l;
	}

	public String getUserNameFromSession() {
		System.out.println("CURRENT-USERNAME: " + SecurityContextUtil.getCurrentUser().getUserFullName());
		return SecurityContextUtil.getCurrentUser().getUserFullName();
	}
}
