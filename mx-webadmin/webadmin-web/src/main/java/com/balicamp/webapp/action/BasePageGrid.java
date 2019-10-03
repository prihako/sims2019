package com.balicamp.webapp.action;

import java.util.List;

import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.webapp.tapestry.component.ExtendedGrid;

/**
 * Base class khusus untuk page yang menggunakan {@link ExtendedGrid}.
 * 
 * @author Wayan Ari Agustina
 * @version $Id: BasePageGrid.java 436 2013-04-24 11:32:33Z rudi.sadria $
 */
public abstract class BasePageGrid extends BasePage {

	public abstract ExtendedGrid<? extends BaseAdminModel> getGrid();

	public int getFirstResult() {
		return getGrid().getFirstPage();
	}

	public int getMaxResults() {
		return getPageSize();
	}

	public abstract void setCacheTableRowCount(int rowCount);

	public abstract int getCacheTableRowCount();

	public abstract void setCacheTableData(List<?> data);

	public abstract List<?> getCacheTableData();

}
