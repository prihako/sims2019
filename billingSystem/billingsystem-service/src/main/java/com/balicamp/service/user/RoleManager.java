package com.balicamp.service.user;

import java.util.List;

import com.balicamp.model.common.MethodResponse;
import com.balicamp.model.function.Function;
import com.balicamp.model.user.Role;
import com.balicamp.service.IManager;

/**
 * @version $Id: RoleManager.java 113 2012-12-12 04:15:16Z bagus.sugitayasa $
 */
public interface RoleManager extends IManager {

	List<Role> getEditableAdminRoleList();

	List<Role> getEditableCustomerRoleList();

	Role findByName(String name);

	List<Role> findByIds(List<Long> ids);

	List<Function> getFunctionList(Long roleId);

	// List<ProductTree> getProductTreeList(Long roleId);

	MethodResponse<?> cekDeleteReferencialIntegrity(Role role);

	void delete(Role role);

	/**
	 * find {@link Role} by criteria
	 * 
	 * @param searchCriteria
	 * @param i
	 * @param j
	 * @return list {@link Role}
	 */

	/**
	 * persist {@link Role}
	 * 
	 * @param entity
	 */
	void save(Role entity);

	void deleteById(Long id);

	Role findById(Long id);

	void updateDataRole(Role role);
}
