package com.balicamp.service.impl.user;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.user.AdminGroupDao;
import com.balicamp.dao.user.UserAdminGroupDao;
import com.balicamp.model.user.AdminGroup;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.user.AdminGroupManager;
import com.balicamp.util.CommonUtil;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 * @version $Id: AdminGroupManagerImpl.java 1511 2012-07-02 08:04:30Z
 *          wayan.agustina $
 */
@Service("adminGroupManager")
public class AdminGroupManagerImpl extends AbstractManager implements
		AdminGroupManager {
	private AdminGroupDao adminGroupDao;// NOPMD

	private UserAdminGroupDao userAdminGroupDao;

	@Autowired
	public void setAdminGroupDao(AdminGroupDao adminGroupDao) {
		this.adminGroupDao = adminGroupDao;
	}

	@Autowired
	public void setUserAdminGroupDao(UserAdminGroupDao userAdminGroupDao) {
		this.userAdminGroupDao = userAdminGroupDao;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deleteAdminGroupAndMember(Long adminGroupId) {
		userAdminGroupDao.deleteByAdminGroupId(adminGroupId);
		adminGroupDao.delete(adminGroupDao.findById(adminGroupId));
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void saveAdminGroupAndMember(AdminGroup adminGroup,
			Set<Long> userIdSet) {
		CommonUtil.interceptAuditModel(adminGroup);
		adminGroupDao.saveOrUpdate(adminGroup);
		userAdminGroupDao.deleteByAdminGroupId(adminGroup.getId());
		for (Long userId : userIdSet) {
			userAdminGroupDao.saveByUserIdAndAdminGroupId(userId,
					adminGroup.getId());
		}
	}

	@Override
	public AdminGroup findById(Long groupId) {
		return adminGroupDao.findById(groupId);
	}

	@Override
	public Object getDefaultDao() {
		return adminGroupDao;
	}

}
