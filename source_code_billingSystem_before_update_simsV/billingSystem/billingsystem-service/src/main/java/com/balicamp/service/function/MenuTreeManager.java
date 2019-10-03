package com.balicamp.service.function;

import java.util.Set;

import com.balicamp.model.function.MenuTree;
import com.balicamp.model.user.User;
import com.balicamp.service.IManager;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public interface MenuTreeManager extends IManager {

	void softReset();

	Set<MenuTree> getAllMenus(User user);

	Set<MenuTree> getTopMenus(Set<MenuTree> allMenus);

	Set<MenuTree> getChildMenus(Set<MenuTree> allMenus, MenuTree parent);
}
