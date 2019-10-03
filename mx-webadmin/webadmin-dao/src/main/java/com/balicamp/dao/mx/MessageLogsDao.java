/**
 * 
 */
package com.balicamp.dao.mx;

import java.util.List;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.mx.MessageLogs;
import com.balicamp.model.mx.MessageLogsDto;
import com.balicamp.model.mx.MessageLogsParameter;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: MessageLogsDao.java 503 2013-05-24 08:13:16Z rudi.sadria $
 */
public interface MessageLogsDao extends GenericDao<MessageLogs, Long> {

	public String getRawMessageByParam(MessageLogsDto param);

	public Long getCountMessageLogsByProperty(MessageLogsParameter param);
	
	public Object getMessageLogsData(String trxId, String endpointCode, boolean isRequest);

	public List<?> getMessageLogsByProperty(MessageLogsParameter param, int firstResult, int maxResult);
}
