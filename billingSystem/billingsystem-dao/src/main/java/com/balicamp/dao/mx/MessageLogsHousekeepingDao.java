package com.balicamp.dao.mx;

import java.util.List;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.mx.MessageLogs;
import com.balicamp.model.mx.MessageLogsDto;
import com.balicamp.model.mx.MessageLogsParameter;

public interface MessageLogsHousekeepingDao extends GenericDao<MessageLogs, Long> {
	public String getRawMessageByParam(MessageLogsDto param);

	public List<Object> getCountMessageLogsByProperty(MessageLogsParameter param);

	public List<Object> getMessageLogsByProperty(MessageLogsParameter param, int firstResult, int maxResult);
}
