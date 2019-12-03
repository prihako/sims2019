/**
 *
 */
package com.balicamp.dao.hibernate.mx;

import org.springframework.stereotype.Repository;

import com.balicamp.dao.mx.ForcePaymentLogDao;
import com.balicamp.model.mx.ForcePaymentLog;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: MessageLogsDaoHibernate.java 503 2013-05-24 08:13:16Z
 *          rudi.sadria $
 */
@Repository
public class ForcePaymentLogDaoHibernate extends
		MxGenericDaoHibernate<ForcePaymentLog, Long> implements ForcePaymentLogDao {

	public ForcePaymentLogDaoHibernate() {
		super(ForcePaymentLog.class);
	}

}
