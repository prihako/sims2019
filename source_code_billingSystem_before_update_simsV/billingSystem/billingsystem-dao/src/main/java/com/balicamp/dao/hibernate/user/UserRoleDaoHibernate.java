/**
 * 
 */
package com.balicamp.dao.hibernate.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.dao.user.UserRoleDao;
import com.balicamp.model.user.Role;
import com.balicamp.model.user.UserRole;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: UserRoleDaoHibernate.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
@Repository("userRoleDao")
public class UserRoleDaoHibernate extends AdminGenericDaoImpl<UserRole, Double> implements UserRoleDao {

	public UserRoleDaoHibernate() {
		super(UserRole.class);
	}

	@Override
	public int deleteByUserIdAndRoleId(Long userId, Long roleId) {
		Query query = getSession().createQuery(
				"delete UserRole as ur where ur.userRoleId.user.id=:userId and ur.userRoleId.role.id=:roleId");
		query.setLong("userId", userId);
		query.setLong("roleId", roleId);
		return query.executeUpdate();
	}

	@Override
	public int deleteByRoleId(Long roleId) {
		Query query = getSession().createQuery("delete UserRole as ur where ur.userRoleId.role.id=:roleId");
		query.setLong("roleId", roleId);
		return query.executeUpdate();
	}

	@Override
	public int deleteByUserId(Long userId) {
		Query query = getSession().createQuery("delete UserRole as ur where ur.userRoleId.user.id=:userId");
		query.setLong("userId", userId);
		int delRow = query.executeUpdate();
		
		getSession().flush();
//		getSession().clear();
		return delRow;
	}

	@Override
	public long getCountByRole(Role role) {
		Query query = getSession().createQuery("SELECT count(*) FROM UserRole ur WHERE ur.userRoleId.role.id=:roleId");
		query.setLong("roleId", role.getId());
		return (Long) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<UserRole> findUserRoleByUserId(Long userId) {
		Query query = getSession().createQuery("SELECT u FROM UserRole u WHERE u.userRoleId.user.id = :USERID");
		query.setParameter("USERID", userId);
		List<UserRole> lst = query.list();

		return (null == lst ? null : new HashSet<UserRole>(lst));
	}

}
