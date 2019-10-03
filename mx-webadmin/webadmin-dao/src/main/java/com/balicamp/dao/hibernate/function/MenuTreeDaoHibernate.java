/**
 * 
 */
package com.balicamp.dao.hibernate.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.function.MenuTreeDao;
import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.model.function.Function;
import com.balicamp.model.function.MenuTree;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: MenuTreeDaoHibernate.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
@Repository
public class MenuTreeDaoHibernate extends AdminGenericDaoImpl<MenuTree, Long> implements MenuTreeDao {

	public MenuTreeDaoHibernate() {
		super(MenuTree.class);
	}

	@Override
	public List<MenuTree> findByFunction(List<Function> functions) {
		if (functions.size() > 0) {
			Criterion criterion = Restrictions.in("function", functions);
			return findByCriteria(Arrays.asList(criterion), null, -1, -1);
		} else {
			return new ArrayList<MenuTree>();
		}
	}

	@Override
	public List<MenuTree> findByIds(List<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return new ArrayList<MenuTree>();
		}

		return findByCriteria(Arrays.asList(Restrictions.in("id", ids)), null, -1, -1);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MenuTree> getAllMenu() {
		return getSession().createQuery("select a from MenuTree a").list();
	}

}
