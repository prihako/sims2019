package com.balicamp.webapp.action.webadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mx.ParameterMx;
import com.balicamp.service.ParameterMxManager;
import com.balicamp.webapp.action.BasePageGrid;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class ParamaterMxList extends BasePageGrid implements PageBeginRenderListener {

	@InjectSpring("parameterMxManagerImpl")
	public abstract ParameterMxManager getParameterMxManager();

	public abstract ParameterMx getTableRow();

	public abstract void setTableRow(ParameterMx parameterMx);

	public abstract void setDescription(String desc);

	public abstract String getDescription();

	public abstract Set<ParameterMx> getUniqueSet();

	public abstract void setUniqueSet(Set<ParameterMx> param);

	@InjectPage("parameterMxNotification")
	public abstract IPage getParameterMXNotification();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		if (getUniqueSet() == null)
			setUniqueSet(new HashSet<ParameterMx>());
	}

	private Map<Integer, String> unmodifedValues;

	public String getUnmodifiedValue(Integer idMappingDetails) {
		return unmodifedValues.get(idMappingDetails);
	}

	public void isValueChange(IRequestCycle cycle) {
		ParameterMx parameterMx = (ParameterMx) getTableRow();
		Set<ParameterMx> uniqueSet = getUniqueSet();
		Integer id = parameterMx.getIdMappingDetail();
		String valueBaru = parameterMx.getValueBaru();

		if (!unmodifedValues.get(id).equals(valueBaru)) {
			uniqueSet.add(parameterMx);
		}
		setUniqueSet(uniqueSet);
	}

	public IBasicTableModel getTableModel() {
		return new IBasicTableModel() {

			@Override
			public int getRowCount() {
				if (getCacheTableRowCount() <= 0) {
					setCacheTableRowCount(getParameterMxManager().getRowCount(getDescription()));
				}
				return getCacheTableRowCount();
			}

			@SuppressWarnings("unchecked")
			@Override
			public Iterator<ParameterMx> getCurrentPageRows(int nFirst, int nPageSize, ITableColumn objSortColumn,
					boolean bSortOrder) {

				if (getCacheTableData() == null) {
					List<ParameterMx> rows = getParameterMxManager().findAll(getDescription(), nFirst, nPageSize);
					unmodifedValues = new HashMap<Integer, String>();
					for (ParameterMx p : rows) {
						unmodifedValues.put(p.getIdMappingDetail(), p.getValueBaru());
					}
					setCacheTableData(rows);
				}

				return ((List<ParameterMx>) getCacheTableData()).iterator();
			}
		};
	}

	public void onSearch() {
		if (getRequestCycle().isRewinding())
			getTableModel();
	}

	public IPage doEdit() {
		List<ParameterMx> valueList = new ArrayList<ParameterMx>();
		for (ParameterMx entity : getUniqueSet()) {
			String strValueLama = entity.getStrValueLama().replaceAll("[:\\d+$]", "");
			String strValueBaru = entity.getStrValueBaru().replaceAll("[:\\d+$]", "");
			strValueLama += ":" + unmodifedValues.get(entity.getIdMappingDetail());
			strValueBaru += ":" + entity.getValueBaru();
			entity.setStrValueLama(strValueLama);
			entity.setStrValueBaru(strValueBaru);
			entity.setValueLama(unmodifedValues.get(entity.getIdMappingDetail()));
			entity.setUserPengubah(getUserLoginFromSession().getUsername());
			valueList.add(entity);
		}
		getParameterMxManager().updateValues(valueList);
		ParameterMxNotification mxNotification = (ParameterMxNotification) getParameterMXNotification();
		mxNotification.setParameterMxList(valueList);
		return mxNotification;
	}
}
