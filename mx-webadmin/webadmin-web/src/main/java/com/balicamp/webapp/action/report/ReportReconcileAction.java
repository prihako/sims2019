package com.balicamp.webapp.action.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import com.balicamp.model.mx.ReconcileDto;
import com.balicamp.util.DateUtil;

public class ReportReconcileAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4068137339926852221L;

	public String createReport(Map<String, String> parameters,
			List<ReconcileDto> list, String fileType, ServletContext context,
			String tempOutputPath) throws JRException, IOException,
			FileNotFoundException {

		DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
		Calendar cal = Calendar.getInstance();
		String date = parameters.get("trxDate").toString().replace("-", "_");

		String fullPath = null;
		File dirFile = null;
		String zipName = null;
		String bankName = parameters.get("bankName");

		if (fileType.equalsIgnoreCase("XLS")) {
			String printFileName = null;

			fullPath = initFullPath(tempOutputPath, "");
			dirFile = new File(fullPath);
			System.out
					.println("Full path of transaction logs report in excel format "
							+ fullPath);
			// dirFile.mkdir();

			// JRXlsExporter exporter = new JRXlsExporter();
			JExcelApiExporter exporter = new JExcelApiExporter();
			JRBeanCollectionDataSource beanColDataSource2 = new JRBeanCollectionDataSource(
					list);

			printFileName = context
					.getRealPath("/WEB-INF/report/jasper/reconcileReportXLS.jasper");

			printFileName = JasperFillManager.fillReportToFile(printFileName,
					parameters, beanColDataSource2);

			exporter.setParameter(JRExporterParameter.INPUT_FILE_NAME,
					printFileName);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
					fullPath + "/" + "report_reconcile_" + date + "_"
							+ bankName + ".xls");
			// exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
			// Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
					Boolean.TRUE);
			System.out
					.println("PROPERTY_DETECT_CELL_TYPE : "
							+ exporter
									.getParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE));
			// exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN,
			// Boolean.TRUE);
			exporter.setParameter(
					JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, true);
			System.out
					.println("PROPERTY_DETECT_CELL_TYPE : "
							+ exporter
									.getParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED));
			exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS,
					false);
			System.out
					.println("PROPERTY_DETECT_CELL_TYPE : "
							+ exporter
									.getParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS));

			exporter.exportReport();
			// zipName = date + "_excel_report.zip";
			zipName = "report_reconcile_" + date + "_" + bankName + ".xls";
		} else if (fileType.equalsIgnoreCase("PDF")) {
			fullPath = initFullPath(tempOutputPath, "");
			dirFile = new File(fullPath);
			System.out
					.println("Full path of transaction logs report in pdf format "
							+ fullPath);
			// dirFile.mkdir();

			InputStream inputStream = context
					.getResourceAsStream("/WEB-INF/report/jrxml/reconcileReport.jrxml");
			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(
					list);
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);
			// jasperReport.setWhenNoDataType(jasperReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, beanColDataSource);
			// JasperExportManager.exportReportToPdfStream(jasperPrint, new
			// FileOutputStream(new File(fullPath + "/"
			// + date)
			// + ".pdf"));
			JasperExportManager.exportReportToPdfFile(jasperPrint, fullPath
					+ "/report_reconcile_" + date + "_" + bankName + ".pdf");
			// zipName = date + "_pdf_report.zip";
			zipName = "report_reconcile_" + date + "_" + bankName + ".pdf";
		}
		// EasyZip.zip(fullPath, tempOutputPath + "/" + zipName);
		return zipName;
	}

	public String createReportWithoutContext(Map<String, String> parameters,
			Map<String, String> finalParameters, List<ReconcileDto> list,
			List<ReconcileDto> reconciledList, String jasperPath,
			String jrxmlPath, String tempOutputPath, String channelCode,
			String fileType) throws JRException, IOException,
			FileNotFoundException {

		DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
		Calendar cal = Calendar.getInstance();
		String date = dateFormat.format(cal.getTime());

		String fullPath = null;
		File dirFile = null;
		String reportName = null;

		String printFileNameBeforeReconcile = null;
		String printFileNameAfterReconcile = null;

		fullPath = initFullPath(tempOutputPath, date);
		dirFile = new File(fullPath);

		if (!dirFile.mkdir()) {
			dirFile.mkdirs();
		}

		if (fileType.equalsIgnoreCase("XLS")) {
			// Generate Excel File

			JExcelApiExporter exporterXLS1 = new JExcelApiExporter();
			JExcelApiExporter exporterXLS2 = new JExcelApiExporter();

			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(
					list);
			JRBeanCollectionDataSource beanColDataSourceReconciled = new JRBeanCollectionDataSource(
					reconciledList);

			// printFileName =
			// context.getRealPath("/WEB-INF/report/jasper/reconcileReportXLS.jasper");
			printFileNameBeforeReconcile = new File(jasperPath)
					.getCanonicalPath();
			printFileNameAfterReconcile = new File(jasperPath)
					.getCanonicalPath();

			printFileNameBeforeReconcile = JasperFillManager
					.fillReportToFile(printFileNameBeforeReconcile, parameters,
							beanColDataSource);
			printFileNameAfterReconcile = JasperFillManager.fillReportToFile(
					printFileNameAfterReconcile, finalParameters,
					beanColDataSourceReconciled);

			// exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
			// Boolean.TRUE);
			// exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN,
			// Boolean.TRUE);
			System.out.println("Generating XLS Original");
			exporterXLS1.setParameter(JRExporterParameter.INPUT_FILE_NAME,
					printFileNameBeforeReconcile);
			exporterXLS1.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
					fullPath + "/report_reconcile_" + date + "_original_"
							+ channelCode + ".xls");
			exporterXLS1.setParameter(
					JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			System.out
					.println("PROPERTY_DETECT_CELL_TYPE : "
							+ exporterXLS1
									.getParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE));
			exporterXLS1.setParameter(
					JRXlsExporterParameter.IS_IGNORE_GRAPHICS, false);
			System.out
					.println("PROPERTY_IGNORE_GRAPHICS : "
							+ exporterXLS1
									.getParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS));
			exporterXLS1.setParameter(
					JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, true);
			System.out
					.println("PROPERTY_FONT_SIZE_FIX_ENABLED : "
							+ exporterXLS1
									.getParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED));
			exporterXLS1.exportReport();

			System.out.println("Generating XLS Reconciled");
			exporterXLS2.setParameter(JRExporterParameter.INPUT_FILE_NAME,
					printFileNameAfterReconcile);
			exporterXLS2.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
					fullPath + "/report_reconcile_" + date + "_reconciled_"
							+ channelCode + ".xls");
			exporterXLS2.setParameter(
					JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			System.out
					.println("PROPERTY_DETECT_CELL_TYPE : "
							+ exporterXLS2
									.getParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE));
			exporterXLS2.setParameter(
					JRXlsExporterParameter.IS_IGNORE_GRAPHICS, false);
			System.out
					.println("PROPERTY_IGNORE_GRAPHICS : "
							+ exporterXLS2
									.getParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS));
			exporterXLS2.setParameter(
					JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, true);
			System.out
					.println("PROPERTY_FONT_SIZE_FIX_ENABLED : "
							+ exporterXLS2
									.getParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED));
			exporterXLS2.exportReport();

			System.out
					.println("Full path of transaction logs report in excel format "
							+ fullPath + ": SUCCESS!");

		} else if (fileType.equalsIgnoreCase("PDF")) {
			// Generate PDF
			InputStream inputStream = new FileInputStream(jrxmlPath);

			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(
					list);
			JRBeanCollectionDataSource beanColDataSourceReconciled = new JRBeanCollectionDataSource(
					reconciledList);
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);
			// jasperReport.setWhenNoDataType(jasperReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL);
			JasperPrint jasperPrintBeforeReconcile = JasperFillManager
					.fillReport(jasperReport, parameters, beanColDataSource);
			jasperPrintBeforeReconcile.setPageHeight(595);
			jasperPrintBeforeReconcile.setPageWidth(842);
			JasperExportManager.exportReportToPdfFile(
					jasperPrintBeforeReconcile, fullPath + "/report_reconcile_"
							+ date + "_original_" + channelCode + ".pdf");

			JasperPrint jasperPrintAfterReconcile = JasperFillManager
					.fillReport(jasperReport, finalParameters,
							beanColDataSourceReconciled);
			jasperPrintAfterReconcile.setPageHeight(595);
			jasperPrintAfterReconcile.setPageWidth(842);
			JasperExportManager.exportReportToPdfFile(
					jasperPrintAfterReconcile, fullPath + "/report_reconcile_"
							+ date + "_reconciled_" + channelCode + ".pdf");

			System.out
					.println("Full path of transaction logs report in pdf format "
							+ fullPath + ": SUCCESS!");
		}
		reportName = "report_reconcile_" + date;
		return reportName;
	}

	public String createReportWithoutContextByDataType(
			Map<String, String> parameters, List<ReconcileDto> list,
			String jasperPath, String jrxmlPath, String tempOutputPath,
			String channelCode, String fileType, String dataType)
			throws JRException, IOException, FileNotFoundException {

		DateFormat dateFormat 	= new SimpleDateFormat("dd_MM_yyyy");
		Calendar cal 			= Calendar.getInstance();
		String date 			= dateFormat.format(cal.getTime());

		String fullPath 	= null;
		File dirFile 		= null;
		String reportName 	= null;

		String printFileName 	= null;
		String jasperFileName 	= null;

		fullPath 	= initFullPath(tempOutputPath, date);
		dirFile 	= new File(fullPath);

		if (!dirFile.mkdir()) {
			dirFile.mkdirs();
		}

		if (fileType.equalsIgnoreCase("XLS")) {
			// Generate Excel File
			JExcelApiExporter exporterXLS1 = new JExcelApiExporter();
			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);

			jasperFileName = new File(jasperPath).getCanonicalPath();
			printFileName = JasperFillManager.fillReportToFile(jasperFileName, parameters, beanColDataSource);

			exporterXLS1.setParameter(JRExporterParameter.INPUT_FILE_NAME,printFileName);
			exporterXLS1.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,fullPath + "/report_reconcile_" + date + "_" + dataType + "_" + channelCode + ".xls");
			exporterXLS1.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exporterXLS1.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, false);
			exporterXLS1.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, true);
			exporterXLS1.exportReport();

			System.out.println("Full path of transaction logs report in excel format " + fullPath + ": SUCCESS!");
		} else if (fileType.equalsIgnoreCase("PDF")) {
			// Generate PDF
			InputStream inputStream = new FileInputStream(jrxmlPath);
			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);
			
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
			jasperPrint.setPageWidth(842);
			jasperPrint.setPageHeight(595);
			JasperExportManager.exportReportToPdfFile(jasperPrint, fullPath + "/report_reconcile_" + date + "_" + dataType + "_" + channelCode + ".pdf");

			System.out.println("Full path of transaction logs report in pdf format " + fullPath + ": SUCCESS!");
		}
		reportName = "report_reconcile_" + date;
		return reportName;
	}

	public String createReportWithoutContextByDataTypeWeekly(
			Map<String, String> parameters, List<ReconcileDto> list,
			String jasperPath, String jrxmlPath, String tempOutputPath,
			String channelCode, String fileType, String dataType)
			throws JRException, IOException, FileNotFoundException {

		DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(new Date());
		calStart.add(Calendar.DATE, -7);
		String dateStart = dateFormat.format(calStart.getTime());

		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(new Date());
		calEnd.add(Calendar.DATE, -1);
		String dateEnd = dateFormat.format(calEnd.getTime());

		String fullPath = null;
		File dirFile = null;
		String reportName = null;

		String printFileName = null;
		String jasperFileName = null;

		fullPath = initFullPath(tempOutputPath, dateStart + "_" + dateEnd);
		dirFile = new File(fullPath);

		if (!dirFile.mkdir()) {
			dirFile.mkdirs();
		}

		if (fileType.equalsIgnoreCase("XLS")) {
			// Generate Excel File

			JExcelApiExporter exporterXLS1 = new JExcelApiExporter();

			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(
					list);

			// printFileName =
			// context.getRealPath("/WEB-INF/report/jasper/reconcileReportXLS.jasper");
			jasperFileName = new File(jasperPath).getCanonicalPath();

			printFileName = JasperFillManager.fillReportToFile(jasperFileName,
					parameters, beanColDataSource);

			// exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
			// Boolean.TRUE);
			// exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN,
			// Boolean.TRUE);
			exporterXLS1.setParameter(JRExporterParameter.INPUT_FILE_NAME,
					printFileName);
			exporterXLS1.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
					fullPath + "/report_reconcile_" + dateStart + "_" + dateEnd
							+ "_" + dataType + "_" + channelCode + ".xls");
			exporterXLS1.setParameter(
					JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exporterXLS1.setParameter(
					JRXlsExporterParameter.IS_IGNORE_GRAPHICS, false);
			exporterXLS1.setParameter(
					JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, true);

			exporterXLS1.exportReport();

			System.out
					.println("Full path of transaction logs report in excel format "
							+ fullPath + ": SUCCESS!");

		} else if (fileType.equalsIgnoreCase("PDF")) {
			// Generate PDF
			InputStream inputStream = new FileInputStream(jrxmlPath);

			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(
					list);

			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);
			// jasperReport.setWhenNoDataType(jasperReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, beanColDataSource);
			jasperPrint.setPageHeight(595);
			jasperPrint.setPageWidth(842);
			JasperExportManager.exportReportToPdfFile(jasperPrint, fullPath
					+ "/report_reconcile_" + dateStart + "_" + dateEnd + "_"
					+ dataType + "_" + channelCode + ".pdf");

			System.out
					.println("Full path of transaction logs report in pdf format "
							+ fullPath + ": SUCCESS!");
		}
		reportName = "report_reconcile_" + dateStart + "_" + dateEnd;
		return reportName;
	}

	public String createReportWithoutContextByDataTypeMonthly(
			Map<String, String> parameters, List<ReconcileDto> list,
			String jasperPath, String jrxmlPath, String tempOutputPath,
			String channelCode, String fileType, String dataType)
			throws JRException, IOException, FileNotFoundException {

		DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtil.substractMonth(new Date(), 1));
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		String dateStart = dateFormat.format(cal.getTime());
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		String dateEnd = dateFormat.format(cal.getTime());

		String fullPath = null;
		File dirFile = null;
		String reportName = null;

		String printFileName = null;
		String jasperFileName = null;

		fullPath = initFullPath(tempOutputPath, dateStart + "_" + dateEnd);
		dirFile = new File(fullPath);

		if (!dirFile.mkdir()) {
			dirFile.mkdirs();
		}

		if (fileType.equalsIgnoreCase("XLS")) {
			// Generate Excel File

			JExcelApiExporter exporterXLS1 = new JExcelApiExporter();

			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(
					list);

			// printFileName =
			// context.getRealPath("/WEB-INF/report/jasper/reconcileReportXLS.jasper");
			jasperFileName = new File(jasperPath).getCanonicalPath();

			printFileName = JasperFillManager.fillReportToFile(jasperFileName,
					parameters, beanColDataSource);

			// exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
			// Boolean.TRUE);
			// exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN,
			// Boolean.TRUE);
			exporterXLS1.setParameter(JRExporterParameter.INPUT_FILE_NAME,
					printFileName);
			exporterXLS1.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
					fullPath + "/report_reconcile_" + dateStart + "_" + dateEnd
							+ "_" + dataType + "_" + channelCode + ".xls");
			exporterXLS1.setParameter(
					JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exporterXLS1.setParameter(
					JRXlsExporterParameter.IS_IGNORE_GRAPHICS, false);
			exporterXLS1.setParameter(
					JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, true);

			exporterXLS1.exportReport();

			System.out
					.println("Full path of transaction logs report in excel format "
							+ fullPath + ": SUCCESS!");

		} else if (fileType.equalsIgnoreCase("PDF")) {
			// Generate PDF
			InputStream inputStream = new FileInputStream(jrxmlPath);

			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(
					list);

			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);
			// jasperReport.setWhenNoDataType(jasperReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, beanColDataSource);
			jasperPrint.setPageHeight(595);
			jasperPrint.setPageWidth(842);
			JasperExportManager.exportReportToPdfFile(jasperPrint, fullPath
					+ "/report_reconcile_" + dateStart + "_" + dateEnd + "_"
					+ dataType + "_" + channelCode + ".pdf");

			System.out
					.println("Full path of transaction logs report in pdf format "
							+ fullPath + ": SUCCESS!");
		}
		reportName = "report_reconcile_" + dateStart + "_" + dateEnd;
		return reportName;
	}

	private static String initFullPath(String path, String fName) {

		System.out.println("*=*=*=*=path  : " +  path);
		System.out.println("*=*=*=*=fName : " +  fName);
		
		File dir = new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		File tmpReport = new File(path + "/" + "test.txt");
		try {
			if (!tmpReport.createNewFile()) {
				throw new RuntimeException("Tidak bisa menulis pada directory "
						+ path);
			} else {
				tmpReport.delete();
				String fullPath = path + "/" + fName;
				return fullPath;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
