package com.balicamp.service.report.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.mx.TransactionLogDao;
import com.balicamp.dao.mx.TransactionLogHousekeepingDao;
import com.balicamp.dao.parameter.SystemParameterDao;
import com.balicamp.dao.report.ReportDao;
import com.balicamp.model.report.ReportColumModel;
import com.balicamp.model.report.ReportModel;
import com.balicamp.report.ColumParamGenerator;
import com.balicamp.report.ReportContent;
import com.balicamp.report.ReportFormater;
import com.balicamp.report.ReportGenerator;
import com.balicamp.report.ReportHeader;
import com.balicamp.report.ReportParam;
import com.balicamp.report.ReportParamHeader;
import com.balicamp.report.ReportResult;
import com.balicamp.report.exception.RawColumnFormatException;
import com.balicamp.report.exception.RawDataFormatException;
import com.balicamp.report.param.ColumnParam;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.report.CustomProcess;
import com.balicamp.service.report.IReportService;
import com.balicamp.util.DateConverter;
import com.balicamp.util.DateUtil;
import com.balicamp.util.wrapper.ReportCriteria;
import com.balicamp.util.zip.EasyZip;

@Service("GenericReportService")
public class GenericReportService extends AbstractManager implements IReportService {

	@Autowired
	private SystemParameterDao systemParameterDao;

	@Autowired
	private ReportDao reportDao;

	@Autowired
	private TransactionLogDao transactionLogDao;

	@Autowired
	private TransactionLogHousekeepingDao transactionLogHousekeepingDao;

	@Autowired
	private ReportFormater reportFormater;

	@Autowired
	private ColumParamGenerator columnGenerator;

	@Autowired
	private ReportGenerator reportGenerator;

	@Autowired
	private HashMap<String, CustomProcess> customProcess;

	@Override
	public Object getDefaultDao() {
		return reportDao;
	}

	public GenericReportService() {
	}

	private String reportPath = null;

	@Override
	public ReportResult generateReport(List<ReportParam> reportParam) {
		String filePosfix = DateConverter.dateToString(new Date(), "dd_MM_yyyy_hh_mm_ss");
		String dirPath = getReportPath() + "/" + filePosfix;
		File dirFile = new File(dirPath);
		dirFile.mkdir();

		// write semua report yang ada di parameter List reportParam ke file
		ReportResult result = writeReport(reportParam, dirPath, filePosfix);

		if (result.isGenerate()) {

			try {
				String fileName = filePosfix + ".zip";
				EasyZip.zip(dirPath, getReportPath() + "/" + fileName);
				result.setFileName(fileName);

			} catch (IOException e) {
				result.setGenerate(false);
			}
		}

		return result;
	}

	private ReportResult writeReport(List<ReportParam> reportParam, String dirPath, String filePosfix) {
		ReportResult result = new ReportResult();
		result.setGenerate(false);

		if (null != reportParam && !reportParam.isEmpty()) {

			for (ReportParam rp : reportParam) {

				ReportModel model = reportDao.findById(rp.getReportId());

				try {

					// generate colums, tambahkan colum generic dulu kalau ada,
					// setelah itu baru tambahkan colum di spesifik nya
					List<ReportColumModel> columModel = null;

					Long parentGeneric = model.getParentGeneric();

					int lastIndex = 0;

					if (null != parentGeneric) {
						ReportModel generic = reportDao.findById(parentGeneric);
						List<ReportColumModel> tmpcolMod = generic.getColums();
						// di iterasi manual agar tetap menjaga urutan
						// field-field yang akan ditampilkan
						if (null != tmpcolMod && !tmpcolMod.isEmpty()) {
							int totalCol = tmpcolMod.size()
									+ (null == model.getColums() ? 0 : model.getColums().size());
							columModel = new ArrayList<ReportColumModel>(totalCol);

							for (int i = 0; i < tmpcolMod.size(); i++) {
								columModel.add(i, tmpcolMod.get(i));
							}
							lastIndex = lastIndex + tmpcolMod.size();
						}

					}

					List<ReportColumModel> spesificColum = model.getColums();
					if (null != spesificColum && !spesificColum.isEmpty()) {

						if (null == columModel) {
							int totalCol = (null == model.getColums() ? 0 : model.getColums().size());
							columModel = new ArrayList<ReportColumModel>(totalCol);
						}

						for (int i = 0; i < spesificColum.size(); i++) {
							ReportColumModel tmp = spesificColum.get(i);
							columModel.add(lastIndex + i, tmp);
						}
					}
					if (null == columModel) {
						columModel = new ArrayList<ReportColumModel>();
					}

					List<ColumnParam> colums = columnGenerator.generateColumParam(columModel);

					// query dapetin datanya.
					List<ReportCriteria> reportCriterias = buildReportCriteria(rp, model);
					List<String> datas = transactionLogDao.findRawData(reportCriterias, model);
					List<String> datasHousekeeping = transactionLogHousekeepingDao.findRawData(reportCriterias, model);

					datasHousekeeping.addAll(datas);

					// untuk ATMB tambahin disini yah
					if (!model.getTipe().equals("GEN") && !model.getTipe().equals("NOT_GEN")) {
						datasHousekeeping = customProcess.get(model.getTipe()).doProcess(datasHousekeeping);
					}

					if (rp.isCsv()) {
						ReportContent content = reportFormater.formatSparatedValue(datasHousekeeping, colums, false);

						ReportHeader header = generateHeader(rp, content, model);
						StringWriter writer = reportGenerator.generateCSV(content, header);

						String filePath = dirPath + "/" + model.getFilePrefixName() + "_" + filePosfix + ".csv";
						writeToFile(filePath, writer);
					}

					if (rp.isTxt()) {
						ReportContent content = reportFormater.formatSparatedValue(datasHousekeeping, colums, true);

						ReportHeader header = generateHeader(rp, content, model);

						StringWriter writer = reportGenerator.generateTXT(content, header);

						String filePath = dirPath + "/" + model.getFilePrefixName() + "_" + filePosfix + ".txt";
						writeToFile(filePath, writer);
					}

					result.setGenerate(true);

				} catch (RawColumnFormatException e) {
					result.setErrorMessage(e.getMessage());
					e.printStackTrace();
				} catch (RawDataFormatException e) {
					result.setErrorMessage(e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					result.setErrorMessage("gagal mengenerate report " + model.getFilePrefixName());
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	private ReportHeader generateHeader(ReportParam rp, ReportContent content, ReportModel model) {
		// generate header param report
		ReportParamHeader headerParam = new ReportParamHeader(rp);
		headerParam.setReportLength(content.getLine().length());
		headerParam.setJudul(model.getNamaReport());
		headerParam.setLine(content.getLine());
		headerParam.setTglCetak(new Date());
		headerParam.setUserId(rp.getUserId());

		String bank = systemParameterDao.findParamValueByParamName("webadmin.report.nama.bank");
		headerParam.setBank(bank);

		// format report header
		ReportHeader header = reportFormater.createHeader(headerParam);

		return header;
	}

	private List<ReportCriteria> buildReportCriteria(ReportParam rp, ReportModel model) {

		List<ReportCriteria> crits = new ArrayList<ReportCriteria>();

		// kriteria kode_transaksi
		ReportCriteria kodeTransaksiCrits = new ReportCriteria(model.getResKodeTransaksi(), "id.transactionCode",
				model.getKodeTransaksi());
		crits.add(kodeTransaksiCrits);

		// kriteria rc_channel
		ReportCriteria rcTransaksiCrits = new ReportCriteria(model.getResRcChannel(), "id.channelRc", model.getRc());
		crits.add(rcTransaksiCrits);

		// kriteria tanggal awal
		String formatTglAwal = DateUtil.convertDateToString(rp.getTglAwal(), DateUtil.getDatePattern());
		ReportCriteria tglAwalTransaksiCrits = new ReportCriteria(ReportCriteria.GREATER_EQUALS, "id.transactionTime",
				formatTglAwal);
		crits.add(tglAwalTransaksiCrits);

		// kriteria tanggal akhir
		String formatTglAkhir = DateUtil.convertDateToString(rp.getTglAkhir(), DateUtil.getDatePattern());
		ReportCriteria tglAkhirTransaksiCrits = new ReportCriteria(ReportCriteria.LESS_EQUALS, "id.transactionTime",
				formatTglAkhir);
		crits.add(tglAkhirTransaksiCrits);

		// kriteria tambahan filter
		ReportCriteria filterCriteria = new ReportCriteria("%X%", "id.raw", model.getFilter());
		crits.add(filterCriteria);

		return crits;
	}

	private void writeToFile(String filePath, StringWriter writer) throws IOException {

		File file = new File(filePath);
		file.createNewFile();
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(writer.toString());
		fileWriter.close();
	}

	private String getReportPath() {

		File file = new File("");

		if (null == reportPath) {
			reportPath = systemParameterDao.findParamValueByParamName("webadmin.download.temporary.path");
		}
		if (!reportPath.startsWith("/"))
			reportPath = "/" + reportPath;

		return file.getAbsolutePath() + reportPath;
	}

}
