package com.balicamp.service.report.impl;

import static com.balicamp.Constants.SystemParameter.Configuration.CONTEXT_PATH_REAL_DIR;

import java.io.File;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.service.report.Report;
import com.balicamp.service.report.ReportData;
import com.balicamp.service.report.ReportManager;
import com.balicamp.service.report.util.BaseReportUtil;
import com.balicamp.util.CommonUtil;

/**
 * report base class 
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 * @version $Id: ReportBase.java 347 2013-02-22 04:02:58Z bagus.sugitayasa $
 */
public abstract class ReportBase implements Report, BeanNameAware {
	protected final Log log = LogFactory.getLog(getClass());

	private SystemParameterManager systemParameterManager;

	// param key
	public final static String PARAM_KEY_REPORT_TITLE = "reportTitle";
	public final static String PARAM_KEY_REPORT_UTIL = "reportUtil";
	public final static String PARAM_KEY_PRINTED_BY = "printedBy";

	// Date format untuk get param date
	// public final static DateFormat urlParamDateFormat = new
	// SimpleDateFormat("yyyyMMdd");
	public final static DateTimeFormatter urlParamDateFormat = DateTimeFormat.forPattern("yyyyMMdd");

	// BeanNameAware
	public void setBeanName(String beanName) {
		setName(beanName);
	}

	// dataSourceType
	protected String dataSourceType;

	public void setDataSourceType(String dataSourceType) {
		this.dataSourceType = dataSourceType;
	}

	public String getDataSourceType() {
		return dataSourceType;
	}

	// name
	protected String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isNeedDbConnection() {
		return false;
	}

	public ReportData getReportData(Map<String, String> inputParameter, Connection connection, HttpSession httpSession) {
		ReportData reportData = new ReportData();

		String baseReportPath = getBaseReportPath();
		String designFilePath = new StringBuilder(baseReportPath).append(File.separator).append("jrxml")
				.append(File.separator).append(name).append(".jrxml").toString();

		String jasperFilePath = new StringBuilder(baseReportPath).append(File.separator).append("jasper")
				.append(File.separator).append(name).append(".jasper").toString();

		String resourceDir = new StringBuilder(baseReportPath).append(File.separator).append("resource")
				.append(File.separator).append(name).toString();

		reportData.setDesignFilePath(designFilePath);
		reportData.setJasperFilePath(jasperFilePath);
		reportData.setResourceDir(resourceDir);

		reportData.setParameters(generateParameter(inputParameter, httpSession));
		reportData.setJrDataSource(generateJRDataSource(inputParameter, connection, httpSession, reportData));

		return reportData;
	}

	// util
	protected JRDataSource generateJRDataSource(Map<String, String> inputParameter, Connection connection,
			HttpSession httpSession, ReportData reportData) {
		return null;
	}

	protected Map<String, Object> generateParameter(Map<String, String> inputParameter, HttpSession httpSession) {
		return (Map) inputParameter;
	}

	protected Map<String, Object> readParameterFromSession(HttpSession httpSession) {
		return (Map<String, Object>) httpSession.getAttribute(ReportManager.HTTPSESSION_PARAMETER);
	}

	protected Object readDataFromSession(String key, HttpSession httpSession) {
		return httpSession.getAttribute(key);
	}

	protected String getStringParam(Map<String, String> parameterMap, String key, String defaultValue) {
		String value = defaultValue;
		if (parameterMap.containsKey(key))
			value = parameterMap.get(key);

		return value;
	}

	public String getBaseReportPath() {
		String basePath = systemParameterManager.getStringConfiguration(CONTEXT_PATH_REAL_DIR, "");
		return new StringBuilder(basePath).append(File.separator).append("WEB-INF").append(File.separator)
				.append("report").toString();
	}

	public BaseReportUtil getReportUtil() {
		return new BaseReportUtil();
	}

	// util
	protected Long getLongValue(Map<String, String> dataMap, String key, Long defaultValue) {
		String valueString = dataMap.get(key);
		if (CommonUtil.isEmpty(valueString))
			return defaultValue;
		try {
			return Long.valueOf(valueString);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/*
	 * will be override for skn rtgs export report
	 */
	public void getReportData(Map<String, String> inputParameter, HttpSession httpSession, OutputStream outputStream) {

	}

	// setter
	@Autowired
	public void setSystemParameterManager(SystemParameterManager systemParameterManager) {
		this.systemParameterManager = systemParameterManager;
	}

}
