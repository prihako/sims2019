/**
 * 
 */
package com.balicamp.util.admin;

import org.hibernate.Session;

/**
 * Class for generate Oracle sequence
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: OracleSequenceGenerator.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
public class OracleSequenceGenerator implements SequenceGenerator {

	private Session sesion;

	public OracleSequenceGenerator( Session sesion ) {
		this.sesion = sesion;
	}

	@Override
	public Long next(String sequenceName) {
		Long result;
		result = Long.valueOf(String.valueOf(sesion.createSQLQuery("select " + sequenceName + ".NEXTVAL from DUAL")
				.uniqueResult()));
		return result;
	}
}
