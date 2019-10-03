package com.balicamp.service.impl.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.function.MenuTreeDao;
import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.function.Function;
import com.balicamp.model.function.MenuTree;
import com.balicamp.model.user.User;
import com.balicamp.service.function.FunctionRoleManager;
import com.balicamp.service.function.MenuTreeManager;
import com.balicamp.service.impl.AbstractManager;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: MenuTreeManagerImpl.java 113 2012-12-12 04:15:16Z bagus.sugitayasa $
 */
@Service("menuTreeManager")
//@Lazy(value = true)
public class MenuTreeManagerImpl extends AbstractManager implements MenuTreeManager, InitializingBean {
	private static final Long ZERO = Long.valueOf(0L);

	@Autowired
	private MenuTreeDao menuTreeDao;

	@Autowired
	private FunctionRoleManager functionRoleManager;

	private Map<Long, MenuTree> menuTreeById;

	private Map<Long, List<MenuTree>> menuTreeByFunctionId;

	
	public void setMenuTreeDao(MenuTreeDao menuTreeDao) {
		this.menuTreeDao = menuTreeDao;
	}

	public void setFunctionRoleManager(FunctionRoleManager functionRoleManager) {
		this.functionRoleManager = functionRoleManager;
	}
	
	public void afterPropertiesSet() throws Exception {
		softReset();
	}

	public void softReset() {
		SearchCriteria criteria = new SearchCriteria("menuTree");
		criteria.addOrder(Order.asc("menuOrder"));

		List<MenuTree> all = new ArrayList<MenuTree>();

		try {
			//all = menuTreeDao.findByCriteria(criteria, -1, -1);
			all = menuTreeDao.getAllMenu();
		} catch (Exception e) {
			all = null;
			e.printStackTrace();
		}

		Map<Long, MenuTree> menuTreeById = new HashMap<Long, MenuTree>();
		Map<Long, List<MenuTree>> menuTreeByFunctionId = new HashMap<Long, List<MenuTree>>();

		if (all != null) {
			for (MenuTree mt : all) {
				if (!"disable".equalsIgnoreCase(mt.getMenuType())) {
					menuTreeById.put(mt.getId(), mt);

					Function func = mt.getFunction();
					Long funcId = func == null ? null : func.getId();

					if (funcId != null) {
						List<MenuTree> list = menuTreeByFunctionId.get(funcId);
						if (list == null) {
							list = new ArrayList<MenuTree>();
							menuTreeByFunctionId.put(funcId, list);
						}

						list.add(mt);
					}
				}
			}
		}

		// APPLY!
		this.menuTreeById = menuTreeById;
		this.menuTreeByFunctionId = menuTreeByFunctionId;
	}

	private List<MenuTree> findByFunctions(Set<Long> functionIds) {
		List<MenuTree> trees = new ArrayList<MenuTree>();

		for (Long functionId : functionIds) {
			List<MenuTree> once = menuTreeByFunctionId.get(functionId);
			if ((once != null) && !once.isEmpty()) {
				trees.addAll(once);
			}
		}

		return trees;
	}

	public Set<MenuTree> getAllMenus(User user) {
		Set<MenuTree> allMenus = new TreeSet<MenuTree>();

		Set<Long> functionIds = functionRoleManager.getFunctionIds(user.getRoleSet());
		if (functionIds != null) {
			List<MenuTree> list = findByFunctions(functionIds);
			addRequiredParent(list);

			for (MenuTree menuTree : list) {
				if (menuTree != null) {
					allMenus.add(menuTree);
				}
			}
		}

		return allMenus;
	}

	public Set<MenuTree> getTopMenus(Set<MenuTree> allMenus) {
		Set<MenuTree> topMenus = new TreeSet<MenuTree>();

		for (MenuTree menuTree : allMenus) {
			Long parentId = menuTree.getMenuParentId();

			/*if (ZERO.equals(parentId) || (parentId == null)) {
				topMenus.add(menuTree);
			}*/
			
			if (parentId.equals(Long.valueOf(1)) || (parentId == null)) {
				topMenus.add(menuTree);
			}
		}

		return topMenus;
	}

	public Set<MenuTree> getChildMenus(Set<MenuTree> allMenus, MenuTree parent) {
		Long parentId = parent.getId();
		Set<MenuTree> children = new TreeSet<MenuTree>();

		for (MenuTree child : allMenus) {
			if (child.getMenuParentId().equals(parentId)) {
				children.add(child);
			}
		}

		return children;
	}

	private void addRequiredParent(List<MenuTree> menuTreeList) {
		if (menuTreeList == null) {
			return;
		}

		Set<Long> ids = new HashSet<Long>();
		for (MenuTree mt : menuTreeList) {
			ids.add(mt.getId());
		}

		Set<MenuTree> requiredParents = new HashSet<MenuTree>();

		for (MenuTree menuTree : menuTreeList) {
			if (!ids.contains(menuTree.getMenuParentId())) {
				MenuTree parent = menuTreeById.get(menuTree.getMenuParentId());

				if (parent != null) {
					requiredParents.add(parent);
					ids.add(parent.getId());

					while ((parent != null) && (parent.getMenuParentId() != 0)) {
						parent = menuTreeById.get(parent.getMenuParentId());

						if ((parent != null) && !ids.contains(parent.getId())) {
							requiredParents.add(parent);
							ids.add(parent.getId());
						}
					}
				}
			}
		}

		menuTreeList.addAll(requiredParents);
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}
}
