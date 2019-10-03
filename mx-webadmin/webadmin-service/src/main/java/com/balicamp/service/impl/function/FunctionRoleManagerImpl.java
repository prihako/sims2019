package com.balicamp.service.impl.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.function.FunctionDao;
import com.balicamp.dao.function.FunctionRoleDao;
import com.balicamp.dao.user.RoleDao;
import com.balicamp.model.function.Function;
import com.balicamp.model.function.FunctionRole;
import com.balicamp.model.user.Role;
import com.balicamp.service.function.FunctionRoleManager;
import com.balicamp.service.impl.AbstractManager;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: FunctionRoleManagerImpl.java 1551 2012-07-04 09:24:11Z
 *          wayan.agustina $
 */
@Service("functionRoleManager")
// @Lazy(value = true)
public class FunctionRoleManagerImpl extends AbstractManager implements FunctionRoleManager, InitializingBean {

	private FunctionRoleDao functionRoleDao;

	private RoleDao roleDao;

	private FunctionDao functionDao;

	private Map<Long, Set<Long>> functionIdsByRoleId;

	private Map<Long, Set<Function>> functionsByRoleId;

	private ReadLock sharedLock;

	private WriteLock exclusiveLock;

	@Autowired
	public void setFunctionDao(FunctionDao functionDao) {
		this.functionDao = functionDao;
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
		sharedLock = lock.readLock();
		exclusiveLock = lock.writeLock();

		this.functionIdsByRoleId = new HashMap<Long, Set<Long>>();
		this.functionsByRoleId = new HashMap<Long, Set<Function>>();

	}

	@Autowired
	public void setFunctionRoleDao(FunctionRoleDao functionRoleDao) {
		this.functionRoleDao = functionRoleDao;
	}

	public void afterPropertiesSet() throws Exception {
		softReset();
	}

	public void softReset() {
		Map<Long, Set<Long>> functionIdsByRoleId = new HashMap<Long, Set<Long>>();
		Map<Long, Set<Function>> functionsByRoleId = new HashMap<Long, Set<Function>>();

		List<FunctionRole> all = new ArrayList<FunctionRole>();

		try {
			all = functionRoleDao.findAll();
		} catch (Exception e) {
			all = null;
		}
		if (all != null) {

			for (FunctionRole each : all) {
				Long roleId = each.getId().getRole().getId();
				Long functionId = each.getId().getFunction().getId();

				Function func = functionDao.findById(functionId);

				Set<Long> ids = functionIdsByRoleId.get(roleId);
				if (ids == null) {
					ids = new HashSet<Long>();
					functionIdsByRoleId.put(roleId, ids);
				}

				ids.add(functionId);

				Set<Function> funcs = functionsByRoleId.get(roleId);
				if (funcs == null) {
					funcs = new HashSet<Function>();
					functionsByRoleId.put(roleId, funcs);
				}

				funcs.add(func);
			}
		}

		// APPLY!
		exclusiveLock.lock();

		this.functionIdsByRoleId = functionIdsByRoleId;
		this.functionsByRoleId = functionsByRoleId;

		exclusiveLock.unlock();
	}

	public Set<Long> getFunctionIds(Role role) {
		sharedLock.lock();
		try {
			return functionIdsByRoleId.get(role.getId());
		} finally {
			sharedLock.unlock();
		}
	}

	public Set<Long> getFunctionIds(Set<Role> roles) {
		if (roles == null) {
			return null;
		}

		Set<Long> joined = new HashSet<Long>();

		sharedLock.lock();
		try {
			for (Role role : roles) {
				Set<Long> ptrs = functionIdsByRoleId.get(role.getId());
				if (ptrs != null) {
					joined.addAll(ptrs);
				}
			}
		} finally {
			sharedLock.unlock();
		}

		return joined;
	}

	public Set<Function> getFunctions(Role role) {
		sharedLock.lock();
		try {
			return functionsByRoleId.get(role.getId());
		} finally {
			sharedLock.unlock();
		}
	}

	public Set<Function> getFunctions(Set<Role> roles) {
		if (roles == null) {
			return null;
		}

		Set<Function> joined = new HashSet<Function>();

		sharedLock.lock();
		try {
			for (Role role : roles) {
				Set<Function> ptrs = functionsByRoleId.get(role.getId());
				if (ptrs != null) {
					joined.addAll(ptrs);
				}
			}
		} finally {
			sharedLock.unlock();
		}

		return joined;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void saveRoleAndFunctions(Role role, Set<Long> functionIds) {
		functionRoleDao.deleteByRoleId(role.getId());
		// roleDao.updateDataRole(role);

		for (Long functionId : functionIds) {
			functionRoleDao.saveByRoleIdAndFunctionId(role.getId(), functionId);
		}

		exclusiveLock.lock();
		try {
			functionIdsByRoleId.put(role.getId(), functionIds);
		} finally {
			exclusiveLock.unlock();
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int deleteByRoleId(Long roleId) {
		int result = functionRoleDao.deleteByRoleId(roleId);
		if (result > 0) {
			exclusiveLock.lock();
			try {
				functionIdsByRoleId.remove(roleId);
			} finally {
				exclusiveLock.unlock();
			}
		}

		return result;
	}

	@Override
	public Object getDefaultDao() {
		return null;
	}
}
