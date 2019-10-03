package com.balicamp.dao.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 * hibernate dao param if need cascade criterion
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 * @version $Id: SearchCriteria.java 205 2013-01-14 01:46:19Z bagus.sugitayasa $
 */
public class SearchCriteria implements Serializable {

	private static final long serialVersionUID = 7199710786522387226L;

	private String entityName;
	private int joinType = CriteriaSpecification.INNER_JOIN;
	private List<Criterion> criterionList;
	private List<Order> orderList;
	private List<SearchCriteria> subSearchCriteriaList;
	private boolean distinct;
	private Map<String, String> paramMapStr;

	// untuk menampung string
	private Map<String, Integer> strSmpExs = new HashMap<String, Integer>();

	public SearchCriteria() {
		// TODO : nothing
		paramMapStr = new HashMap<String, String>();
	}

	/**
	 * @param entityName
	 */
	public SearchCriteria(String entityName) {
		setEntityName(entityName);
	}

	public SearchCriteria(String entityName, int joinType) {
		setEntityName(entityName);
		setJoinType(joinType);
	}

	/**
	 * 
	 * @param entityName
	 * @param criterionList
	 * @param subSearchCriteriaList
	 * @param orderList
	 */
	public SearchCriteria(String entityName, List<Criterion> criterionList, List<SearchCriteria> subSearchCriteriaList,
			List<Order> orderList) {
		setEntityName(entityName);
		setCriterionList(criterionList);
		setOrderList(orderList);
		setSubSearchCriteriaList(subSearchCriteriaList);
	}

	/**
	 * create SearchCriteria from string expression
	 * @param entityNameExpression
	 * @return
	 */
	public static SearchCriteria createSearchCriteria(String entityNameExpression) {
		String[] entityNameList = entityNameExpression.split("[.]");
		SearchCriteria searchCriteria = new SearchCriteria(entityNameList[0]);

		if (entityNameList.length > 1)
			createSearchCriteria(searchCriteria, entityNameList, 1);

		return searchCriteria;
	}

	private static void createSearchCriteria(SearchCriteria searchCriteria, String[] entityNameList, int depth) {
		SearchCriteria subSearchCriteria = searchCriteria.getSubSearchCriteria(entityNameList[depth], true);
		depth++;
		if (depth < entityNameList.length) {
			createSearchCriteria(subSearchCriteria, entityNameList, depth);
		}
	}

	/**
	 * add SubSearchCriteria
	 * @param subSearchCriteria
	 */
	public void addSubSearchCriteria(SearchCriteria subSearchCriteria) {
		if (getSubSearchCriteriaList() == null) {
			setSubSearchCriteriaList(new ArrayList<SearchCriteria>());
		}
		getSubSearchCriteriaList().add(subSearchCriteria);
	}

	public SearchCriteria getSubSearchCriteria(String entityName, boolean createIfNotFound) {
		if (getSubSearchCriteriaList() == null)
			setSubSearchCriteriaList(new ArrayList<SearchCriteria>());

		for (SearchCriteria searchCriteria : getSubSearchCriteriaList()) {
			if (searchCriteria.getEntityName().equals(entityName)) {
				return searchCriteria;
			}
		}
		if (createIfNotFound) {
			SearchCriteria subSearchCriteria = new SearchCriteria(entityName);
			getSubSearchCriteriaList().add(subSearchCriteria);
			return subSearchCriteria;
		}
		return null;
	}

	public SearchCriteria getSubSearchCriteria(String subEntityNameExpression) {
		String[] entityNameList = subEntityNameExpression.split("[.]");
		return getSubSearchCriteria(this, entityNameList, 0);
	}

	private SearchCriteria getSubSearchCriteria(SearchCriteria parentSearchCriteria, String[] entityNameList, int depth) {
		SearchCriteria subSearchCriteria = parentSearchCriteria.getSubSearchCriteria(entityNameList[depth], false);

		if (subSearchCriteria == null)
			return null;

		depth++;
		if (depth >= entityNameList.length)
			return subSearchCriteria;

		return getSubSearchCriteria(subSearchCriteria, entityNameList, depth);
	}

	/**
	 * addCriterion
	 * @param criterion
	 */
	public SearchCriteria addCriterion(Criterion criterion) {
		if (getCriterionList() == null) {
			setCriterionList(new ArrayList<Criterion>());
		}
		getCriterionList().add(criterion);

		// put to map<SimpleExpression, indexOfCriterion>
		strSmpExs.put(criterion.toString(), getCriterionList().size() - 1);
		return this;
	}

	/**
	 * Method ini untuk me-remove Criterion berdasarkan index ke [value dari Map]<br/>
	 * str simpleExpression sebagai parameter : propertyName=value<br/>
	 * ex : removeCriterion(id.transactionCode=xxx)
	 * @param strSmpEx String SimpleExpression 
	 */
	public void removeCriterion(String strSmpEx) {
		for (String key : strSmpExs.keySet()) {
			if (key.equals(strSmpEx)) {
				List<Criterion> cts = getCriterionList();
				cts.remove((int) strSmpExs.get(key));
				break;
			}
		}
	}

	public boolean addCriterion(Criterion criterion, String subEntityNameExpression) {
		SearchCriteria subSearchCriteria = getSubSearchCriteria(subEntityNameExpression);
		if (subSearchCriteria == null)
			return false;
		subSearchCriteria.addCriterion(criterion);
		return true;
	}

	/**
	 * @param order
	 */
	public void addOrder(Order order) {
		if (getOrderList() == null) {
			setOrderList(new ArrayList<Order>());
		}
		getOrderList().add(order);
	}

	public boolean addOrder(Order order, String subEntityNameExpression) {
		if (subEntityNameExpression == null || subEntityNameExpression.length() == 0) {
			addOrder(order);
			return true;
		}
		SearchCriteria subSearchCriteria = getSubSearchCriteria(subEntityNameExpression);
		if (subSearchCriteria == null)
			return false;
		subSearchCriteria.addOrder(order);
		return true;
	}

	// getter setter
	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public List<Criterion> getCriterionList() {
		return criterionList;
	}

	public void setCriterionList(List<Criterion> criterionList) {
		this.criterionList = criterionList;
	}

	public List<SearchCriteria> getSubSearchCriteriaList() {
		return subSearchCriteriaList;
	}

	public void setSubSearchCriteriaList(List<SearchCriteria> subSearchCriteriaList) {
		this.subSearchCriteriaList = subSearchCriteriaList;
	}

	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}

	public int getJoinType() {
		return joinType;
	}

	public void setJoinType(int joinType) {
		this.joinType = joinType;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (object == this)
			return true;
		if (object == null)
			return false;
		if (!(object instanceof SearchCriteria))
			return false;

		SearchCriteria rhs = (SearchCriteria) object;
		return new EqualsBuilder().append(this.orderList, rhs.orderList).append(this.criterionList, rhs.criterionList)
				.append(this.subSearchCriteriaList, rhs.subSearchCriteriaList).append(this.entityName, rhs.entityName)
				.append(this.joinType, rhs.joinType).isEquals();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("entityName", this.entityName).append("joinType", this.joinType)
				.append("orderList", this.orderList).append("criterionList", this.criterionList)
				.append("subSearchCriteriaList", this.subSearchCriteriaList).toString();
	}

	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public Map<String, String> getParamMapStr() {
		if (paramMapStr == null) {
			paramMapStr = new HashMap<String, String>();
		}
		return paramMapStr;
	}

	/**
	 * Method ini digunakan untuk extract SearchCriteria menjadi List of SearchCriteria yang isinya adalah :<br/>
	 * <ol>
	 * <li>SearchCriteria Main</li>
	 * <li>Pembentukan SearchCriteria baru dari SubSearchCriteria</li>
	 * </ol>
	 * @param tmp SearchCriteria
	 * @return List of {@link SearchCriteria}
	 */
	public List<SearchCriteria> getListOfSearchCriteria(SearchCriteria tmp) {
		String entityName = tmp.getEntityName();
		List<Criterion> cts = tmp.getCriterionList();
		List<SearchCriteria> subCts = tmp.getSubSearchCriteriaList();

		List<SearchCriteria> searchCriterias = new ArrayList<SearchCriteria>();

		// Add Main Criterion
		SearchCriteria scMain = SearchCriteria.createSearchCriteria(entityName);
		if (cts != null) {
			for (Criterion ct : cts) {
				scMain.addCriterion(ct);
			}
		}

		// Build child SearchCriteria from SubCriteria
		if (subCts != null) {
			for (SearchCriteria sc : subCts) {
				String eN = sc.getEntityName();
				List<Criterion> ccts = sc.getCriterionList();

				SearchCriteria childSc = SearchCriteria.createSearchCriteria(eN);
				for (Criterion cct : ccts) {
					childSc.addCriterion(cct);
				}
				searchCriterias.add(childSc);
			}
		}
		searchCriterias.add(scMain);
		return searchCriterias;
	}

	/*
	 * public SearchCriteria removeCriterion(Criterion criterion) {
	 * if (getCriterionList() == null) {
	 * setCriterionList(new ArrayList<Criterion>());
	 * }
	 * getCriterionList().add(criterion);
	 * return this;
	 * }
	 */
}
