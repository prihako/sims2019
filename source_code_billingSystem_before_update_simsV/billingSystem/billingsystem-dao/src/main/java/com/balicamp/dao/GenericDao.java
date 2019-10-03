package com.balicamp.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.balicamp.dao.helper.SearchCriteria;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 * @version $Id: GenericDao.java 147 2013-01-08 02:37:45Z nyoman.parwata $
 */
public interface GenericDao<T, PK extends Serializable> {
	/**
	 * get all row
	 * @return
	 */
	List<T> findAll();

	/**
	 * get
	 * @param id
	 * @return
	 */
	T findById(PK id);

	/**
	 * cek if pojo exist
	 * @param id
	 * @return
	 */
	boolean exists(PK id);

	/**
	 * save use merge method
	 * @param object
	 * @return
	 */
	T saveMerge(T object);

	/**
	 * save use saveOrUpdate method
	 * @param object
	 */
	void saveOrUpdate(T object);

	/**
	 * save use save method
	 * @param object
	 */
	void save(T object);

	/**
	 * save use update method
	 * @param object
	 */
	void update(T object);

	/**
	 * save a same type of collection of object use save method
	 * @param collection of object
	 */
	void saveCollection(Collection<T> object);

	/**
	 * delete entity
	 * @param id
	 */
	/* void delete(PK id); */

	void delete(T object);

	/**
	 * 
	 * @param exampleEntity
	 * @param excludeProperty
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	List<T> findByExample(T exampleEntity, List<String> excludeProperty, int firstResult, int maxResults);

	/**
	 * 
	 * @param criterionList
	 * @param orderList
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	List<T> findByCriteria(List<Criterion> criterionList, List<Order> orderList, int firstResult, int maxResults);

	T findSingleByCriteria(List<Criterion> criterionList);

	T findSingleByCriteria(Criterion criterion);

	T findFirstByCriteria(List<Criterion> criterionList);

	T findFirstByCriteria(Criterion criterion);

	/**
	 * @param criterionList
	 * @return
	 */
	Integer findByCriteriaCount(List<Criterion> criterionList);

	/**
	 * @param searchCriteria
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	List<T> findByCriteria(SearchCriteria searchCriteria, int firstResult, int maxResults);
	
	/**
	 * 
	 * @param searchCriteria kriteria untuk pencarian yang menghasilkan List of Model
	 * @return
	 */
	
	List<T> findByCriteria(SearchCriteria searchCriteria);

	T findSingleByCriteria(SearchCriteria searchCriteria);

	/**
	 * @param searchCriteria
	 * @return
	 */
	Integer findByCriteriaCount(SearchCriteria searchCriteria);

	/**
	 * 
	 * @param id
	 * @param exampleEntity
	 * @param excludeProperty
	 * @return
	 */
	boolean isUniqueAvailable(PK id, T exampleEntity, List<String> excludeProperty);

	/**
	 * @param id
	 * @param criterionList
	 * @return
	 */
	boolean isUniqueAvailableByCriteria(PK id, List<Criterion> criterionList);

	/**
	 * 
	 * @param object
	 */
	void evict(T object);

	/**
	 * update property locked
	 * @param entity
	 * @param locked
	 * @return
	 */
	boolean setLocked(T entity, boolean locked);

	void flush();

	public HibernateTemplate getCurrentHibernateTemplate();
}