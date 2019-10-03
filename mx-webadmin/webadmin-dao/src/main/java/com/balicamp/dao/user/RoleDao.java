/**
 * 
 */
package com.balicamp.dao.user;

import java.util.List;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.user.Role;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: RoleDao.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
public interface RoleDao extends AdminGenericDao<Role, Long> {
	List<Role> getEditableAdminRoleList();

	List<Role> getEditableCustomerRoleList();

	void delete(Long id);

	public void updateDataRole(Role role);
}
