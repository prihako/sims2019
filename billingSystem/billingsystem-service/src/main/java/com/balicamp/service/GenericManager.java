package com.balicamp.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.user.User;

/**
 * Interface generic manager
 * 
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public interface GenericManager<T, PK extends Serializable> { // NOPMD

	/**
	 * get all row
	 * 
	 * @return
	 */
	List<T> findAll();

	/**
	 * get
	 * 
	 * @param id
	 * @return
	 */
	T findById(PK id);

	T findByIdUser(PK id, User user);

	/**
	 * cek if pojo exist
	 * 
	 * @param id
	 * @return
	 */
	boolean exists(PK id);

	/**
	 * save use merge method
	 * 
	 * @param object
	 * @return
	 */
	T saveMerge(T object);

	/**
	 * save use saveOrUpdate method
	 * 
	 * @param object
	 */
	void saveOrUpdate(T object);

	/**
	 * save use save method
	 * 
	 * @param object
	 */
	void save(T object);

	/**
	 * save use update method
	 * 
	 * @param object
	 */
	void update(T object);

	/**
	 * save collection of object
	 * 
	 * @param collection
	 *            of object added by kbudiarto <- maybe risky
	 */
	void saveCollection(Collection<T> object);

	/**
	 * delete entity
	 * 
	 * @param id
	 */
	void delete(PK id);

	void delete(List<PK> idList);

	void delete(T object);

	void delete(Collection<T> object);

	/**
	 * 
	 * @param exampleEntity
	 * @param excludeProperty
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	List<T> findByExample(T exampleEntity, List<String> excludeProperty,
			int firstResult, int maxResults);

	/**
	 * 
	 * @param criterionList
	 * @param orderList
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	List<T> findByCriteria(List<Criterion> criterionList,
			List<Order> orderList, int firstResult, int maxResults);

	T findSingleByCriteria(List<Criterion> criterionList);

	T findSingleByCriteria(Criterion criterion);

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
	List<T> findByCriteria(SearchCriteria searchCriteria, int firstResult,
			int maxResults);

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
	boolean isUniqueAvailable(PK id, T exampleEntity,
			List<String> excludeProperty);

	/**
	 * @param id
	 * @param criterionList
	 * @return
	 */
	boolean isUniqueAvailableByCriteria(PK id, List<Criterion> criterionList);

	void flush();
}