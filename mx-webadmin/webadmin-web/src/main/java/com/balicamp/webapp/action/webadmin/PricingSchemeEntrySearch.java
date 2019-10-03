package com.balicamp.webapp.action.webadmin;

import java.util.List;
import java.util.logging.Logger;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.form.BeanPropertySelectionModel;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mx.PriorityRouting;
import com.balicamp.service.priority.PriorityManager;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.constant.PriorityConstant.Parameter;
import com.balicamp.webapp.model.priority.ObjectModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class PricingSchemeEntrySearch extends BasePageList implements PageBeginRenderListener {

	private static Logger LOGGER = Logger.getLogger(PricingSchemeEntrySearch.class.getSimpleName());

	public abstract void setMsgnotif(String msgnotif);

	public abstract String getMsgnotif();

	public abstract void setRows(int rows);

	@Persist("client")
	public abstract int getRows();

	public abstract void setKeys(String keys);

	@Persist("client")
	public abstract String getKeys();

	public abstract void setKeys2(String keys2);

	@Persist("client")
	public abstract String getKeys2();

	private BeanPropertySelectionModel criteriaModel = null;

	public BeanPropertySelectionModel getCriteriaModel() {
		final List<ObjectModel> criteriaList = Parameter.getCriteria();
		criteriaModel = new BeanPropertySelectionModel(criteriaList, "obj");
		return criteriaModel;
	}

	public abstract void setCriteria(ObjectModel criteria);

	public abstract ObjectModel getCriteria();

	public abstract void setCriteria2(ObjectModel criteria2);

	public abstract ObjectModel getCriteria2();

	@InjectSpring("priorityManager")
	public abstract PriorityManager getPriorityManager();

	public abstract void setPriorities(List<PriorityRouting> priorities);

	@Persist("client")
	public abstract List<PriorityRouting> getPriorities();

	@InjectPage("pricingSchemeEntry")
	public abstract PricingSchemeEntry getPricingSchemeEntry();

	/**
	*
	* @return
	*/
	public IPage onSearch() {
		if (getKeys() == null) {
			setMsgnotif(getText("priority.error.entry.criteria.notnull"));
			return null;
		}

		if (getPrioritiesRowCount() <= 0) {
			setMsgnotif(getText("priority.message.notif.notfound", new Object[] { getKeys() }));
		} else {
			setMsgnotif(null);

			PricingSchemeEntry entry = getPricingSchemeEntry();
			List<PriorityRouting> list = getPriorityManager().findPriority(getTableSearchCriteria(), 0, getRows());
			entry.setPriorities(list);
			entry.setRows(getRows());
			entry.setCategory(getTableSearchCriteria());
			return entry;
		}
		return null;
	}

	private int getPrioritiesRowCount() {
		int rows = getPriorityManager().findByCriteriaCount(getTableSearchCriteria());
		setRows(rows);
		return rows;
	}

	/**
	 * construct searchcriteria
	 * @return
	 */
	public SearchCriteria getTableSearchCriteria() {
		SearchCriteria searchCriteria = SearchCriteria.createSearchCriteria("priorityRouting");

		if (getKeys() != null && !getKeys().equals("") && getCriteria() != null) {
			searchCriteria.addCriterion(Restrictions.ilike(getCriteria().getKey(), "%" + getKeys() + "%"));
		}
		if (getKeys2() != null && !getKeys2().equals("") && getCriteria2() != null) {
			searchCriteria.addCriterion(Restrictions.ilike(getCriteria2().getKey(), "%" + getKeys2() + "%"));
		}

		searchCriteria.addOrder(Order.asc("description").ignoreCase());
		return searchCriteria;
	}
}
