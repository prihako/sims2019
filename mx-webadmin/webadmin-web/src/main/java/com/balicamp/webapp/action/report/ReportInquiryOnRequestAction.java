package com.balicamp.webapp.action.report;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintPage;
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

import com.balicamp.model.mx.InquiryReconcileDto;

public class ReportInquiryOnRequestAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4068137339926852221L;

	public String createReport(Map<String, String> parameters, List<InquiryReconcileDto> list, String fileType,
			ServletContext context, String tempOutputPath) throws JRException, IOException, FileNotFoundException {

		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		Calendar cal = Calendar.getInstance();
		String date = dateFormat.format(cal.getTime());

		// String fullPath = null;
		// File dirFile = null;
		String fileName = null;

		if (fileType.equalsIgnoreCase("XLS")) {
			String printFileName = null;
			// fullPath = initFullPath(tempOutputPath, date +
			// "payment_status_excel_report");
			// dirFile = new File(fullPath);
			// System.out.println("Full path of transaction logs report in excel format "
			// + fullPath);
			// dirFile.mkdir();

			JExcelApiExporter exporter = new JExcelApiExporter();
			JRBeanCollectionDataSource beanColDataSource2 = new JRBeanCollectionDataSource(list);

			printFileName = context.getRealPath("/WEB-INF/report/jasper/inquiryOnRequestReport.jasper");

			printFileName = JasperFillManager.fillReportToFile(printFileName, parameters, beanColDataSource2);

			exporter.setParameter(JRExporterParameter.INPUT_FILE_NAME, printFileName);
			// exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
			// fullPath + "/" + "payment_status_" + date
			// + ".xls");
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, tempOutputPath + "/" + "payment_status_" + date
					+ ".xls");

			exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);

			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);

			exporter.exportReport();
			fileName = "payment_status_" + date + ".xls";
		} else if (fileType.equalsIgnoreCase("PDF")) {
			// fullPath = initFullPath(tempOutputPath, date + "_pdf_report");
			// dirFile = new File(fullPath);
			// System.out.println("Full path of transaction logs report in pdf format "
			// + fullPath);
			// dirFile.mkdir();

			InputStream inputStream = context.getResourceAsStream("/WEB-INF/report/jrxml/inquiryOnRequestReport.jrxml");

			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			jasperReport.setWhenNoDataType(jasperReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
			removeBlankPage(jasperPrint.getPages());

			// JasperExportManager.exportReportToPdfFile(jasperPrint, fullPath +
			// "/" + "payment_status_" + date + ".pdf");
			JasperExportManager.exportReportToPdfFile(jasperPrint, tempOutputPath + "/" + "payment_status_" + date
					+ ".pdf");
			fileName = "payment_status_" + date + ".pdf";
		}
		// EasyZip.zip(fullPath, tempOutputPath + "/" + fileName);
		return fileName;
	}

	private void removeBlankPage(List<JRPrintPage> pages) {

		for (Iterator<JRPrintPage> i = pages.iterator(); i.hasNext();) {
			JRPrintPage page = i.next();
			if (page.getElements().size() <= 15)
				i.remove();
		}
	}

	// private static String initFullPath(String path, String fName) {
	//
	// File tmpReport = new File(path + "/" + "test.txt");
	// try {
	// if (!tmpReport.createNewFile()) {
	// throw new RuntimeException("Tidak bisa menulis pada directory " + path);
	// } else {
	// tmpReport.delete();
	// String fullPath = path + "/" + fName;
	// return fullPath;
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// return null;
	// }

}
