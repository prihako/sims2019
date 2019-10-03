package com.balicamp.dao.admin;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.admin.BaseAdminModel;

/**
 * The base interface of all Data Access Object.
 * 
 * @author <a href="mailto:wayan.agustina@sigma.co.id">I Wayan Ari Agustina</a>
 *
 * @param <POJO> Aplication Pojo
 * @param <PK> Primary key type, must be serializable object.
 */

public interface AdminGenericDao<POJO extends Serializable, PK extends Serializable> extends GenericDao<POJO, PK> {

	/**
	 * Find some records by specific columns(fields name).
	 * 
	 * @param filter The query filter, in format(FieldName, fieldValue)
	 * @param maxResult The maximum query result.
	 * @return List of of pojo.
	 */
	public List<POJO> findByFieldName(Map<String, Object> filter, int maxResult);

	/**
	 * Same as findByFieldName(Map<String, Object>, int),
	 * but return only a single result.
	 * @param filter The query filter, in format(FieldName, fieldValue)
	 * @return Single pojo specified by the fieldName and fieldValue.
	 * @throws Exception if the result set returns more than 1 record.
	 */
	public POJO findSingleByFieldName(Map<String, Object> filter);

	/**
	 * set Identifier automatically for each class of children who extends {@link BaseAdminModel}
	 * @param pojo definition of BaseAdminModel
	 */
	public void setIdentifier(POJO pojo);

	void setIdentifier(Collection<POJO> coolection);

	/**
	 * This method same as getHibernateTemplate().loadAll(this.persistentClass)<br>Customize with String Query 
	 * @param fieldName {@link List} of FiledName who mapping AS {@link Collection} with other table 
	 * @param filter the Query filter, in format (filterString, filterValue)
	 * @return Single pojo specified by the fieldName and filter
	 */
	public List<POJO> findAllByFieldName(List<String> fieldName, Map<String, Object> filter);
	
	public List<POJO> findAllByFieldName(Map<String, Object> filter);
}
