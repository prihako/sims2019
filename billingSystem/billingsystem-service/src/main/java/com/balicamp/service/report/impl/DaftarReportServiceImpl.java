package com.balicamp.service.report.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.dao.mx.TransactionLogDao;
import com.balicamp.dao.parameter.SystemParameterDao;
import com.balicamp.dao.report.ReportDao;
import com.balicamp.model.report.ReportColumModel;
import com.balicamp.model.report.ReportModel;
import com.balicamp.report.ColumParamGenerator;
import com.balicamp.report.OldReportHeader;
import com.balicamp.report.ReportContent;
import com.balicamp.report.ReportFormater;
import com.balicamp.report.ReportGenerator;
import com.balicamp.report.exception.RawColumnFormatException;
import com.balicamp.report.exception.RawDataFormatException;
import com.balicamp.report.param.ColumnParam;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.report.DaftarReportService;

@Service("DaftarReportServiceImpl")
public class DaftarReportServiceImpl extends AbstractManager implements DaftarReportService {

	@Autowired
	private SystemParameterDao systemParameterDao;

	@Autowired
	private ReportDao reportDao;

	@Autowired
	private TransactionLogDao transactionLogDao;

	@Autowired
	private ReportFormater reportFormater;

	@Autowired
	private ColumParamGenerator columnGenerator;

	@Autowired
	private ReportGenerator reportGenerator;

	@Override
	public Object getDefaultDao() {
		return reportDao;
	}

	public DaftarReportServiceImpl() {
	}

	private String reportPath = null;
	private SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_mm_ss");

	private OldReportHeader handleReport(long idReport, SearchCriteria criteria, String tipe, boolean isFixedLength) {
		OldReportHeader header = new OldReportHeader();

		header.setReportTipe(tipe.equals(OldReportHeader.REPORT_TYPE_CSV) ? OldReportHeader.REPORT_TYPE_CSV
				: OldReportHeader.REPORT_TYPE_TXT);

		header.setParamStrMap(criteria.getParamMapStr());
		header.setGenerate(false);

		ReportModel report = reportDao.findById(idReport);
		List<ReportColumModel> columns = report.getColums();

		header.setReportName(report.getNamaReport());
		header.setKodeReport(report.getKodeReport());

		if (null == criteria) {
			criteria = new SearchCriteria();
		}

		try {
			List<String> datas = transactionLogDao.findRawData(criteria);

			String extension = (tipe.equals(OldReportHeader.REPORT_TYPE_TXT) ? ".txt" : ".csv");
			String fileName = report.getNamaReport() + "_" + formater.format(new Date()) + extension;
			fileName = fileName.replaceAll(" ", "_");
			header.setFileName(fileName);

			/* generate txt report yang sudah di generate */
			List<ColumnParam> columParam = columnGenerator.generateColumParam(columns);
			ReportContent pojos = reportFormater.formatSparatedValue(datas, columParam, isFixedLength);

			StringWriter writer = null;
			if (tipe.equals(OldReportHeader.REPORT_TYPE_CSV)) {
				writer = reportGenerator.generateCSV(pojos);
			} else if (tipe.equals(OldReportHeader.REPORT_TYPE_TXT)) {
				writer = reportGenerator.generateTXT(pojos);
			}

			File file = new File(getReportPath() + "/" + fileName);
			file.createNewFile();
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(writer.toString());
			fileWriter.close();
		} catch (RawColumnFormatException e) {
			e.printStackTrace();
			throw new RuntimeException();
		} catch (RawDataFormatException e) {
			e.printStackTrace();
			throw new RuntimeException();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		header.setGenerate(true);
		return header;
	}

	public OldReportHeader handleReport(long idReport, SearchCriteria criteria, String tipe) {
		return handleReport(idReport, criteria, tipe, true);
	}

	@Override
	public OldReportHeader handleReportTXT(long idReport, SearchCriteria criteria, boolean isFixedLength) {
		return handleReport(idReport, criteria, OldReportHeader.REPORT_TYPE_TXT, isFixedLength);
	}

	@Override
	public OldReportHeader handleReportCSV(long idReport, SearchCriteria criteria, boolean isFixedLength) {
		return handleReport(idReport, criteria, OldReportHeader.REPORT_TYPE_CSV, isFixedLength);
	}

	private String getReportPath() {
		if (null == reportPath) {
			reportPath = systemParameterDao.findParamValueByParamName("mayora.download.temporary.path");
		}

		return reportPath;
	}

	@Override
	public List<ReportModel> findGridData(SearchCriteria kriteria, int first, int max) {
		return reportDao.findByCriteria(kriteria, first, max);
	}

}
