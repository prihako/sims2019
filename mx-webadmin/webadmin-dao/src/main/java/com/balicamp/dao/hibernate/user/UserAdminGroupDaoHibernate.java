/**
 * 
 */
package com.balicamp.dao.hibernate.user;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.dao.user.UserAdminGroupDao;
import com.balicamp.model.user.AdminGroup;
import com.balicamp.model.user.User;
import com.balicamp.model.user.UserAdminGroup;
import com.balicamp.model.user.UserAdminGroupId;
import com.balicamp.util.CommonUtil;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: UserAdminGroupDaoHibernate.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
@Repository
public class UserAdminGroupDaoHibernate extends AdminGenericDaoImpl<UserAdminGroup, Double> implements UserAdminGroupDao {

	public UserAdminGroupDaoHibernate() {
		super(UserAdminGroup.class);
	}

	@Override
	public int deleteByUserId(Long userId) {
		Query query = getSession().createQuery("delete UserAdminGroup as uag where uag.id.user.id=:userId");
		query.setLong("userId", userId);
		return query.executeUpdate();
	}

	@Override
	public int deleteByAdminGroupId(Long adminGroupId) {
		Query query = getSession().createQuery("delete UserAdminGroup as uag where uag.id.adminGroup.id=:adminGroupId");
		query.setLong("adminGroupId", adminGroupId);
		return query.executeUpdate();
	}

	@Override
	public void saveByUserIdAndAdminGroupId(Long userId, Long adminGroupId) {
		User user = new User(userId);
		AdminGroup adminGroup = new AdminGroup();
		adminGroup.setId(adminGroupId);
		UserAdminGroupId id = new UserAdminGroupId(user, adminGroup);
		UserAdminGroup uag = new UserAdminGroup(id);
		CommonUtil.interceptAuditModel(uag);
		saveOrUpdate(uag);
	}

	@Override
	public List<UserAdminGroup> findByUser(User user) {
		List<Criterion> criterionList = Arrays.asList(new Criterion[] { Restrictions.eq("id.user", user) });
		return findByCriteria(criterionList, null, -1, -1);
	}

}
