package com.balicamp.dao.hibernate.mx;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.model.admin.BaseAdminModel;

/**
 * Base class dao khusus untuk data akses ke database sims.
 * 
 * @author Wayan Ari Agustina
 * @version $Id: $
 */
public class SimsGenericDaoHibernate<T extends BaseAdminModel, PK extends Serializable> extends
		AdminGenericDaoImpl<T, PK> {

	private SessionFactory hibernateSessionFactory;

	public SimsGenericDaoHibernate(Class<T> persistentClass) {
		super(persistentClass);
	}

	@Autowired(required = true)
	public void setHibernateSessionFactory(
			@Qualifier("simsHibernateSessionFactory") SessionFactory hibernateSessionFactory) {
		this.hibernateSessionFactory = hibernateSessionFactory;
		super.setSessionFactory(hibernateSessionFactory);
	}

	public SessionFactory getHibernateSessionFactory() {
		return hibernateSessionFactory;
	}

}
