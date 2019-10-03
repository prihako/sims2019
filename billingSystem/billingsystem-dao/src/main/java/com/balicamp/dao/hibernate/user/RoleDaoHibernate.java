/**
 * 
 */
package com.balicamp.dao.hibernate.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.dao.user.RoleDao;
import com.balicamp.model.user.Role;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: RoleDaoHibernate.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
@Repository
public class RoleDaoHibernate extends AdminGenericDaoImpl<Role, Long> implements RoleDao {

	private Log logging = LogFactory.getLog(RoleDaoHibernate.class);

	public RoleDaoHibernate() {
		super(Role.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getEditableAdminRoleList() {
		Query query = getSession().createQuery("from Role as r where (r.editable = 1) and (r.rtype = :rtype)");
		query.setString("rtype", "A");

		List<Role> rl = new ArrayList<Role>();
		rl = query.list();
		return rl;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getEditableCustomerRoleList() {
		Query query = getSession().createQuery("from Role as r where (r.editable = 1) and (r.rtype = :rtype)");
		query.setString("rtype", "C");

		List<Role> rls = new ArrayList<Role>();
		rls = query.list();
		return rls;
	}

	@Override
	public void delete(Long id) {
		getSession().createQuery("delete from Role as r where r.id = :id").setParameter("id", id).executeUpdate();
	}

	@Override
	public void updateDataRole(Role role) {
		Query qry = getSession().createQuery("UPDATE Role r SET r.name=:name, r.description=:description WHERE r.id=:id")
				.setParameter("name", role.getName())
				.setParameter("description", role.getDescription())
				.setParameter("id", role.getId());
		qry.executeUpdate();
		
	}
}
