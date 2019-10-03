/**
 * 
 */
package com.balicamp.dao.user;

import java.util.List;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.user.User;
import com.balicamp.model.user.UserAdminGroup;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: UserAdminGroupDao.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
public interface UserAdminGroupDao extends AdminGenericDao<UserAdminGroup, Double> {
	public int deleteByUserId(Long userId);

	public int deleteByAdminGroupId(Long adminGroupId);

	public void saveByUserIdAndAdminGroupId(Long userId, Long id);

	public List<UserAdminGroup> findByUser(User user);
}
