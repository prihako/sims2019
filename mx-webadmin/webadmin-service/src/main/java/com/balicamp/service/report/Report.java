package com.balicamp.service.report;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * @version $Id: Report.java 347 2013-02-22 04:02:58Z bagus.sugitayasa $
 */
public interface Report {

	/**
	 * report name
	 * @return
	 */
	public String getName();

	/**
	 * data source type
	 * @return
	 */
	public String getDataSourceType();

	/**
	 * @return
	 */
	public boolean isNeedDbConnection();

	/**
	 * reportdata
	 * @param parameterMap
	 * @param connection
	 * @return
	 */
	public ReportData getReportData(Map<String, String> inputParameter, Connection connection, HttpSession httpSession);

	public void getReportData(Map<String, String> inputParameter, HttpSession httpSession, OutputStream outputStream);

}
