/**
 * 
 */
package com.balicamp.dao.hibernate.log;

import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.dao.log.AuditLogDao;
import com.balicamp.model.log.AuditLog;
import com.balicamp.model.user.User;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: AuditLogDaoHibernate.java 368 2013-03-08 04:09:59Z wayan.agustina $
 */
@Repository
public class AuditLogDaoHibernate extends AdminGenericDaoImpl<AuditLog, String> implements AuditLogDao {

	public AuditLogDaoHibernate() {
		super(AuditLog.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> findUserByIds(Set<Long> ids) {
		StringBuffer hql = new StringBuffer("select u from User u where 1=1 and u.id in (");
		for(Long id : ids){
			hql.append(id+",");
		}
		Query query = getSession().createQuery(hql.substring(0, hql.length()-1)+")");
		return query.list();
	}

}
