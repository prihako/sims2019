package com.balicamp.service.impl.parameter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.parameter.SystemParameterDao;
import com.balicamp.model.constant.ModelConstant;
import com.balicamp.model.log.AuditLog;
import com.balicamp.model.parameter.SystemParameter;
import com.balicamp.model.parameter.SystemParameterId;
import com.balicamp.model.user.User;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.log.AuditLogManager;
import com.balicamp.service.parameter.SystemParameterManager;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 * @version $Id: SystemParameterManagerImpl.java 1541 2012-07-04 03:50:27Z
 *          arya.sutrisna $
 */
@Service("systemParameterManager")
public class SystemParameterManagerImpl extends AbstractManager implements SystemParameterManager, InitializingBean {

	private static final long serialVersionUID = 1L;

	private SystemParameterDao systemParameterDao;

	private AuditLogManager auditLogManager;

	private Map<SystemParameter, SystemParameter> systemParameterById;

	private Map<String, Map<String, SystemParameter>> systemParameterByGroup;

	private Map<String, List<SystemParameter>> editableSystemParameterByGroup;

	private List<String> editableGroupList;

	@Autowired
	public void setSystemParameterDao(SystemParameterDao systemParameterDao) {
		this.systemParameterDao = systemParameterDao;

	}

	@Autowired
	public void setAuditLogManager(AuditLogManager auditLogManager) {
		this.auditLogManager = auditLogManager;
	}

	@Override
	public void softReset() {
		systemParameterById = new HashMap<SystemParameter, SystemParameter>();
		systemParameterByGroup = new HashMap<String, Map<String, SystemParameter>>();
		editableSystemParameterByGroup = new HashMap<String, List<SystemParameter>>();
		editableGroupList = new ArrayList<String>();

		List<SystemParameter> systemParameterList = systemParameterDao.findAll();
		for (SystemParameter sparam : systemParameterList) {
			systemParameterById.put(sparam, sparam);
			String groupName = sparam.getSystemParameterId().getGroup();

			Map<String, SystemParameter> map = systemParameterByGroup.get(groupName);
			if (map == null) {
				map = new HashMap<String, SystemParameter>();
				systemParameterByGroup.put(groupName, map);
			}
			map.put(sparam.getSystemParameterId().getName(), sparam);

			// if (sparam.isEditable()) { -->> menjadi
			if (sparam.getEditable() == 1) {
				List<SystemParameter> list = editableSystemParameterByGroup.get(groupName);
				if (list == null) {
					list = new ArrayList<SystemParameter>();
					editableSystemParameterByGroup.put(groupName, list);
				}
				list.add(sparam);
			}

			if (sparam.getEditable() == 1 && !editableGroupList.contains(groupName)) {
				editableGroupList.add(groupName);
			}
		}
	}

	@Override
	public Map<String, SystemParameter> findMapByGroup(String group) {
		return systemParameterByGroup.get(group);
	}

	public String getStringValue(SystemParameter id, String defaultValue) {
		SystemParameter sparam = new SystemParameter();
		sparam = systemParameterById.get(id);
		if (sparam == null) {
			return defaultValue;
		}

		return sparam.getParamValue();
	}

	@Override
	public String getStringValue(String key, String defaultValue) {
		return getStringValue(createSystemParameterId(key), defaultValue);
	}

	@Override
	public String[] getStringArrayValue(String key, String defaultValue, String regexSeparator) {
		String valueString = getStringValue(createSystemParameterId(key), defaultValue);
		String[] valueStringSplited = valueString.split(regexSeparator);

		List<String> valueStringList = new ArrayList<String>();
		for (String valueStringEach : valueStringSplited) {
			valueStringEach = valueStringEach.trim();

			if (valueStringEach.length() > 0) {
				valueStringList.add(valueStringEach);
			}
		}

		return valueStringList.toArray(new String[0]);
	}

	@Override
	public int getIntValue(SystemParameterId id, int defaultValue) {
		try {
			return Integer.valueOf(getStringValue(id, String.valueOf(defaultValue)));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	@Override
	public int getIntValue(String key, int defaultValue) {
		return getIntValue(createSystemParameterId(key), defaultValue);
	}

	public long getLongValue(SystemParameter id, long defaultValue) {
		try {
			return Long.valueOf(getStringValue(id, String.valueOf(defaultValue)));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	@Override
	public long getLongValue(String key, long defaultValue) {
		return getLongValue(createSystemParameterId(key), defaultValue);
	}

	public boolean getBooleanValue(SystemParameter id, boolean defaultValue) {
		try {
			String valueString = getStringValue(id, String.valueOf(defaultValue));
			if (valueString != null && (valueString.equalsIgnoreCase("true") || valueString.equalsIgnoreCase("1"))) {
				return true;
			}

			return false;
		} catch (Exception e) {
			return defaultValue;
		}
	}

	@Override
	public boolean getBooleanValue(String key, boolean defaultValue) {
		return getBooleanValue(createSystemParameterId(key), defaultValue);
	}

	public BigDecimal getBigDecimalValue(SystemParameter id, BigDecimal defaultValue) {
		try {
			return new BigDecimal(getStringValue(id, String.valueOf(defaultValue)));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	@Override
	public BigDecimal getBigDecimalValue(String key, BigDecimal defaultValue) {
		return getBigDecimalValue(createSystemParameterId(key), defaultValue);
	}

	@Override
	public List<String> getEditableGroupList() {
		return editableGroupList;
	}

	@Override
	public List<SystemParameter> findEditableByGroupName(String groupName) {
		return editableSystemParameterByGroup.get(groupName);
	}

	// configuration
	private final Properties configurationProperties = new Properties();

	@Override
	public void addConfiguration(String key, String value) {
		configurationProperties.put(key, value);
	}

	@Override
	public String getStringConfiguration(String key, String defaultValue) {
		return configurationProperties.getProperty(key, defaultValue);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public AuditLog saveParameter(SystemParameter parameter, User user, String oldValue) {
		// update database
		systemParameterDao.saveOrUpdate(parameter);

		// save audit log
		return auditLogManager.save(ModelConstant.ReffNumType.PARAMETER, "", "auditLog.info.format.parameter",
				new Object[] { parameter.getSystemParameterId().getName(), oldValue, parameter.getParamValue() },
				user.getId(), new Date());
	}

	private volatile boolean paused = false;

	@Override
	public void doPause() {
		this.paused = true;
	}

	@Override
	public void doContinue() {
		this.paused = false;
	}

	@Override
	public void setPause(boolean paused) {
		this.paused = paused;
	}

	@Override
	public boolean isPaused() {
		return paused;
	}

	private SystemParameter createSystemParameterId(String input) {
		String inputSplited[] = input.split(":");

		SystemParameter systemParameter = new SystemParameter();
		if (inputSplited.length == 2) {
			systemParameter.getSystemParameterId().setGroup(inputSplited[0]);
			systemParameter.getSystemParameterId().setName(inputSplited[1]);
		} else {
			inputSplited = input.split("[.]");
			systemParameter.getSystemParameterId().setGroup(inputSplited[0]);
			systemParameter.getSystemParameterId().setName(input);
		}

		return systemParameter;
	}

	@Override
	public SystemParameter findById(String paramGroup, String paramName) {
		return systemParameterDao.findById(paramGroup, paramName);
	}

	@Override
	public SystemParameter findByParamNameAndParamGroup(String paramGroup, String paramName) {
		return findById(paramGroup, paramName);
	}

	@Override
	public int getIntValue(SystemParameter id, int defaultValue) {
		return new Integer(getStringValue(id.getSystemParameterId(), String.valueOf(defaultValue)));
	}

	@Override
	public String getStringValue(SystemParameterId id, String defaultValue) {
		SystemParameter ids = findSystemParameterById(id.getGroup(), id.getName());
		SystemParameter sparam = systemParameterById.get(ids);
		if (sparam == null) {
			return defaultValue;
		}

		return sparam.getParamValue();
	}

	@Override
	public long getLongValue(SystemParameterId id, long defaultValue) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getBooleanValue(SystemParameterId id, boolean defaultValue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BigDecimal getBigDecimalValue(SystemParameterId id, BigDecimal defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String findParamValueByParamName(String paramName) {
		return systemParameterDao.findParamValueByParamName(paramName);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public void insertSystemParameter(SystemParameter parameter) {

		systemParameterDao.insertSystemParameter(parameter);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public void updateSystemParameter(SystemParameter parameter) {

		systemParameterDao.updateSystemParameter(parameter);
	}

	@Override
	public SystemParameter findSystemParameterById(String paramGroup, String paramName) {

		SystemParameter sParam = systemParameterDao.findSystemParameterById(paramGroup, paramName);

		return sParam;
	}

	@Override
	public SystemParameter findScheduler(String paramGroup, String paramName) {

		SystemParameter sParam = systemParameterDao.findSystemParameterById(paramGroup, paramName);

		return sParam;
	}

	@Override
	public Object getDefaultDao() {

		return systemParameterDao;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		softReset();

	}

	@Override
	public SystemParameter findByParamName(String paramName) {

		SystemParameter sParam = systemParameterDao.findByParamName(paramName);

		return sParam;
	}

	@Override
	public Map<String, Object> findftpLogin(String ftp) {

		Map<String, Object> ftpLogin = new HashMap<String, Object>();
		ftpLogin.put("ip", systemParameterDao.findParamValueByParamName(ftp + ".ip"));
		ftpLogin.put("userName", systemParameterDao.findParamValueByParamName(ftp + ".userName"));
		ftpLogin.put("password", systemParameterDao.findParamValueByParamName(ftp + ".password"));
		ftpLogin.put("port", systemParameterDao.findParamValueByParamName(ftp + ".port"));

		return ftpLogin;
	}

	@Override
	public List<SystemParameter> findListByParamGroup(String paramGroup) {
		List<SystemParameter> sParamList = systemParameterDao.findListByParamGroup(paramGroup);
		return sParamList;
	}

	@Override
	public SystemParameter findByParamGroupAndValue(String paramGroup,
			String paramValue) {
		SystemParameter sParam = systemParameterDao.findByParamGroupAndValue(paramGroup, paramValue);
		return sParam;
	}

	@Override
	public SystemParameter findByParamGroupAndName(String paramGroup,
			String paramName) {
		SystemParameter sParam = systemParameterDao.findByParamGroupAndValue(paramGroup, paramName);
		return sParam;
	}

}
