/**
 * 
 */
package com.balicamp.dao.user;

import java.util.List;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.user.UserCardNumber;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: UserCardNumberDao.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
public interface UserCardNumberDao extends AdminGenericDao<UserCardNumber, Long> {
	public List<UserCardNumber> findByCardNumber(String cardNumber);
}
