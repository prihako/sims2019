/**
 *
 */
package com.balicamp.dao.hibernate.parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.dao.parameter.SystemParameterDao;
import com.balicamp.model.parameter.SystemParameter;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: SystemParameterDaoHibernate.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
@Repository
public class SystemParameterDaoHibernate extends AdminGenericDaoImpl<SystemParameter, String> implements
		SystemParameterDao {

	Log loging = LogFactory.getLog(SystemParameterDaoHibernate.class);

	public SystemParameterDaoHibernate() {
		super(SystemParameter.class);
	}

	@Override
	public SystemParameter findById(String paramGroup, String paramName) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("group", paramGroup);
		filter.put("name", paramName);
		return findSingleByFieldName(filter);
	}

	@Override
	public SystemParameter findByParamNameAndParamGroup(String paramGroup, String paramName) {
		return findById(paramGroup, paramName);
	}

	@Override
	public int getIntValue(SystemParameter id, String defaultValue) {
		int val = 0;
		Criteria crt = getSession().createCriteria(SystemParameter.class);
		crt.add(Restrictions.eq("group", id.getSystemParameterId().getGroup()))
				.add(Restrictions.eq("name", id.getSystemParameterId().getName()))
				.add(Restrictions.eq("paramValue", defaultValue));

		val = (Integer) crt.uniqueResult();
		return val;
	}

	@Override
	public int getIntValue(String paramGroup, String paramName) {
		int val = 0;
		Criteria crt = getSession().createCriteria(SystemParameter.class);
		crt.add(Restrictions.eq("group", paramGroup)).add(Restrictions.eq("name", paramName));

		val = (Integer) crt.uniqueResult();

		return val;
	}

	@Override
	public String getStringValue(SystemParameter id, String defaultValue) {
		// TODO : method ini belum dipakai di BE dan ISO
		return null;
	}

	@Override
	public String findParamValueByParamName(String paramName) {
		String paramVal = "";
		Query sql = getSession().createQuery("SELECT s.paramValue FROM SystemParameter s WHERE s.systemParameterId.name = :name");
		sql.setParameter("name", paramName);
		paramVal = (String) sql.uniqueResult();
		/*
		 * Criteria crt = getSession().createCriteria(SystemParameter.class).add(
		 * Restrictions.eq("paramName", paramName));
		 * sysParam = (SystemParameter) crt.uniqueResult();
		 */
		return paramVal;
	}

	@Override
	public void insertSystemParameter(SystemParameter parameter) {

		getHibernateTemplate().save(parameter);
		/*
		 * Query sql = getSession()
		 * .createQuery("INSERT INTO SystemParameter " +
		 * "(param_group, param_name, param_value, description, editable, hidden, locked, pattern) "
		 * +
		 * " SELECT (paramGroup, paramName, paramValue, description, editable, hidden, locked, pattern)"
		 * );
		 * sql.setParameter("paramGroup", parameter.getParamGroup());
		 * sql.setParameter("paramName", parameter.getParamName());
		 * sql.setParameter("paramValue", parameter.getParamValue());
		 * sql.setParameter("description", parameter.getDescription());
		 * sql.setParameter("editable", parameter.getEditable());
		 * sql.setParameter("hidden", parameter.getHidden());
		 * sql.setParameter("locked", parameter.getLocked());
		 * sql.setParameter("pattern", parameter.getPattern());
		 * sql.executeUpdate();
		 */

	}

	@Override
	public void updateSystemParameter(SystemParameter parameter) {
		getHibernateTemplate().update(parameter);
	}

	@Override
	public SystemParameter findSystemParameterById(String paramGroup, String paramName) {

		SystemParameter sParam = new SystemParameter();

		Query sql = getSession()
				.createQuery(
						"SELECT a FROM SystemParameter a WHERE a.systemParameterId.group=:group AND a.systemParameterId.name=:name");
		sql.setParameter("group", paramGroup);
		sql.setParameter("name", paramName);

		try {
			sParam = (SystemParameter) sql.uniqueResult();
		} catch (Exception e) {
			sParam = null;
		}

		return sParam;
	}

	@Override
	public SystemParameter findByParamName(String paramName) {

		SystemParameter sParam= new SystemParameter();

		Query sql = getSession().createQuery("SELECT s FROM SystemParameter s WHERE s.systemParameterId.name = :name");
		sql.setParameter("name", paramName);

		try {
			sParam=(SystemParameter)sql.uniqueResult();
		} catch (Exception e) {
			sParam=null;
		}
		return sParam;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemParameter> findListByParamGroup(String paramGroup) {

		List<SystemParameter> sParamList = new ArrayList<SystemParameter>();
		Query sql = getSession().createQuery("SELECT s FROM SystemParameter s WHERE s.systemParameterId.group = :group "
				+ "order by s.paramValue");
		sql.setParameter("group", paramGroup);

		try {
			sParamList=sql.list();
		} catch (Exception e) {
			sParamList=null;
		}
		return sParamList;
	}

	@Override
	public SystemParameter findByParamGroupAndValue(String paramGroup, String paramValue) {

		SystemParameter sParam= new SystemParameter();

		Query sql = getSession().createQuery("SELECT s FROM SystemParameter s WHERE s.systemParameterId.group = :group "
				+ "and s.paramValue = :value ");
		sql.setParameter("group", paramGroup);
		sql.setParameter("value", paramValue);

		try {
			sParam=(SystemParameter)sql.uniqueResult();
		} catch (Exception e) {
			sParam=null;
		}
		return sParam;
	}

	@Override
	public SystemParameter findByParamGroupAndName(String paramGroup, String paramName) {

		SystemParameter sParam= new SystemParameter();

		Query sql = getSession().createQuery("SELECT s FROM SystemParameter s WHERE s.systemParameterId.group = :group "
				+ "and s.systemParameterId.name = :name ");
		sql.setParameter("group", paramGroup);
		sql.setParameter("name", paramName);

		try {
			sParam=(SystemParameter)sql.uniqueResult();
		} catch (Exception e) {
			sParam=null;
		}
		return sParam;
	}

}
