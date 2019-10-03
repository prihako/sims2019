/**
 * 
 */
package com.balicamp.dao.hibernate.user;

import java.util.Arrays;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.dao.user.UserCardNumberDao;
import com.balicamp.model.user.UserCardNumber;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: UserCardNumberDaoHibernate.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
@Repository
public class UserCardNumberDaoHibernate extends AdminGenericDaoImpl<UserCardNumber, Long> implements UserCardNumberDao {

	public UserCardNumberDaoHibernate() {
		super(UserCardNumber.class);
	}

	@Override
	public List<UserCardNumber> findByCardNumber(String cardNumber) {
		return findByCriteria(Arrays.asList(new Criterion[] { Restrictions.eq("cardNumber", cardNumber) }), null, -1,
				-1);
	}

}
