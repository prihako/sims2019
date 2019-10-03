package com.balicamp.service.report.datasource;

import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 * @version $id: $
 */
public class ResultSetJRDataSource extends JRResultSetDataSource {

	protected ResultSet resultSet;
	protected Statement statement;

	public ResultSetJRDataSource(ResultSet resultSet) {
		super(resultSet);
		this.resultSet = resultSet;
	}

	public ResultSetJRDataSource(ResultSet resultSet, Statement statement) {
		this(resultSet);
		this.statement = statement;
	}

	@Override
	public boolean next() throws JRException {
		boolean nextAvailable = super.next();

		if (!nextAvailable) {
			// close resultset
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (Exception e) {
					// ignore
				}
			}

			// close statment
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception e) {
					// ignore
				}
			}
		}

		return nextAvailable;
	}

}
