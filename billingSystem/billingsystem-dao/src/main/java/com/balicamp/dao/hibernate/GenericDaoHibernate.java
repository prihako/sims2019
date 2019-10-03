package com.balicamp.dao.hibernate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.aop.support.AopUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.balicamp.dao.GenericDao;
import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.exception.ApplicationException;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class GenericDaoHibernate<T, PK extends Serializable> extends HibernateDaoSupport implements GenericDao<T, PK> {
	protected final Log log = LogFactory.getLog(getClass());// NOPMD

	protected Class<T> persistentClass;

	private static final String UNCHECKED = "unchecked";

	public GenericDaoHibernate(final Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<T> findAll() {
		return super.getHibernateTemplate().loadAll(this.persistentClass);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public T findById(PK id) {
		T entity = (T) super.getHibernateTemplate().get(this.persistentClass, id);
		return entity;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean exists(PK id) {
		T entity = (T) super.getHibernateTemplate().get(this.persistentClass, id);
		return entity != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings(UNCHECKED)
	public T saveMerge(T object) {
		return (T) super.getHibernateTemplate().merge(object);
	}

	public void saveOrUpdate(T object) {
		super.getHibernateTemplate().saveOrUpdate(object);
		getHibernateTemplate().flush();
	}

	public void save(T object) {
		super.getHibernateTemplate().save(object);
		getHibernateTemplate().flush();
	}

	public void update(T object) {
		super.getHibernateTemplate().update(object);
		getHibernateTemplate().flush();
	}

	public void saveCollection(Collection<T> collection) {
		super.getHibernateTemplate().saveOrUpdateAll(collection);
		getHibernateTemplate().flush();
	}

	/**
	 * {@inheritDoc}
	 */
	/*
	 * public void delete(PK id) {
	 * delete(this.findById(id));
	 * }
	 */

	public void delete(T object) {
		if (object != null) {
			super.getHibernateTemplate().delete(object);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings(UNCHECKED)
	public List<T> findByExample(final T exampleEntity, final List<String> excludeProperty, final int firstResult,
			final int maxResults) {
		if (exampleEntity == null) {
			return findAll();
		}

		return (List<T>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(persistentClass);
				Example example = Example.create(exampleEntity);
				if (excludeProperty != null) {
					for (String exclude : excludeProperty) {
						example.excludeProperty(exclude);
					}
				}
				criteria.add(example);
				if (firstResult >= 0 && maxResults > 0) {
					criteria.setFirstResult(firstResult);
					criteria.setMaxResults(maxResults);
				}
				return criteria.list();
			}
		});

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings(UNCHECKED)
	public List<T> findByCriteria(final List<Criterion> criterionList, final List<Order> orderList,
			final int firstResult, final int maxResults) {
		return (List<T>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = constructCriteria(session, criterionList, orderList, firstResult, maxResults);
				return criteria.list();
			}
		});
	}

	public T findSingleByCriteria(List<Criterion> criterionList) {
		return getSingleItem(findByCriteria(criterionList, null, -1, -1));
	}

	@SuppressWarnings("unchecked")
	public T findSingleByCriteria(final Criterion criterion) {
		return (T) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(persistentClass);
				criteria.add(criterion);
				return criteria.uniqueResult();
			}
		});
	}

	public T findFirstByCriteria(List<Criterion> criterionList) {
		return getFirstItem(findByCriteria(criterionList, null, -1, -1));
	}

	@SuppressWarnings("unchecked")
	public T findFirstByCriteria(final Criterion criterion) {
		return (T) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(persistentClass);
				criteria.add(criterion);

				return getFirstItem(criteria.list());
			}
		});
	}

	protected Criteria constructCriteria(Session session, List<Criterion> criterionList, List<Order> orderList,
			int firstResult, int maxResults) {
		Criteria criteria = session.createCriteria(persistentClass);

		if (criterionList != null) {
			for (Criterion criterion : criterionList) {
				criteria.add(criterion);
			}
		}

		if (orderList != null) {
			for (Order order : orderList) {
				criteria.addOrder(order);
			}
		}

		if (firstResult >= 0 && maxResults > 0) {
			criteria.setFirstResult(firstResult);
			criteria.setMaxResults(maxResults);
		}
		return criteria;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer findByCriteriaCount(final List<Criterion> criterionList) {
		return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(persistentClass);
				if (criterionList != null) {
					for (Criterion criterion : criterionList) {
						criteria.add(criterion);
					}
				}

				criteria.setProjection(Projections.rowCount());

				return (Integer) criteria.uniqueResult();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isUniqueAvailable(PK id, T exampleEntity, List<String> excludeProperty) {
		List<T> objectList = findByExample(exampleEntity, excludeProperty, -1, -1);

		if (objectList.size() == 0) {
			return true;
		}

		if (id == null) {
			return false;
		}

		for (T tmpObject : objectList) {
			ClassMetadata classMetadata = getSessionFactory().getClassMetadata(tmpObject.getClass());
			Serializable identifier = classMetadata.getIdentifier(tmpObject, EntityMode.POJO);
			if (!id.equals(identifier)) {
				return false;
			}
		}
		return true;

	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isUniqueAvailableByCriteria(PK id, List<Criterion> criterionList) {

		List<T> objectList = findByCriteria(criterionList, null, -1, -1);

		if (objectList.size() == 0) {
			return true;
		}

		if (id == null) {
			return false;
		}

		for (T tmpObject : objectList) {
			String className = tmpObject.getClass().getName();
			if (AopUtils.isCglibProxy(tmpObject)) {
				className = className.substring(0, className.indexOf("$$"));
			}
			ClassMetadata classMetadata = getSessionFactory().getClassMetadata(className);
			Serializable identifier = classMetadata.getIdentifier(tmpObject, EntityMode.POJO);
			if (!id.equals(identifier)) {
				return false;
			}
		}
		return true;

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByCriteria(final SearchCriteria searchCriteria, final int firstResult, final int maxResults) {
		return (List<T>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = constructCriteria(session, searchCriteria, true);
				if (firstResult >= 0) {
					criteria.setFirstResult(firstResult);
				}
				if (maxResults >= 0) {
					criteria.setMaxResults(maxResults);
				}

				if (searchCriteria.isDistinct()) {
					criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
				}

				return criteria.list();
			}

		});

	}
	
	

	public T findSingleByCriteria(SearchCriteria searchCriteria) {
		List<T> entityList = findByCriteria(searchCriteria, -1, -1);
		return getSingleItem(entityList);
	}

	protected Criteria constructCriteria(Session session, SearchCriteria searchCriteria, boolean withOrder) {
		// first level citeria must current persistentClass
		Criteria criteria = session.createCriteria(persistentClass);
		if (searchCriteria.isDistinct())
			criteria.setResultTransformer(new DistinctRootEntityResultTransformer());

		if (searchCriteria == null) {
			return criteria;
		}
		constructCriteria(searchCriteria, criteria, withOrder);
		return criteria;
	}

	private void constructCriteria(SearchCriteria searchCriteria, Criteria criteria, boolean withOrder) {
		if (searchCriteria.getCriterionList() != null) {
			for (Criterion criterion : searchCriteria.getCriterionList()) {
				criteria.add(criterion);
			}
		}

		if (withOrder && searchCriteria.getOrderList() != null) {
			for (Order order : searchCriteria.getOrderList()) {
				criteria.addOrder(order);
			}
		}

		if (searchCriteria.getSubSearchCriteriaList() != null) {
			for (SearchCriteria subSearchCriteria : searchCriteria.getSubSearchCriteriaList()) {
				Criteria subCriteria = null;
				if (subSearchCriteria.getJoinType() == CriteriaSpecification.INNER_JOIN) {
					subCriteria = criteria.createCriteria(subSearchCriteria.getEntityName());
				} else {
					subCriteria = criteria.createCriteria(subSearchCriteria.getEntityName(),
							subSearchCriteria.getJoinType());
				}

				constructCriteria(subSearchCriteria, subCriteria, withOrder);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Integer findByCriteriaCount(final SearchCriteria searchCriteria) {
		return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = constructCriteria(session, searchCriteria, false);
				criteria.setProjection(Projections.rowCount());

				return (Integer) criteria.uniqueResult();

			}
		});
	}

	public List<?> findByHQL(Query query, int firstResult, int maxResult) {
		if (firstResult >= 0 && maxResult > 0) {
			query.setFirstResult(firstResult);
			query.setMaxResults(maxResult);
		}

		return query.list();
	}

	public void evict(T object) {
		if (object == null)
			getSession().clear();
		getSession().evict(object);
	}

	public boolean setLocked(T entity, boolean locked) {
		// disconnect from session
		getSession().evict(entity);

		// save
		try {
			PropertyUtils.setProperty(entity, "locked", locked);
			saveOrUpdate(entity);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void flush() {
		getHibernateTemplate().flush();
	}

	/**
	 * get first element of java.util.List object
	 * 
	 * @param dataList
	 * @return fist element, null if list contain no element
	 */
	protected T getFirstItem(List<T> dataList) {
		if (dataList == null || dataList.size() == 0)
			return null;

		return dataList.get(0);
	}

	protected T getSingleItem(List<T> dataList) {
		if (dataList == null || dataList.size() == 0)
			return null;

		if (dataList.size() > 1)
			throw new ApplicationException("data found " + dataList.size());

		return dataList.get(0);
	}

	@Override
	public HibernateTemplate getCurrentHibernateTemplate() {
		return getHibernateTemplate();
	}

	@Override
	public List<T> findByCriteria(SearchCriteria searchCriteria) {
		return findByCriteria(searchCriteria, -1, -1);
	}

}
