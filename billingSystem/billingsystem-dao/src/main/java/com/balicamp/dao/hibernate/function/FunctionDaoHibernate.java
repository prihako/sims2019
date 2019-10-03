/**
 * 
 */
package com.balicamp.dao.hibernate.function;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.function.FunctionDao;
import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.model.function.Function;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: FunctionDaoHibernate.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
@Repository
public class FunctionDaoHibernate extends AdminGenericDaoImpl<Function, Long> implements FunctionDao {

	private Log logging = LogFactory.getLog(FunctionDaoHibernate.class);

	public FunctionDaoHibernate() {
		super(Function.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Function> getBankAdminFunctionList() {
		Query query = getSession().createQuery("from Function as f where f.type=:type order by f.description");
		query.setString("type", "bankAdmin");

		List<Function> fcs = new ArrayList<Function>();
		try {
			fcs = query.list();
		} catch (Exception e) {
			logging.error(e.getMessage());
			return null;
		}

		return fcs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Function> getCustomerFunctionList(Boolean systemOnly) {
		String sql;

		if (systemOnly == null) {
			sql = "from Function as f where f.type = :type order by f.description";
		} else {
			sql = "from Function as f where (f.type = :type) and (f.systemOnly = :systemOnly) order by f.description";
		}

		Query query = getSession().createQuery(sql);

		query.setString("type", "customer");
		if (systemOnly != null) {
			query.setBoolean("systemOnly", systemOnly.booleanValue());
		}

		List<Function> fcs = new ArrayList<Function>();
		try {
			fcs = query.list();
		} catch (Exception e) {
			logging.error(e.getMessage());
			return null;
		}

		return fcs;
	}

	@Override
	public List<Function> findByRoleId(Long roleId) {
		List<Function> result = new ArrayList<Function>();

		return result;
	}

}
