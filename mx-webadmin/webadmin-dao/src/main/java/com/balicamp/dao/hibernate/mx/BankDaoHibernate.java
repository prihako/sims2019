/**
 *
 */
package com.balicamp.dao.hibernate.mx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.GenericDaoHibernate;
import com.balicamp.dao.mx.BankDao;
import com.balicamp.model.mx.Bank;
import com.balicamp.util.TransactionsQueryUtil;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionsDaoHibernate.java 518 2013-06-25 10:13:37Z rudi.sadria $
 */
@Repository
public class BankDaoHibernate extends GenericDaoHibernate<Bank, Long> implements BankDao {


	@Autowired
	private TransactionsQueryUtil queryUtil;

	public BankDaoHibernate() {
		super(Bank.class);
	}
}
