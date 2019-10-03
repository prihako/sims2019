package com.balicamp.service.report;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 * Report manager
 * 
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 * @version $Id: ReportManager.java 347 2013-02-22 04:02:58Z bagus.sugitayasa $
 */
public interface ReportManager {

	public final static String DATASOURCE_EMPTY = "empty";

	public final static String DATASOURCE_HTTPSESSION = "httpCession";

	public final static String DATASOURCE_DB_CONNECTION = "dbConnection";

	public final static String DATASOURCE_HIBERNATE_SESSSION = "hibernateSession";

	public final static String DATASOURCE_DATA_PRODUCER = "dataProducer";

	public final static String HTTPSESSION_DATASOURCE = "jrDataSource";

	public final static String HTTPSESSION_PARAMETER = "jrParameter";

	/**
	 * get Report by name
	 * 
	 * @param name
	 * @return
	 */
	public Report getReport(String name);

	/**
	 * @param name
	 * @return
	 */
	public JasperDesign getJasperDesign(ReportData reportData);

	/**
	 * @param name
	 * @return
	 */
	public JasperReport getJasperReport(ReportData reportData);

	/**
	 * create jasperPrint
	 * 
	 * @param name
	 * @param parameters
	 * @param httpSession
	 * @return
	 */
	public JasperPrint generateJasperPrint(String name, Map<String, String> parameters, HttpSession httpSession);

	public void generateJasperPrint(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream);

	public File generateJasperPrint(String name, Map<String, String> parameters, HttpSession httpSession,
			File destination) throws IOException;

	/**
	 * create report pdf format
	 * 
	 * @param name
	 * @param parameters
	 * @param httpSession
	 * @return
	 */
	public byte[] generatePdf(String name, Map<String, String> parameters, HttpSession httpSession);

	public void generatePdf(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream);

	public void generatePdfTmpFile(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream);

	public void generatePdfPipeStream(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream);

	/**
	 * create report rtf format
	 * 
	 * @param name
	 * @param parameters
	 * @param httpSession
	 * @return
	 */
	public byte[] generateRtf(String name, Map<String, String> parameters, HttpSession httpSession);

	public void generateRtf(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream);

	/**
	 * create report xls format
	 * 
	 * @param name
	 * @param parameters
	 * @param httpSession
	 * @return
	 */
	public byte[] generateXls(String name, Map<String, String> parameters, HttpSession httpSession);

	public void generateXls(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream);

	public void generateXlsTmpFile(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream);

	public void generatePipeStream(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream);

	/**
	 * create report csv format
	 * 
	 * @param name
	 * @param parameters
	 * @param httpSession
	 * @return
	 */
	public byte[] generateCsv(String name, Map<String, String> parameters, HttpSession httpSession);

	public void generateCsvTmpFile(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream);

	public void generateCsv(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream);

	/**
	 * create report txt format
	 * 
	 * @param name
	 * @param parameters
	 * @param httpSession
	 * @return
	 */
	public byte[] generateTxt(String name, Map<String, String> parameters, HttpSession httpSession);

	public void generateTxtTmpFile(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream);

	public void generateTxt(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream);

	/**
	 * create report odt format
	 * 
	 * @param name
	 * @param parameters
	 * @param httpSession
	 * @return
	 */
	public byte[] generateOdt(String name, Map<String, String> parameters, HttpSession httpSession);

	public void generateOdt(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream);

	public JasperPrint generateJasperPrint(ReportData reportData);

	public void generatePdf(ReportData reportData, OutputStream outputStream);

	public void generateXls(ReportData reportData, OutputStream outputStream);

	public void generateHtml(ReportData reportData, OutputStream outputStream, String imagesDir, Object imagesUri,
			HttpSession session);

	public void generateHtmlTmpFile(String name, Map<String, String> parameters, String imagesDir, Object imagesUri,
			HttpSession httpSession, OutputStream outputStream);

	public void generateSknTmpFile(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream);

	public void generateRtgsTmpFile(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream);

	public void generateTxtToSpesificPath(String name, Map<String, String> parameters, HttpSession httpSession,
			String reportResultLocation);
}
