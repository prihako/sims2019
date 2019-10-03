package com.balicamp.service.report.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRDataSource;

import com.balicamp.exception.ApplicationException;
import com.balicamp.service.report.ReportData;
import com.balicamp.service.report.ReportManager;
import com.balicamp.service.report.datasource.ResultSetJRDataSource;
import com.balicamp.util.DatabaseSpecificQueries;

/**
 * Report Audit Log
 * @author yohan
 * @version $Id: ReportUserActivity.java 368 2013-03-08 04:09:59Z wayan.agustina $
 */
public class ReportUserActivity extends ReportBase {
	public final static String PARAM_KEY_START_TRX_DATE = "startTrxDate";
	public final static String PARAM_KEY_END_TRX_DATE = "endTrxDate";
	public final static String PARAM_KEY_USERID = "userId";
	public final static String PARAM_KEY_TRX_TYPE = "trxType";

	public ReportUserActivity() {
		super();
		setDataSourceType(ReportManager.DATASOURCE_DATA_PRODUCER);
	}

	@Override
	public boolean isNeedDbConnection() {
		return true;
	}

	@Override
	protected Map<String, Object> generateParameter(Map<String, String> inputParameter, HttpSession httpSession) {
		Map<String, Object> parameters = super.generateParameter(inputParameter, httpSession);

		parameters.put(PARAM_KEY_PRINTED_BY, /*
											 * SecurityContextUtil.getCurrentUser
											 * ().getUserFullName()
											 */"ADMIN 1");
		parameters.put(PARAM_KEY_REPORT_UTIL, getReportUtil());
		try {
			parameters.put(PARAM_KEY_START_TRX_DATE,
					urlParamDateFormat.parseDateTime((String) inputParameter.get(PARAM_KEY_START_TRX_DATE)).toDate());
			parameters.put(PARAM_KEY_END_TRX_DATE,
					urlParamDateFormat.parseDateTime((String) inputParameter.get(PARAM_KEY_END_TRX_DATE)).toDate());
		} catch (Exception e) {
			log.error("error date parameter", e);
			throw new ApplicationException("", e);
		}
		return parameters;
	}

	@Override
	protected JRDataSource generateJRDataSource(Map<String, String> inputParameter, Connection connection,
			HttpSession httpSession, ReportData reportData) {

		try {
			Date startDate = (Date) reportData.getParameters().get(PARAM_KEY_START_TRX_DATE);
			Date endDate = (Date) reportData.getParameters().get(PARAM_KEY_END_TRX_DATE);
			String trxType = (String) reportData.getParameters().get(PARAM_KEY_TRX_TYPE);
			String userId = (String) reportData.getParameters().get(PARAM_KEY_USERID);

			StringBuffer sqlQuery = new StringBuffer(DatabaseSpecificQueries.RUA_GJRDS);
			if (trxType != null) {
				sqlQuery.append(" and a.data_type like '").append(trxType).append("-%'");
			}
			if (userId != null) {
				sqlQuery.append(" and u.user_name = '").append(userId.toUpperCase()).append("'");
			}

			PreparedStatement statement = connection.prepareStatement(sqlQuery.toString());
			statement.setDate(1, new java.sql.Date(startDate.getTime()));
			statement.setDate(2, new java.sql.Date(endDate.getTime()));

			ResultSet resultSet = statement.executeQuery();

			ResultSetJRDataSource resultSetJRDataSource = new ResultSetJRDataSource(resultSet, statement);
			return resultSetJRDataSource;

		} catch (Exception e) {
			log.error("", e);
			throw new ApplicationException("", e);
		}
	}
}
