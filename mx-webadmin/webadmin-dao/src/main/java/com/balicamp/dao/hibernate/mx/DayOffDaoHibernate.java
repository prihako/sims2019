/**
 *
 */
package com.balicamp.dao.hibernate.mx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.GenericDaoHibernate;
import com.balicamp.dao.mx.DayOffDao;
import com.balicamp.model.mx.DayOff;
import com.balicamp.util.TransactionsQueryUtil;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionsDaoHibernate.java 518 2013-06-25 10:13:37Z rudi.sadria $
 */
@Repository
public class DayOffDaoHibernate extends GenericDaoHibernate<DayOff, Long> implements DayOffDao {


	@Autowired
	private TransactionsQueryUtil queryUtil;

	public DayOffDaoHibernate() {
		super(DayOff.class);
	}
}
