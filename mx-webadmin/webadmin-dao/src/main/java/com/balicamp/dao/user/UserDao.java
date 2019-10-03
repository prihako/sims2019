/**
 * 
 */
package com.balicamp.dao.user;

import java.util.List;
import java.util.Map;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.user.User;
import com.balicamp.model.user.UserDisplay;
import com.balicamp.model.user.UserGroupDisplay;
import com.balicamp.model.user.UserSingle;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: UserDao.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
public interface UserDao extends AdminGenericDao<User, Long> {
	User findByUsername(String username);

	List<User> findAllEnabled();

	List<UserDisplay> findUser(String userName, String email, String roleName, int firstResult, int maxResult);

	List<UserGroupDisplay> findAdminUser();

	List<UserGroupDisplay> findByGroupId(Long groupId);

	int findUserCount(String userName, String email, String roleName);

	List<User> findCustomerById(Long minUserId, int limit);
	
	void updatePassword(User user);

	void insertByFieldName(Map<String, Object> fields);
	
	void saveUserSingle(UserSingle userSingel);
}
