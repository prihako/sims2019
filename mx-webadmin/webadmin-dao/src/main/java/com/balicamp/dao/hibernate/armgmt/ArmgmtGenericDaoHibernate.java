package com.balicamp.dao.hibernate.armgmt;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.model.admin.BaseAdminModel;

public class ArmgmtGenericDaoHibernate<T extends BaseAdminModel, PK extends Serializable>
		extends AdminGenericDaoImpl<T, PK> {

	private SessionFactory hibernateSessionFactory;

	public ArmgmtGenericDaoHibernate(Class<T> persistentClass) {
		super(persistentClass);
	}

	@Autowired(required = true)
	public void setHibernateSessionFactory(
			@Qualifier("armgmtHibernateSessionFactory") SessionFactory hibernateSessionFactory) {
		this.hibernateSessionFactory = hibernateSessionFactory;
		super.setSessionFactory(hibernateSessionFactory);
	}

	public SessionFactory getHibernateSessionFactory() {
		return hibernateSessionFactory;
	}

}
