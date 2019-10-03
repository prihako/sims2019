/**
 * 
 */
package com.balicamp.dao.user;

import java.util.Set;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.user.Role;
import com.balicamp.model.user.UserRole;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: UserRoleDao.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
public interface UserRoleDao extends AdminGenericDao<UserRole, Double> {
	
	public UserRole findyByUserIDandRoleID(Long userId, Long roleId);
	public int deleteByUserIdAndRoleId(Long userId, Long roleId);

	public int deleteByRoleId(Long roleId);

	public int deleteByUserId(Long userId);

	public long getCountByRole(Role role);
	
	public Set<UserRole> findUserRoleByUserId(Long userId);
	
}
