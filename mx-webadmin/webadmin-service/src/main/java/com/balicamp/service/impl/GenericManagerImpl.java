package com.balicamp.service.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.GenericDao;
import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.user.User;
import com.balicamp.service.GenericManager;
import com.balicamp.service.common.MessageSourceWrapper;

/**
 * Implementation GenericManager Interface
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class GenericManagerImpl<T, PK extends Serializable> implements GenericManager<T, PK> { // NOPMD
	protected final Log log = LogFactory.getLog(getClass());// NOPMD

	protected GenericDao<T, PK> genericDao;
	protected MessageSourceWrapper messageSourceWrapper;

	public GenericManagerImpl(final GenericDao<T, PK> genericDao) {
		this.genericDao = genericDao;
	}

	@Autowired
	public void setMessageSourceWrapper(MessageSourceWrapper messageSourceWrapper) {
		this.messageSourceWrapper = messageSourceWrapper;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<T> findAll() {
		return genericDao.findAll();
	}

	/**
	 * {@inheritDoc}
	 */
	public T findById(PK id) {
		return genericDao.findById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public T findByIdUser(PK id, User user) {
		List<Criterion> criterionList = Arrays.asList((Criterion) Restrictions.eq("id", id),
				(Criterion) Restrictions.eq("user", user));
		return getFirstItem(genericDao.findByCriteria(criterionList, null, -1, -1));
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean exists(PK id) {
		return genericDao.exists(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public T saveMerge(T object) {
		return genericDao.saveMerge(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void saveOrUpdate(T object) {
		genericDao.saveOrUpdate(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void save(T object) {
		genericDao.save(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void delete(T id) {
		genericDao.delete(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<T> findByExample(T exampleEntity, List<String> excludeProperty, int firstResult, int maxResults) {
		return genericDao.findByExample(exampleEntity, excludeProperty, firstResult, maxResults);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<T> findByCriteria(List<Criterion> criterionList, List<Order> orderList, int firstResult, int maxResults) {
		return genericDao.findByCriteria(criterionList, orderList, firstResult, maxResults);
	}

	public T findSingleByCriteria(List<Criterion> criterionList) {
		return genericDao.findSingleByCriteria(criterionList);
	}

	public T findSingleByCriteria(Criterion criterion) {
		return genericDao.findSingleByCriteria(criterion);
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer findByCriteriaCount(List<Criterion> criterionList) {
		return genericDao.findByCriteriaCount(criterionList);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isUniqueAvailable(PK id, T exampleEntity, List<String> excludeProperty) {
		return genericDao.isUniqueAvailable(id, exampleEntity, excludeProperty);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<T> findByCriteria(SearchCriteria searchCriteria, int firstResult, int maxResults) {
		return genericDao.findByCriteria(searchCriteria, firstResult, maxResults);
	}

	public T findSingleByCriteria(SearchCriteria searchCriteria) {
		return genericDao.findSingleByCriteria(searchCriteria);
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer findByCriteriaCount(SearchCriteria searchCriteria) {
		return genericDao.findByCriteriaCount(searchCriteria);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isUniqueAvailableByCriteria(PK id, List<Criterion> criterionList) {
		return genericDao.isUniqueAvailableByCriteria(id, criterionList);
	}

	/**
	 * get first element of java.util.List object
	 * @param dataList
	 * @return fist element, null if list contain no element
	 */
	protected T getFirstItem(List<T> dataList) {
		if (dataList == null || dataList.size() == 0)
			return null;

		return dataList.get(0);
	}

	// util message resource
	protected String getText(String key) {
		return messageSourceWrapper.getMessage(key, null, "", null);
	}

	protected String getText(String key, String defaultMessage) {
		return messageSourceWrapper.getMessage(key, null, defaultMessage, null);
	}

	protected String getText(String key, String defaultMessage, Locale locale) {
		return messageSourceWrapper.getMessage(key, null, defaultMessage, locale);
	}

	protected String getText(String key, Object[] args) {
		return messageSourceWrapper.getMessage(key, args, "", null);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void update(T object) {
		this.saveOrUpdate(object);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void saveCollection(Collection<T> object) {
		this.saveCollection(object);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void delete(PK id) {
		this.delete(id);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void delete(List<PK> idList) {
		this.delete(idList);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void delete(Collection<T> object) {
		this.delete(object);

	}

	@Override
	public void flush() {
		this.flush();

	}

}
