package com.balicamp.dao.log;

import java.util.List;
import java.util.Set;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.log.AuditLog;
import com.balicamp.model.user.User;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public interface AuditLogDao extends AdminGenericDao<AuditLog, String> {

	List<User> findUserByIds(Set<Long> ids);
}
