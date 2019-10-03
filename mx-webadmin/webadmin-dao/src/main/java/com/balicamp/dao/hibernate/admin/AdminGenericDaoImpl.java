package com.balicamp.dao.hibernate.admin;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.dao.hibernate.GenericDaoHibernate;
import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.util.admin.PostgreSQLSequenceGenerator;

/**
 * 
 * The base class of all Data Access Object.
 * 
 * @author <a href="mailto:wayan.agustina@sigma.co.id">I Wayan Ari Agustina</a>
 * @version $Id: AdminGenericDaoImpl.java 1602 2012-07-10 06:28:32Z
 *          wayan.agustina $
 * 
 * @param <POJO> Application Pojo
 * @param <PK>
 *            The Primary key object type, must implements the
 *            java.io.Serializable interface. Don't use primitive data type
 *            here, for example(int, double, ...). Please use the wrapper class,
 *            for example (Integer, Double).
 */

public class AdminGenericDaoImpl<POJO extends BaseAdminModel, PK extends Serializable> extends
		GenericDaoHibernate<POJO, PK> implements AdminGenericDao<POJO, PK> {

	private SessionFactory hibernateSessionFactory;

	public AdminGenericDaoImpl(Class<POJO> persistentClass) {
		super(persistentClass);
	}

	/**
	 * This is an alternative method to inject Hibernate SessionFactory to this
	 * class. Because setSessionFactory() in HibernateDaoSupport is a final
	 * method.
	 * 
	 * @param hibernateSessionFactory
	 *            The hibernate SessionFactory
	 */
	@Autowired(required = true)
	public void setHibernateSessionFactory(
			@Qualifier("adminHibernateSessionFactory") SessionFactory hibernateSessionFactory) {

		this.hibernateSessionFactory = hibernateSessionFactory;
		super.setSessionFactory(hibernateSessionFactory);
	}

	public SessionFactory getHibernateSessionFactory() {
		return hibernateSessionFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<POJO> findByFieldName(Map<String, Object> filter, int maxResult) {
		Criteria criteria = getSession().createCriteria(persistentClass);
		for (String key : filter.keySet()) {
			criteria.add(Restrictions.eq(key, filter.get(key)));
		}

		if (maxResult >= 1) {
			criteria.setMaxResults(maxResult);
		}
		return criteria.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public POJO findSingleByFieldName(Map<String, Object> filter) {
		Criteria criteria = getSession().createCriteria(persistentClass);
		for (String key : filter.keySet()) {
			criteria.add(Restrictions.eq(key, filter.get(key)));
		}

		return (POJO) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<POJO> findAllByFieldName(Map<String, Object> filter) {
		Criteria criteria = getSession().createCriteria(persistentClass);
		for (String key : filter.keySet()) {
			criteria.add(Restrictions.eq(key, filter.get(key)));
		}

		return criteria.list();
	}

	/**
	 * fungsi untuk mengisi field id yang diisi sequence sebelum disimpan,
	 * dibuat model seperti ini agar sequence nya urut
	 * 
	 * @param pojo
	 *            object pojo yang akan diset Id nya
	 */
	public void setIdentifier(POJO pojo) {
		if ((pojo instanceof ISequencesModel) && (((ISequencesModel) pojo).getId() == null)) {
			PostgreSQLSequenceGenerator seqGenerator = new PostgreSQLSequenceGenerator(getSession());
			ISequencesModel seqPojo = ((ISequencesModel) pojo);
			String seqName = seqPojo.getSequenceName();
			
			seqPojo.setId(seqGenerator.next(seqName) +1);
//			seqPojo.setId(seqGenerator.next("t_audit_log_id_seq"));

		}
	}

	/**
	 * fungsi untuk mengisi field id yang diisi sequence sebelum disimpan,
	 * dibuat model seperti ini agar sequence nya urut
	 * 
	 * @param collection
	 *            : collection of pojo yang akan diset Id nya
	 */
	@Override
	public void setIdentifier(Collection<POJO> coolection) {
		for (POJO pojo2 : coolection) {
			setIdentifier(pojo2);
		}
	}

	/**
	 * Save single entity object, and assign the id manually if the id was null.<br>
	 * added By : Sugitayasa
	 */
	public void save(POJO object) {
		setIdentifier(object);
		super.save(object);
	}

	/**
	 * Save or update single entity object, and assign the id manually if the id
	 * was null.<br>
	 * added By : Sugitayasa
	 */
	public POJO saveMerge(POJO object) {
		setIdentifier(object);
		return super.saveMerge(object);
	}

	/**
	 * Save multiple entity object, and assign the id manually if the id were
	 * null.<br>
	 * added By : Sugitayasa
	 */
	@Override
	public void saveCollection(Collection<POJO> collection) {
		super.saveCollection(collection);
	}

	/**
	 * Save or update multiple entity object, and assign the id manually if the
	 * id were null.<br>
	 * added By : Sugitayasa
	 */
	public void saveOrUpdate(POJO object) {
		setIdentifier(object);
		super.saveOrUpdate(object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<POJO> findAllByFieldName(List<String> fieldName, Map<String, Object> filter) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT p FROM " + persistentClass.getName() + " AS p ");
		for (String f : fieldName) {
			buffer.append("JOIN FETCH p." + f + " ");
		}

		if (filter.size() > 0) {
			for (String key : filter.keySet()) {
				buffer.append("AND p." + key + "=" + filter.get(key));
			}
		}
		return getHibernateTemplate().find(buffer.toString());
	}
}
