/**
 * 
 */
package com.balicamp.dao.function;

import java.util.List;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.function.Function;
import com.balicamp.model.function.MenuTree;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: MenuTreeDao.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
public interface MenuTreeDao extends AdminGenericDao<MenuTree, Long> {
	List<MenuTree> findByFunction(List<Function> functions);

	List<MenuTree> findByIds(List<Long> ids);

	List<MenuTree> getAllMenu();
}
