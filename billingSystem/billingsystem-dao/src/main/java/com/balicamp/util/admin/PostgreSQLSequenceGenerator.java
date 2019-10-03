package com.balicamp.util.admin;

import org.hibernate.Session;

/**
 * Class for generate PostgreSQL sequence
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: PostgreSQLSequenceGenerator.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
public class PostgreSQLSequenceGenerator implements SequenceGenerator {

	private Session sesion;

	public PostgreSQLSequenceGenerator( Session sesion ) {
		this.sesion = sesion;
	}

	public Long next(String sequenceName) {
		Long result;
		result = Long.valueOf(String.valueOf(sesion.createSQLQuery("select nextval('" + sequenceName + "')")
				.uniqueResult()));
		return result;
	}

	public void resetTo(int start, String sequenceName) {
		sesion.createSQLQuery("alter sequence " + sequenceName + " restart with " + start);
	}

}
