package com.balicamp.service.parameter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.balicamp.model.log.AuditLog;
import com.balicamp.model.parameter.SystemParameter;
import com.balicamp.model.parameter.SystemParameterId;
import com.balicamp.model.user.User;
import com.balicamp.service.IManager;

/**
 * SystemParameter Manager
 *
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 * @version $Id: SystemParameterManager.java 1515 2012-07-02 09:15:21Z
 *          wayan.agustina $
 */
public interface SystemParameterManager extends IManager {

	void softReset();

	/**
	 * find System patameter by group name
	 *
	 * @param group
	 * @return Map of name and systemparameter
	 */
	public Map<String, SystemParameter> findMapByGroup(String group);

	/**
	 * get String value
	 *
	 * @param systemParameterId
	 * @param defaultValue
	 * @param fromCache
	 * @return
	 */
	public String getStringValue(SystemParameterId id, String defaultValue);

	public String getStringValue(String key, String defaultValue);

	public String[] getStringArrayValue(String key, String defaultValue,
			String regexSeparator);

	/**
	 * get int value
	 *
	 * @param systemParameterId
	 * @param defaultValue
	 * @param fromCache
	 * @return
	 */
	public int getIntValue(SystemParameter id, int defaultValue);

	public int getIntValue(String key, int defaultValue);

	/**
	 * get long value
	 *
	 * @param systemParameterId
	 * @param defaultValue
	 * @param fromCache
	 * @return
	 */
	public long getLongValue(SystemParameterId id, long defaultValue);

	public long getLongValue(String key, long defaultValue);

	/**
	 * get boolean value
	 *
	 * @param systemParameterId
	 * @param defaultValue
	 * @param fromCache
	 * @return true if value = true or 1, otherwise return false
	 */
	public boolean getBooleanValue(SystemParameterId id, boolean defaultValue);

	public boolean getBooleanValue(String key, boolean defaultValue);

	public BigDecimal getBigDecimalValue(SystemParameterId id,
			BigDecimal defaultValue);

	public BigDecimal getBigDecimalValue(String key, BigDecimal defaultValue);

	/**
	 * find Group that contain editable SystemParameter
	 *
	 * @return
	 */
	public List<String> getEditableGroupList();

	/**
	 * Find SystemParameter by group name
	 *
	 * @param groupName
	 * @return
	 */
	public List<SystemParameter> findEditableByGroupName(String groupName);

	// configuration
	/**
	 * add configuration
	 */
	public void addConfiguration(String key, String value);

	/**
	 * get string value of configuration
	 *
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getStringConfiguration(String key, String defaultValue);

	public AuditLog saveParameter(SystemParameter parameter, User user,
			String oldValue);

	// pause
	void doPause();

	void doContinue();

	void setPause(boolean pause);

	boolean isPaused();

	public SystemParameter findById(String paramGroup, String paramName);

	public SystemParameter findByParamNameAndParamGroup(String paramGroup,
			String paramName);

	/**
	 * get {@link SystemParameter} by paramName
	 *
	 * @param paramName
	 * @return {@link SystemParameter}
	 */
	public String findParamValueByParamName(String paramName);

	public void insertSystemParameter(SystemParameter parameter);

	public void updateSystemParameter(SystemParameter parameter);

	public SystemParameter findSystemParameterById(String paramGroup,
			String paramName);

	public SystemParameter findScheduler(String paramGroup, String paramName);

	int getIntValue(SystemParameterId id, int defaultValue);

	public SystemParameter findByParamName(String paramName);

	public Map<String, Object> findftpLogin(String ftp);

	public List<SystemParameter> findListByParamGroup(String paramGroup);

	public SystemParameter findByParamGroupAndValue(String paramGroup, String paramValue);

	public SystemParameter findByParamGroupAndName(String paramGroup, String paramName);
}
