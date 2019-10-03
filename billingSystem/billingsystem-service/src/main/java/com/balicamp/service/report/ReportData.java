package com.balicamp.service.report;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * Report Data, data parameter for generate Report
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 * @version $Id: ReportData.java 347 2013-02-22 04:02:58Z bagus.sugitayasa $
 */
public class ReportData {

	private JRDataSource jrDataSource;
	private Map<String, Object> parameters;
	private String designFilePath;
	private String jasperFilePath;
	private String resourceDir;

	// constructor
	public ReportData() {
	}

	public ReportData(JRDataSource jrDataSource, Map<String, Object> parameters) {
		setJrDataSource(jrDataSource);
		setParameters(parameters);
	}

	// getter setter
	public JRDataSource getJrDataSource() {
		return jrDataSource;
	}

	public void setJrDataSource(JRDataSource jrDataSource) {
		this.jrDataSource = jrDataSource;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public String getDesignFilePath() {
		return designFilePath;
	}

	public void setDesignFilePath(String designFilePath) {
		this.designFilePath = designFilePath;
	}

	public String getJasperFilePath() {
		return jasperFilePath;
	}

	public void setJasperFilePath(String jasperFilePath) {
		this.jasperFilePath = jasperFilePath;
	}

	public String getResourceDir() {
		return resourceDir;
	}

	public void setResourceDir(String resourceDir) {
		this.resourceDir = resourceDir;
	}

}
