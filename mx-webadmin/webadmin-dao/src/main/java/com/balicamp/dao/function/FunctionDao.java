/**
 * 
 */
package com.balicamp.dao.function;

import java.util.List;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.function.Function;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: FunctionDao.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
public interface FunctionDao extends AdminGenericDao<Function, Long> {
	List<Function> getBankAdminFunctionList();

	List<Function> getCustomerFunctionList(Boolean systemOnly);

	List<Function> findByRoleId(Long roleId);
}
