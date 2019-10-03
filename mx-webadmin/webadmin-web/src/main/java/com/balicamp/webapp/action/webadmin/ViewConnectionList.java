package com.balicamp.webapp.action.webadmin;

import java.util.List;

import org.apache.tapestry.event.PageBeginRenderListener;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.service.IManager;
import com.balicamp.webapp.action.BaseCustomPageList;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * @author Wayan Agustina
 * @version $Id: ViewConnectionList.java 305 2013-02-11 09:31:48Z wayan.agustina $
 */
public abstract class ViewConnectionList extends BaseCustomPageList implements PageBeginRenderListener {

	@InjectSpring("endpointsManagerImpl")
	public abstract IManager getGridManager();

	public void refresh(SearchCriteria searchCriteria) {
		doRefresh();
	}

	/**
	 * @return Data yang akan ditampilkan pada grid.
	 * Ini adalah method yg dapat digunakan dengan query gaya bebas,
	 * sesuai dengan dengan method - method pada DAO anda,
	 * asalkan dapat mengembalikan List yg berisikan pojo.
	 * 
	 * NOTE: jangan lupa gunakan method getFisrtPage() dan getMaxResults()
	 * dari BaseCustomComponent sebagai argument limit filtering.
	 * Karena jika tidak mengunakan method - method tersebut maka, grid result
	 * akan kembali direset dari awal.
	 */
	public List<?> getGridData() {

		return getGridManager().findByCriteria(getFilterCriteria(), getBaseCustomComponent().getFistPage(),
				getBaseCustomComponent().getMaxResult());
	}

	/**
	 * Refresh/Reload table.
	 */
	public void doRefresh() {
		setRealRender(true);
		getBaseCustomComponent().getTableModel();
	}

}
