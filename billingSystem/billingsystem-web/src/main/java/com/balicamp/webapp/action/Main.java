package com.balicamp.webapp.action;

import javax.servlet.http.HttpSession;

import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.springframework.web.bind.ServletRequestUtils;

import test.CommonUtil;
import test.Constants;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public abstract class Main 	extends BasePage implements PageBeginRenderListener
{
		
	public abstract void setSrcWelcome(String srcWelcome);
	public abstract String getSrcWelcome();

	public abstract void setLeftMenuPage(String leftMenuPagePage);
	public abstract String getLeftMenuPage();
	
	public void initLeftMenuPage(){
		String page = "leftMenu";
		
		setLeftMenuPage(
			new StringBuilder()
			.append("frame/")
			.append(page)
			.append(".html")
			.toString()
		);
	}
	
	public void pageBeginRender(PageEvent pageEvent) {		
		super.pageBeginRender(pageEvent);
		
		//cek session apakah boleh buka main page
		HttpSession httpSession = getSession();
		Boolean authMainPage = (Boolean) httpSession.getAttribute(Constants.HttpSessionAttribute.AUTH_MAIN_PAGE);
		if ( authMainPage == null || !authMainPage.booleanValue() ){
			httpSession.invalidate();
		} else
			httpSession.setAttribute(Constants.HttpSessionAttribute.AUTH_MAIN_PAGE, null);
		
		if (isRealRender()) {
			String param = pageEvent.getRequestCycle().getParameter("msg");
			if (param != null) {
				if (param.endsWith(".html")) {
					param = param.substring(0, param.length()-5);
				}
				setSrcWelcome("wellcome.html?msg=" + param);
			} else {
				setSrcWelcome("wellcome.html");
			}

			String page = ServletRequestUtils.getStringParameter(getRequest(), "page", "");
			if ( !CommonUtil.isEmpty(page) ){
				setSrcWelcome(page);
			}
		}
		
		if ( getLeftMenuPage() == null )
			initLeftMenuPage();
		
	}
		
	
}
