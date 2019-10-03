package com.balicamp.service.report.impl;

import static com.balicamp.Constants.SystemParameter.Configuration.CONTEXT_PATH_REAL_DIR;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balicamp.Constants;
import com.balicamp.exception.ApplicationException;
import com.balicamp.service.common.TaskExecutorWrapper;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.service.report.Report;
import com.balicamp.service.report.ReportData;
import com.balicamp.service.report.ReportManager;
import com.balicamp.service.report.ZipManager;
import com.balicamp.service.report.datasource.GenericJRDataSource;
import com.balicamp.util.FileUtil;
import com.balicamp.util.JasperUtil;

public class ReportManagerImpl implements ReportManager {
	private static final Log log = LogFactory.getLog(ReportManagerImpl.class);

	private DataSource dataSource;

	private Map<String, Report> reportMap;

	private TaskExecutorWrapper taskExecutorWrapper;

	private SystemParameterManager systemParameterManager;

	private ZipManager zipManager;

	public ReportManagerImpl() {
	}

	private static boolean containsErrorMessage(Throwable error, String message) {
		message = message.toLowerCase();

		Throwable current = error;

		while (current != null) {
			String errorMsg = current.getMessage();

			if ((errorMsg != null) && errorMsg.toLowerCase().contains(message)) {
				return true;
			}

			current = current.getCause();
		}

		return false;
	}

	public Report getReport(String name) {
		Report report = reportMap.get(name);
		if (report == null) {
			log.warn("unable to find report with name " + name);
		}

		return report;
	}

	public JasperDesign getJasperDesign(ReportData reportData) {
		String designFilePath = reportData.getDesignFilePath();
		String resourceDir = reportData.getResourceDir();

		try {
			JasperDesign jasperDesign = JRXmlLoader.load(designFilePath);

			JasperUtil.adjustResourceLocation(jasperDesign, resourceDir);
			return jasperDesign;
		} catch (Exception e) {
			log.error("fail load jrxml file " + designFilePath, e);
			throw new ApplicationException("fail load jrxml file" + designFilePath, e);
		}
	}

	public JasperReport getJasperReport(ReportData reportData) {
		String designFilePath = reportData.getDesignFilePath();
		String jasperFilePath = reportData.getJasperFilePath();

		File designFile = new File(designFilePath);
		File jasperFile = new File(jasperFilePath);

		JasperReport jasperReport = null;

		if (!jasperFile.exists() || (designFile.lastModified() > jasperFile.lastModified())) {
			try {
				JasperDesign jasperDesign = getJasperDesign(reportData);
				jasperReport = JasperCompileManager.compileReport(jasperDesign);

				FileUtil.mkdirs(jasperFile.getParent());
				JRSaver.saveObject(jasperReport, jasperFile);
			} catch (Exception e) {
				log.error("fail compile jasper report " + designFilePath, e);
				throw new ApplicationException("fail compile jasper report " + designFilePath, e);
			}

		} else {
			try {
				jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
			} catch (JRException e) {
				log.error("fail load jasper report " + jasperFile, e);
				throw new ApplicationException("fail load jasper report" + jasperFile, e);
			}
		}

		return jasperReport;
	}

	@SuppressWarnings("unchecked")
	public JasperPrint generateJasperPrint(String name, Map<String, String> parameters, HttpSession httpSession) {
		JasperPrint jasperPrint = null;
		Report report = getReport(name);

		if (report == null) {
			throw new ApplicationException("report " + name + " cannot be found");
		}

		Connection connection = null;
		try {
			if (report.isNeedDbConnection() || report.getDataSourceType().equals(DATASOURCE_DB_CONNECTION))
				connection = dataSource.getConnection();

			ReportData reportData = report.getReportData(parameters, connection, httpSession);

			JasperReport jasperReport = getJasperReport(reportData);

			if (report.getDataSourceType().equals(DATASOURCE_EMPTY)) {
				jasperPrint = JasperFillManager.fillReport(jasperReport, reportData.getParameters(),
						new JREmptyDataSource());
			} else if (report.getDataSourceType().equals(DATASOURCE_HTTPSESSION)) {
				JRDataSource jrDataSource = (JRDataSource) httpSession.getAttribute(HTTPSESSION_DATASOURCE);
				Map<Object, Object> jrParameter = (Map<Object, Object>) httpSession.getAttribute(HTTPSESSION_PARAMETER);

				// reset pointer first
				if (jrDataSource instanceof GenericJRDataSource) {
					GenericJRDataSource genericJRDataSource = (GenericJRDataSource) jrDataSource;
					genericJRDataSource.resetPointer();
				}

				jasperPrint = JasperFillManager.fillReport(jasperReport, jrParameter, jrDataSource);
			} else if (report.getDataSourceType().equals(DATASOURCE_DB_CONNECTION)) {
				connection = dataSource.getConnection();
				jasperPrint = JasperFillManager.fillReport(jasperReport, reportData.getParameters(), connection);
			} else if (report.getDataSourceType().equals(DATASOURCE_DATA_PRODUCER)) {
				jasperPrint = JasperFillManager.fillReport(jasperReport, reportData.getParameters(),
						reportData.getJrDataSource());
			} else {
				jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
			}
		} catch (Exception e) {
			log.error("fail generate jasper print for report " + name, e);
			throw new ApplicationException("fail generate jasper print for report " + name, e);
		} finally {
			try {
				connection.close();
			} catch (Exception e) {}
		}

		return jasperPrint;
	}

	public void generateJasperPrintAsyn(final String name, final Map<String, String> parameters,
			final HttpSession httpSession, final OutputStream outputStream) {
		taskExecutorWrapper.executeTask(new Runnable() {
			public void run() {
				generateJasperPrint(name, parameters, httpSession, outputStream);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public void generateJasperPrint(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream) {
		Report report = getReport(name);

		if (report == null)
			throw new ApplicationException("report " + name + " cannot be found");

		Connection connection = null;
		try {
			if (report.isNeedDbConnection() || report.getDataSourceType().equals(DATASOURCE_DB_CONNECTION))
				connection = dataSource.getConnection();

			ReportData reportData = report.getReportData(parameters, connection, httpSession);
			JasperReport jasperReport = getJasperReport(reportData);

			if (report.getDataSourceType().equals(DATASOURCE_EMPTY)) {
				JasperFillManager.fillReportToStream(jasperReport, outputStream, reportData.getParameters(),
						new JREmptyDataSource());
			} else if (report.getDataSourceType().equals(DATASOURCE_HTTPSESSION)) {
				JRDataSource jrDataSource = (JRDataSource) httpSession.getAttribute(HTTPSESSION_DATASOURCE);
				Map<Object, Object> jrParameter = (Map<Object, Object>) httpSession.getAttribute(HTTPSESSION_PARAMETER);

				// reset pointer first
				if (jrDataSource instanceof GenericJRDataSource) {
					GenericJRDataSource genericJRDataSource = (GenericJRDataSource) jrDataSource;
					genericJRDataSource.resetPointer();
				}

				JasperFillManager.fillReportToStream(jasperReport, outputStream, jrParameter, jrDataSource);
			} else if (report.getDataSourceType().equals(DATASOURCE_DB_CONNECTION)) {
				connection = dataSource.getConnection();
				JasperFillManager
						.fillReportToStream(jasperReport, outputStream, reportData.getParameters(), connection);
			} else if (report.getDataSourceType().equals(DATASOURCE_DATA_PRODUCER)) {
				JasperFillManager.fillReportToStream(jasperReport, outputStream, reportData.getParameters(),
						reportData.getJrDataSource());
			} else {
				JasperFillManager.fillReportToStream(jasperReport, outputStream, reportData.getParameters(),
						new JREmptyDataSource());
			}
		} catch (Exception e) {
			log.error("fail generate jasper print for report " + name, e);
			throw new ApplicationException("fail generate jasper print for report " + name, e);
		} finally {
			try {
				connection.close();
			} catch (Exception e) {}
		}
	}

	public File generateJasperPrint(String name, Map<String, String> parameters, HttpSession httpSession,
			File destination) throws IOException {
		DateFormat concatDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		if (destination == null) {
			String basePath = systemParameterManager.getStringConfiguration(CONTEXT_PATH_REAL_DIR, "");
			StringBuilder fileName = new StringBuilder().append(basePath).append(File.separator).append("WEB-INF")
					.append(File.separator).append("report").append(Constants.FILE_SEP).append("tmp")
					.append(Constants.FILE_SEP).append(name).append("_").append(concatDateFormat.format(new Date()))
					.append(".print");
			destination = new File(fileName.toString());
		}

		if (!destination.getParentFile().exists())
			destination.getParentFile().mkdirs();

		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(destination);
			generateJasperPrint(name, parameters, httpSession, outputStream);
		} finally {
			if (outputStream != null)
				outputStream.close();
		}

		return destination;
	}

	public byte[] generatePdf(String name, Map<String, String> parameters, HttpSession httpSession) {
		try {
			JasperPrint jasperPrint = generateJasperPrint(name, parameters, httpSession);
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (JRException e) {
			log.error("fail generate pdf for report " + name, e);
			throw new ApplicationException("fail generate pdf for report " + name, e);
		}
	}

	public void generatePdf(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream) {
		try {
			JasperPrint jasperPrint = generateJasperPrint(name, parameters, httpSession);
			JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
		} catch (Exception e) {
			log.error("fail generate pdf for report " + name, e);
			throw new ApplicationException("fail generate pdf for report " + name, e);
		}
	}

	public void generatePdfTmpFile(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream) {
		File destination = null;
		try {
			destination = generateJasperPrint(name, parameters, httpSession, (File) null);
			JasperExportManager.exportReportToPdfStream(new FileInputStream(destination), outputStream);
		} catch (Exception e) {
			log.error("fail generate pdf tmp for report " + name, e);
			throw new ApplicationException("fail generate pdf tmp for report " + name, e);
		} finally {
			if (destination != null)
				destination.delete();
		}
	}

	public void generatePdfPipeStream(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream) {
		try {
			PipedInputStream pipedInputStream = new PipedInputStream();
			PipedOutputStream pipedOutputStream = new PipedOutputStream(pipedInputStream);
			generateJasperPrintAsyn(name, parameters, httpSession, pipedOutputStream);

			JasperExportManager.exportReportToPdfStream(pipedInputStream, outputStream);
		} catch (Exception e) {
			log.error("fail generate pdf", e);
			throw new ApplicationException("fail generate pdf", e);
		}
	}

	public byte[] generateRtf(String name, Map<String, String> parameters, HttpSession httpSession) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		generateRtf(name, parameters, httpSession, outputStream);
		return outputStream.toByteArray();
	}

	public void generateRtf(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream) {
		try {
			JasperPrint jasperPrint = generateJasperPrint(name, parameters, httpSession);
			JRRtfExporter exporter = new JRRtfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exporter.exportReport();
		} catch (JRException e) {
			log.error("fail generate rtf", e);
			throw new ApplicationException("fail generate rtf", e);
		}
	}

	public byte[] generateXls(String name, Map<String, String> parameters, HttpSession httpSession) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		generateXls(name, parameters, httpSession, outputStream);
		return outputStream.toByteArray();
	}

	public void generateXls(final String name, final Map<String, String> parameters, final HttpSession httpSession,
			OutputStream outputStream) {

		try {
			JasperPrint jasperPrint = generateJasperPrint(name, parameters, httpSession);

			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET, 65500);
			exporter.exportReport();

		} catch (Exception e) {
			log.error("fail generate xls", e); // NOPMD
			throw new ApplicationException("fail generate xls", e); // NOPMD
		}
	}

	public void generateXlsTmpFile(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream) {
		File destination = null;
		try {
			destination = generateJasperPrint(name, parameters, httpSession, destination);

			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setParameter(JRExporterParameter.INPUT_FILE, destination);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET, 65500);
			exporter.exportReport();

		} catch (Exception e) {
			log.error("fail generate xls", e); // NOPMD
			throw new ApplicationException("fail generate xls", e); // NOPMD
		} finally {
			if (destination != null)
				destination.delete();
		}
	}

	public void generatePipeStream(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream) {
		try {
			PipedInputStream pipedInputStream = new PipedInputStream();
			PipedOutputStream pipedOutputStream = new PipedOutputStream(pipedInputStream);

			generateJasperPrintAsyn(name, parameters, httpSession, pipedOutputStream);

			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setParameter(JRExporterParameter.INPUT_STREAM, pipedInputStream);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET, 65500);
			exporter.exportReport();

		} catch (Exception e) {
			log.error("fail generate xls", e); // NOPMD
			throw new ApplicationException("fail generate xls", e); // NOPMD
		}
	}

	public byte[] generateCsv(String name, Map<String, String> parameters, HttpSession httpSession) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		generateCsv(name, parameters, httpSession, outputStream);

		return outputStream.toByteArray();
	}

	public void generateCsvTmpFile(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream) {
		File destination = null;
		try {
			destination = generateJasperPrint(name, parameters, httpSession, (File) null);

			JRCsvExporter exporter = new JRCsvExporter();
			exporter.setParameter(JRExporterParameter.INPUT_FILE, destination);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exporter.exportReport();
		} catch (Exception e) {
			log.error("fail generate csv", e);
			throw new ApplicationException("fail generate csv", e);
		} finally {
			if (destination != null)
				destination.delete();
		}
	}

	public void generateCsv(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream) {
		try {
			JasperPrint jasperPrint = generateJasperPrint(name, parameters, httpSession);

			JRCsvExporter exporter = new JRCsvExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exporter.exportReport();
		} catch (JRException e) {
			log.error("fail generate cvs", e);
			throw new ApplicationException("fail generate csv", e);
		}
	}

	public byte[] generateTxt(String name, Map<String, String> parameters, HttpSession httpSession) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		generateTxt(name, parameters, httpSession, outputStream);

		return outputStream.toByteArray();
	}

	public void generateTxtTmpFile(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream) {
		File destination = null;
		try {
			destination = generateJasperPrint(name, parameters, httpSession, (File) null);

			JRTextExporter exporter = new JRTextExporter();
			exporter.setParameter(JRExporterParameter.INPUT_FILE, destination);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			// field heigth is 12, if more then add one blank line
			exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, 12);
			// one field with width must be 0 if modulus by character width. the
			// length of the character each line if the lenght of paper widht
			// divide by char widht
			exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, 4);
			exporter.setParameter(JRTextExporterParameter.BETWEEN_PAGES_TEXT, "");
			exporter.exportReport();
		} catch (Exception e) {
			log.error("fail generate text", e);
			throw new ApplicationException("fail generate text", e);
		} finally {
			if (destination != null)
				destination.delete();
		}
	}

	public void generateTxt(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream) {
		try {
			JasperPrint jasperPrint = generateJasperPrint(name, parameters, httpSession);

			JRTextExporter exporter = new JRTextExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			// field heigth is 12, if more then add one blank line
			exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, 12);
			// one field with width must be 0 if modulus by character width. the
			// length of the character each line if the lenght of paper widht
			// divide by char widht
			exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, 4);
			exporter.setParameter(JRTextExporterParameter.BETWEEN_PAGES_TEXT, "");
			exporter.exportReport();
		} catch (JRException e) {
			log.error("fail generate cvs", e);
			throw new ApplicationException("fail generate text", e);
		}
	}

	public byte[] generateOdt(String name, Map<String, String> parameters, HttpSession httpSession) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		generateOdt(name, parameters, httpSession, outputStream);
		return outputStream.toByteArray();
	}

	public void generateOdt(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream) {
		try {
			JasperPrint jasperPrint = generateJasperPrint(name, parameters, httpSession);

			JROdtExporter exporter = new JROdtExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exporter.exportReport();

		} catch (JRException e) {
			log.error("fail generate xls", e);
			throw new ApplicationException("fail generate xls", e);
		}
	}

	public JasperPrint generateJasperPrint(ReportData reportData) {
		JasperReport jasperReport = getJasperReport(reportData);

		try {
			return JasperFillManager.fillReport(jasperReport, reportData.getParameters(), reportData.getJrDataSource());
		} catch (JRException e) {
			if (!containsErrorMessage(e, "Session was invalidated")) {
				// suppress log error
				log.error("fail generate jasper print " + reportData.getDesignFilePath(), e);
			}

			throw new ApplicationException("fail generate jasper print " + reportData.getDesignFilePath(), e);
		}
	}

	public void generatePdf(ReportData reportData, OutputStream outputStream) {
		try {
			JasperPrint jasperPrint = generateJasperPrint(reportData);
			JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
		} catch (Exception e) {
			if (containsErrorMessage(e, "Session was invalidated")) {
				// suppress error
				return;
			}

			log.error("fail generate pdf " + reportData.getDesignFilePath(), e);
			throw new ApplicationException("fail generate pdf " + reportData.getDesignFilePath(), e);
		}
	}

	public void generateXls(ReportData reportData, OutputStream outputStream) {
		try {
			JasperPrint jasperPrint = generateJasperPrint(reportData);

			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET, 65500);
			exporter.exportReport();
		} catch (Exception e) {
			log.error("fail generate pdf " + reportData.getDesignFilePath(), e);
			throw new ApplicationException("fail generate pdf " + reportData.getDesignFilePath(), e);
		}
	}

	public void generateHtmlTmpFile(String name, Map<String, String> parameters, String imagesDir, Object imagesUri,
			HttpSession session, OutputStream outputStream) {
		try {
			generateHtml(generateJasperPrint(name, parameters, session), outputStream, imagesDir, imagesUri, session);
		} catch (Exception e) {
			log.error("fail generate html " + name, e);
			throw new ApplicationException("fail generate html " + name, e);
		}
	}

	public void generateHtml(ReportData reportData, OutputStream outputStream, String imagesDir, Object imagesUri,
			HttpSession session) {
		try {
			JasperPrint jasperPrint = generateJasperPrint(reportData);
			jasperPrint.setProperty("directPrint", (String) reportData.getParameters().get("directPrint"));

			generateHtml(jasperPrint, outputStream, imagesDir, imagesUri, session);
		} catch (Exception e) {
			log.error("fail generate html " + reportData.getDesignFilePath(), e);
			throw new ApplicationException("fail generate html " + reportData.getDesignFilePath(), e);
		}
	}

	public void generateHtml(JasperPrint jasperPrint, OutputStream outputStream, String imagesDir, Object imagesUri,
			HttpSession session) {

		String directPrint = jasperPrint.getProperty("directPrint");

		try {
			JRHtmlExporter exporter = new JRHtmlExporter();

			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);

			exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR_NAME, imagesDir);
			exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, imagesUri);
			exporter.setParameter(JRHtmlExporterParameter.CHARACTER_ENCODING, "UTF-8");

			if ("true".equals(directPrint)) {
				exporter.setParameter(
						JRHtmlExporterParameter.HTML_HEADER,
						"<html>\n"
								+ "<head>\n"
								+ " <title></title>\n"
								+ " <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n"
								+ " <style type=\"text/css\">\n"
								+ " a {text-decoration: none}\n"
								+ " .no-select {\n"
								+ "     user-select: none;\n"
								+ "     -moz-user-select: none;\n"
								+ "     -o-user-select: none;\n"
								+ "     -khtml-user-select: none;\n"
								+ "     -webkit-user-select: none;\n"
								+ "     cursor: default;\n"
								+ " }\n"
								+ " </style>\n"
								+ " <script type=\"text/javascript\">\n"
								+ " function makeUnselectable(node) {\n"
								+ "     if (node.nodeType == 1) {\n"
								+ "         node.unselectable = true;\n"
								+ "     }\n"
								+ "     var child = node.firstChild;\n"
								+ "     while (child) {\n"
								+ "         makeUnselectable(child);\n"
								+ "         child = child.nextSibling;\n"
								+ "     }\n"
								+ " }\n"
								+ " function bodyOnLoad() {\n"
								+ "   makeUnselectable(document.getElementById(\"tbl01\"));\n"
								+ "   if (window && window.print) {\n"
								+ "     window.print();\n"
								+ "   }\n"
								+ " }\n"
								+ " </script>\n"
								+ "</head>\n"
								+ "<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\" onload=\"bodyOnLoad()\">\n"
								+ "<table id=\"tbl01\" class=\"no-select\" onselectstart=\"return false;\" ondragstart=\"return false;\" onmousedown=\"return false;\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
								+ "<tr><td align=\"left\">\n" + "\n");
				exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "</td></tr>\n" + "</table>\n" + "</body>\n"
						+ "</html>\n");
			} else {
				exporter.setParameter(
						JRHtmlExporterParameter.HTML_HEADER,
						"<html>\n"
								+ "<head>\n"
								+ " <title></title>\n"
								+ " <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n"
								+ " <style type=\"text/css\">\n"
								+ " a {text-decoration: none}\n"
								+ " .no-select {\n"
								+ "     user-select: none;\n"
								+ "     -moz-user-select: none;\n"
								+ "     -o-user-select: none;\n"
								+ "     -khtml-user-select: none;\n"
								+ "     -webkit-user-select: none;\n"
								+ "     cursor: default;\n"
								+ " }\n"
								+ " </style>\n"
								+ " <script type=\"text/javascript\">\n"
								+ " function makeUnselectable(node) {\n"
								+ "     if (node.nodeType == 1) {\n"
								+ "         node.unselectable = true;\n"
								+ "     }\n"
								+ "     var child = node.firstChild;\n"
								+ "     while (child) {\n"
								+ "         makeUnselectable(child);\n"
								+ "         child = child.nextSibling;\n"
								+ "     }\n"
								+ " }\n"
								+ " function bodyOnLoad() {\n"
								+ "   makeUnselectable(document.getElementById(\"tbl01\"));\n"
								+ " }\n"
								+ " </script>\n"
								+ "</head>\n"
								+ "<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\" onload=\"bodyOnLoad()\">\n"
								+ "<table id=\"tbl01\" class=\"no-select\" onselectstart=\"return false;\" ondragstart=\"return false;\" onmousedown=\"return false;\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
								+ "<tr><td align=\"left\">\n" + "\n");
				exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "</td></tr>\n" + "</table>\n" + "</body>\n"
						+ "</html>\n");
			}

			session.setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);

			exporter.exportReport();
		} catch (Exception e) {
			if (containsErrorMessage(e, "Session was invalidated")) {
				// suppress error
				return;
			}

			log.error("fail generate html " + jasperPrint.getName(), e);
			throw new ApplicationException("fail generate html " + jasperPrint.getName(), e);
		}
	}

	public void generateSknTmpFile(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream) {
		try {
			parameters.put("type", "skn");
			generateFilePrint(name, parameters, httpSession, outputStream);
		} catch (Exception e) {
			log.error("fail generate text", e);
			throw new ApplicationException("fail generate text", e);
		}
	}

	public void generateRtgsTmpFile(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream) {
		try {
			parameters.put("type", "rtgs");
			generateFilePrint(name, parameters, httpSession, outputStream);
		} catch (Exception e) {
			log.error("fail generate text", e);
			throw new ApplicationException("fail generate text", e);
		}
	}

	public void generateFilePrint(String name, Map<String, String> parameters, HttpSession httpSession,
			OutputStream outputStream) {
		Report report = getReport(name);

		if (report == null)
			throw new ApplicationException("report " + name + " not found");

		try {
			report.getReportData(parameters, httpSession, outputStream);
		} catch (Exception ex) {
			log.error("Error when get report Data" + ex.getMessage());
		}
	}

	public File generateFilePrint(String name, Map<String, String> parameters, HttpSession httpSession, File destination)
		throws IOException {
		DateFormat concatDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		if (destination == null) {
			String basePath = systemParameterManager.getStringConfiguration(CONTEXT_PATH_REAL_DIR, "");
			StringBuilder fileName = new StringBuilder().append(basePath).append(File.separator).append("WEB-INF")
					.append(File.separator).append("report").append(Constants.FILE_SEP).append("tmp")
					.append(Constants.FILE_SEP).append(name).append("_").append(concatDateFormat.format(new Date()))
					.append(".print");
			destination = new File(fileName.toString());
		}

		if (!destination.getParentFile().exists())
			destination.getParentFile().mkdirs();

		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(destination);
			generateFilePrint(name, parameters, httpSession, outputStream);
		} finally {
			if (outputStream != null)
				outputStream.close();
		}

		return destination;
	}

	// setter
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setReportMap(Map<String, Report> reportMap) {
		this.reportMap = reportMap;
	}

	public void setTaskExecutorWrapper(TaskExecutorWrapper taskExecutorWrapper) {
		this.taskExecutorWrapper = taskExecutorWrapper;
	}

	public void setSystemParameterManager(SystemParameterManager systemParameterManager) {
		this.systemParameterManager = systemParameterManager;
	}

	public void generateTxtToSpesificPath(String name, Map<String, String> parameters, HttpSession httpSession,
			String fileName) {
		/*
		 * try {
		 * Map<String, Object> pMap = (Map<String, Object>) httpSession
		 * .getAttribute(ReportTopUpPulsa.HTTP_SESSION_NAME);
		 * String path = (String) pMap.get("path");
		 * String reportFolder = (String) pMap.get("reportFolder");
		 * String[] out = (String[]) pMap.get("outfileName");
		 * String[] id = (String[]) pMap.get("uploadFileName");
		 * List<String> files = new ArrayList<String>();
		 * for (int i = 0; i < out.length; i++) {
		 * if (id != null)
		 * pMap.put("uploadFileName", id[i]);
		 * files.add(path + out[i] + ".txt");
		 * 
		 * JasperPrint jasperPrint = generateJasperPrint(name, parameters,
		 * httpSession);
		 * 
		 * JRCsvExporter exporter = new JRCsvExporter();
		 * exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, "\\|");
		 * exporter.setParameter(JRCsvExporterParameter.RECORD_DELIMITER,
		 * "\r\n");
		 * exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		 * exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, path +
		 * out[i] + ".txt");
		 * exporter.exportReport();
		 * 
		 * }
		 * 
		 * try {
		 * zipManager.zipFiles(files, path, fileName + ".zip");
		 * } catch (IOException e) {
		 * log.error("fail generate zip");
		 * e.printStackTrace();
		 * }
		 * 
		 * } catch (JRException e) {
		 * log.error("fail generate txt", e);
		 * throw new ApplicationException("fail generate txt", e);
		 * }
		 */
	}

	public ZipManager getZipManager() {
		return zipManager;
	}

	public void setZipManager(ZipManager zipManager) {
		this.zipManager = zipManager;
	}
}
