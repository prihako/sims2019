package com.balicamp.util.admin;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: SequenceGenerator.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
public interface SequenceGenerator {
	Long next(String sequenceName);
}
