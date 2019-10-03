package com.balicamp.dao;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.Oracle10gDialect;

public class Oracle10gDialectOverrider extends Oracle10gDialect{

public Oracle10gDialectOverrider(){
    super();
    registerHibernateType(Types.NVARCHAR, Hibernate.STRING.getName());
}

}
