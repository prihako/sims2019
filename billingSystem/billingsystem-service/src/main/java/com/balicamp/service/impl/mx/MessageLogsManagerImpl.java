/**
 * 
 */
package com.balicamp.service.impl.mx;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import com.balicamp.model.mx.Transactions;
import com.balicamp.service.AbitMessageParser;
import com.balicamp.service.MessageLogsManager;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.util.DateUtil;
import com.balicamp.util.mx.BeanUtility;
import com.balicamp.util.mx.Iso8583Utility;
import com.balicamp.util.mx.WriteToWord;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: MessageLogsManagerImpl.java 504 2013-05-24 08:15:06Z rudi.sadria $
 */
@Service("messageLogsManagerImpl")
public class MessageLogsManagerImpl extends AbstractManager implements MessageLogsManager {

	private static final long serialVersionUID = 6134138675287729810L;

	private static final String PARAM_NAME = "mayora.download.temporary.path";

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
		List<MessageLogs> messageLogsHousekeeping = messageLogHousekeepingDao.findByCriteria(scMsgLogs, firstResult,
				maxResult);
		messageLogsHousekeeping.addAll(messageLogs);

		if (messageLogsHousekeeping.size() > 0) {

			for (MessageLogs msgLogs : messageLogsHousekeeping) {

				// Init MessageDisplay
				MessageLogsDto display = new MessageLogsDto();
				display.setConversionTime(msgLogs.getId().getConversionTime());

				// Load TransactionLogs
				if (msgLogs.getId().getTransactionId() != null) {

					scTransLogs.addCriterion(Restrictions.eq("id.transactionId", msgLogs.getId().getTransactionId()));
					List<TransactionLogs> transLogs = transactionLogDao.findByCriteria(scTransLogs);
					List<TransactionLogs> transLogsHousekeeping = transactionLogHousekeepingDao
							.findByCriteria(scTransLogs);

					transLogsHousekeeping.addAll(transLogs);
					// remove Criterion by transactionId
					scTransLogs.removeCriterion("id.transactionId=" + msgLogs.getId().getTransactionId());
					if (transLogs.size() > 0) {
						for (TransactionLogs tLogs : transLogsHousekeeping) {
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

										display.setTransactionName(trans.getName() != null ? trans.getName() : null);
										display.setTransactionCode(tLogs.getId().getTransactionCode());

										// set another display properties
										display.setTransactionDateTime(tLogs.getId().getTransactionTime() != null ? tLogs
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
	public MessageLogsDto getRawMessageLogs(MessageLogsDto parameter) {
		MessageLogsDto display = new MessageLogsDto();
		String rawData = messageLogDao.getRawMessageByParam(parameter) != null ? messageLogDao
				.getRawMessageByParam(parameter) : "";

		String rawDataHousekeeping = messageLogHousekeepingDao.getRawMessageByParam(parameter) != null ? messageLogHousekeepingDao
				.getRawMessageByParam(parameter) : "";

		if (rawDataHousekeeping.length() > 0)
			display.setRaw(rawDataHousekeeping);
		else if (rawData.length() > 0)
			display.setRaw(rawData);

		String configPath = parameterManager.findByParamName("other.messageLogs.configFile").getParamValue();

		if (parameter.getEndpointCode().equals("abt01")) {
			String formattedMsg = abitMessageParser.parse(display.getRaw(), true);
			display.setFormatedMessage(formattedMsg);
			return display;
		}

		display.setFormatedMessage(Iso8583Utility.printToHTMLFormatted(display.getRaw(), configPath, parameter.getEndpointCode()));

		return display;
	}

	@Override
	public String exportToWord(List<MessageLogsDto> listDisplay) {

		List<StringBuffer> strBuffers = new ArrayList<StringBuffer>();
		String configPath = parameterManager.findByParamName("other.messageLogs.configFile").getParamValue();
		for (MessageLogsDto dis : listDisplay) {
			MessageLogsDto display = new MessageLogsDto();

			//20-8-2013 edited by Sangbas
			WriteToWord.checkNullField(dis);

			BeanUtility.copyProperties(dis, display);
			
			String endPoint = dis.getEndpointCode();
			MessageLogsDto disTmp = getRawMessageLogs(dis);
			if (endPoint.equals("chws")) {
				display.setRaw(disTmp.getRaw());
				display.setFormatedMessage(disTmp.getRaw());
			} else {
				display.setRaw(disTmp.getRaw());
				display.setFormatedMessage(Iso8583Utility.print(disTmp.getRaw(), configPath, dis.getEndpointCode()));
			}
			StringBuffer strBuffer = formatedTextBeforeExport(display);
			strBuffers.add(strBuffer);
		}

		String path = systemParameterDao.findParamValueByParamName(PARAM_NAME);
		return WriteToWord.doWrite(strBuffers, path, DEFAULT_FILENAME);
	}

	private StringBuffer formatedTextBeforeExport(MessageLogsDto display) {
		if (display.getEndpointCode().equals("abt01")) {
			String formattedMsg = abitMessageParser.parse(display.getRaw(), false);
			display.setFormatedMessage(formattedMsg);
			// return new StringBuffer(formattedMsg);
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append("Transaction ID\t\t\t: " + display.getTransactionId() + "\r\n");
		buffer.append("Transaction Code\t\t\t: " + display.getTransactionCode() + "\r\n");
		buffer.append("Transaction Description\t\t: " + display.getTransactionName() + "\r\n");
		buffer.append("Transaction Date\t\t\t: "
				+ DateUtil.convertDateToString(display.getTransactionDateTime(), "dd MMM yyyy HH:mm:ss") + "\r\n");
		buffer.append("Endpoint\t\t\t: " + display.getEndpointCode() + "\r\n");
		buffer.append("Response Code\t\t\t: " + display.getRc() + "\r\n");
		buffer.append("Response Code Desciption\t\t: " + display.getRcDesc() + "\r\n");
		buffer.append("Raw Message\t\t\t: " + display.getRaw() + "\r\n");
		buffer.append("Formated Message\t\t:\r" + display.getFormatedMessage() + "\r\n\r\n");
		return buffer;
	}

	@Override
	public List<MessageLogsDto> findMessageLogsByParameter(MessageLogsParameter messageLogsParameter, int firstResult,
			int maxResult) {
		List<MessageLogsDto> msgDisplay = new ArrayList<MessageLogsDto>();
		List<Object> rows = (List<Object>) messageLogDao.getMessageLogsByProperty(messageLogsParameter, firstResult,
				maxResult);
		List<Object> rowsHousekeeping = (List<Object>) messageLogHousekeepingDao.getMessageLogsByProperty(
				messageLogsParameter, firstResult, maxResult);

		List<EndpointRcs> endpointRcsRows = endpointRcsDao.findAll();
		rows.addAll(rowsHousekeeping);

		String configPath = parameterManager.findByParamName("other.messageLogs.configFile").getParamValue();

		for (Object o : rows) {
			Object[] row = (Object[]) o;
			
			MessageLogsDto dto = new MessageLogsDto();
			dto.setTransactionId((String) row[0]);
			dto.setConversionTime((Date) row[1]);
			dto.setEndpointCode((String) row[2]);
			dto.setMappingCode((String) row[3]);
			dto.setRequest(Boolean.valueOf(row[4].toString()));
			dto.setTransactionDateTime((Date) row[6]);
			dto.setTransactionCode((String) row[7]);
			
			if (row.length > 9) {
				if (row[9] != null) {
					dto.setTransactionName((String) row[9]);
				} else {
					//dto.setTransactionName((String) row[9]);
				}
				dto.setRaw((String) row[11]);				
			} else {
				dto.setTransactionName(transactionsDao.findByFieldName(dto.getTransactionCode()));
				dto.setRaw((String) row[8]);
			}
			
			if (dto.isRequest()) {
				dto.setTransactionName(dto.getTransactionName() + " Req");
				dto.setRc(null);
				dto.setRcDesc(null);
			} else {
				dto.setTransactionName(dto.getTransactionName() + " Rsp");
				if (dto.getEndpointCode().equals("abt01")) {
					HashMap<String, String> abitMap = abitMessageParser.parseToHashMap(dto.getRaw());
					if (abitMap != null) {
						if (abitMap.get("WF@MSC") != null)
							dto.setRc(abitMap.get("WF@MSC").trim());
						else if (abitMap.get("WF@MSCT") != null)
							dto.setRc(abitMap.get("WF@MSCT").trim());
						if (dto.getRc() == null || dto.getRc().length() == 0)
							dto.setRcDesc("ABIT-APPROVED");
					}
				} else {
					String rc = Iso8583Utility.getResponseCode(dto.getRaw(), configPath, dto.getEndpointCode());
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
		List<Object> rests = (List<Object>) messageLogDao.getCountMessageLogsByProperty(messageLogsParameter);
		List<Object> restsHousekeeping = (List<Object>) messageLogHousekeepingDao
				.getCountMessageLogsByProperty(messageLogsParameter);

		restsHousekeeping.addAll(rests);

		int count = 0;
		for (Object o : restsHousekeeping) {
			Object[] arrObject = (Object[]) o;
			count += Long.valueOf(arrObject[1].toString()).intValue();
		}
		return count;
	}
}
