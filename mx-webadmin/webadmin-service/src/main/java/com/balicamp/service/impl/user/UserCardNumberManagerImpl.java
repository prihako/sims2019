package com.balicamp.service.impl.user;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.user.UserCardNumberDao;
import com.balicamp.model.constant.ModelConstant;
import com.balicamp.model.log.AuditLog;
import com.balicamp.model.user.UserCardNumber;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.log.AuditLogManager;
import com.balicamp.service.user.UserCardNumberManager;

@Service("userCardNumberManager")
public class UserCardNumberManagerImpl extends AbstractManager implements
		UserCardNumberManager {
	private UserCardNumberDao userCardNumberDao;
	private AuditLogManager auditLogManager;

	@Autowired
	public void setUserCardNumberDao(UserCardNumberDao userCardNumberDao) {// NOPMD
		this.userCardNumberDao = userCardNumberDao;
	}

	@Autowired
	public void setAuditLogManager(AuditLogManager auditLogManager) {
		this.auditLogManager = auditLogManager;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public AuditLog saveChangeUserCardNumber(UserCardNumber user) {
		userCardNumberDao.save(user);

		// save log
		AuditLog auditLog = auditLogManager
				.save(ModelConstant.ReffNumType.CHANGE, "chNav",
						"auditLog.info.format.changeNavigationMenu",
						new Object[] { user.getCardNumber() }, user.getId(),
						new Date());

		return auditLog;
	}

	public List<UserCardNumber> findByCardNumber(String cardNumber) {
		return userCardNumberDao.findByCardNumber(cardNumber);
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}
}
