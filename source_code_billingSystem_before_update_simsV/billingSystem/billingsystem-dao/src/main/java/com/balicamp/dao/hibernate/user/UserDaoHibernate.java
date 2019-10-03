/**
 * 
 */
package com.balicamp.dao.hibernate.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.dao.user.UserDao;
import com.balicamp.model.constant.ModelConstant;
import com.balicamp.model.user.User;
import com.balicamp.model.user.UserDisplay;
import com.balicamp.model.user.UserGroupDisplay;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: UserDaoHibernate.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
@Repository
public class UserDaoHibernate extends AdminGenericDaoImpl<User, Long> implements UserDao {

	public UserDaoHibernate() {
		super(User.class);
	}

	@Override
	public User findByUsername(String username) {
		Query query = getSession().createQuery(
				"select u from User as u where u.enabled = :enabled and u.userName = :userName");
		query.setBoolean("enabled", true);
		query.setString("userName", username);

		List<?> list = query.list();
		if ((list == null) || list.isEmpty()) {
			return null;
		}

		return (User) list.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findAllEnabled() {
		Query query = getSession().createQuery(
				"select u from User as u left join fetch u.userRoleSet as ur  where u.enabled = :enabled");
		query.setBoolean("enabled", true);

		List<User> usrs = new ArrayList<User>();
		usrs = query.list();

		return usrs;
	}

	@Override
	public List<UserDisplay> findUser(String userName, String email, String roleName, int firstResult, int maxResult) {
		boolean byUser = false;
		boolean byEmail = false;
		boolean byRoleName = false;
		StringBuffer sqlQuery = new StringBuffer(
				"select u.id, r.id, u.userName, u.email, r.name, u.userFullName from User as u"
						+ " left join u.userRoleSet as ur" + // NOPMD
						" left join ur.userRoleId.role as r where u.enabled=1 and u.status = 'A'");

		if (userName != null && !userName.trim().equals("")) {
			sqlQuery.append(" and u.userName = :userName");
			byUser = true;
		}
		if (email != null && !email.trim().equals("")) {
			sqlQuery.append(" and u.email = :email");
			byEmail = true;
		}
		if (roleName != null && !roleName.trim().equals("")) {
			sqlQuery.append(" and r.name = :roleName");
			byRoleName = true;
		}

		Query query = getSession().createQuery(sqlQuery.toString());
		if (byUser)
			query.setString("userName", userName);
		if (byEmail)
			query.setString("email", email);
		if (byRoleName)
			query.setString("roleName", roleName);

		List<?> queryResult = findByHQL(query, firstResult, maxResult);

		List<UserDisplay> result = new ArrayList<UserDisplay>();
		for (Object o : queryResult) {
			Object[] arrObject = (Object[]) o;
			UserDisplay userDisplay = new UserDisplay((Long) arrObject[0], (Long) arrObject[1], (String) arrObject[2],
					(String) arrObject[3], (String) arrObject[4], (String) arrObject[5]);
			result.add(userDisplay);
		}
		return result;
	}

	@Override
	public List<UserGroupDisplay> findAdminUser() {
		List<UserGroupDisplay> result = new ArrayList<UserGroupDisplay>();
		String sqlQuery = "select u.id, u.userName, r.name from User as u" + " left join u.userRoleSet as ur"
				+ " left join ur.userRoleId.role as r " + " where u.enabled=1 and u.status=:status" + " order by u.id";

		Query query = getSession().createQuery(sqlQuery);
		query.setString("status", ModelConstant.User.STATUS_ADMIN);
		List<?> queryResult = findByHQL(query, -1, -1);

		Long prevId = -1L;
		UserGroupDisplay userGroupDisplay = null;

		for (Object o : queryResult) {
			Object[] arrObject = (Object[]) o;

			if (prevId.longValue() != ((Long) arrObject[0]).longValue()) {
				userGroupDisplay = new UserGroupDisplay((Long) arrObject[0], (String) arrObject[1],
						(String) arrObject[2]);
				result.add(userGroupDisplay);
				prevId = userGroupDisplay.getUserId();
			} else {
				StringBuffer sb = new StringBuffer(userGroupDisplay.getUserRoles());
				userGroupDisplay.setUserRoles(sb.append(", ").append((String) arrObject[2]).toString());
			}
		}

		return result;
	}

	@Override
	public List<UserGroupDisplay> findByGroupId(Long groupId) {
		List<UserGroupDisplay> result = new ArrayList<UserGroupDisplay>();
		StringBuffer sqlQuery = new StringBuffer("select u.id, u.userName, r.name from UserAdminGroup as uag"
				+ " left join uag.id.user as u" + " left join u.userRoleSet as ur"
				+ " left join ur.userRoleId.role as r "
				+ " where u.enabled=1 and u.status=:status and uag.id.adminGroup.id=:adminGroupId" + " order by u.id");

		Query query = getSession().createQuery(sqlQuery.toString());
		query.setString("status", ModelConstant.User.STATUS_ADMIN);
		query.setLong("adminGroupId", groupId);

		List<?> queryResult = findByHQL(query, -1, -1);
		Long prevId = -1L;

		UserGroupDisplay userGroupDisplay = null;

		for (Object o : queryResult) {
			Object[] arrObject = (Object[]) o;

			if (prevId != (Long) arrObject[0]) {
				userGroupDisplay = new UserGroupDisplay((Long) arrObject[0], (String) arrObject[1],
						(String) arrObject[2]);
				result.add(userGroupDisplay);
				prevId = userGroupDisplay.getUserId();
			} else {
				StringBuffer sb = new StringBuffer(userGroupDisplay.getUserRoles());
				userGroupDisplay.setUserRoles(sb.append(", ").append((String) arrObject[2]).toString());
			}
		}
		return result;
	}

	@Override
	public int findUserCount(String userName, String email, String roleName) {
		boolean byUser = false;
		boolean byEmail = false;
		boolean byRoleName = false;

		StringBuffer sqlQuery = new StringBuffer("select count(u.id) from User as u" + " left join u.userRoleSet as ur"
				+ " left join ur.userRoleId.role as r where u.enabled = 1");

		if (userName != null && !userName.trim().equals("")) {
			sqlQuery.append(" and u.userName = :userName");
			byUser = true;
		}

		if (email != null && !email.trim().equals("")) {
			sqlQuery.append(" and u.email = :email");
			byEmail = true;
		}

		if (roleName != null && !roleName.trim().equals("")) {
			sqlQuery.append(" and r.name = :roleName");
			byRoleName = true;
		}

		Query query = getSession().createQuery(sqlQuery.toString());
		if (byUser) {
			query.setString("userName", userName);
		}
		if (byEmail) {
			query.setString("email", email);
		}
		if (byRoleName) {
			query.setString("roleName", roleName);
		}

		List<?> queryResult = findByHQL(query, -1, -1);

		Long rowCount = (Long) queryResult.get(0);
		return rowCount.intValue();
	}

	@Override
	public List<User> findCustomerById(Long minUserId, int limit) {
		SearchCriteria searchCriteria = SearchCriteria.createSearchCriteria("user");
		searchCriteria.addCriterion(Restrictions.gt("id", minUserId));
		searchCriteria.addCriterion(Restrictions.ne("status", ModelConstant.User.STATUS_ADMIN));

		searchCriteria.addOrder(Order.asc("id"));

		return findByCriteria(searchCriteria, 0, limit);
	}

	public User findById(Long id) {
		Query query = getSession().createQuery(
				"select u from User u left join " + "fetch u.userRoleSet left join fetch u.userAdminGroupSet" +
				// " left join fetch u.userAccountSet" +
						" left join fetch u.persGroupList where u.id=:id order by u.userName asc");
		query.setParameter("id", id);
		return (User) query.uniqueResult();
	}

	@Override
	public void updatePassword(User user) {
		User tmpUser = findById(user.getId());
		tmpUser.setPassword(user.getPassword());
		tmpUser.setChangePasswordDate(user.getChangePasswordDate());
		tmpUser.copy(user);
		saveOrUpdate(tmpUser);
	}

	@Override
	public void insertByFieldName(Map<String, Object> fields) {
		String sqlfields = "insert into User(";
		String sqlvalues = "values(";

		for (String field : fields.keySet()) {
			sqlfields += field + ",";
			sqlvalues += ":" + field + ",";
		}

		sqlfields = sqlfields.substring(0, sqlfields.length() - 1) + ")";
		sqlvalues = sqlvalues.substring(0, sqlvalues.length() - 1) + ")";

		String sql = sqlfields + sqlvalues;

		Query query = getSession().createQuery(sql);
		for (String key : fields.keySet()) {
			query.setParameter(":" + key, fields.get(key));
		}
		query.executeUpdate();
	}

}
