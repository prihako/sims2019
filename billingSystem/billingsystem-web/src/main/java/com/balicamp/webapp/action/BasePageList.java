package com.balicamp.webapp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.annotations.Asset;
import org.apache.tapestry.annotations.Bean;
import org.apache.tapestry.annotations.InjectMeta;
import org.apache.tapestry.bean.EvenOdd;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.webapp.tapestry.EmptyTableModel;

/**
 * @version  $Id: BasePageList.java 315 2013-02-13 02:48:50Z wayan.agustina $
 */
public abstract class BasePageList extends AdminBasePage implements PageBeginRenderListener {

	@InjectMeta("page.size")
	public abstract int getDefaultPageSize();

	@Asset("/images/arrow_up.png")
	public abstract IAsset getUpArrow();

	@Asset("/images/arrow_down.png")
	public abstract IAsset getDownArrow();

	public abstract Object getRow();

	public abstract Integer getCurentRow();

	public abstract void setCurentRow(Integer curentRow);

	public Integer getIncRow() {
		if (getCurentRow() == null) {
			setCurentRow(0);
		}
		setCurentRow(getCurentRow() + 1);
		return getCurentRow();
	}

	@Bean
	public abstract EvenOdd getEvenOdd();

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		getEvenOdd().setEven(false);
	}

	/**
	 * emptyOrderMaping
	 */
	protected static final Map<String, String> emptyOrderMaping = new HashMap<String, String>();

	/**
	 * empty table model
	 */
	public EmptyTableModel emptyTableModel = new EmptyTableModel();

	/**
	 * generate table model
	 * harus overwrite
	 * @return
	 */
	public IBasicTableModel createTableModel() {
		return emptyTableModel;
	}

	/**
	 * 
	 * @return
	 */
	public IBasicTableModel getTableModel() {

		if (!isRealRender())
			return emptyTableModel;

		if (getCacheTableModel() == null) {
			IBasicTableModel tableModel = createTableModel();
			if (tableModel != null)
				setCacheTableModel(tableModel);
		}

		// return empty table model if cache not found
		if (getCacheTableModel() != null)
			return getCacheTableModel();
		else
			return emptyTableModel;

	}

	public abstract void setCacheTableRowCount(int rowCount);

	public abstract int getCacheTableRowCount();

	public abstract void setCacheTableData(List<?> data);

	public abstract List<?> getCacheTableData();

	public abstract IBasicTableModel getCacheTableModel();

	public abstract void setCacheTableModel(IBasicTableModel cacheTableModel);
}
