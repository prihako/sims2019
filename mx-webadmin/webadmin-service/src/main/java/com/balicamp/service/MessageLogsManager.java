/**
 * 
 */
package com.balicamp.service;

import java.util.HashMap;
import java.util.List;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mx.MessageLogsDto;
import com.balicamp.model.mx.MessageLogsParameter;
import com.balicamp.model.user.User;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: MessageLogsManager.java 504 2013-05-24 08:15:06Z rudi.sadria $
 */
public interface MessageLogsManager extends IManager {

	/**
	 * Find by parameter and wrap into DTO {@link MessageLogsDto}
	 * @param searchCriteria searchCriteria
	 * @return List of {@link MessageLogsDto}
	 */
	@Deprecated
	public List<MessageLogsDto> findMessageLogByFilterCriteria(SearchCriteria searchCriteria, int firstResult,
			int maxResult);

	/**
	 * Get RawCount By Custom SearchCriteria
	 * @param searchCriteria
	 * @return int row count
	 */
	@Deprecated
	public int getRowCount(SearchCriteria searchCriteria);

	/**
	 * Get Raw data and formated to IsoMessage Format By {@link MessageLogsDto} 
	 * @param parameter
	 * @return MessageLogDisplay
	 */
	public MessageLogsDto getRawMessageLogs(MessageLogsDto parameter, boolean isHtml);

	/**
	 * Export to word document
	 * @param listDisplay
	 * @return String fileName
	 */
	public String exportToWord(List<MessageLogsDto> listDisplay);

	public String exportToWordNotCompress(List<MessageLogsDto> listDisplay);

	/**
	 * Get RawCount By MessageLogsParameter
	 * @param messageLogsParameter
	 * @return int row count
	 */
	public int getRowCountByParameter(MessageLogsParameter messageLogsParameter);

	public List<MessageLogsDto> findMessageLogsByParameter(MessageLogsParameter messageLogsParameter, int firstResult,
			int maxResult);

	public HashMap getRawMessageLogs(MessageLogsDto parameter);

	/**
	 * Find by MessageLogs 
	 * @param trxId
	 * @param endpointCode
	 * @param isRequest
	 * @return List of MessageLogsDto
	 */
	public Object getMessageLogsData(String trxId, String endpointCode, boolean isRequest);

	public String exportToWord(List<MessageLogsDto> lines, String realPath, User userLoginFromSession);
}
