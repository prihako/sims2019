package com.balicamp.webapp.action.frame;

import java.util.HashSet;
import java.util.Set;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.function.MenuTree;
import com.balicamp.model.user.User;
import com.balicamp.service.function.MenuTreeManager;
import com.balicamp.util.SecurityContextUtil;
import com.balicamp.webapp.action.AdminBasePage;

public abstract class LeftMenu extends AdminBasePage implements PageBeginRenderListener {

	public abstract String getMenu();

	public abstract void setMenu(String menu);

	@InjectObject("spring:menuTreeManager")
	public abstract MenuTreeManager getMenuTreeManager();

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if (getMenu() == null) {
			initializeMenu();
		}
	}

	@SuppressWarnings("unused")
	private User getCurrentUser() {
		/* temporary solution karena page login belum sempurna */
		// HARDCODE : harusnya diambil dari user yang login caranya :
		User currentUser = SecurityContextUtil.getCurrentUser();

		/*
		 * Long userId = new Long(8);
		 * User user = new User();
		 * user = (User) getSimpleUserManager().findById(userId);
		 * //dapatkan SET user_role lalu set ke User
		 * Set<UserRole> roleUser =
		 * getSimpleUserManager().findUserRoleByUserId(userId);
		 * user.setUserRoleSet(roleUser);
		 */

		// return currentUser;
		return getUserManager().findById(8L);

	}

	private void initializeMenu() {
		User currentUser = SecurityContextUtil.getCurrentUser();

		Set<MenuTree> allMenus = null;

		if (currentUser != null) {
			allMenus = getMenuTreeManager().getAllMenus(currentUser);
		} else {
			allMenus = new HashSet<MenuTree>();
		}

		Set<MenuTree> top = getMenuTreeManager().getTopMenus(allMenus);

		StringBuffer buffer = new StringBuffer();
		buffer.append("<ul class=\"menu\">");

		for (MenuTree menu : top) {
			constructMenu(allMenus, menu, 1, buffer);
		}

		buffer.append("</ul>");
		setMenu(buffer.toString());
	}

	/*
	 * private void constructMenu(Set<MenuTree> allMenus, MenuTree menu,
	 * int level, StringBuffer buffer) {
	 * }
	 */
	private void constructMenu(Set<MenuTree> allMenus, MenuTree menu, int level, StringBuffer buffer) {

		Set<MenuTree> children = getMenuTreeManager().getChildMenus(allMenus, menu);

		buffer.append("<li><a href=");

		String contextPath = getRequest().getContextPath();
		String targetURL = menu.getMenuUrl();

		if (targetURL != null) {
			targetURL = targetURL.trim();
			if (targetURL.startsWith("/")) {
				targetURL = contextPath + targetURL;
			}
		}

		if (!children.isEmpty())
			buffer.append("\"#\"");
		else {
			buffer.append("\"");
			buffer.append(targetURL);
			buffer.append("\"");

			if ("_blank".equals(menu.getMenuType())) {
				buffer.append(" target=\"_blank\"");
			} else {
				buffer.append(" target=\"mainFrame\"");
			}
		}

		buffer.append("class=\"menu");
		buffer.append(level);

		buffer.append("\">");
		buffer.append(getText(menu.getMenuTitle()));
		buffer.append("</a>");

		if (!children.isEmpty()) {
			buffer.append("<ul style=\"display:none\" class=\"hide\">");

			int nextLevel = level + 1;
			for (MenuTree child : children) {
				constructMenu(allMenus, child, nextLevel, buffer);
			}

			buffer.append("</ul>");
		}

		buffer.append("</li>");
	}

	public IPage onClickLink(IRequestCycle cycle) {
		return null;
	}
}
