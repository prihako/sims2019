/**
 * 
 */
package com.balicamp.dao.hibernate.user;

import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.dao.user.AdminGroupDao;
import com.balicamp.model.user.AdminGroup;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: AdminGroupDaoHibernate.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
@Repository
public class AdminGroupDaoHibernate extends AdminGenericDaoImpl<AdminGroup, Long> implements AdminGroupDao {

	public AdminGroupDaoHibernate() {
		super(AdminGroup.class);
	}
}
