package com.balicamp.service.impl.mx;

import java.awt.Color;
import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.balicamp.Constants;
import com.balicamp.dao.iar.IarDao;
import com.balicamp.dao.ikrap.IkrapDao;
import com.balicamp.dao.kalibrasi.KalibrasiDao;
import com.balicamp.dao.mx.EndpointRcsDao;
import com.balicamp.dao.mx.TransactionLogDao;
import com.balicamp.dao.mx.TransactionLogHousekeepingDao;
import com.balicamp.dao.mx.TransactionsDao;
import com.balicamp.dao.parameter.SystemParameterDao;
import com.balicamp.dao.pengujian.PengujianDao;
import com.balicamp.dao.pengujian.SertifikasiDao;
import com.balicamp.dao.reor.ReorDao;
import com.balicamp.dao.sims.BillingDao;
import com.balicamp.dao.unar.UnarDao;
import com.balicamp.model.mx.AnalisaTransactionLogsDto;
import com.balicamp.model.mx.EndpointRcs;
import com.balicamp.model.mx.InquiryReconcileDto;
import com.balicamp.model.mx.MessageLogsDto;
import com.balicamp.model.mx.MessageLogsParameter;
import com.balicamp.model.mx.ReconcileDto;
import com.balicamp.model.mx.Transactions;
import com.balicamp.model.parameter.SystemParameter;
import com.balicamp.model.user.User;
import com.balicamp.service.MessageLogsManager;
import com.balicamp.service.TransactionLogsManager;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.util.DateUtil;
import com.balicamp.util.InquiryPaymentStatusUtil;
import com.balicamp.util.LogHelper;
import com.balicamp.util.mx.WriteToWord;
import com.lowagie.text.Cell;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

@Service("transactionLogsManagerImpl")
public class TransactionLogsManagerImpl extends AbstractManager implements TransactionLogsManager {
	private static final long serialVersionUID = -3908230684618992868L;

	@Autowired
	private TransactionLogDao transactionLogDao;

	@Autowired
	private EndpointRcsDao endpointRcsDao;

	@Autowired
	private TransactionsDao transactionDao;

	@Autowired
	@Qualifier("messageLogsManagerImpl")
	private MessageLogsManager messageLogsManager;

	@Autowired
	private TransactionLogHousekeepingDao transactionLogHousekeepingDao;

	@Autowired
	private SystemParameterDao systemParameterDao;

	@Autowired
	private BillingDao billingDao;
	
	@Autowired
	private ReorDao reorDao;
	
	@Autowired
	private UnarDao unarDao;
	
	@Autowired
	private IarDao iarDao;
	
	@Autowired
	private IkrapDao ikrapDao;
	
	@Autowired
	private PengujianDao pengujianDao;
	
	@Autowired
	private SertifikasiDao sertifikasiDao;
	
	@Autowired
	private KalibrasiDao kalibrasiDao;

	@Override
	public Object getDefaultDao() {
		return transactionLogDao;
	}

	List<AnalisaTransactionLogsDto> result = null;

	@Override
	public List<AnalisaTransactionLogsDto> findTransactions(String txId, String txCode, String channelCode,
			String klienID, String invoiceNo, String endpointTransactionType, String responseCode, Date startDate,
			Date endDate, String rawKey, int first, int max) {

		List<Object> queryResultMx = (List<Object>) transactionLogDao.findTransactions(txId, endpointTransactionType,
				txCode, klienID, invoiceNo, startDate, endDate, rawKey, first, max);

		result = new ArrayList<AnalisaTransactionLogsDto>();
		for (Object o : queryResultMx) {

			Object[] row = (Object[]) o;
			AnalisaTransactionLogsDto dto = new AnalisaTransactionLogsDto();
			dto.setTrxId((String) row[0]);
			dto.setTrxTime((String) row[1]);
			dto.setTrxCode((String) row[2]);
			dto.setTrxDesc((String) row[3]);
			dto.setChannelCode((String) row[4]);
			dto.setChannelRc((String) row[5]);
			dto.setMxRc((String) row[8]);
			dto.setRcDesc((String) row[6]);
			dto.setRaw((String) row[7]);
			dto.setKlienID((String) row[7], (String) row[5], (String) row[2], klienID);
			dto.setClientName((String) row[7], (String) row[5], (String) row[2]);
			dto.setInvoiceNo((String) row[7], (String) row[5], (String) row[2], invoiceNo);
			dto.setEndpoint((String) row[7], (String) row[5], (String) row[2]);

			if (dto.getChannelRc().equals(responseCode)) {
				result.add(dto);
			} else if (!dto.getChannelRc().equals(responseCode)) {// response
				result.add(dto);
			} else {
				result.add(new AnalisaTransactionLogsDto());
			}
		}

		log.info("TransactionLogsManagerImpl RESULT Size " + result.size());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findMessageLogsByTxId(String txId) {
		List<String> raw = new ArrayList<String>();
		List<Object> queryResultMx = (List<Object>) transactionLogDao.findRawMessage(txId);

		for (Object o : queryResultMx) {
			Object[] row = (Object[]) o;
			raw.add((String) row[7]);
		}
		return raw;
	}

	private static final String PARAM_NAME = "webadmin.download.temporary.path";
	private static final String DEFAULT_FILENAME = "TransactionLogs";

	@Override
	public String exportToWord(List<AnalisaTransactionLogsDto> lines, String pathFromServlet, User user) {
		List<List<Cell>> listPage = new ArrayList<List<Cell>>();

		for (AnalisaTransactionLogsDto dis : lines) {
			// StringBuffer buffer = new StringBuffer();
			try {
				// headers
				List<Cell> cellList = new ArrayList<Cell>();
				Cell header = new Cell(new Phrase("Transaction Log", FontFactory.getFont(FontFactory.TIMES_BOLD, 14)));
				header.setHorizontalAlignment(1);
				header.setColspan(2);
				header.setBackgroundColor(Color.GRAY);
				cellList.add(header);
				cellList.add(new Cell("Transaction ID"));
				cellList.add(new Cell(dis.getTrxId()));
				cellList.add(new Cell("Transaction Time"));
				cellList.add(new Cell("" + dis.getTrxTime()));
				cellList.add(new Cell("Transaction Code"));
				cellList.add(new Cell(dis.getTrxCode()));
				cellList.add(new Cell("Transaction Name"));
				cellList.add(new Cell(dis.getTrxDesc()));
				cellList.add(new Cell("Channel Code"));
				cellList.add(new Cell(dis.getChannelCode()));
				cellList.add(new Cell("Channel RC"));
				cellList.add(new Cell(dis.getChannelRc()));
				cellList.add(new Cell("RC Description"));
				cellList.add(new Cell(dis.getRcDesc()));

				Cell detailCell = new Cell(new Phrase("Detail Transaction Data", FontFactory.getFont(
						FontFactory.TIMES_BOLD, 14)));
				detailCell.setHorizontalAlignment(1);
				detailCell.setColspan(2);
				detailCell.setBackgroundColor(Color.GRAY);
				cellList.add(detailCell);

				Cell subHeader1 = new Cell(new Phrase("Name",
						FontFactory.getFont(FontFactory.TIMES_BOLD, 14, Font.BOLD)));
				subHeader1.setHorizontalAlignment(1);
				subHeader1.setBackgroundColor(Color.GRAY);
				cellList.add(subHeader1);

				Cell subHeader2 = new Cell(new Phrase("Value", FontFactory.getFont(FontFactory.TIMES_BOLD, 14,
						Font.BOLD)));
				subHeader2.setHorizontalAlignment(1);
				subHeader2.setBackgroundColor(Color.GRAY);
				cellList.add(subHeader2);

				Properties prop = new Properties();
				try {
					prop.load(new StringReader(dis.getRaw()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				Set<Object> setOfProp = prop.keySet();

				for (Object s : setOfProp) {
					String key = (String) s;
					String value = prop.getProperty(key);
					cellList.add(new Cell(key));
					cellList.add(new Cell(value));
				}
				listPage.add(cellList);

				// add Message Logs
				Cell messageLogs = new Cell(new Phrase("Message Logs Data", FontFactory.getFont(FontFactory.TIMES_BOLD,
						14)));
				messageLogs.setHorizontalAlignment(1);
				messageLogs.setColspan(2);
				messageLogs.setBackgroundColor(Color.GRAY);
				cellList.add(messageLogs);

				MessageLogsParameter messageLogsParameter = new MessageLogsParameter();
				messageLogsParameter.setTransactionId(dis.getTrxId());
				List<MessageLogsDto> messageLogsList = messageLogsManager.findMessageLogsByParameter(
						messageLogsParameter, -1, -1);

				for (int i = 0; i < messageLogsList.size(); i++) {
					MessageLogsDto dto = messageLogsList.get(i);

					String detailMsg = "";

					if (i == 0)
						detailMsg = dto.getEndpointCode() + " To MX ";
					else {
						if (dto.isRequest()) {
							detailMsg = "MX To " + dto.getEndpointCode();
						} else {
							detailMsg = dto.getEndpointCode() + " To MX ";
						}
					}

					Cell detailMessageLogs = new Cell(new Phrase("[" + (i + 1) + "] " + detailMsg, FontFactory.getFont(
							FontFactory.TIMES_BOLD, 10)));
					detailMessageLogs.setHorizontalAlignment(1);
					detailMessageLogs.setColspan(2);
					detailMessageLogs.setBackgroundColor(Color.YELLOW);
					cellList.add(detailMessageLogs);

					cellList.add(new Cell("Mapping Name"));
					cellList.add(new Cell(dto.getMappingName()));
					cellList.add(new Cell("Transaction Date"));
					cellList.add(new Cell(DateUtil.convertDateToString(dto.getConversionTime(), "dd MMM yyyy HH:mm:ss")));
					cellList.add(new Cell("Endpoint Code"));
					cellList.add(new Cell(dto.getEndpointCode()));
					cellList.add(new Cell("Response Code"));
					cellList.add(new Cell(dto.getRc()));
					cellList.add(new Cell("Response Code Description"));
					cellList.add(new Cell(dto.getRcDesc()));
					cellList.add(new Cell("Raw Message"));
					cellList.add(new Cell(dto.getRaw()));
					cellList.add(new Cell("Formatted Raw Message"));

					try {
						dto = messageLogsManager.getRawMessageLogs(dto, false);
					} catch (Exception dt) {
						dto.setFormatedMessage("Gagal Parsing Message");
					}
					cellList.add(new Cell(new Paragraph(dto.getFormatedMessage())));
				}

			} catch (Exception e) {}
		}

		String path = systemParameterDao.findParamValueByParamName(PARAM_NAME);
		return WriteToWord.doWriteTable(listPage, path, DEFAULT_FILENAME, pathFromServlet, user);
	}

	@Override
	public Map<String, Long> getChartTotalTransaction(Date startDate, Date endDate) {
		Map<String, Long> map = new HashMap<String, Long>();

		Long total = transactionLogDao.getTotalCountTransaction(startDate, endDate, null);
		Long totalSuccess = transactionLogDao.getTotalCountTransaction(startDate, endDate, "00");
		Long totalTimeOut = transactionLogDao.getTotalCountTransaction(startDate, endDate, "ZZ");
		Long totalFailed = total - totalSuccess - totalTimeOut;

		map.put("totalSuccess", totalSuccess);
		map.put("totalTimeOut", totalTimeOut);
		map.put("totalFailed", totalFailed);

		return map;
	}

	@Override
	public Map<String, Long> getTotalTransactionForDashboard(Date toDate) {
		Map<String, Long> map = new HashMap<String, Long>();

		Long total = transactionLogDao.getTotalCountTransaction(null, toDate, null);
		Long totalSuccess = transactionLogDao.getTotalCountTransaction(null, toDate, "00");
		Long totalTimeOut = transactionLogDao.getTotalCountTransaction(null, toDate, "ZZ");
		Long totalFailed = total - totalSuccess - totalTimeOut;

		map.put("total", total);
		map.put("totalSuccess", totalSuccess);
		map.put("totalTimeOut", totalTimeOut);
		map.put("totalFailed", totalFailed);

		return map;
	}

	@Override
	public List<AnalisaTransactionLogsDto> getLastTransactionsLog(Date toTimeMilis, int i) {
		List<Object> queryResultMx = (List<Object>) transactionLogDao.findLastTransactionsLog(i);

		List<AnalisaTransactionLogsDto> result = new ArrayList<AnalisaTransactionLogsDto>();
		for (Object o : queryResultMx) {
			Object[] row = (Object[]) o;
			AnalisaTransactionLogsDto dto = new AnalisaTransactionLogsDto();
			dto.setTrxId((String) row[0]);
			dto.setTrxTime((String) row[1]);
			dto.setTrxCode((String) row[2]);
			dto.setTrxDesc((String) row[3]);
			dto.setChannelCode((String) row[4]);
			dto.setChannelRc((String) row[5]);
			dto.setRcDesc((String) row[6]);
			dto.setRaw((String) row[7]);
			result.add(dto);
		}
		return result;
	}

	@Override
	public List<Object> findByTransaction(String klienID, String invoiceNo, String transactionType, String endpoint,
			String responseCode, Date startDate, Date endDate) {
		// TODO Auto-generated method stub

		List<Object> queryResultMx = transactionLogDao.findByTransaction(klienID, invoiceNo, transactionType, endpoint,
				responseCode, startDate, endDate);

		List<Object> result = new ArrayList<Object>();
		for (Object o : queryResultMx) {

			Object[] row = (Object[]) o;
			AnalisaTransactionLogsDto dto = new AnalisaTransactionLogsDto();
			dto.setTrxId((String) row[0]);
			dto.setTrxTime((String) row[1]);
			dto.setTrxCode((String) row[2]);
			dto.setTrxDesc((String) row[3]);
			dto.setChannelCode((String) row[4]);
			dto.setChannelRc((String) row[5]);
			dto.setMxRc((String) row[8]);
			dto.setRcDesc((String) row[6]);
			dto.setRaw((String) row[7]);
			dto.setKlienID((String) row[7], (String) row[5], (String) row[2], klienID);
			dto.setClientName((String) row[7], (String) row[5], (String) row[2]);
			dto.setInvoiceNo((String) row[7], (String) row[5], (String) row[2], invoiceNo);
			dto.setEndpoint((String) row[7], (String) row[5], (String) row[2]);

			if (dto.getEndpoint().equals(endpoint) && dto.getChannelRc().equals(responseCode)) {
				log.info("endpoint & response ->" + responseCode);

				result.add(dto);

			} else if (dto.getEndpoint().equals(endpoint) && !dto.getChannelRc().equals(responseCode)) {
				log.info("endpoint & response all->" + responseCode);

				result.add(dto);

			} else {
				log.info("endpoint & response null" + responseCode);

				result.add(new AnalisaTransactionLogsDto());
			}
		}
		// return queryResultMx;
		return result;

	}

	@Override
	public Object findTransactionsByTransactionID(String txId) {
		// TODO Auto-generated method stub

		log.info("transactionObject txId " + txId);

		Object transactionObject = transactionLogDao.findTransactionsByTransactionID(txId);

		return transactionObject;
	}

	@Override
	public List<AnalisaTransactionLogsDto> findTransactions(String txId, String transactionType, String txCode,
			String klienID, String invoiceNo, Date startDate, Date endDate, String rawKey, int first, int max) {
		List<Object> queryResultMx = (List<Object>) transactionLogDao.findTransactions(txId, transactionType, txCode,
				klienID, invoiceNo, startDate, endDate, rawKey, first, max);

		result = new ArrayList<AnalisaTransactionLogsDto>();
		for (Object o : queryResultMx) {

			Object[] row = (Object[]) o;
			AnalisaTransactionLogsDto dto = new AnalisaTransactionLogsDto();
			dto.setTrxId((String) row[0]);
			dto.setTrxTime((String) row[1]);
			dto.setTrxCode((String) row[2]);
			dto.setTrxDesc((String) row[3]);
			dto.setChannelCode((String) row[4]);
			dto.setChannelRc((String) row[5]);
			dto.setMxRc((String) row[8]);
			dto.setRcDesc((String) row[6]);
			dto.setRaw((String) row[7]);
			dto.setKlienID((String) row[7], (String) row[5], (String) row[2], klienID);
			dto.setClientName((String) row[7], (String) row[5], (String) row[2]);
			dto.setInvoiceNo((String) row[7], (String) row[5], (String) row[2], invoiceNo);
			dto.setEndpoint((String) row[7], (String) row[5], (String) row[2]);

			Properties prop = new Properties();
			try {
				prop.load(new StringReader(dto.getRaw()));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// add in 23-10-2014
			String channelId = prop.getProperty("/custom/channelID/text()");
			if (channelId != null) {
				dto.setChannelId(systemParameterDao.findParamValueByParamName(channelId));
			}

			if (row[8] != null) {
				dto.setMxRcDesc((String) row[8]
						+ (systemParameterDao.findSystemParameterById("mx", (String) row[8])).getParamValue());
			}

			if (row[5] != null) {
				dto.setChannelRcDesc((String) row[5]
						+ (systemParameterDao.findSystemParameterById("mandiri", (String) row[5])).getParamValue());
			}

			if (klienID != null && invoiceNo == null) {
				if (klienID.equals(prop.getProperty("/custom/clientID/text()"))) {
					result.add(dto);

				}
			} else if (klienID == null && invoiceNo != null) {
				if (invoiceNo.equals(prop.getProperty("/custom/invoiceNumber/text()"))) {
					result.add(dto);

				}

			} else if (klienID != null && invoiceNo != null) {
				if (invoiceNo.equals(prop.getProperty("/custom/invoiceNumber/text()"))
						&& klienID.equals(prop.getProperty("/custom/clientID/text()"))) {
					result.add(dto);

				}

			} else {
				result.add(dto);

			}

		}

		return result;
	}

	@Override
	public List<AnalisaTransactionLogsDto> findTransactions(String txId, String transactionType, String txCode,
			String klienID, String invoiceNo, Date startDate, Date endDate, String rawKey, Transactions transaction) {
		List<Object> queryResultMx = (List<Object>) transactionLogDao.findTransactions(txId, transactionType, txCode,
				klienID, invoiceNo, startDate, endDate, rawKey, transaction);

		result = new ArrayList<AnalisaTransactionLogsDto>();
		for (Object o : queryResultMx) {

			Object[] row = (Object[]) o;
			AnalisaTransactionLogsDto dto = new AnalisaTransactionLogsDto();
			dto.setTrxId((String) row[0]);
			dto.setTrxTime((String) row[1]);
			dto.setTrxCode((String) row[2]);
			dto.setTrxDesc((String) row[3]);
			dto.setChannelCode((String) row[4]);
			dto.setChannelRc((String) row[5]);
			dto.setMxRc((String) row[8]);
			dto.setRcDesc((String) row[6]);
			dto.setRaw((String) row[7]);
			dto.setKlienID((String) row[7], (String) row[5], (String) row[2], klienID);
			dto.setClientName((String) row[7], (String) row[5], (String) row[2]);
			dto.setInvoiceNo((String) row[7], (String) row[5], (String) row[2], invoiceNo);
			dto.setEndpoint((String) row[7], (String) row[5], (String) row[2]);

			Properties prop = new Properties();
			try {
				prop.load(new StringReader(dto.getRaw()));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// add in 23-10-2014
			String channelId = prop.getProperty("/custom/channelID/text()");
			if (channelId != null) {
				dto.setChannelId(systemParameterDao.findParamValueByParamName(channelId));
			}

			if (row[8] != null) {
				dto.setMxRcDesc((systemParameterDao.findSystemParameterById("mx", (String) row[8])).getParamValue());
			}
			if (row[5] != null) {
				dto.setChannelRcDesc((systemParameterDao.findSystemParameterById("mandiri", (String) row[5]))
						.getParamValue());
			}

			if (klienID != null && invoiceNo == null) {
				if (klienID.equals(prop.getProperty("/custom/clientID/text()"))) {
					result.add(dto);

				}
			} else if (klienID == null && invoiceNo != null) {
				if (invoiceNo.equals(prop.getProperty("/custom/invoiceNumber/text()"))) {
					result.add(dto);

				}

			} else if (klienID != null && invoiceNo != null) {
				if (invoiceNo.equals(prop.getProperty("/custom/invoiceNumber/text()"))
						&& klienID.equals(prop.getProperty("/custom/clientID/text()"))) {
					result.add(dto);

				}

			} else {
				result.add(dto);

			}

		}

		return result;
	}

	@Override
	public Long getRowCount(String txId, String txCode, String channelCode, Date startDate, Date endDate, String rawKey) {

		return transactionLogDao.findTransactionCount(txId, txCode, startDate, endDate, rawKey);

	}

	@Override
	public boolean findTransactionLogByInvoiceNo(String invoiceNo, String clientId) {
		return transactionLogDao.findTransactionLogByInvoiceNo(invoiceNo, clientId);
	}

	@Override
	public List<AnalisaTransactionLogsDto> findTransactionsPlusRc(String billerCode, String txCode, String klienID,
			String invoiceNo, Date startDate, Date endDate, EndpointRcs billerRc, String channelCode,
			String respCodeChannel) {

		List<Object> queryResultMx = (List<Object>) transactionLogDao.findTransactionsPlusRc(billerCode, txCode,
				klienID, invoiceNo, startDate, endDate, billerRc, channelCode, respCodeChannel);

		result = new ArrayList<AnalisaTransactionLogsDto>();
		int i = 1;
		for (Object o : queryResultMx) {

			Object[] row = (Object[]) o;
			AnalisaTransactionLogsDto dto = new AnalisaTransactionLogsDto();
			dto.setTrxId((String) row[0]);
			dto.setTrxTime((String) row[1]);
			dto.setTrxDesc((String) row[3]);
			dto.setBillerRcDesc((String) row[10]);
			dto.setChannelRcDesc((String) row[6]);

			dto.setClientId((String) row[13]);
			dto.setNameKlien((String) row[14]);
			dto.setInvoiceNumber((String) row[12]);

			// add in 23-10-2014
			String channelId = (String) row[7];
			if (channelId != null) {
				dto.setChannelId(systemParameterDao.findParamValueByParamName(channelId));
			}

			// buat nomor urut, 28-09-2015
			dto.setNo(i);
			i++;

			result.add(dto);

		}

		return result;
	}

	@Override
	public String findParamValueByParamName() {
		String tempOutputPath = systemParameterDao.findParamValueByParamName("webadmin.download.temporary.path");
		return tempOutputPath;
	}

	@Override
	public String findParamValueByParamName(String param) {
		String tempOutputPath = systemParameterDao.findParamValueByParamName(param);
		return tempOutputPath;
	}

	@Override
	public List<InquiryReconcileDto> findTransactionLogsWebadmin(String channel, Set<String> invoiceNo,
			String clientId, String transactionCode, String reconcileStatus, String successChannelStatus,
			Map<String, Integer> mapCount) {

		List<InquiryReconcileDto> result = new ArrayList<InquiryReconcileDto>();
		Map<String, Object[]> queryResultMx = (Map<String, Object[]>) transactionLogDao
				.findLastSuccessTransactionLogsWebadmin(channel, invoiceNo, clientId, transactionCode,
						successChannelStatus);

		int settled = 0;
		int notSettled = 0;
		int no = 0;

		for (Map.Entry<String, Object[]> o : queryResultMx.entrySet()) {
			no++;
			Object[] row = (Object[]) o.getValue();

			InquiryReconcileDto dto = new InquiryReconcileDto();
			dto.setNo(no);
			dto.setTrxId(checkIfNull(row[0]));
			dto.setClientId(checkIfNull(row[1]));
			dto.setInvoiceNo(checkIfNull(row[3]));
			dto.setTransactionName(checkIfNull(row[4]));
			dto.setDelivChannel(checkIfNull(row[5]));
			dto.setBillerRc(checkIfNull(row[6]));
			dto.setChannelRc(checkIfNull(row[7]));
			dto.setTransactionTime(checkIfNull(row[8]));
			dto.setBankName(checkIfNull(row[9]));
			dto.setBillerResponse(checkIfNull(row[10]));
			dto.setChannelResponse(checkIfNull(row[11]));

			Object simsStatus = null;
			Object invoiceObj = row[3];
			Object channelRc = row[7];
			if (invoiceObj != null) {
				simsStatus = billingDao.findBillingByInvoiceNo(checkIfNull(invoiceObj),
						new String[] { "8", "9", "10", "11", "38", "48" });
				if (simsStatus != null) {
					dto.setSimsStatus("Paid");
					if (checkIfNull(channelRc).equals("00")) {
						dto.setReconcileStatus("Settled");
						settled++;
					} else {
						dto.setReconcileStatus("Unsettled");
						notSettled++;
					}
				} else {
					simsStatus = billingDao.findBillingByInvoiceNo(checkIfNull(invoiceObj));
					if (simsStatus != null) {
						dto.setSimsStatus("Unpaid");
						if (checkIfNull(channelRc).equals("00")) {
							dto.setReconcileStatus("Unsettled");
							notSettled++;
						} else {
							dto.setReconcileStatus("Settled");
							settled++;
						}
					} else {
						dto.setSimsStatus("Not Found");
					}
				}

				if (simsStatus != null) {
					dto.setInvoiceDueDate((((Object[]) simsStatus)[2]).toString());
					dto.setClientName((((Object[]) simsStatus)[3]).toString());
				}
			}

			result.add(dto);
		}

		mapCount.put("settled", settled);
		mapCount.put("notSettled", notSettled);

		return result;
	}

	@Override
	public List<InquiryReconcileDto> findTransactionLogsWebadminWithMap(String channel, Set<String> invoiceNo,
			String clientId, Set<String> transactionCode, String reconcileStatus, String successChannelStatus,
			Map<String, Integer> mapCount) {

		List<InquiryReconcileDto> result = new ArrayList<InquiryReconcileDto>();

		Map<String, String> biTypeCancel = new HashMap<String, String>();
		biTypeCancel.put("12", "12");

		// Search invoice MX with channel
		Map<String, Object[]> queryResultMx = (Map<String, Object[]>) transactionLogDao
				.findAllTransactionLogsWebadminPayment(channel, invoiceNo, clientId, transactionCode);

		// Sort Map by invoice No
		Map<String, Object[]> sortedMap = InquiryPaymentStatusUtil.sortByComparatorInvoiceNo(queryResultMx);

		// Group by invoice no
		Map<String, Map<String, Object[]>> groupMap = InquiryPaymentStatusUtil.groupingMapByInvoiceNo(sortedMap);

		// Remove duplicate invoice no and get channel response 00 or latest
		// transaction
		Map<String, Object[]> invoiceMx = InquiryPaymentStatusUtil.removeDuplicate(groupMap);

		// Search invoice MX exclude channel
		Map<String, Object[]> queryResultMxAllChannel = (Map<String, Object[]>) transactionLogDao
				.findAllTransactionLogsWebadminPayment("all", invoiceNo, clientId, transactionCode);

		// Sort Map by invoice No
		Map<String, Object[]> sortedMapAllChannel = InquiryPaymentStatusUtil
				.sortByComparatorInvoiceNo(queryResultMxAllChannel);

		// Group by invoice no
		Map<String, Map<String, Object[]>> groupMapAllChannel = InquiryPaymentStatusUtil
				.groupingMapByInvoiceNo(sortedMapAllChannel);

		// Remove duplicate invoice no and get channel response 00 or latest
		// transaction
		Map<String, Object[]> invoiceMxAllChannel = InquiryPaymentStatusUtil.removeDuplicate(groupMapAllChannel);

		// Find in table sims
		Map<String, Object[]> queryResultSims = (Map<String, Object[]>) billingDao.findBillingByInvoiceNo(invoiceNo,
				clientId);

		Map<String, Object[]> queryResultSimsPaid = (Map<String, Object[]>) billingDao.findBillingByInvoiceNo(
				invoiceNo, new String[] { "8", "9", "10", "11", "38", "48" }, clientId);

		int settled = 0;
		int notSettled = 0;
		int no = 0;

		for (String invNo : invoiceNo) {

			if (queryResultSims.get(invNo) != null) {
				no++;

				if (invoiceMx.get(invNo) != null) {
					Object[] row = invoiceMx.get(invNo);
					InquiryReconcileDto dto = new InquiryReconcileDto();
					dto.setNo(no);
					if (!checkIfNull(row[0]).contains("DUMMY")) {
						dto.setTrxId(checkIfNull(row[0]));
						dto.setClientId(checkIfNull(row[1]));
						dto.setClientName(checkIfNull(row[2]));
						dto.setInvoiceNo(checkIfNull(row[3]));
						dto.setTransactionName(checkIfNull(row[4]));
						dto.setDelivChannel(checkIfNull(row[5]));
						dto.setBillerRc(checkIfNull(row[6]));
						dto.setChannelRc(checkIfNull(row[7]));
						dto.setTransactionTime(checkIfNull(row[8]));
						dto.setBankName(checkIfNull(row[9]));
						dto.setBillerResponse(checkIfNull(row[10]));
						dto.setChannelResponse(checkIfNull(row[11]));
					} else {
						dto.setInvoiceNo(invNo);
						dto.setTransactionName("-");
						dto.setDelivChannel(checkIfNull(row[5]));
						dto.setBillerRc("-");
						dto.setChannelRc("-");
						dto.setTransactionTime("-");
						dto.setBankName("-");
						dto.setBillerResponse("-");
						dto.setChannelResponse("-");
					}

					Object[] simsStatus = null;
					Object invoiceObj = row[3];
					Object channelRc = row[7];
					Object transactionCodeObj = row[12];
					if (invoiceObj != null) {
						simsStatus = queryResultSimsPaid.get(invoiceObj.toString());
						if (simsStatus != null) {
							dto.setSimsStatus("Paid");
							if (checkIfNull(channelRc).equals(successChannelStatus)
									&& checkIfNull(transactionCodeObj).equalsIgnoreCase("BILL.PAY")) {
								dto.setReconcileStatus("Settled");
								settled++;
							} else {
								dto.setReconcileStatus("Unsettled");
								notSettled++;
							}
						} else {
							simsStatus = queryResultSims.get(invoiceObj.toString());
							if (simsStatus != null) {
								if (biTypeCancel.get(checkIfNull(simsStatus[1])) != null) {
									dto.setSimsStatus("Cancel");
								} else {
									dto.setSimsStatus("Unpaid");
								}

								if (checkIfNull(channelRc).equals(successChannelStatus)
										&& checkIfNull(transactionCodeObj).equalsIgnoreCase("BILL.PAY")) {
									dto.setReconcileStatus("Unsettled");
									notSettled++;
								} else {
									dto.setReconcileStatus("Unsettled");
									notSettled++;
								}
							} else {
								dto.setSimsStatus("Not Found In Sims");
							}
						}

						if (simsStatus != null) {
							if(simsStatus[2]!=null){
								dto.setInvoiceDueDate(simsStatus[2].toString());
							}else
								dto.setInvoiceDueDate("");
							dto.setClientName(simsStatus[3].toString());
							dto.setClientId(checkIfNull(simsStatus[7]));
						}
					}

					result.add(dto);
				} else {
					if (invoiceMxAllChannel.get(invNo) == null && channel.equalsIgnoreCase("all")) {
						Object[] col = queryResultSims.get(invNo);
						InquiryReconcileDto dto = new InquiryReconcileDto();
						dto.setNo(no);
						dto.setTrxId("-");
						dto.setClientId(checkIfNull(col[7]));
						dto.setClientName(checkIfNull(col[6]));
						dto.setInvoiceNo(invNo);
						dto.setTransactionName("-");
						dto.setDelivChannel("-");
						dto.setBillerRc("-");
						dto.setChannelRc("-");
						dto.setTransactionTime("-");
						dto.setBankName("-");
						dto.setBillerResponse("-");
						dto.setChannelResponse("-");
						if (biTypeCancel.get(checkIfNull(col[1])) != null) {
							dto.setSimsStatus("Cancel");
						} else if (Constants.BiTypePaid.biTypes.get(checkIfNull(col[1])) != null) {
							dto.setSimsStatus("Paid");
						} else {
							dto.setSimsStatus("Unpaid");
						}
						dto.setReconcileStatus("Unsettled");
						notSettled++;
						result.add(dto);
					}
				}
			}

			if (!reconcileStatus.equalsIgnoreCase("all") && result.size() > 0) {
				no = 0;
				List<InquiryReconcileDto> resultFinal = new ArrayList<InquiryReconcileDto>();
				for (int i = 0; i < result.size(); i++) {
					InquiryReconcileDto temp = result.get(i);
					if (temp.getReconcileStatus().equalsIgnoreCase(reconcileStatus)) {
						resultFinal.add(temp);
						no++;
					}
				}

				if (reconcileStatus.equalsIgnoreCase("settled")) {
					notSettled = 0;
				} else {
					settled = 0;
				}

				result = resultFinal;
			}

			mapCount.put("settled", settled);
			mapCount.put("notSettled", notSettled);
		}

		return result;
	}

	@Override
	public List<ReconcileDto> findTransactionLogsWebadmin(HashMap<String, Object[]> mt940Map, HashMap<String, Object[]> mxMap, 
			String channel,
			Set<String> invoiceNo, String clientId, String transactionCode, Date trxDate, String reconcileStatus,
			Map<String, Integer> mapCount, Map<String, Long> mapCountAmount) {
		log.info("mt940Map : " + mt940Map.size());
		log.info("mxMap : " + mxMap.size());
		log.info("channel : " + channel);
		log.info("invoiceNo : " + invoiceNo.size());
		log.info("clientId : " + clientId);
		log.info("transactionCode : " + transactionCode);
		log.info("trxDate : " + trxDate);
		log.info("reconcileStatus : " + reconcileStatus);
		log.info("mapCount : " + mapCount);
		log.info("mapCountAmount : " + mapCountAmount);
		

		/* Randhi */
		List<ReconcileDto> result 		= new ArrayList<ReconcileDto>();
		List<ReconcileDto> resultTemp 	= new ArrayList<ReconcileDto>();
		
//		reconcile item - MX - hnd
		Map<String, Object[]> resultMx 			= new HashMap<String, Object[]>();
//		reconcile item - BILLING (10) - hnd
		Map<String, Object[]> resultBilling 	= new HashMap<String, Object[]>();
//		reconcile item - REOR (20) - hnd
		Map<String, Object[]> resultReor 		= new HashMap<String, Object[]>();
//		reconcile item - IAR (40) - hnd
		Map<String, Object[]> resultUnar 		= new HashMap<String, Object[]>();
//		reconcile item - IAR (50) - hnd
		Map<String, Object[]> resultIar 		= new HashMap<String, Object[]>();
//		reconcile item - IKRAP (60) - hnd
		Map<String, Object[]> resultIkrap 		= new HashMap<String, Object[]>();
//		reconcile item - SERTIFIKASI (70) - hnd
		Map<String, Object[]> resultSertifikasi = new HashMap<String, Object[]>();
//		reconcile item - PENGUJIAN (80) - hnd
		Map<String, Object[]> resultPengujian 	= new HashMap<String, Object[]>();
//		reconcile item - KALIBRASI (90) - hnd
		Map<String, Object[]> resultKalibrasi 	= new HashMap<String, Object[]>();

		int no 					= 0;
		int settled 			= 0;
		int notSettled 			= 0;
		int unconfirmed 		= 0;
		long totalAmount 		= 0;
		long amountSettled 		= 0;
		long amountNotSettled 	= 0;
		long amountUnconfirmed 	= 0;

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		
		//KALIBRASI-90
		if(transactionCode.equals(Constants.EndpointCode.KALIBRASI_CODE)){
			Object[] kalibrasi 		= null;
			Object[] kalibrasiRecon = null;
			Object[] mt940Data 		= null;
			Object[] mxData			= null;
			Object[] mxDataAbnormal	= null;
				
			if(invoiceNo.size()>0 && !mt940Map.isEmpty()){
				for (String invoice : invoiceNo) {
					mt940Data = mt940Map.get(invoice);
					kalibrasi = kalibrasiDao.findBillingByInvoiceAndDate(invoice, trxDate, mt940Data);
					resultKalibrasi.put(invoice, kalibrasi);
				}
			}else if(invoiceNo.size()>0 && !mxMap.isEmpty()){
				for (String invoice : invoiceNo) {
					mxDataAbnormal 	= mxMap.get(invoice);
					kalibrasi 		= kalibrasiDao.findBillingByInvoiceAndDate(invoice, trxDate, mt940Data);
					resultKalibrasi.put(invoice, kalibrasi);
				}
			}
			
			resultMx = transactionLogDao.findAllTransactionLogsWebadminReconcile(
					channel, resultKalibrasi.keySet(), clientId, transactionCode, 
					new String[] { "00" }, new String[] { "00" }, trxDate);
			 
			for (String keys : resultKalibrasi.keySet()) {
				no++;
				ReconcileDto dto 	= new ReconcileDto();
				mxData				= resultMx.get(keys);
				kalibrasiRecon		= resultKalibrasi.get(keys);
				
				if(invoiceNo.size()>0 && !mt940Map.isEmpty()){
					mt940Data = mt940Map.get(keys);
				}
				
				if (reconcileStatus.equalsIgnoreCase("unsettled")
						|| reconcileStatus.equalsIgnoreCase("unsettled/need confirmation")
						|| reconcileStatus.equalsIgnoreCase("all")) {
							
					if (mt940Map.keySet().contains(keys) && kalibrasiRecon == null) { //MT940 ada, CORE ga ada
						String paymentAmount 			= mt940Data[7] != null ? mt940Data[7].toString() : "0";
						String invoiceMt940Status 		= "Paid";
						String invoiceReconcileStatus 	= "Unsettled";
						dto = saveToReconcileDto(
								no, keys, mxData, kalibrasiRecon, mt940Data, channel, invoiceMt940Status, "Unpaid", 
								invoiceReconcileStatus, transactionCode);
						amountNotSettled = amountNotSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						notSettled++;
					}else if (!mt940Map.keySet().contains(keys) && mxMap != null) { //MT940 ga ada, MX ada
						String paymentAmount 			= (mxData!=null && mxData[15] != null) ? mxData[15].toString() : "0";
						String invoiceMt940Status 		= "Unpaid";
						String invoiceReconcileStatus 	= "Unsettled";
						dto = saveToReconcileDto(
								no, keys, mxData, kalibrasiRecon, mt940Data, channel, invoiceMt940Status, "Paid", 
								invoiceReconcileStatus, transactionCode);
						amountNotSettled = amountNotSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						notSettled++;
					}
				}
				
				if (reconcileStatus.equalsIgnoreCase("settled")
						|| reconcileStatus.equalsIgnoreCase("all")) {
//					MT940 ada, CORE ada
					if (mt940Map.keySet().contains(keys) && kalibrasiRecon != null) {
						String paymentAmount 			= mt940Data[7] != null ? mt940Data[7].toString() : "0";
						String invoiceMt940Status 		= "Paid";
						String invoiceReconcileStatus 	= "Settled";
						dto = saveToReconcileDto(
								no, keys, mxData, kalibrasiRecon, mt940Data, channel, invoiceMt940Status, "Paid", 
								invoiceReconcileStatus,  transactionCode);
						amountSettled = amountSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						settled++;
					}
				}
			}
			// Sort By Time
			try {
				result.addAll(sortingDataByTransactionTime(resultTemp));
			} catch (Exception e) {
				e.printStackTrace();
			}
			resultTemp.clear();
			
			totalAmount = (amountSettled + amountNotSettled + amountUnconfirmed);
			mapCountAmount.put	("totalAmount", mapCountAmount.get("totalAmount") + totalAmount);
			mapCount.put		("settled", mapCount.get("settled") + settled);
			mapCount.put		("notSettled", mapCount.get("notSettled") + notSettled);
			mapCount.put		("unconfirmed", mapCount.get("unconfirmed") + unconfirmed);
			mapCountAmount.put	("amountSettled", mapCountAmount.get("amountSettled") + amountSettled);
			mapCountAmount.put	("amountNotSettled", mapCountAmount.get("amountNotSettled") + amountNotSettled);
			mapCountAmount.put	("amountUnconfirmed", mapCountAmount.get("amountUnconfirmed") + amountUnconfirmed);
		}
		
		//PENGUJIAN-80
		if(transactionCode.equals(Constants.EndpointCode.PAP_CODE)){
			Object[] pengujian 		= null;
			Object[] pengujianRecon = null;
			Object[] mt940Data 		= null;
			Object[] mxData			= null;
			Object[] mxDataAbnormal	= null;
			
			if(invoiceNo.size()>0 && !mt940Map.isEmpty()){
				for (String invoice : invoiceNo) {
					mt940Data = mt940Map.get(invoice);
					pengujian = pengujianDao.findBillingByInvoiceAndDate(invoice, trxDate, mt940Data);
					resultPengujian.put(invoice, pengujian);
				}
			}else if(invoiceNo.size()>0 && !mxMap.isEmpty()){
				for (String invoice : invoiceNo) {
					mxDataAbnormal 	= mxMap.get(invoice);
					pengujian 		= pengujianDao.findBillingByInvoiceAndDate(invoice, trxDate, mxDataAbnormal);
					resultPengujian.put(invoice, pengujian);
				}
			}
			
			resultMx = transactionLogDao.findAllTransactionLogsWebadminReconcile(
					channel, resultPengujian.keySet(), clientId, transactionCode, 
					new String[] { "00" }, new String[] { "00" }, trxDate);
			 
			for (String keys : resultPengujian.keySet()) {
				no++;
				ReconcileDto dto 	= new ReconcileDto();
				mxData				= resultMx.get(keys);
				pengujianRecon		= resultPengujian.get(keys);
				
				if(invoiceNo.size()>0 && !mt940Map.isEmpty()){
					mt940Data = mt940Map.get(keys);
				}
				
				if (reconcileStatus.equalsIgnoreCase("unsettled")
						|| reconcileStatus.equalsIgnoreCase("unsettled/need confirmation")
						|| reconcileStatus.equalsIgnoreCase("all")) {
					
					if (mt940Map.keySet().contains(keys) && pengujianRecon == null) { //MT940 ada, CORE ga ada
						String paymentAmount 			= mt940Data[7] != null ? mt940Data[7].toString() : "0";
						String invoiceMt940Status 		= "Paid";
						String invoiceReconcileStatus 	= "Unsettled";
						dto = saveToReconcileDto(
								no, keys, mxData, pengujianRecon, mt940Data, channel, invoiceMt940Status, "Unpaid", 
								invoiceReconcileStatus, transactionCode);
						amountNotSettled = amountNotSettled + Double.valueOf(paymentAmount).longValue();;
						resultTemp.add(dto);
						notSettled++;
					}else if (!mt940Map.keySet().contains(keys) && mxMap != null) { //MT940 ga ada, MX ada
						String paymentAmount 			= (mxData!=null && mxData[15] != null) ? mxData[15].toString() : "0";
						String invoiceMt940Status 		= "Unpaid";
						String invoiceReconcileStatus 	= "Unsettled";
						dto = saveToReconcileDto(
								no, keys, mxData, pengujianRecon, mt940Data, channel, invoiceMt940Status, "Paid", 
								invoiceReconcileStatus, transactionCode);
						amountNotSettled = amountNotSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						notSettled++;
					}
				}
				
				if (reconcileStatus.equalsIgnoreCase("settled")
						|| reconcileStatus.equalsIgnoreCase("all")) {

//					MT940 ada, CORE ada
					if (mt940Map.keySet().contains(keys) && pengujianRecon != null) {
						String paymentAmount 			= mt940Data[7] != null ? mt940Data[7].toString() : "0";
						String invoiceMt940Status 		= "Paid";
						String invoiceReconcileStatus 	= "Settled";
						dto = saveToReconcileDto(
								no, keys, mxData, pengujianRecon, mt940Data, channel, invoiceMt940Status, "Paid", 
								invoiceReconcileStatus, transactionCode);
						amountSettled = amountSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						settled++;
					}
				}
			}
			// Sort By Time
			try {
				result.addAll(sortingDataByTransactionTime(resultTemp));
			} catch (Exception e) {
				e.printStackTrace();
			}
			resultTemp.clear();
			
			totalAmount = (amountSettled + amountNotSettled + amountUnconfirmed);
			mapCountAmount.put	("totalAmount", mapCountAmount.get("totalAmount") + totalAmount);
			mapCount.put		("settled", mapCount.get("settled") + settled);
			mapCount.put		("notSettled", mapCount.get("notSettled") + notSettled);
			mapCount.put		("unconfirmed", mapCount.get("unconfirmed") + unconfirmed);
			mapCountAmount.put	("amountSettled", mapCountAmount.get("amountSettled") + amountSettled);
			mapCountAmount.put	("amountNotSettled", mapCountAmount.get("amountNotSettled") + amountNotSettled);
			mapCountAmount.put	("amountUnconfirmed", mapCountAmount.get("amountUnconfirmed") + amountUnconfirmed);
		}
		
		//SERTIFIKASI-70
		if(transactionCode.equals(Constants.EndpointCode.PERANGKAT_CODE)){
			Object[] sertifikasi 		= null;
			Object[] sertifikasiRecon 	= null;
			Object[] mxData				= null;
			Object[] mxDataAbnormal		= null;
			Object[] mt940Data 			= null;
			
			if(invoiceNo.size()>0){
				for (String invoice : invoiceNo) {
					if(!mt940Map.isEmpty()){
						mt940Data = mt940Map.get(invoice);
					}else if (!mxMap.isEmpty()){
						mxDataAbnormal 	= mxMap.get(invoice);
					}
					String invoiceSP2 	= invoice.substring(0,invoice.length()-6)+"/SP2/KSDP/";
					String invoiceMonth = invoice.substring(4,6); 
					if(invoiceMonth.equals("01")){
						invoiceSP2 += "I/";
					}else if(invoiceMonth.equals("02")){
						invoiceSP2 += "II/";
					}else if(invoiceMonth.equals("03")){
						invoiceSP2 += "III/";
					}else if(invoiceMonth.equals("04")){
						invoiceSP2 += "IV/";
					}else if(invoiceMonth.equals("05")){
						invoiceSP2 += "V/";
					}else if(invoiceMonth.equals("06")){
						invoiceSP2 += "VI/";
					}else if(invoiceMonth.equals("07")){
						invoiceSP2 += "VII/";
					}else if(invoiceMonth.equals("08")){
						invoiceSP2 += "VIII/";
					}else if(invoiceMonth.equals("09")){
						invoiceSP2 += "IX/";
					}else if(invoiceMonth.equals("10")){
						invoiceSP2 += "X/";
					}else if(invoiceMonth.equals("11")){
						invoiceSP2 += "XI/";
					}else if(invoiceMonth.equals("12")){
						invoiceSP2 += "XII/";
					}
					invoiceSP2 += invoice.substring(invoice.length()-4);
					
					if(!mt940Map.isEmpty()){
						log.info("dataSource SERTIFIKASI: MT940 - "+invoiceSP2);
						sertifikasi = sertifikasiDao.findBillingByInvoiceAndDate(invoiceSP2, trxDate, mt940Data, "MT940");
					}else if (!mxMap.isEmpty()){
						log.info("dataSource SERTIFIKASI: MXData - "+invoiceSP2);
						sertifikasi = sertifikasiDao.findBillingByInvoiceAndDate(invoiceSP2, trxDate, mxDataAbnormal, "MXData");
					}
					resultSertifikasi.put(invoice, sertifikasi);
				}
			}
			
			resultMx = transactionLogDao.findAllTransactionLogsWebadminReconcile(
					channel, resultSertifikasi.keySet(), clientId, transactionCode, new String[] { "00" }, new String[] { "00" }, trxDate);
			
			log.info("resultSertifikasi : " + resultSertifikasi);
			log.info("resultMx : " + resultMx);
			for (String keys : resultSertifikasi.keySet()) {
				no++;
				ReconcileDto dto 	= new ReconcileDto();
				mxData 				= resultMx.get(keys);
				sertifikasiRecon	= resultSertifikasi.get(keys);
				
				if(invoiceNo.size()>0 && !mt940Map.isEmpty()){
					mt940Data = mt940Map.get(keys);
				}
				
				if (reconcileStatus.equalsIgnoreCase("unsettled")
						|| reconcileStatus.equalsIgnoreCase("unsettled/need confirmation")
						|| reconcileStatus.equalsIgnoreCase("all")) {
					
					if (mt940Map.keySet().contains(keys) && sertifikasiRecon == null) { //MT940 ada, CORE ga ada
						String paymentAmount 			= mt940Data[7] != null ? mt940Data[7].toString() : "0";
						String invoiceMt940Status 		= "Paid";
						String invoiceReconcileStatus 	= "Unsettled";
						dto = saveToReconcileDto(
								no, keys, mxData, sertifikasiRecon, mt940Data, channel, invoiceMt940Status, "Unpaid", 
								invoiceReconcileStatus, transactionCode);
						amountNotSettled = amountNotSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						notSettled++;
					}else if (!mt940Map.keySet().contains(keys) && mxData != null) { // MT940 ga ada, MX ada
						String paymentAmount 			= (mxData!=null && mxData[15] != null) ? mxData[15].toString() : "0";
						String invoiceMt940Status 		= "Unpaid";
						String invoiceReconcileStatus 	= "Unsettled";
						dto = saveToReconcileDto(
								no, keys, mxData, sertifikasiRecon, mt940Data, channel, invoiceMt940Status, "Paid", 
								invoiceReconcileStatus, transactionCode);
						amountNotSettled = amountNotSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						notSettled++;
					}					
				}
				
				if (reconcileStatus.equalsIgnoreCase("settled")
						|| reconcileStatus.equalsIgnoreCase("all")) {

//					MT940 ada, CORE ada
					if (mt940Map.keySet().contains(keys) && sertifikasiRecon != null) {
						String paymentAmount 			= mt940Data[7] != null ? mt940Data[7].toString() : "0";
						String invoiceMt940Status 		= "Paid";
						String invoiceReconcileStatus 	= "Settled";
						dto = saveToReconcileDto(
								no, keys, mxData, sertifikasiRecon, mt940Data, channel, invoiceMt940Status, 
								"Paid", invoiceReconcileStatus,  transactionCode);
						amountSettled = amountSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						settled++;
					}
				}
			}
			// Sort By Time
			try {
				result.addAll(sortingDataByTransactionTime(resultTemp));
			} catch (Exception e) {
				e.printStackTrace();
			}
			resultTemp.clear();
			
			totalAmount = amountSettled + amountNotSettled + amountUnconfirmed;

			mapCount.put("settled", mapCount.get("settled") + settled);
			mapCount.put("notSettled", mapCount.get("notSettled") + notSettled);
			mapCount.put("unconfirmed", mapCount.get("unconfirmed") + unconfirmed);
			mapCountAmount.put("amountSettled", mapCountAmount.get("amountSettled") + amountSettled);
			mapCountAmount.put("amountNotSettled", mapCountAmount.get("amountNotSettled") + amountNotSettled);
			mapCountAmount.put("amountUnconfirmed", mapCountAmount.get("amountUnconfirmed") + amountUnconfirmed);
			mapCountAmount.put("totalAmount", mapCountAmount.get("totalAmount") + totalAmount);
//			mapCountAmount.put("totalAmountDenda", mapCountAmount.get("totalAmountDenda") + totalAmountDenda);
		}

		//IKRAP-60
		if(transactionCode.equals(Constants.EndpointCode.IKRAP_CODE)){
			Object[] ikrap 			= null;
			Object[] ikrapRecon 	= null;
			Object[] mt940Data 		= null;
			Object[] mxData			= null;
			Object[] mxDataAbnormal	= null;
				
			if(invoiceNo.size()>0 && !mt940Map.isEmpty()){
				for (String invoice : invoiceNo) {
					mt940Data 	= mt940Map.get(invoice);
					ikrap 		= ikrapDao.findBillingByInvoiceAndDate(invoice, trxDate, mt940Data);
					resultIkrap.put(invoice, ikrap);
				}
			}else if(invoiceNo.size()>0 && !mxMap.isEmpty()){
				for (String invoice : invoiceNo) {
					mxDataAbnormal 	= mxMap.get(invoice);
					ikrap 		= ikrapDao.findBillingByInvoiceAndDate(invoice, trxDate, mt940Data);
					resultIkrap.put(invoice, ikrap);
				}
			}
			
			resultMx = transactionLogDao.findAllTransactionLogsWebadminReconcile(
					channel, resultIkrap.keySet(), clientId, transactionCode, 
					new String[] { "00" }, new String[] { "00" }, trxDate);
			 
			for (String keys : resultIkrap.keySet()) {
				no++;
				ReconcileDto dto 	= new ReconcileDto();
				mxData				= resultMx.get(keys);
				ikrapRecon			= resultIkrap.get(keys);
				
				if(invoiceNo.size()>0 && !mt940Map.isEmpty()){
					mt940Data = mt940Map.get(keys);
				}
				
				if (reconcileStatus.equalsIgnoreCase("unsettled")
						|| reconcileStatus.equalsIgnoreCase("unsettled/need confirmation")
						|| reconcileStatus.equalsIgnoreCase("all")) {
							
					if (mt940Map.keySet().contains(keys) && ikrapRecon == null) { //MT940 ada, CORE ga ada
						String paymentAmount 			= mt940Data[7] != null ? mt940Data[7].toString() : "0";
						String invoiceMt940Status 		= "Paid";
						String invoiceReconcileStatus 	= "Unsettled";
						dto = saveToReconcileDto(
								no, keys, mxData, ikrapRecon, mt940Data, channel, invoiceMt940Status, "Unpaid", 
								invoiceReconcileStatus, transactionCode);
						amountNotSettled = amountNotSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						notSettled++;
					}else if (!mt940Map.keySet().contains(keys) && mxMap != null) { //MT940 ga ada, MX ada
						String paymentAmount 			= (mxData!=null && mxData[15] != null) ? mxData[15].toString() : "0";
						String invoiceMt940Status 		= "Unpaid";
						String invoiceReconcileStatus 	= "Unsettled";
						dto = saveToReconcileDto(
								no, keys, mxData, ikrapRecon, mt940Data, channel, invoiceMt940Status, "Paid", 
								invoiceReconcileStatus, transactionCode);
						amountNotSettled = amountNotSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						notSettled++;
					}
				}
				
				if (reconcileStatus.equalsIgnoreCase("settled")
						|| reconcileStatus.equalsIgnoreCase("all")) {
//					MT940 ada, CORE ada
					if (mt940Map.keySet().contains(keys) && ikrapRecon != null) {
						String paymentAmount 			= mt940Data[7] != null ? mt940Data[7].toString() : "0";
						String invoiceMt940Status 		= "Paid";
						String invoiceReconcileStatus 	= "Settled";
						dto = saveToReconcileDto(
								no, keys, mxData, ikrapRecon, mt940Data, channel, invoiceMt940Status, "Paid", 
								invoiceReconcileStatus, transactionCode);
						amountSettled = amountSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						settled++;
					}
				}
			}
			// Sort By Time
			try {
				result.addAll(sortingDataByTransactionTime(resultTemp));
			} catch (Exception e) {
				e.printStackTrace();
			}
			resultTemp.clear();
			
			totalAmount = (amountSettled + amountNotSettled + amountUnconfirmed);
			mapCountAmount.put	("totalAmount", mapCountAmount.get("totalAmount") + totalAmount);
			mapCount.put		("settled", mapCount.get("settled") + settled);
			mapCount.put		("notSettled", mapCount.get("notSettled") + notSettled);
			mapCount.put		("unconfirmed", mapCount.get("unconfirmed") + unconfirmed);
			mapCountAmount.put	("amountSettled", mapCountAmount.get("amountSettled") + amountSettled);
			mapCountAmount.put	("amountNotSettled", mapCountAmount.get("amountNotSettled") + amountNotSettled);
			mapCountAmount.put	("amountUnconfirmed", mapCountAmount.get("amountUnconfirmed") + amountUnconfirmed);
		}
		
		//IAR-50
		if(transactionCode.equals(Constants.EndpointCode.IAR_CODE)){
			Object[] iar 			= null;
			Object[] iarRecon 		= null;
			Object[] mt940Data 		= null;
			Object[] mxData			= null;
			Object[] mxDataAbnormal	= null;
				
			if(invoiceNo.size()>0 && !mt940Map.isEmpty()){
				for (String invoice : invoiceNo) {
					mt940Data 	= mt940Map.get(invoice);
					iar 		= iarDao.findBillingByInvoiceAndDate(invoice, trxDate, mt940Data);
					resultIar.put(invoice, iar);
				}
			}else if(invoiceNo.size()>0 && !mxMap.isEmpty()){
				for (String invoice : invoiceNo) {
					mxDataAbnormal 	= mxMap.get(invoice);
					iar 		= iarDao.findBillingByInvoiceAndDate(invoice, trxDate, mt940Data);
					resultIar.put(invoice, iar);
				}
			}
			
			resultMx = transactionLogDao.findAllTransactionLogsWebadminReconcile(
					channel, resultIar.keySet(), clientId, transactionCode, 
					new String[] { "00" }, new String[] { "00" }, trxDate);
			 
			for (String keys : resultIar.keySet()) {
				no++;
				ReconcileDto dto 	= new ReconcileDto();
				mxData				= resultMx.get(keys);
				iarRecon			= resultIar.get(keys);
				
				if(invoiceNo.size()>0 && !mt940Map.isEmpty()){
					mt940Data = mt940Map.get(keys);
				}
				
				if (reconcileStatus.equalsIgnoreCase("unsettled")
						|| reconcileStatus.equalsIgnoreCase("unsettled/need confirmation")
						|| reconcileStatus.equalsIgnoreCase("all")) {
							
					if (mt940Map.keySet().contains(keys) && iarRecon == null) { //MT940 ada, CORE ga ada
						String paymentAmount 			= mt940Data[7] != null ? mt940Data[7].toString() : "0";
						String invoiceMt940Status 		= "Paid";
						String invoiceReconcileStatus 	= "Unsettled";
						dto = saveToReconcileDto(
								no, keys, mxData, iarRecon, mt940Data, channel, invoiceMt940Status, "Unpaid", 
								invoiceReconcileStatus, transactionCode);
						amountNotSettled = amountNotSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						notSettled++;
					}else if (!mt940Map.keySet().contains(keys) && mxMap != null) { //MT940 ga ada, MX ada
						String paymentAmount 			= (mxData!=null && mxData[15] != null) ? mxData[15].toString() : "0";
						String invoiceMt940Status 		= "Unpaid";
						String invoiceReconcileStatus 	= "Unsettled";
						dto = saveToReconcileDto(
								no, keys, mxData, iarRecon, mt940Data, channel, invoiceMt940Status, "Paid", 
								invoiceReconcileStatus, transactionCode);
						amountNotSettled = amountNotSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						notSettled++;
					}
				}
				
				if (reconcileStatus.equalsIgnoreCase("settled")
						|| reconcileStatus.equalsIgnoreCase("all")) {
//					MT940 ada, CORE ada
					if (mt940Map.keySet().contains(keys) && iarRecon != null) {
						String paymentAmount 			= mt940Data[7] != null ? mt940Data[7].toString() : "0";
						String invoiceMt940Status 		= "Paid";
						String invoiceReconcileStatus 	= "Settled";
						dto = saveToReconcileDto(
								no, keys, mxData, iarRecon, mt940Data, channel, invoiceMt940Status, "Paid", 
								invoiceReconcileStatus, transactionCode);
						amountSettled = amountSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						settled++;
					}
				}
			}
			// Sort By Time
			try {
				result.addAll(sortingDataByTransactionTime(resultTemp));
			} catch (Exception e) {
				e.printStackTrace();
			}
			resultTemp.clear();
			
			totalAmount = (amountSettled + amountNotSettled + amountUnconfirmed);
			mapCountAmount.put	("totalAmount", mapCountAmount.get("totalAmount") + totalAmount);
			mapCount.put		("settled", mapCount.get("settled") + settled);
			mapCount.put		("notSettled", mapCount.get("notSettled") + notSettled);
			mapCount.put		("unconfirmed", mapCount.get("unconfirmed") + unconfirmed);
			mapCountAmount.put	("amountSettled", mapCountAmount.get("amountSettled") + amountSettled);
			mapCountAmount.put	("amountNotSettled", mapCountAmount.get("amountNotSettled") + amountNotSettled);
			mapCountAmount.put	("amountUnconfirmed", mapCountAmount.get("amountUnconfirmed") + amountUnconfirmed);
		}
		
		//UNAR-40
		if(transactionCode.equals(Constants.EndpointCode.UNAR_CODE)){
			Object[] unar 			= null;
			Object[] unarRecon 		= null;
			Object[] mt940Data 		= null;
			Object[] mxData			= null;
			Object[] mxDataAbnormal	= null;
				
			if(invoiceNo.size()>0 && !mt940Map.isEmpty()){
				for (String invoice : invoiceNo) {
					mt940Data 	= mt940Map.get(invoice);
					unar 		= unarDao.findBillingByInvoiceAndDate(invoice, trxDate, mt940Data);
					resultUnar.put(invoice, unar);
				}
			}else if(invoiceNo.size()>0 && !mxMap.isEmpty()){
				for (String invoice : invoiceNo) {
					mxDataAbnormal 	= mxMap.get(invoice);
					unar 		= unarDao.findBillingByInvoiceAndDate(invoice, trxDate, mt940Data);
					resultUnar.put(invoice, unar);
				}
			}
			
			resultMx = transactionLogDao.findAllTransactionLogsWebadminReconcile(
					channel, resultUnar.keySet(), clientId, transactionCode, 
					new String[] { "00" }, new String[] { "00" }, trxDate);
			 
			for (String keys : resultUnar.keySet()) {
				no++;
				ReconcileDto dto 	= new ReconcileDto();
				mxData				= resultMx.get(keys);
				unarRecon			= resultUnar.get(keys);
				
				if(invoiceNo.size()>0 && !mt940Map.isEmpty()){
					mt940Data = mt940Map.get(keys);
				}
				
				if (reconcileStatus.equalsIgnoreCase("unsettled")
						|| reconcileStatus.equalsIgnoreCase("unsettled/need confirmation")
						|| reconcileStatus.equalsIgnoreCase("all")) {
							
					if (mt940Map.keySet().contains(keys) && unarRecon == null) { //MT940 ada, CORE ga ada
						String paymentAmount 			= mt940Data[7] != null ? mt940Data[7].toString() : "0";
						String invoiceMt940Status 		= "Paid";
						String invoiceReconcileStatus 	= "Unsettled";
						dto = saveToReconcileDto(
								no, keys, mxData, unarRecon, mt940Data, channel, invoiceMt940Status, "Unpaid", 
								invoiceReconcileStatus, transactionCode);
						amountNotSettled = amountNotSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						notSettled++;
					}else if (!mt940Map.keySet().contains(keys) && mxMap != null) { //MT940 ga ada, MX ada
						String paymentAmount 			= (mxData!=null && mxData[15] != null) ? mxData[15].toString() : "0";
						String invoiceMt940Status 		= "Unpaid";
						String invoiceReconcileStatus 	= "Unsettled";
						dto = saveToReconcileDto(
								no, keys, mxData, unarRecon, mt940Data, channel, invoiceMt940Status, "Paid", 
								invoiceReconcileStatus, transactionCode);
						amountNotSettled = amountNotSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						notSettled++;
					}
				}
				
				if (reconcileStatus.equalsIgnoreCase("settled")
						|| reconcileStatus.equalsIgnoreCase("all")) {
//					MT940 ada, CORE ada
					if (mt940Map.keySet().contains(keys) && unarRecon != null) {
						String paymentAmount 			= mt940Data[7] != null ? mt940Data[7].toString() : "0";
						String invoiceMt940Status 		= "Paid";
						String invoiceReconcileStatus 	= "Settled";
						dto = saveToReconcileDto(
								no, keys, mxData, unarRecon, mt940Data, channel, invoiceMt940Status, "Paid", 
								invoiceReconcileStatus, transactionCode);
						amountSettled = amountSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						settled++;
					}
				}
			}
			// Sort By Time
			try {
				result.addAll(sortingDataByTransactionTime(resultTemp));
			} catch (Exception e) {
				e.printStackTrace();
			}
			resultTemp.clear();
			
			totalAmount = (amountSettled + amountNotSettled + amountUnconfirmed);
			mapCountAmount.put	("totalAmount", mapCountAmount.get("totalAmount") + totalAmount);
			mapCount.put		("settled", mapCount.get("settled") + settled);
			mapCount.put		("notSettled", mapCount.get("notSettled") + notSettled);
			mapCount.put		("unconfirmed", mapCount.get("unconfirmed") + unconfirmed);
			mapCountAmount.put	("amountSettled", mapCountAmount.get("amountSettled") + amountSettled);
			mapCountAmount.put	("amountNotSettled", mapCountAmount.get("amountNotSettled") + amountNotSettled);
			mapCountAmount.put	("amountUnconfirmed", mapCountAmount.get("amountUnconfirmed") + amountUnconfirmed);
		}
		
		//REOR-20
		if(transactionCode.equals(Constants.EndpointCode.REOR_CODE)){
			Object[] reor 			= null;
			Object[] reorRecon 		= null;
			Object[] mt940Data 		= null;
			Object[] mxData			= null;
			Object[] mxDataAbnormal	= null;
			
			log.info("invoiceNo : " + LogHelper.toString(invoiceNo));
			
			if(invoiceNo.size()>0 && !mt940Map.isEmpty()){
				for (String invoice : invoiceNo) {
					mt940Data 	= mt940Map.get(invoice);
					reor 		= reorDao.findBillingByInvoiceAndDate(invoice, trxDate, mt940Data);
					resultReor.put(invoice, reor);
				}
			}else if(invoiceNo.size()>0 && !mxMap.isEmpty()){
				for (String invoice : invoiceNo) {
					mxDataAbnormal 	= mxMap.get(invoice);
					reor 		= reorDao.findBillingByInvoiceAndDate(invoice, trxDate, mt940Data);
					resultReor.put(invoice, reor);
				}
			}
			
			resultMx = transactionLogDao.findAllTransactionLogsWebadminReconcile(
					channel, resultReor.keySet(), clientId, transactionCode, 
					new String[] { "00" }, new String[] { "00" }, trxDate);
			 
			for (String keys : resultReor.keySet()) {
				no++;
				ReconcileDto dto 	= new ReconcileDto();
				mxData				= resultMx.get(keys);
				reorRecon			= resultReor.get(keys);
				
				if(invoiceNo.size()>0 && !mt940Map.isEmpty()){
					mt940Data = mt940Map.get(keys);
				}
				
				log.info("mxData : " + LogHelper.toString(mxData));
				log.info("resultReor : " + LogHelper.toString(reorRecon));
				log.info("mt940Map : " + LogHelper.toString(mt940Map));
				
				if (reconcileStatus.equalsIgnoreCase("unsettled")
						|| reconcileStatus.equalsIgnoreCase("unsettled/need confirmation")
						|| reconcileStatus.equalsIgnoreCase("all")) {
							
					if (mt940Map.keySet().contains(keys) && reorRecon == null) { //MT940 ada, CORE ga ada
						String paymentAmount 			= mt940Data[7] != null ? mt940Data[7].toString() : "0";
						String invoiceMt940Status 		= "Paid";
						String invoiceReconcileStatus 	= "Unsettled";
						dto = saveToReconcileDto(
								no, keys, mxData, reorRecon, mt940Data, channel, invoiceMt940Status, "Unpaid", 
								invoiceReconcileStatus, transactionCode);
						amountNotSettled = amountNotSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						notSettled++;
					}else if (!mt940Map.keySet().contains(keys) && mxMap != null) { //MT940 ga ada, MX ada
						String paymentAmount 			= (mxData!=null && mxData[15] != null) ? mxData[15].toString() : "0";
						String invoiceMt940Status 		= "Unpaid";
						String invoiceReconcileStatus 	= "Unsettled";
						dto = saveToReconcileDto(
								no, keys, mxData, reorRecon, mt940Data, channel, invoiceMt940Status, "Paid", 
								invoiceReconcileStatus, transactionCode);
						amountNotSettled = amountNotSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						notSettled++;
					}
				}
				
				if (reconcileStatus.equalsIgnoreCase("settled")
						|| reconcileStatus.equalsIgnoreCase("all")) {
//					MT940 ada, CORE ada
					if (mt940Map.keySet().contains(keys) && reorRecon != null) {
						String paymentAmount 			= mt940Data[7] != null ? mt940Data[7].toString() : "0";
						String invoiceMt940Status 		= "Paid";
						String invoiceReconcileStatus 	= "Settled";
						dto = saveToReconcileDto(
								no, keys, mxData, reorRecon, mt940Data, channel, invoiceMt940Status, "Paid", 
								invoiceReconcileStatus, transactionCode);
						amountSettled = amountSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						settled++;
					}
				}
			}
			// Sort By Time
			try {
				result.addAll(sortingDataByTransactionTime(resultTemp));
			} catch (Exception e) {
				e.printStackTrace();
			}
			resultTemp.clear();
			
			totalAmount = (amountSettled + amountNotSettled + amountUnconfirmed);
			mapCountAmount.put	("totalAmount", mapCountAmount.get("totalAmount") + totalAmount);
			mapCount.put		("settled", mapCount.get("settled") + settled);
			mapCount.put		("notSettled", mapCount.get("notSettled") + notSettled);
			mapCount.put		("unconfirmed", mapCount.get("unconfirmed") + unconfirmed);
			mapCountAmount.put	("amountSettled", mapCountAmount.get("amountSettled") + amountSettled);
			mapCountAmount.put	("amountNotSettled", mapCountAmount.get("amountNotSettled") + amountNotSettled);
			mapCountAmount.put	("amountUnconfirmed", mapCountAmount.get("amountUnconfirmed") + amountUnconfirmed);
		}
		
		//BILLING-10
		if(transactionCode.equals(Constants.EndpointCode.BHP_CODE)){
			Object[] billingRecon	= null;
			Object[] mxData			= null;
			Object[] mt940Data 		= null;
			
			resultBilling 	= billingDao.findAllBillingByInvoiceNoAndDate(invoiceNo, trxDate);
			resultMx 		= transactionLogDao.findAllTransactionLogsWebadminReconcile(
									channel, resultBilling.keySet(), clientId, transactionCode, new String[] { "00" }, new String[] { "00" }, trxDate);
			 
			for (String keys : resultBilling.keySet()) {
				no++;
				ReconcileDto dto 	= new ReconcileDto();
				mxData				= resultMx.get(keys);
				billingRecon		= resultBilling.get(keys);
				
				if(invoiceNo.size()>0 && !mt940Map.isEmpty()){
					for (String invoice : invoiceNo) {
						mt940Data = mt940Map.get(invoice);
					}
				}

				if (reconcileStatus.equalsIgnoreCase("unsettled")
						|| reconcileStatus.equalsIgnoreCase("unsettled/need confirmation")
						|| reconcileStatus.equalsIgnoreCase("all")) {
					
					if (mt940Map.keySet().contains(keys) && billingRecon == null) { //MT940 ada, CORE ga ada
						String paymentAmount 			= mt940Data[7] != null ? mt940Data[7].toString() : "0";
						String invoiceMt940Status 		= "Paid";
						String invoiceReconcileStatus 	= "Unsettled";
						dto = saveToReconcileDto(
								no, keys, mxData, billingRecon, mt940Data, channel, invoiceMt940Status, "Unpaid", 
								invoiceReconcileStatus, transactionCode);
						amountNotSettled = amountNotSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						notSettled++;
					}else if (!mt940Map.keySet().contains(keys) && mxData != null) { // MT940 ga ada, MX ada
						String paymentAmount 			= (mxData!=null && mxData[15] != null) ? mxData[15].toString() : "0";
						String invoiceMt940Status 		= "Unpaid";
						String invoiceReconcileStatus 	= "Unsettled";
						dto = saveToReconcileDto(
								no, keys, mxData, billingRecon, mt940Data, channel, invoiceMt940Status, "Paid", 
								invoiceReconcileStatus, transactionCode);
						amountNotSettled = amountNotSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						notSettled++;
					}
				}
				
				if (reconcileStatus.equalsIgnoreCase("settled")
						|| reconcileStatus.equalsIgnoreCase("all")) {

//					MT940 ada, CORE ada
					if (mt940Map.keySet().contains(keys) && billingRecon != null) {
						String paymentAmount 			= mt940Data[7] != null ? mt940Data[7].toString() : "0";
						String invoiceMt940Status 		= "Paid";
						String invoiceReconcileStatus 	= "Settled";
						dto = saveToReconcileDto(
								no, keys, mxData, billingRecon, mt940Data, channel, invoiceMt940Status, "Paid", 
								invoiceReconcileStatus, transactionCode);
						amountSettled = amountSettled + Double.valueOf(paymentAmount).longValue();
						resultTemp.add(dto);
						settled++;
					}
				}
				// Sort By Time
				try {
					result.addAll(sortingDataByTransactionTime(resultTemp));
//					sortingDataByTransactionTime(resultTemp);
				} catch (Exception e) {
					e.printStackTrace();
				}
//				result.addAll(resultTemp);
				resultTemp.clear();

				totalAmount = (amountSettled + amountNotSettled + amountUnconfirmed);

				mapCount.put("settled", mapCount.get("settled") + settled);
				mapCount.put("notSettled", mapCount.get("notSettled") + notSettled);
				mapCount.put("unconfirmed", mapCount.get("unconfirmed") + unconfirmed);
				mapCountAmount.put("amountSettled", mapCountAmount.get("amountSettled") + amountSettled);
				mapCountAmount.put("amountNotSettled", mapCountAmount.get("amountNotSettled") + amountNotSettled);
				mapCountAmount.put("amountUnconfirmed", mapCountAmount.get("amountUnconfirmed") + amountUnconfirmed);
				mapCountAmount.put("totalAmount", mapCountAmount.get("totalAmount") + totalAmount);
//				mapCountAmount.put("totalAmountDenda", mapCountAmount.get("totalAmountDenda") + totalAmountDenda);

			}
		}
		return result;
	}

	public String checkIfNull(Object obj) {
		String str = null;
		if (obj == null) {
			str = "";
		} else {
			str = obj.toString();
		}

		return str;
	}

	@Override
	public List<AnalisaTransactionLogsDto> findTransactionsPlusRcWithMap(String billerCode, String txCode,
			String klienID, Set<String> invoiceNo, Date startDate, Date endDate, EndpointRcs billerRc,
			String channelCode, String respCodeChannel) {
		
		List<Object> queryResultMx = (List<Object>) transactionLogDao.findTransactionsPlusRcWithMap(billerCode, txCode,
				klienID, invoiceNo, startDate, endDate, billerRc, channelCode, respCodeChannel);

		result = new ArrayList<AnalisaTransactionLogsDto>();
		int i = 1;
		for (Object o : queryResultMx) {

			Object[] row = (Object[]) o;
			AnalisaTransactionLogsDto dto = new AnalisaTransactionLogsDto();
			dto.setTrxId((String) row[0]);
			dto.setTrxTime((String) row[1]);
			dto.setTrxDesc((String) row[3]);
			dto.setBillerRcDesc((String) row[10]);
			dto.setChannelRcDesc((String) row[6]);

			dto.setClientId((String) row[13]);
			dto.setNameKlien((String) row[14]);
			dto.setInvoiceNumber((String) row[12]);

			String channelId = (String) row[7];
			// untuk membedakan deskripsi channel id,
			String channelCd = (String) row[4];
			if (channelId != null && channelCd != null) {
				if (channelCd.equalsIgnoreCase("chws")) {
					dto.setChannelId(systemParameterDao.findParamValueByParamName(channelId));
				} else if (channelCd.equalsIgnoreCase("chws2")) {
					SystemParameter systemParameter = systemParameterDao.findByParamName("transactionLogs.channelId.bni." + channelId);
					if (systemParameter != null) {
						dto.setChannelId(systemParameter.getParamValue());
					} else {
						dto.setChannelId(channelId);
					}
				} else if (channelCd.equalsIgnoreCase("chws3")) {
					SystemParameter systemParameter = systemParameterDao.findByParamName("transactionLogs.channelId.bri." + channelId);
					if (systemParameter != null) {
						dto.setChannelId(systemParameter.getParamValue());
					} else {
						dto.setChannelId(channelId);
					}
				}
			}

			// buat nomor urut, 28-09-2015
			dto.setNo(i);
			
			// 20180823 - hendy - untuk akomodir tampilan nominal pada halaman inquiry transaction log webadmin
			if(row[2].toString().contains(".")){
				String[] trxType = row[2].toString().split("\\.");
				if(trxType[1].equalsIgnoreCase("PAY")){
					if(row[16].toString().contains(".")){
						String[] invoiceAmount = row[16].toString().split("\\.");
						dto.setInvoiceAmount(invoiceAmount[0]);
					}else{
						dto.setInvoiceAmount(row[16].toString());
					}
				}else if(trxType[1].equalsIgnoreCase("INQ")){
					if(row[17].toString().contains(".")){
						String[] invoiceAmountInq = row[17].toString().split("\\.");
						dto.setInvoiceAmount(invoiceAmountInq[0]);
					}else{
						dto.setInvoiceAmount(row[17].toString());
					}
				}else{
					dto.setInvoiceAmount("0");
				}
			}

			// for handle biller_code emptry and response biller code empty
			String transactionType = (String) row[15];
			String prefixTransactionCode = Constants.TransactionType.transactionType.get(transactionType.replace("\r",
					""));

			if (prefixTransactionCode != null) {
				if (billerCode != null && !billerCode.equalsIgnoreCase("all")) {
					if (billerCode.startsWith(prefixTransactionCode.toLowerCase())) {
						result.add(dto);
						i++;
					}
				} else {
					result.add(dto);
					i++;
				}
			}

		}

		return result;
	}

	@Override
	public List<AnalisaTransactionLogsDto> findTransactionsOrderByTimeAndInvoiceNo(String billerCode, String txCode,
			String klienID, Set<String> invoiceNo, Date startDate, Date endDate, EndpointRcs billerRc,
			String channelCode, String respCodeChannel) {
		
		
		List<Object> queryResultMx = (List<Object>) transactionLogDao.findTransactionsOrderByTimeAndInvoiceNo(
				billerCode, txCode, klienID, invoiceNo, startDate, endDate, billerRc, channelCode, respCodeChannel);

		result = new ArrayList<AnalisaTransactionLogsDto>();
		int i = 1;
		for (Object o : queryResultMx) {

			Object[] row = (Object[]) o;
			AnalisaTransactionLogsDto dto = new AnalisaTransactionLogsDto();
			dto.setTrxId((String) row[0]);
			dto.setTrxTime((String) row[1]);
			dto.setTrxDesc((String) row[3]);
			dto.setBillerRcDesc((String) row[10]);
			dto.setChannelRcDesc((String) row[6]);

			dto.setClientId((String) row[13]);
			dto.setNameKlien((String) row[14]);
			dto.setInvoiceNumber((String) row[12]);

			String channelId = (String) row[7];
			// untuk membedakan deskripsi channel id,
			String channelCd = (String) row[4];
			if (channelId != null && channelCd != null) {
				if (channelCd.equalsIgnoreCase("chws")) {
					dto.setChannelId(systemParameterDao.findParamValueByParamName(channelId));
				} else if (channelCd.equalsIgnoreCase("chws2")) {
					com.balicamp.model.parameter.SystemParameter systemParameter = systemParameterDao
							.findByParamName("transactionLogs.channelId.bni." + channelId);
					if (systemParameter != null) {
						dto.setChannelId(systemParameter.getParamValue());
					} else {
						dto.setChannelId(channelId);
					}
				}
			}

			// buat nomor urut, 28-09-2015
			dto.setNo(i);
			i++;

			result.add(dto);

		}

		return result;
	}
	
	private ReconcileDto saveToReconcileDto(int no, String invoiceNo, Object[] mxData, Object[] coreData, Object[] mt940Data, 
			String channelCode,  String mt940Status, String coreStatus, String reconcileStatus, String transactionCode) {
		
		log.info("mxData : " + LogHelper.toString(mxData));
		log.info("coreData : " + LogHelper.toString(coreData));
		log.info("mt940Data : " + LogHelper.toString(mt940Data));
		
		ReconcileDto dto 		= new ReconcileDto();
		DecimalFormat numFormat = new DecimalFormat("Rp #,###,###");

		dto.setInvoiceNo(invoiceNo);

//  	MX guide:
//  	0 tlw.TRANSACTION_ID, 	1  tlw.client_id, 	2  tlw.client_name, 3  tlw.invoice_no, 			4 t.name as transaction_name, 
//  	5 tlw.channel_id, 		6  tlw.biller_rc, 	7  tlw.channel_rc, 	8  tlw.TRANSACTION_TIME, 	9 ep.name as bank_name, 
//  	10 channel_response, 	11 biller_response, 12 tlw.biller_code, 13 tlw.recon_flag, 			14 tlw.channel_code "				
		if (mxData != null) {
			dto.setTrxId			(mxData[0] == null || mxData[0].equals("") ? "-" : mxData[0].toString());
			dto.setTransactionName	(mxData[4] == null || mxData[4].equals("") ? "-" : mxData[4].toString());
			if (mxData[5] != null && !mxData[5].equals("")) {
				dto.setDelivChannel(mxData[5].toString());
				if(channelCode.equalsIgnoreCase("chws")){
					dto.setPaymentChannel(
							systemParameterDao.findParamValueByParamName(mxData[5].toString()));
				}else if(channelCode.equalsIgnoreCase("chws2")){
					dto.setPaymentChannel(
							systemParameterDao.findParamValueByParamName("transactionLogs.channelId.bni." + mxData[5].toString()));
				}else if(channelCode.equalsIgnoreCase("chws3")){
					dto.setPaymentChannel(
							systemParameterDao.findParamValueByParamName("transactionLogs.channelId.bri." + mxData[5].toString()));
				}else{
					dto.setPaymentChannel("-");
				}
				
				if(dto.getPaymentChannel() == null) {
					dto.setPaymentChannel("Unknown");
				}
			}
			dto.setTransactionTime	(mxData[8] == null  || mxData[8].equals("")  ? "-" : mxData[8].toString());
			dto.setBankName			(mxData[9] == null  || mxData[9].equals("")  ? "-" : mxData[9].toString().toUpperCase());					
			dto.setBillerRc			(mxData[10] == null || mxData[10].equals("") ? "-" : mxData[10].toString());
			dto.setChannelRc		(mxData[11] == null || mxData[11].equals("") ? "-" : mxData[11].toString());
			dto.setReconFlag		(mxData[13] == null || mxData[13].equals("") ? "-" : mxData[13].toString());
		} else {
			dto.setTrxId("-");
			dto.setInvoiceNo(invoiceNo);
			dto.setTransactionName("-");
			dto.setDelivChannel("-");
			dto.setBillerRc("-");
			dto.setChannelRc("-");
			dto.setTransactionTime("-");
			dto.setPaymentChannel("-");
			dto.setReconFlag("-");
			dto.setBankName("-");
			dto.setBankBranch("-");
		}

		log.info("dto 1 : " + dto.toString());
		
//		MT940 guide:
//		0 - filename		1 - parse date			2 - trx date			3 - client id		
//		4 - invoice id		5 - payment channel		6 - branch code			7 - trx amount			
//		8 - error desc		9 - bank name		
		if(mt940Data != null){
			if (dto.getBankName().equalsIgnoreCase("-")){
				dto.setBankName(mt940Data[9] == null  || mt940Data[9].equals("")  ? "-" : mt940Data[9].toString().toUpperCase());
			}
			
			if (dto.getPaymentChannel().equalsIgnoreCase("-")){
				dto.setDelivChannel		(mt940Data[5] == null  || mt940Data[5].equals("")  ? "-" : mt940Data[5].toString());
				dto.setPaymentChannel	(mt940Data[5] == null  || mt940Data[5].equals("")  ? "-" : mt940Data[5].toString());
			}
			dto.setBankBranch		(mt940Data[6]  == null || mt940Data[6].equals("")  ? "-" : mt940Data[6].toString());
			dto.setPaymentDateSims	(mt940Data[2]  != null ?  mt940Data[2].toString() : "-");
			dto.setRawData			(mt940Data[10] != null ?  mt940Data[10].toString() : "-");
			dto.setTrxAmount		(mt940Data[7]  != null ?  numFormat.format(mt940Data[7]) : "-");
		}
		
		dto.setNo(no);
		dto.setMt940Status(mt940Status);
		dto.setSimsStatus(coreStatus);
		dto.setReconcileStatus(reconcileStatus);

		log.info("dto 2 : " + dto.toString());
		
//		Pengujian guide:
//		0 Invoice No H2H,	1 TglBayar, 	2 JmlBayar, 	3 CustName, 	4 CustID,		5 SP2/Remarks
		if(transactionCode.equals(Constants.EndpointCode.PAP_CODE) && coreData != null){
			dto.setClientId			(coreData[4]  != null ? coreData[4].toString() : "-");
			dto.setClientName		(coreData[3]  != null ? coreData[3].toString() : "-");
			dto.setRemarks			(coreData[5] != null ? coreData[5].toString() : "-");
			dto.setPaymentDateSims	(dto.getPaymentDateSims() 	== null && coreData[1]  != null ? coreData[1].toString() : "-");
			dto.setTrxAmount		(dto.getTrxAmount() 		== null && coreData[2]  != null ? numFormat.format(coreData[2]) : "-");
		}
		
//		Sertifikasi guide:
//		0. SP2_NO		1. CUST_ID		2. CUST_NAME
//		3. TGL_BYR		4. EXP_DATE		5. JML_BYR
		if(transactionCode.equals(Constants.EndpointCode.PERANGKAT_CODE) && coreData != null){
			dto.setClientId			(coreData[1] != null ? coreData[1].toString() : "-");
			dto.setClientName		(coreData[2] != null ? coreData[2].toString() : "-");
			dto.setPaymentDateSims	(coreData[3] != null ? coreData[3].toString() : "-");
			dto.setTrxAmount		(coreData[5] != null ? numFormat.format(coreData[5]) : "-");
			dto.setRemarks			(coreData[0] != null ? coreData[0].toString() : "-");
			dto.setInvoiceDueDate	(coreData[4] != null ? coreData[4].toString() : "-");
		}
		
//		Ikrap guide:
//		0. InvoiceNumber	1. IdRegistrant		2. DueDate
//		3. PaymentDate		4. NameRegistrant	5. InvoiceAmount
		if(transactionCode.equals(Constants.EndpointCode.IKRAP_CODE) && coreData != null){
			dto.setClientId			(coreData[1] != null ? coreData[1].toString() : "-");
			dto.setClientName		(coreData[4] != null ? coreData[4].toString() : "-");
			dto.setPaymentDateSims	(coreData[3] != null ? coreData[3].toString() : "-");
			dto.setTrxAmount		(coreData[5] != null ? numFormat.format(coreData[5]) : "-");
			dto.setRemarks			(coreData[0] != null ? coreData[0].toString() : "-");
			dto.setInvoiceDueDate	(coreData[2] != null ? coreData[2].toString() : "-");
		}
		
//		Iar guide:
//		0. InvoiceNumber	1. IdRegistrant		2. DueDate
//		3. PaymentDate		4. NameRegistrant	5. InvoiceAmount
		if(transactionCode.equals(Constants.EndpointCode.IAR_CODE) && coreData != null){
			dto.setClientId			(coreData[1] != null ? coreData[1].toString() : "-");
			dto.setClientName		(coreData[4] != null ? coreData[4].toString() : "-");
			dto.setPaymentDateSims	(coreData[3] != null ? coreData[3].toString() : "-");
			dto.setTrxAmount		(coreData[5] != null ? numFormat.format(coreData[5]) : "-");
			dto.setRemarks			(coreData[0] != null ? coreData[0].toString() : "-");
			dto.setInvoiceDueDate	(coreData[2] != null ? coreData[2].toString() : "-");
		}
		
//		Unar guide:
//		0. InvoiceNumber	1. IdRegistrant		2. DueDate
//		3. PaymentDate		4. NameRegistrant	5. InvoiceAmount
		if(transactionCode.equals(Constants.EndpointCode.UNAR_CODE) && coreData != null){
			dto.setClientId			(coreData[1] != null ? coreData[1].toString() : "-");
			dto.setClientName		(coreData[4] != null ? coreData[4].toString() : "-");
			dto.setPaymentDateSims	(coreData[3] != null ? coreData[3].toString() : "-");
			dto.setTrxAmount		(coreData[5] != null ? numFormat.format(coreData[5]) : "-");
			dto.setRemarks			(coreData[0] != null ? coreData[0].toString() : "-");
			dto.setInvoiceDueDate	(coreData[2] != null ? coreData[2].toString() : "-");
		}		
		
//		Reor guide:
//		0. InvoiceNumber	1. IdRegistrant		2. DueDate
//		3. PaymentDate		4. NameRegistrant	5. InvoiceAmount
		if(transactionCode.equals(Constants.EndpointCode.REOR_CODE) && coreData != null){
			dto.setClientId			(coreData[1] != null ? coreData[1].toString() : "-");
			dto.setClientName		(coreData[4] != null ? coreData[4].toString() : "-");
			dto.setPaymentDateSims	(coreData[3] != null ? coreData[3].toString() : "-");
			dto.setTrxAmount		(coreData[5] != null ? numFormat.format(coreData[5]) : "-");
			dto.setRemarks			(coreData[0] != null ? coreData[0].toString() : "-");
			dto.setInvoiceDueDate	(coreData[2] != null ? coreData[2].toString() : "-");
		}		
		
//		0+ "TO_CHAR(B.BI_MONEY_RECEIVED,'dd-MM-yyyy HH24:MI:SS'), "
//		1+ "B.BI_TYPE, "
//		2+ "TO_CHAR(B.BI_PAY_UNTIL,'dd-MM-yyyy'), "
//		3+ "AD.AD_COMPANY, "
//		4+ "B.BI_REF_NO, "
//		5+ "B.BI_ID_REMINDER_ORIG, "
//		6+ "AD.AD_MAN_NUMBER, "
//		7+ "B.BI_MANUAL, "
//		8+ "B.BI_ID, "
//		9+ "L.NUM_TXT, "
//		10+ "B.BI_COMMENT, "
//		11+ "B.BI_COST_BILL "
		if(transactionCode.equals(Constants.EndpointCode.BHP_CODE) && coreData != null){
			dto.setClientId			(coreData[6] != null ? coreData[6].toString() : "-");
			dto.setClientName		(coreData[3] != null ? coreData[3].toString() : "-");
			dto.setPaymentDateSims	(coreData[0] != null ? coreData[0].toString() : "-");
			dto.setTrxAmount		(coreData[11] != null ? numFormat.format(coreData[11]) : "-");
			dto.setRemarks			(coreData[10] != null ? coreData[10].toString() : "-");
			dto.setInvoiceDueDate	(coreData[2] != null ? coreData[2].toString() : "-");
		}	
		
		return dto;		
	}
	
	private String biTypeToSimsStatus(String biType) {
		String simsStatus = "-";
		Set<String> SimsStatusPaid = new HashSet<String>(
				Arrays.asList(new String[] { "8", "9", "10", "11", "38", "48" }));
		Set<String> SimsStatusCancelled = new HashSet<String>(Arrays.asList(new String[] { "5", "6", "7", "12" }));
		Set<String> SimsStatusBadDebt = new HashSet<String>(Arrays.asList(new String[] { "16" }));

		if (SimsStatusPaid.contains(biType)) {
			simsStatus = "Paid";
		} else if (SimsStatusCancelled.contains(biType)) {
			simsStatus = "Cancelled";
		} else if (SimsStatusBadDebt.contains(biType)) {
			simsStatus = "BadDebt";
		} else {
			simsStatus = "Unpaid";
		}

		return simsStatus;
	}

	private List<ReconcileDto> sortingDataByTransactionTime(List<ReconcileDto> unsortedList) {
		List<ReconcileDto> sortedList = new ArrayList<ReconcileDto>();

		if (unsortedList.isEmpty()) {
			return unsortedList;
		} else {
			ReconcileDto unsortedListArray[] = new ReconcileDto[unsortedList.size()];
			unsortedListArray = unsortedList.toArray(unsortedListArray);
			bubbleSortReconcileDtoList(unsortedListArray);
			sortedList.addAll(Arrays.asList(unsortedListArray));
			return sortedList;
		}
	}

	private void bubbleSortReconcileDtoList(ReconcileDto[] unsortedListArray) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		int n = unsortedListArray.length;
		ReconcileDto temp = new ReconcileDto();

		for (int i = 0; i < n; i++) {
			for (int j = 1; j < (n - i); j++) {

				try {
					if(unsortedListArray[j - 1].getTransactionTime() != null && unsortedListArray[j].getTransactionTime() != null){
						if(!unsortedListArray[j - 1].getTransactionTime().equals("-") && !unsortedListArray[j].getTransactionTime().equals("-")){
							if (sdf.parse(unsortedListArray[j - 1].getTransactionTime()).compareTo(sdf.parse(unsortedListArray[j].getTransactionTime())) > 0) {
								// swap the elements!
								temp 						= unsortedListArray[j - 1];
								unsortedListArray[j - 1] 	= unsortedListArray[j];
								unsortedListArray[j] 		= temp;
							}
						}
					}
				} catch (ParseException e) {
					log.info((new Date() + ", unsortedListArray[j - 1].getPaymentChannel() : " + unsortedListArray[j - 1].getPaymentChannel()));
					log.info((new Date() + ", unsortedListArray[j].getPaymentChannel() : " + unsortedListArray[j].getPaymentChannel()));
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		String phone = "4935082018";
		System.out.println(phone.substring(4,6));
		System.out.println(phone.substring(phone.length()-4));
	}
	

	@Override
	public boolean updateReconFlag(String invoiceNo, String reconFlag, String channelRc, String billerRc, String transactionCode) {
		return transactionLogDao.updateReconFlag(invoiceNo, reconFlag, channelRc, billerRc, transactionCode);
	}

	@Override
	public void insertDummyDataWebadminReconcile(String trxCode, String channelCode, String billerCode, String invoiceNo, String reconFlag, Date transactionDate) {
		transactionLogDao.insertDummyDataWebadminReconcile(trxCode, channelCode, billerCode, invoiceNo, reconFlag, transactionDate);
	}

	@Override
	public Map<String, Object[]> findAllTransactionLogsWebadminReconcileByDate(
			String channel, String transactionCode, String[] billerRc,
			String[] channelRc, Date trxDate) {
		// TODO Auto-generated method stub
		return transactionLogDao.findAllTransactionLogsWebadminReconcileByDate(channel, transactionCode, billerRc, channelRc, trxDate);
	}
}
