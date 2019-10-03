package com.balicamp.webapp.tapestry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableSortingState;
import org.hibernate.criterion.Order;

import com.balicamp.dao.helper.EntityPropertyPath;
import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.service.IManager;

/**
 * Generic implementation of IBasicTableModel
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class GenericTableModel<T> implements IBasicTableModel {

	private IManager genericManager;

	private SearchCriteria searchCriteria;
	private Map<String, String> orderMaping;

	public GenericTableModel() {
	}

	public GenericTableModel(IManager genericManager, SearchCriteria searchCriteria, Map<String, String> orderMaping) {
		setGenericManager(genericManager);
		setSearchCriteria(searchCriteria);
		setOrderMaping(orderMaping);
	}

	private List<T> entityList;

	public Iterator<T> getCurrentPageRows(int nFirst, int nPageSize, ITableColumn objSortColumn, boolean bSortOrder) {
		// empty table
		if (genericManager == null || searchCriteria == null)
			return (new ArrayList<T>()).iterator();

		if (orderMaping != null && objSortColumn != null) {
			String entityPropertyPathExp = orderMaping.get(objSortColumn.getColumnName());

			if (entityPropertyPathExp != null) {
				EntityPropertyPath entityPropertyPath = new EntityPropertyPath(entityPropertyPathExp);
				Order order = null;
				if (bSortOrder == ITableSortingState.SORT_ASCENDING) {
					order = Order.asc(entityPropertyPath.getPropertyNama());
				} else {
					order = Order.desc(entityPropertyPath.getPropertyNama());
				}
				searchCriteria.addOrder(order, entityPropertyPath.getEntityPath());
			}
		}

		if (entityList == null)
			entityList = (List<T>) genericManager.findByCriteria(searchCriteria, nFirst, nPageSize);

		return entityList.iterator();
	}

	private Integer rowCount = null;

	public int getRowCount() {
		// empty table
		if (genericManager == null) {
			return 0;
		}

		if (rowCount == null) {
			if (searchCriteria != null) {
				rowCount = genericManager.findByCriteriaCount(searchCriteria);
			}

		}

		if (rowCount == null) {
			return 0;
		}

		return rowCount.intValue();
	}

	// properties setter
	public void setGenericManager(IManager genericManager) {
		this.genericManager = genericManager;
	}

	public void setSearchCriteria(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public void setOrderMaping(Map<String, String> orderMaping) {
		this.orderMaping = orderMaping;
	}

}
