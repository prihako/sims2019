package com.balicamp.dao.hibernate.unar;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.model.admin.BaseAdminModel;

public class UnarGenericDaoHibernate<T extends BaseAdminModel, PK extends Serializable>
		extends AdminGenericDaoImpl<T, PK> {

	private SessionFactory hibernateSessionFactory;

	public UnarGenericDaoHibernate(Class<T> persistentClass) {
		super(persistentClass);
	}

	@Autowired(required = true)
	public void setHibernateSessionFactory(
			@Qualifier("unarHibernateSessionFactory") SessionFactory hibernateSessionFactory) {
		this.hibernateSessionFactory = hibernateSessionFactory;
		super.setSessionFactory(hibernateSessionFactory);
	}

	public SessionFactory getHibernateSessionFactory() {
		return hibernateSessionFactory;
	}

}
