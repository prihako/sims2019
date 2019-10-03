/**
 * 
 */
package com.balicamp.webapp.action;

import org.apache.tapestry.annotations.InjectComponent;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.service.IManager;
import com.balicamp.webapp.tapestry.component.BaseCustomComponent;

/**
 * This Costum Class must be extends if use {@link BaseCustomComponent}
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: BaseCustomPageList.java 252 2013-01-30 03:15:21Z bagus.sugitayasa $
 */
public abstract class BaseCustomPageList extends BasePageList implements PageBeginRenderListener {

	public SearchCriteria filterCriteria;

	public abstract IManager getGridManager();

	/**
	 * @return Komponen grid 
	 */
	@InjectComponent("grid")
	public abstract BaseCustomComponent<? extends BaseAdminModel> getBaseCustomComponent();

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		/*
		 * if (!pageEvent.getRequestCycle().isRewinding()) {
		 * if (isRealRender()) {
		 * setRealRender(true);
		 * }
		 * }
		 */
	}

	public int getGridRowCount() {
		return getGridManager().findByCriteriaCount(getFilterCriteria());
	}

	/**
	 * @return Search Criteria yg akan digunakan untuk filtering grid.
	 */
	public SearchCriteria getFilterCriteria() {
		return filterCriteria;
	}
}
