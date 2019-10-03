/**
 * 
 */
package com.balicamp.service.impl.mx;

import java.awt.Color;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.dao.mx.EndpointRcsDao;
import com.balicamp.dao.mx.EndpointsDao;
import com.balicamp.dao.mx.MessageLogsDao;
import com.balicamp.dao.mx.MessageLogsHousekeepingDao;
import com.balicamp.dao.mx.TransactionLogDao;
import com.balicamp.dao.mx.TransactionLogHousekeepingDao;
import com.balicamp.dao.mx.TransactionsDao;
import com.balicamp.dao.parameter.SystemParameterDao;
import com.balicamp.model.mx.EndpointRcs;
import com.balicamp.model.mx.MessageLogs;
import com.balicamp.model.mx.MessageLogsDto;
import com.balicamp.model.mx.MessageLogsParameter;
import com.balicamp.model.mx.TransactionLogs;
import com.balicamp.model.mx.TransactionLogsBase;
import com.balicamp.model.mx.TransactionLogsHousekeeping;
import com.balicamp.model.mx.Transactions;
import com.balicamp.model.user.User;
import com.balicamp.service.AbitMessageParser;
import com.balicamp.service.HsmMessageParser;
import com.balicamp.service.MessageLogsManager;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.util.DateUtil;
import com.balicamp.util.mx.ISOMessageUtil;
import com.balicamp.util.mx.WriteToWord;
import com.lowagie.text.Cell;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: MessageLogsManagerImpl.java 504 2013-05-24 08:15:06Z rudi.sadria $
 */
@Service("messageLogsManagerImpl")
public class MessageLogsManagerImpl extends AbstractManager implements MessageLogsManager {

	private static final long serialVersionUID = 6134138675287729810L;

	private static final String PARAM_NAME = "mayora.download.temporary.path";

	private static final String PARAMETER_NAME = "webadmin.download.temporary.path";

	private static final String DEFAULT_FILENAME = "MessageLogs";

	@Autowired
	private SystemParameterManager parameterManager;

	// variabel global untuk count
	SearchCriteria scMsgLogs = null;
	SearchCriteria scTransLogs = null;
	SearchCriteria scTrans = null;

	@Autowired
	private MessageLogsDao messageLogDao;

	@Autowired
	private MessageLogsHousekeepingDao messageLogHousekeepingDao;

	@Autowired
	private TransactionLogDao transactionLogDao;

	@Autowired
	private TransactionLogHousekeepingDao transactionLogHousekeepingDao;

	@Autowired
	private TransactionsDao transactionsDao;

	@Autowired
	private EndpointsDao endpointsDao;

	@Autowired
	private EndpointRcsDao endpointRcsDao;

	@Autowired
	private SystemParameterDao systemParameterDao;

	@Autowired
	@Qualifier("abitMessageParser")
	private AbitMessageParser abitMessageParser;

	@Autowired
	@Qualifier("hsmMessageParser")
	private HsmMessageParser hsmMessageParser;

	public TransactionLogDao getTransactionLogDao() {
		return transactionLogDao;
	}

	public TransactionsDao getTransactionsDao() {
		return transactionsDao;
	}

	public void setEndpointsDao(EndpointsDao endpointsDao) {
		this.endpointsDao = endpointsDao;
	}

	@Override
	public Object getDefaultDao() {
		return messageLogDao;
	}

	@Override
	public List<MessageLogsDto> findMessageLogByFilterCriteria(SearchCriteria searchCriteria, int firstResult,
			int maxResult) {

		// Init DTO
		List<MessageLogsDto> messageLogDisplays = new ArrayList<MessageLogsDto>();

		// Load criteria
		buildSc(searchCriteria);

		// Load MessageLogs
		List<MessageLogs> messageLogs = messageLogDao.findByCriteria(scMsgLogs, firstResult, maxResult);

		if (messageLogs.size() > 0) {

			for (MessageLogs msgLogs : messageLogs) {

				// Init MessageDisplay
				MessageLogsDto display = new MessageLogsDto();
				display.setConversionTime(msgLogs.getId().getConversionTime());

				// Load TransactionLogs
				if (msgLogs.getId().getTransactionId() != null) {

					scTransLogs.addCriterion(Restrictions.eq("id.transactionId", msgLogs.getId().getTransactionId()));
					List<TransactionLogs> transLogs = transactionLogDao.findByCriteria(scTransLogs);
					List<TransactionLogsHousekeeping> transLogsHousekeeping = transactionLogHousekeepingDao
							.findByCriteria(scTransLogs);
					List<TransactionLogsBase> all = new ArrayList<TransactionLogsBase>();
					all.addAll(transLogsHousekeeping);
					all.addAll(transLogs);
					// remove Criterion by transactionId
					scTransLogs.removeCriterion("id.transactionId=" + msgLogs.getId().getTransactionId());
					if (transLogs.size() > 0) {
						for (TransactionLogsBase tLogs : all) {
							if (tLogs != null) {
								if (tLogs.getId().getChannelRc() != "" || tLogs.getId().getChannelRc() != null) {

									// Load mapping from channelRc
									String rcDesc = endpointsDao.findRcDescByEndpCode(
											msgLogs.getId().getEndpointCode(), tLogs.getId().getChannelRc());
									display.setRc(tLogs.getId().getChannelRc());
									display.setRcDesc(rcDesc);
								}

								if (tLogs.getId().getTransactionCode() != null) {

									scTrans.addCriterion(Restrictions.eq("code", tLogs.getId().getTransactionCode()));
									Transactions trans = transactionsDao.findSingleByCriteria(scTrans);
									// remove Criterion by code
									scTrans.removeCriterion("code=" + tLogs.getId().getTransactionCode());

									if (trans != null) {

										// set another display properties
										display.setConversionTime(tLogs.getId().getTransactionTime() != null ? tLogs
												.getId().getTransactionTime() : null);
										display.setTransactionId(tLogs.getId().getTransactionId() != null ? tLogs
												.getId().getTransactionId() : null);
										display.setEndpointCode(msgLogs.getId().getEndpointCode() != null ? msgLogs
												.getId().getEndpointCode() : null);
										display.setMappingCode(msgLogs.getId().getMappingCode() != null ? msgLogs
												.getId().getMappingCode() : null);
										display.setRequest(msgLogs.getId().isIsRequest());

										// add to List of MessageDisplay
										messageLogDisplays.add(display);
									}
								}
							}
						}
					}
				}
			}

		}
		return messageLogDisplays;
	}

	@Override
	public int getRowCount(SearchCriteria searchCriteria) {

		buildSc(searchCriteria);
		int count = 0;

		List<MessageLogs> messageLogs = messageLogDao.findByCriteria(scMsgLogs);
		List<MessageLogs> messageLogsHousekeeping = messageLogHousekeepingDao.findByCriteria(scMsgLogs);
		messageLogsHousekeeping.addAll(messageLogs);

		if (messageLogsHousekeeping.size() > 0) {
			for (MessageLogs msgLogs : messageLogsHousekeeping) {

				// Load TransactionLogs
				if (msgLogs.getId().getTransactionId() != null) {

					scTransLogs.addCriterion(Restrictions.eq("id.transactionId", msgLogs.getId().getTransactionId()));
					List<TransactionLogs> transLogs = transactionLogDao.findByCriteria(scTransLogs);
					// remove Criterion by transactionId
					scTransLogs.removeCriterion("id.transactionId=" + msgLogs.getId().getTransactionId());
					if (transLogs.size() > 0) {
						for (TransactionLogs tLogs : transLogs) {
							if (tLogs != null) {
								if (tLogs.getId().getTransactionCode() != null) {

									scTrans.addCriterion(Restrictions.eq("code", tLogs.getId().getTransactionCode()));
									Transactions trans = transactionsDao.findSingleByCriteria(scTrans);
									// remove Criterion by code
									scTrans.removeCriterion("code=" + tLogs.getId().getTransactionCode());

									if (trans != null)
										// count
										count += 1;
								}
							}
						}
					}
				}
			}
		}
		return count;
	}

	public void buildSc(SearchCriteria sc) {
		// Load criteria
		for (SearchCriteria sc2 : sc.getListOfSearchCriteria(sc)) {
			if (sc.getEntityName().equals("messageLog")) {
				scMsgLogs = sc2;
			} else if (sc.getEntityName().equals("transactionLogs")) {
				scTransLogs = sc2;
			} else if (sc.getEntityName().equals("transactions")) {
				scTrans = sc2;
			}
		}
		scMsgLogs = scMsgLogs != null ? scMsgLogs : SearchCriteria.createSearchCriteria("messageLog");
		scTransLogs = scTransLogs != null ? scTransLogs : SearchCriteria.createSearchCriteria("transactionLogs");
		scTrans = scTrans != null ? scTrans : SearchCriteria.createSearchCriteria("transactions");
	}

	@Override
	public HashMap getRawMessageLogs(MessageLogsDto display) {
		String configPath = parameterManager.findByParamName("other.messageLogs.configFile").getParamValue();

		if (display.getEndpointCode().equals("abt01")) {

			return abitMessageParser.parseToHashMap(display.getRaw());
		} else if (display.getEndpointCode().equals("hsm01")) {

			if (display.isRequest()) {
				return hsmMessageParser.parseToHashMap(display.getRaw(), true);
			} else {
				return hsmMessageParser.parseToHashMap(display.getRaw(), false);
			}
		} else {

			HashMap iso8583 = new HashMap();
			iso8583.putAll(ISOMessageUtil.unpackISOMessage(display.getRaw(), configPath, display.getEndpointCode()));
			return iso8583;
		}
	}

	@Override
	public MessageLogsDto getRawMessageLogs(MessageLogsDto display, boolean isHtml) {

		String configPath = parameterManager.findByParamName("other.messageLogs.configFile").getParamValue();
		Object messageLogs = messageLogDao.getMessageLogsData(display.getTransactionId(), display.getEndpointCode(),
				display.isRequest());

		System.out.println("MessageLogsManagerImpl ISO isHtml : " + isHtml);
		System.out.println("MessageLogsManagerImpl ISO raw message formatted : " + display.getRaw());

		// if (display.getEndpointCode().equals("abt01")) {
		// String formattedMsg = abitMessageParser.parse(display.getRaw(),
		// isHtml);
		// display.setFormatedMessage(formattedMsg);
		// return display;
		// } else if (display.getEndpointCode().equals("hsm01")) {
		// if (display.isRequest()) {
		// String formattedMsg = hsmMessageParser.parse(display.getRaw(),
		// isHtml, true);
		// display.setFormatedMessage(formattedMsg);
		// } else {
		// String formattedMsg = hsmMessageParser.parse(display.getRaw(),
		// isHtml, false);
		// display.setFormatedMessage(formattedMsg);
		// }
		// return display;
		// } else

		if (!display.getEndpointCode().equals("bils0")) {

			StringBuffer strBuf = new StringBuffer();

			// String raws = ((String) messageLogs).replace("\n", "<br/>");
			String raws = ((String) messageLogs);
			strBuf.append(raws);

			display.setFormatedMessage(strBuf.toString());

			return display;
		} else {

			if (display.getRaw() != null) {
				display.setFormatedMessage(ISOMessageUtil.printFormattedIso(
						ISOMessageUtil.unpackISOMessage(display.getRaw(), configPath, display.getEndpointCode()),
						isHtml));

				//
			} else {

				display.setFormatedMessage(ISOMessageUtil.printFormattedIso(
						ISOMessageUtil.unpackISOMessage((String) messageLogs, configPath, display.getEndpointCode()),
						isHtml));

				//
			}

		}

		return display;
	}

	@Override
	public String exportToWord(List<MessageLogsDto> listDisplay) {

		List<StringBuffer> strBuffers = new ArrayList<StringBuffer>();
		for (MessageLogsDto dis : listDisplay) {
			// MessageLogsDto display = new MessageLogsDto();
			// BeanUtility.copyProperties(dis, display);
			dis = getRawMessageLogs(dis, false);
			// display.setRaw(disTmp.getRaw());

			StringBuffer strBuffer = formatedTextBeforeExport(dis, false);
			strBuffers.add(strBuffer);
		}

		System.out.println("MessageLogsManagerImpl CONVERT KE WORD ");

		String path = systemParameterDao.findParamValueByParamName(PARAMETER_NAME);

		System.out.println("MessageLogsManagerImpl CONVERT KE WORD PATH " + path);

		return WriteToWord.doWrite(strBuffers, path, DEFAULT_FILENAME);
	}

	public StringBuffer formatedTextBeforeExport(MessageLogsDto display, boolean isHtml) {
		display = this.getRawMessageLogs(display, isHtml);
		StringBuffer buffer = new StringBuffer();
		buffer.append("Transaction ID           \t\t: " + display.getTransactionId() + "\r\n");
		buffer.append("Mapping Name             \t\t: " + display.getMappingName() + "\r\n");
		buffer.append("Transaction Date         \t\t: "
				+ DateUtil.convertDateToString(display.getConversionTime(), "dd MMM yyyy HH:mm:ss") + "\r\n");
		buffer.append("Endpoint                 \t\t: " + display.getEndpointCode() + "\r\n");
		buffer.append("Response Code            \t\t: " + (display.getRc() == null ? "" : display.getRc()) + "\r\n");
		buffer.append("Response Code Desciption \t\t: " + (display.getRcDesc() == null ? "" : display.getRcDesc())
				+ "\r\n");
		buffer.append("Raw Message              \t\t:\r" + display.getRaw() + "\r\n");
		buffer.append("Formated Message         \t\t:\r" + display.getFormatedMessage() + "\r\n\r\n");
		return buffer;
	}

	@Override
	public List<MessageLogsDto> findMessageLogsByParameter(MessageLogsParameter messageLogsParameter, int firstResult,
			int maxResult) {
		List<MessageLogsDto> msgDisplay = new ArrayList<MessageLogsDto>();
		List<Object> rows = (List<Object>) messageLogDao.getMessageLogsByProperty(messageLogsParameter, firstResult,
				maxResult);
		// List<Object> rowsHousekeeping = (List<Object>)
		// messageLogHousekeepingDao.getMessageLogsByProperty(
		// messageLogsParameter, firstResult, maxResult);

		List<EndpointRcs> endpointRcsRows = endpointRcsDao.findAll();
		// rowsHousekeeping.addAll(rows);

		String configPath = parameterManager.findByParamName("other.messageLogs.configFile").getParamValue();
		for (Object o : rows) {
			Object[] row = (Object[]) o;
			MessageLogsDto dto = new MessageLogsDto();
			dto.setTransactionId((String) row[0]);
			dto.setConversionTime((Date) row[1]);
			dto.setEndpointCode((String) row[2]);
			dto.setMappingCode((String) row[3]);
			dto.setRequest(Boolean.valueOf(row[4].toString()));
			dto.setRaw((String) row[5]);
			dto.setMappingName((String) row[6]);
			if (dto.isRequest()) {
				dto.setMappingName(dto.getMappingName() + " Req");
				dto.setRc(null);
				dto.setRcDesc(null);
			} else {
				dto.setMappingName(dto.getMappingName() + " Resp");
				// if (dto.getEndpointCode().equals("abt01")) {
				// HashMap<String, String> abitMap =
				// abitMessageParser.parseToHashMap(dto.getRaw());
				// if (abitMap != null) {
				// if (abitMap.get("WF@MSC") != null)
				// dto.setRc(abitMap.get("WF@MSC").trim());
				// else if (abitMap.get("WF@MSCT") != null)
				// dto.setRc(abitMap.get("WF@MSCT").trim());
				// if (dto.getRc() == null || dto.getRc().length() == 0)
				// dto.setRcDesc("ABIT-APPROVED");
				// }
				// } else if (dto.getEndpointCode().equals("hsm01")) {
				// if (dto.getRaw().length() >= 32) {
				// String rc = dto.getRaw().substring(30, 32);
				// dto.setRc(rc);
				// }
				// }

				if (!dto.getEndpointCode().equals("bils0")) {

					Properties prop = new Properties();
					try {
						prop.load(new StringReader((String) dto.getRaw()));

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					dto.setRc(prop.getProperty("errorCode"));
					dto.setRcDesc(prop.getProperty("statusDescription"));

				} else {

					System.out.println("AMBIL ISO BILLER endpoint " + dto.getEndpointCode());
					Hashtable temp = ISOMessageUtil.unpackISOMessage(dto.getRaw(), configPath, dto.getEndpointCode());
					// Hashtable temp = Iso8583Utility.unpack(dto.getRaw(),
					// configPath, dto.getEndpointCode());

					String rc = null;
					if (temp != null)
						rc = (String) ISOMessageUtil.unpackISOMessage(dto.getRaw(), configPath, dto.getEndpointCode())
								.get("39");

					System.out.println("AMBIL ISO BILLER DAPET RC -nya " + rc);

					if (rc != null)
						dto.setRc(rc);
				}
				if (dto.getRc() != null) {
					for (EndpointRcs endpointRcs : endpointRcsRows) {
						if (endpointRcs.getEndpoints() != null) {
							if (dto.getRc().equals(endpointRcs.getRc())
									&& dto.getEndpointCode().equals(endpointRcs.getEndpoints().getCode())) {
								dto.setRcDesc(endpointRcs.getDescription());
							}
						}
					}
				}

			}
			msgDisplay.add(dto);
		}

		return msgDisplay;
	}

	@Override
	public int getRowCountByParameter(MessageLogsParameter messageLogsParameter) {

		return Integer.parseInt("" + messageLogDao.getCountMessageLogsByProperty(messageLogsParameter));
	}

	@Override
	public String exportToWord(List<MessageLogsDto> lines, String realPath, User userLoginFromSession) {
		List<List<Cell>> listPage = new ArrayList<List<Cell>>();

		System.out.println("MessageLogsmanagerImpl exporttoword lines " + lines);
		for (MessageLogsDto dis : lines) {
			try {
				// headers
				List<Cell> cellList = new ArrayList<Cell>();
				Cell header = new Cell(new Phrase("Message Log", FontFactory.getFont(FontFactory.TIMES_BOLD, 14)));
				header.setHorizontalAlignment(1);
				header.setColspan(2);
				header.setBackgroundColor(Color.GRAY);
				cellList.add(header);
				cellList.add(new Cell("Transaction ID"));
				cellList.add(new Cell(dis.getTransactionId()));
				cellList.add(new Cell("Mapping Name"));
				cellList.add(new Cell(dis.getMappingName()));
				cellList.add(new Cell("Transaction Date"));
				cellList.add(new Cell(DateUtil.convertDateToString(dis.getConversionTime(), "dd MMM yyyy HH:mm:ss")));
				cellList.add(new Cell("Endpoint Code"));
				cellList.add(new Cell(dis.getEndpointCode()));
				cellList.add(new Cell("Response Code"));
				cellList.add(new Cell(dis.getRc()));
				cellList.add(new Cell("Response Code Description"));
				cellList.add(new Cell(dis.getRcDesc()));
				cellList.add(new Cell("Raw Message"));
				cellList.add(new Cell(dis.getRaw()));
				cellList.add(new Cell("Formatted Raw Message"));

				try {
					dis = getRawMessageLogs(dis, false);
				} catch (Exception dt) {
					dis.setFormatedMessage("Gagal Parsing Message");
				}
				cellList.add(new Cell(new Paragraph(dis.getFormatedMessage())));
				listPage.add(cellList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// String path =
		// systemParameterDao.findParamValueByParamName(PARAM_NAME);
		// System.out.println("MessageLogsManagerImpl path " + path);
		// System.out.println("MessageLogsManagerImpl realPath " + realPath);

		String path = null;
		return WriteToWord.doWriteTable(listPage, path, DEFAULT_FILENAME, realPath, userLoginFromSession);

	}

	@Override
	public Object getMessageLogsData(String trxId, String endpointCode, boolean isRequest) {
		// TODO Auto-generated method stub

		Object messageLogs = messageLogDao.getMessageLogsData(trxId, endpointCode, isRequest);

		System.out.println("MessageLogsManagerImpl OBJECT DATAA --> " + messageLogs);
		return messageLogs;
	}

	@Override
	public String exportToWordNotCompress(List<MessageLogsDto> listDisplay) {
		List<StringBuffer> strBuffers = new ArrayList<StringBuffer>();
		for (MessageLogsDto dis : listDisplay) {
			// MessageLogsDto display = new MessageLogsDto();
			// BeanUtility.copyProperties(dis, display);
			dis = getRawMessageLogs(dis, false);
			// display.setRaw(disTmp.getRaw());

			StringBuffer strBuffer = formatedTextBeforeExport(dis, false);
			strBuffers.add(strBuffer);
		}

		System.out.println("MessageLogsManagerImpl CONVERT KE WORD ");

		String path = systemParameterDao.findParamValueByParamName(PARAMETER_NAME);

		System.out.println("MessageLogsManagerImpl CONVERT KE WORD PATH " + path);

		return WriteToWord.doWriteNotCompress(strBuffers, path, DEFAULT_FILENAME);
	}
}
