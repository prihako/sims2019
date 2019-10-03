package com.balicamp.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.service.IManager;
import com.balicamp.service.common.MessageSourceWrapper;

/**
 * Base class of all manager.
 * 
 * @author <a href="mailto:wayan.agustina@sigma.co.id">I Wayan Ari Agustina</a>
 * @version $Id: AbstractManager.java 326 2013-02-14 09:15:56Z bagus.sugitayasa $
 */
public abstract class AbstractManager implements IManager, Serializable {

	private static final long serialVersionUID = 1L;

	protected final Log log = LogFactory.getLog(getClass());

	protected MessageSourceWrapper messageSourceWrapper;

	@Autowired
	public void setMessageSourceWrapper(MessageSourceWrapper messageSourceWrapper) {
		this.messageSourceWrapper = messageSourceWrapper;
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

	/**
	 * Harus override dan return salah satu dao.
	 * 
	 * @return Default dao
	 */
	public abstract Object getDefaultDao();

	@SuppressWarnings("unchecked")
	private AdminGenericDao<Serializable, Serializable> getDao() {
		return (AdminGenericDao<Serializable, Serializable>) getDefaultDao();
	}

	public Serializable findById(Serializable id) {
		return getDao().findById(id);
	}

	public List<?> findById(List<Serializable> ids) {
		List<Serializable> results = new ArrayList<Serializable>();
		for (Serializable id : ids) {
			Serializable result = findById(id);
			if (null != result) {
				results.add(result);
			}
		}

		return results;
	}

	public List<?> findByCriteria(SearchCriteria searchCriteria, int firstResult, int maxResults) {
		return getDao().findByCriteria(searchCriteria, firstResult, maxResults);
	}

	public Object findSingleByCriteria(SearchCriteria searchCriteria) {
		return getDao().findSingleByCriteria(searchCriteria);
	}

	public Integer findByCriteriaCount(SearchCriteria searchCriteria) {
		return getDao().findByCriteriaCount(searchCriteria);
	}
}
