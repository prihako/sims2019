package com.balicamp.webapp.action.bankadmin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.bankadmin.user.UserEntryEdit;

public abstract class MemberNotification extends AdminBasePage implements
		PageBeginRenderListener {
	public abstract void setParentUsername(String parentUsername);

	public abstract String getParentUsername();

	public abstract void setParentCardNumber(String parentCardNumber);

	public abstract String getParentCardNumber();

	public abstract void setMemberUsername(String memberUsername);

	public abstract String getMemberUsername();

	public abstract void setMemberCardNumber(String memberCardNumber);

	public abstract String getMemberCardNumber();

	public abstract void setUserId(String userId);

	public abstract String getUserId();

	@InjectPage("memberEntry")
	public abstract MemberEntry getMemberEntry();

	protected final Log log = LogFactory.getLog(UserEntryEdit.class);

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		setMessage(getText("memberNotification.title.success"));
	}

	public IPage doBack(IRequestCycle cycle) {
		return getMemberEntry();
	}
}
