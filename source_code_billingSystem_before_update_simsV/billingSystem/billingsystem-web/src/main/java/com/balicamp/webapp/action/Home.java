package com.balicamp.webapp.action;

import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

public abstract class Home extends BasePage implements PageBeginRenderListener {

	public abstract void setLeftMenuPage(String leftMenuPage);

	public abstract String getLeftMenuPage();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if (null == getLeftMenuPage()) {
			initMenu();
		}
	}

	/**
	 * fungsi untuk inisiasi menu, dulu di IB ada banyak menu yang bisa dipakai,
	 * di MAYORA dipakai cuma leftMenu saja 
	 */
	private void initMenu() {
		String baseUrl = getBaseUrl();
		String nameMenu = "LeftMenu";

		StringBuilder builder = new StringBuilder();

		if (baseUrl.endsWith(".html")) {

		}
		setLeftMenuPage("http://127.0.0.1:8080/Home.html?page=LeftMenu&service=page");
	}

}
