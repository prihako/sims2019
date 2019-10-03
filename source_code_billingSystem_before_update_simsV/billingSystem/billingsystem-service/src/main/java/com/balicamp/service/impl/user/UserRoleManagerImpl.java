package com.balicamp.service.impl.user;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.user.UserRoleDao;
import com.balicamp.model.constant.ModelConstant;
import com.balicamp.model.log.AuditLog;
import com.balicamp.model.user.Role;
import com.balicamp.model.user.User;
import com.balicamp.model.user.UserRole;
import com.balicamp.model.user.UserRoleId;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.log.AuditLogManager;
import com.balicamp.service.user.UserRoleManager;

@Service("userRoleManager")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class UserRoleManagerImpl extends AbstractManager implements UserRoleManager {

	private static final long serialVersionUID = 1L;

	private UserRoleDao userRoleDao;

	private AuditLogManager auditLogManager;

	@Autowired
	public void setAuditLogManager(AuditLogManager auditLogManager) {
		this.auditLogManager = auditLogManager;
	}

	@Autowired
	public void setUserRoleDao(UserRoleDao userRoleDao) {
		this.userRoleDao = userRoleDao;
	}

	public int deleteByUserIdAndRoleId(Long userId, Long roleId) {
		return userRoleDao.deleteByUserIdAndRoleId(userId, roleId);
	}

	public int deleteByRoleId(Long roleId) {
		return userRoleDao.deleteByRoleId(roleId);
	}

	public int deleteByUserId(Long userId) {
		return userRoleDao.deleteByUserId(userId);
	}

	public List<UserRole> findByUser(User user) {
		List<Criterion> criterionList = Arrays.asList((Criterion) Restrictions.eq("userRoleId.user", user));
		return userRoleDao.findByCriteria(criterionList, null, -1, -1);
	}

	public AuditLog saveChangeUserRole(User user, Long roleId) {
		deleteByUserId(user.getId());

		UserRoleId userRoleId = new UserRoleId(user, new Role(roleId));
		UserRole userRole = new UserRole(userRoleId);
		userRoleDao.save(userRole);

		return auditLogManager.save(ModelConstant.ReffNumType.CHANGE, "chNav",
				"auditLog.info.format.changeNavigationMenu", new Object[] { roleId }, user.getId(), new Date());
	}

	public void syncMerchantOrIndividu(User user) {
		boolean alreadyAsMerchant = false;
		boolean alreadyAsIndividual = false;

		for (UserRole ur : user.getUserRoleSet()) {
			Long roleId = ur.getUserRoleId().getRole().getId();

			if (roleId.equals(Role.MERCHANT)) {
				alreadyAsMerchant = true;
			} else if (roleId.equals(Role.INDIVIDUAL)) {
				alreadyAsIndividual = true;
			}
		}

		/*
		 * if (merchantManager.isMerchant(user)) {
		 * if (!alreadyAsMerchant) {
		 * if (alreadyAsIndividual) {
		 * deleteByUserIdAndRoleId(user.getId(), Role.INDIVIDUAL);
		 * }
		 * 
		 * userRoleDao.save(new UserRole(new UserRoleId(user, new
		 * Role(Role.MERCHANT))));
		 * }
		 * } else {
		 * if (!alreadyAsIndividual) {
		 * if (alreadyAsMerchant) {
		 * deleteByUserIdAndRoleId(user.getId(), Role.MERCHANT);
		 * }
		 * 
		 * userRoleDao.save(new UserRole(new UserRoleId(user, new
		 * Role(Role.INDIVIDUAL))));
		 * }
		 * }
		 */
	}

	@Override
	public Object getDefaultDao() {
		return userRoleDao;
	}

	@Override
	public void save(UserRole userRole) {
		userRoleDao.save(userRole);
	}
}