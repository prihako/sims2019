package com.balicamp.dao.hibernate;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.DaoHibernate;

@Repository
public class DaoHibernateImpl extends GenericDaoHibernate<Serializable, Serializable> implements DaoHibernate {

	public DaoHibernateImpl() {
		super(Serializable.class);
	}

	@Autowired(required = true)
	public void setHibernateSessionFactory(
			@Qualifier("adminHibernateSessionFactory") SessionFactory hibernateSessionFactory) {

		super.setSessionFactory(hibernateSessionFactory);
	}

	public SessionFactory getHibernateSessionFactory() {
		return getSessionFactory();
	}

}
