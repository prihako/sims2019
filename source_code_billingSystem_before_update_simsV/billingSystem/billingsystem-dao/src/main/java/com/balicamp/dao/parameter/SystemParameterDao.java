/**
 *
 */
package com.balicamp.dao.parameter;

import java.util.List;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.parameter.SystemParameter;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: SystemParameterDao.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
public interface SystemParameterDao extends AdminGenericDao<SystemParameter, String> {

	public SystemParameter findById(String paramGroup, String paramName);

	public SystemParameter findByParamNameAndParamGroup(String paramGroup, String paramName);

	public int getIntValue(SystemParameter id, String defaultValue);

	public int getIntValue(String paramGroup, String paramName);

	public String getStringValue(SystemParameter id, String defaultValue);

	public String findParamValueByParamName(String paramName);

	public void insertSystemParameter(SystemParameter parameter);

	public void updateSystemParameter(SystemParameter parameter);

	public SystemParameter findSystemParameterById(String paramGroup, String paramName);

	public SystemParameter findByParamName(String paramName);

	public List<SystemParameter> findListByParamGroup(String paramGroup);

	public SystemParameter findByParamGroupAndValue(String paramGroup, String paramValue);

	public SystemParameter findByParamGroupAndName(String paramGroup, String paramName);
}
