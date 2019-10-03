package com.balicamp.service.function;

import java.util.List;

import com.balicamp.model.function.Function;
import com.balicamp.service.IManager;

public interface FunctionManager extends IManager {
	List<Function> getBankAdminFunctionList();

	List<Function> getCustomerFunctionList(Boolean systemOnly);

	List<Function> findByRoleId(Long roleId);

	List<Function> findAll();
}
