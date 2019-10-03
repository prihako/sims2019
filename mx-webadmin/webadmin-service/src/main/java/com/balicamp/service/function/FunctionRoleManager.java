package com.balicamp.service.function;

import java.util.Set;

import com.balicamp.model.function.Function;
import com.balicamp.model.user.Role;
import com.balicamp.service.IManager;

public interface FunctionRoleManager extends IManager {

	void softReset();

	void saveRoleAndFunctions(Role role, Set<Long> functionIdSet);

	Set<Long> getFunctionIds(Role role);

	Set<Long> getFunctionIds(Set<Role> roles);

	Set<Function> getFunctions(Role role);

	Set<Function> getFunctions(Set<Role> roles);

	int deleteByRoleId(Long roleId);
}
