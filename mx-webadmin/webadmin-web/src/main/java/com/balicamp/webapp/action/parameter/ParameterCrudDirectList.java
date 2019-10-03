/**
 * 
 */
package com.balicamp.webapp.action.parameter;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.constant.ModelConstant;
import com.balicamp.model.parameter.SystemParameter;
import com.balicamp.service.IManager;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.tapestry.GenericTableModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: ParameterCrudDirectList.java 337 2013-02-19 09:16:01Z bagus.sugitayasa $
 */
public abstract class ParameterCrudDirectList extends BasePageList implements PageBeginRenderListener {

	@InjectSpring("systemParameterManager")
	public abstract SystemParameterManager getSystemParameterManager();

	@InjectPage("tambahParameter")
	public abstract TambahDirectParameter getTambahDirectParameter();

	public abstract void setSearchSystemParameter(SystemParameter searchSystemParameter);

	public abstract SystemParameter getSearchSystemParameter();

	@Persist("client")
	public abstract SystemParameter getParameter();

	public abstract void setParameter(SystemParameter provider);

	@Persist("client")
	public abstract String getParameterName();

	public abstract void setParameterName(String parameterName);

	@Persist("client")
	public abstract String getParameterValue();

	public abstract void setParameterValue(String parameterValue);

	public String getNameParameter(SystemParameter parameter) {
		return parameter.getSystemParameterId().getName();
	}

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	@SuppressWarnings("unused")
	public SearchCriteria getTableSearchCriteria() {
		SearchCriteria searchCriteria = SearchCriteria.createSearchCriteria("systemParameterManager");
		SystemParameter searchSystemParameter = getSearchSystemParameter();

		if (getParameterName() != null) {
			searchCriteria.addCriterion(Restrictions.ilike("systemParameterId.name", "%" + getParameterName() + "%"));
		}
		searchCriteria.addOrder(Order.asc("systemParameterId.name").ignoreCase());
		return searchCriteria;
	}

	@Override
	public IBasicTableModel createTableModel() {
		return new GenericTableModel<SystemParameter>((IManager) getSystemParameterManager(), getTableSearchCriteria(),
				emptyOrderMaping);
	}

	public IPage onEdit(IRequestCycle cycle) {
		Object[] id = cycle.getListenerParameters();

		if (getDelegate().getHasErrors()) {
			return null;
		}

		TambahDirectParameter inputPage = getTambahDirectParameter();
		inputPage.setParameterValue(id[1].toString());
		inputPage.setParameterName(id[0].toString());

		inputPage.setMode(ModelConstant.PendingChange.ACTION_TYPE_EDIT);
		inputPage.setActionType(ModelConstant.PendingChange.ACTION_TYPE_EDIT);
		return inputPage;
	}

	public IPage onSearch() {
		return null;
	}
}