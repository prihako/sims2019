package com.balicamp.webapp.action.webadmin;

import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mx.ParameterMx;
import com.balicamp.webapp.action.BasePage;

public abstract class ParameterMxNotification extends BasePage implements PageBeginRenderListener {

	public abstract void setParameterMxList(List<ParameterMx> paramMxList);

	public abstract List<ParameterMx> getParameterMxList();

	public abstract void setParamMx(ParameterMx parameterMx);

	public abstract ParameterMx getParamMx();

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	public IBasicTableModel getTableModel() {
		return new IBasicTableModel() {

			@Override
			public int getRowCount() {
				return getParameterMxList().size();
			}

			@Override
			public Iterator<ParameterMx> getCurrentPageRows(int nFirst, int nPageSize, ITableColumn objSortColumn,
					boolean bSortOrder) {
				return getParameterMxList().iterator();
			}
		};
	}

}
