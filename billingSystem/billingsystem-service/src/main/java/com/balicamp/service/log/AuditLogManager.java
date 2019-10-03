package com.balicamp.service.log;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.balicamp.model.log.AuditLog;
import com.balicamp.service.IManager;

/**
 * AuditLog Manager
 * 
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public interface AuditLogManager extends IManager {
	/**
	 * save audit log
	 * 
	 * @param reffType
	 * @param subType
	 * @param infoKey
	 * @param infoParam
	 * @param createdBy
	 * @param createdDate
	 * @return
	 */
	AuditLog save(int reffType, String subType, String infoKey, Object[] infoParam, Long createdBy, Date createdDate);

	/**
	 * save audit log
	 * 
	 * @param reffType
	 * @param subType
	 * @param info
	 * @param createdBy
	 * @param createdDate
	 * @return
	 */
	AuditLog save(int reffType, String subType, String info, Long createdBy, Date createdDate);

	/**
	 * save audit log
	 * 
	 * @param reffType
	 * @param subType
	 * @param refNumber
	 * @param infoKey
	 * @param infoParam
	 * @param createdBy
	 * @param createdDate
	 * @return
	 */
	AuditLog save(int reffType, String subType, String refNumber, String infoKey, Object[] infoParam, Long createdBy,
			Date createdDate);

	/**
	 * save audit log
	 * 
	 * @param reffType
	 * @param subType
	 * @param refNumber
	 * @param info
	 * @param createdBy
	 * @param createdDate
	 * @return
	 */
	AuditLog save(int reffType, String subType, String refNumber, String info, Long createdBy, Date createdDate);

	Map<Long, String> findUserByIds(Set<Long> ids);

}
