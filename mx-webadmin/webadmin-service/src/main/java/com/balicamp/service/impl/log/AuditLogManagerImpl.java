package com.balicamp.service.impl.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.log.AuditLogDao;
import com.balicamp.model.log.AuditLog;
import com.balicamp.model.user.User;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.log.AuditLogManager;
import com.balicamp.util.SecurityContextUtil;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 * @version $Id: AuditLogManagerImpl.java 1600 2012-07-10 03:35:23Z
 *          wayan.agustina $
 */
@Service("auditLogManager")
public class AuditLogManagerImpl extends AbstractManager implements AuditLogManager {

	private static final long serialVersionUID = 1L;

	private AuditLogDao auditLogDao;// NOPMD

	// private ReffNumManager reffNumManager;

	@Autowired
	public void setAuditLogDao(AuditLogDao auditLogDao) {
		this.auditLogDao = auditLogDao;
	}

	/*
	 * @Autowired
	 * public void setReffNumManager(ReffNumManager reffNumManager) {
	 * this.reffNumManager = reffNumManager;
	 * }
	 */

	public AuditLog save(int reffType, String subType, String infoKey, Object[] infoParam, Long createdBy,
			Date createdDate) {
		return save(reffType, subType, null, infoKey, infoParam, createdBy, createdDate);
	}

	public AuditLog save(int reffType, String subType, String info, Long createdBy, Date createdDate) {
		return save(reffType, subType, null, info, createdBy, createdDate);
	}

	public AuditLog save(int reffType, String subType, String refNumber, String infoKey, Object[] infoParam,
			Long createdBy, Date createdDate) {
		String info = messageSourceWrapper.getMessage(infoKey, infoParam, infoKey, new Locale("en"));
		return save(reffType, subType, refNumber, info, createdBy, createdDate);
	}

	public AuditLog save(int reffType, String subType, String refNumber, String info, Long createdBy, Date createdDate) {

		if (refNumber == null)
			refNumber = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "-"
					+ String.valueOf(System.currentTimeMillis()) + "-" + createdBy;

		AuditLog auditLog = new AuditLog();
		auditLog.setReferenceNumber(refNumber);
		auditLog.setDataType(reffType + "-" + subType);
		auditLog.setInfo(info);

		if (createdBy != null)
			auditLog.setCreatedBy(createdBy);
		else
			auditLog.setCreatedBy(new User(SecurityContextUtil.getCurrentUserId()).getChangedBy().longValue());

		if (createdDate != null)
			auditLog.setCreatedDate(createdDate);
		else
			auditLog.setCreatedDate(new Date());

		auditLogDao.saveOrUpdate(auditLog);
		// auditLogDao.save(auditLog);

		return auditLog;
	}

	@Override
	public Object getDefaultDao() {
		return auditLogDao;
	}

	@Override
	public Map<Long, String> findUserByIds(Set<Long> ids) {
		List<User> users = auditLogDao.findUserByIds(ids);
		Map<Long, String> userMap = new HashMap<Long, String>();
		for (User user : users) {
			userMap.put(user.getId(), user.getUsername());
		}
		return userMap;
	}
}
