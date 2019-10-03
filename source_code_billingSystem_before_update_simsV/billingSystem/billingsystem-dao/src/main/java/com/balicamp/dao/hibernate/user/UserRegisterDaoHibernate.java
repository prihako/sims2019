/**
 * 
 */
package com.balicamp.dao.hibernate.user;

import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.dao.user.UserRegisterDao;
import com.balicamp.model.user.UserRegister;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: UserRegisterDaoHibernate.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
@Repository
public class UserRegisterDaoHibernate extends AdminGenericDaoImpl<UserRegister, Long> implements UserRegisterDao {

	public UserRegisterDaoHibernate() {
		super(UserRegister.class);
	}

}
