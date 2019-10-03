package com.balicamp.service.impl.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.function.FunctionRoleDao;
import com.balicamp.dao.user.RoleDao;
import com.balicamp.dao.user.UserRoleDao;
import com.balicamp.model.common.MethodResponse;
import com.balicamp.model.function.Function;
import com.balicamp.model.function.FunctionRole;
import com.balicamp.model.user.Role;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.user.RoleManager;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: RoleManagerImpl.java 326 2013-02-14 09:15:56Z bagus.sugitayasa $
 */
@Service("roleManager")
public class RoleManagerImpl extends AbstractManager implements RoleManager {

	private static final long serialVersionUID = 1L;

	private RoleDao roleDao;

	private UserRoleDao userRoleDao;

	private FunctionRoleDao functionRoleDao;

	@Autowired
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	@Autowired
	public void setUserRoleDao(UserRoleDao userRoleDao) {
		this.userRoleDao = userRoleDao;
	}

	@Autowired
	public void setFunctionRoleDao(FunctionRoleDao functionRoleDao) {
		this.functionRoleDao = functionRoleDao;
	}

	public List<Role> getEditableAdminRoleList() {
		return roleDao.getEditableAdminRoleList();
	}

	public List<Role> getEditableCustomerRoleList() {
		return roleDao.getEditableCustomerRoleList();
	}

	public Role findByName(String name) {
		List<Role> dataList = roleDao.findByCriteria(Arrays.asList(Restrictions.ilike("name", name)), null, -1, -1);
		if (dataList == null || dataList.size() == 0)
			return null;

		return dataList.get(0);
	}

	public List<Role> findByIds(List<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return new ArrayList<Role>();
		}

		return roleDao.findByCriteria(Arrays.asList(Restrictions.in("id", ids)), null, -1, -1);
	}

	public List<Function> getFunctionList(Long roleId) {
		List<Function> result = new ArrayList<Function>();

		List<FunctionRole> frs = functionRoleDao.findByCriteria(
				Arrays.asList((Criterion) Restrictions.eq("id.role.id", roleId)), null, -1, -1);

		// Role role = findById(roleId);
		for (FunctionRole functionRole : frs) {
			result.add(functionRole.getId().getFunction());
		}

		return result;
	}

	/*
	 * public List<ProductTree> getProductTreeList(Long roleId) {
	 * List<ProductTree> result = new ArrayList<ProductTree>();
	 * 
	 * List<ProductTreeRole> ptrs = productTreeRoleDao.findByCriteria(Arrays
	 * .asList((Criterion) Restrictions.eq("id.role.id", roleId)),
	 * null, -1, -1);
	 * 
	 * // Role role = findById(roleId);
	 * for (ProductTreeRole productTreeRole : ptrs) {
	 * result.add(productTreeRole.getId().getProductTree());
	 * }
	 * 
	 * return result;
	 * }
	 */

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public MethodResponse<?> cekDeleteReferencialIntegrity(Role role) {
		MethodResponse<?> methodResponse = new MethodResponse<Object>();
		methodResponse.setStatus(true);

		if (methodResponse.isStatus()) {
			long userRoleCount = userRoleDao.getCountByRole(role);
			if (userRoleCount > 0) {
				methodResponse.setStatus(false);
				methodResponse.setMessageKey("updateRole.message.deleteFail.referenceToUser");
				methodResponse.setMessageParam(new String[] { role.getName() });
			}
		}

		return methodResponse;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void delete(Role role) {
		Long roleId = role.getId();

		userRoleDao.deleteByRoleId(roleId);
		functionRoleDao.deleteByRoleId(roleId);
		// productTreeRoleDao.deleteByRoleId(roleId);

		roleDao.delete(role);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void save(Role entity) {
		roleDao.save(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deleteById(Long id) {
		roleDao.delete(id);
	}

	@Override
	public Role findById(Long id) {
		return roleDao.findById(id);
	}

	@Override
	public Object getDefaultDao() {
		return roleDao;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void updateDataRole(Role role) {
		roleDao.updateDataRole(role);

	}

	@Override
	public List<Role> getAll() {
		return roleDao.findAll();
	}

}
