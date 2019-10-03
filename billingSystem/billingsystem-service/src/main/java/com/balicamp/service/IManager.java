package com.balicamp.service;

import java.io.Serializable;
import java.util.List;

import com.balicamp.dao.helper.SearchCriteria;

/**
 * Base class for all Manager.
 * 
 * @author <a href="mailto:wayan.agustina@sigma.co.id">I Wayan Ari Agustina</a>
 * @version $Id: IManager.java 268 2013-02-05 03:28:30Z nyoman.parwata $
 */
public interface IManager {

	public Serializable findById(Serializable id);
	
	public List<?> findById(List<Serializable> ids);

	public List<?> findByCriteria(SearchCriteria searchCriteria, int firstResult, int maxResults);

	public Object findSingleByCriteria(SearchCriteria searchCriteria);

	public Integer findByCriteriaCount(SearchCriteria searchCriteria);

}
