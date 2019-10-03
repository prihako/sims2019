package com.balicamp.service.impl.function;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.function.FunctionDao;
import com.balicamp.model.function.Function;
import com.balicamp.service.function.FunctionManager;
import com.balicamp.service.impl.AbstractManager;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: FunctionManagerImpl.java 1503 2012-07-02 01:45:36Z
 *          arya.sutrisna $
 */
@Service("functionManager")
public class FunctionManagerImpl extends AbstractManager implements
		FunctionManager {
	private FunctionDao dao;

	@Autowired
	public void setDao(FunctionDao dao) {
		this.dao = dao;
	}

	public List<Function> getBankAdminFunctionList() {
		return dao.getBankAdminFunctionList();
	}

	public List<Function> getCustomerFunctionList(Boolean systemOnly) {
		return dao.getCustomerFunctionList(systemOnly);
	}

	public List<Function> findByRoleId(Long roleId) {
		return dao.findByRoleId(roleId);
	}

	@Override
	public List<Function> findAll() {
		return dao.findAll();
	}

	@Override
	public Object getDefaultDao() {

		return null;
	}
}
