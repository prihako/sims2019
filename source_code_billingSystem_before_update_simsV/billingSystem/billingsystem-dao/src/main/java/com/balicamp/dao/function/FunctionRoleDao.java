/**
 * 
 */
package com.balicamp.dao.function;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.function.FunctionRole;
import com.balicamp.model.user.Role;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: FunctionRoleDao.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
public interface FunctionRoleDao extends AdminGenericDao<FunctionRole, Long> {
	public int deleteByRoleId(Long roleId);

	public void saveByRoleIdAndFunctionId(Long roleId, Long functionId);

	public long getCountByRole(Role role);
}
