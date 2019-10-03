package com.balicamp.dao.hibernate.mx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.mx.MxParmDao;
import com.balicamp.model.mx.MxParm;
import com.balicamp.util.TransactionsQueryUtil;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionLogDaoHibernate.java 503 2013-05-24 08:13:16Z rudi.sadria $
 */
@Repository
public class MxParmDaoHibernate extends MxGenericDaoHibernate<MxParm, Long> implements MxParmDao {

	@Autowired
	private TransactionsQueryUtil queryUtil;
	
	public MxParmDaoHibernate() {
		super(MxParm.class);
	}
}