package com.balicamp.service.user;

import java.util.Set;

import com.balicamp.model.user.AdminGroup;
import com.balicamp.service.IManager;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 * @version $Id: AdminGroupManager.java 1503 2012-07-02 01:45:36Z arya.sutrisna
 *          $
 */
public interface AdminGroupManager extends IManager {
	void deleteAdminGroupAndMember(Long adminGroupId);

	void saveAdminGroupAndMember(AdminGroup adminGroup, Set<Long> userIdSet);

	AdminGroup findById(Long groupId);
}
