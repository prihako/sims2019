package com.balicamp.webapp.action;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Bean;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.bean.EvenOdd;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.Hidden;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidationConstraint;

import test.Constants;
import test.MessageConstant;

import com.balicamp.model.constant.ModelConstant;
import com.balicamp.model.parameter.SystemParameterId;
import com.balicamp.model.user.User;
import com.balicamp.service.common.MessageSourceWrapper;
import com.balicamp.service.log.AuditLogManager;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.service.user.UserManager;
import com.balicamp.util.SecurityContextUtil;
import com.balicamp.webapp.tapestry.ValidationDelegate;

/**
 * @version $Id: AdminBasePage.java 517 2013-06-25 10:13:25Z rudi.sadria $
 */
public abstract class AdminBasePage extends org.apache.tapestry.html.BasePage {

	protected final Log log = LogFactory.getLog(getClass());

	protected final Log securityLog = LogFactory.getLog(Constants.Log.SECURITY_LOG);

	@InjectObject("service:tapestry.globals.HttpServletRequest")
	public abstract HttpServletRequest getRequest();

	@InjectObject("service:tapestry.globals.HttpServletResponse")
	public abstract HttpServletResponse getResponse();

	@InjectObject("engine-service:page")
	public abstract IEngineService getEngineService();

	@Persist("client")
	public abstract String getMessage();

	public abstract void setMessage(String message);

	@Persist("session")
	public abstract Object getParamList();

	public abstract void setParamList(Object paramList);

	@InjectObject("spring:userManager")
	public abstract UserManager getUserManager();

	@InjectObject("spring:auditLogManager")
	public abstract AuditLogManager getAuditLogManager();

	/*
	 * @InjectObject("spring:merchantManager") public abstract MerchantManager
	 * getMerchantManager();
	 */

	@InjectObject("spring:systemParameterManager")
	public abstract SystemParameterManager getSystemParameterManager();

	@InjectObject("spring:messageSourceWrapper")
	public abstract MessageSourceWrapper getMessageSourceWrapper();

	public Map<String, String> listTransactionType() {
		Map<String, String[]> trxList = MessageConstant.TransactionType.TRANSACTION_LIST;

		List<String[]> l = new ArrayList<String[]>();

		for (String key : trxList.keySet()) {
			l.add(new String[] { key, getText("messageConstant.transactionType." + key) });
		}

		Collections.sort(l, new Comparator<String[]>() {

			public int compare(String[] a, String[] b) {
				return a[1].compareTo(b[1]);
			}
		});

		Map<String, String> mapTrxType = new LinkedHashMap<String, String>();
		int size = l.size();

		for (int i = 0; i < size; ++i) {
			String[] el = l.get(i);

			mapTrxType.put(el[0], el[1]);
		}

		return mapTrxType;
	}

	public Map<String, String> listTransactionTypePlus(String... keys) {
		Map<String, String> ori = listTransactionType();

		for (String key : keys) {
			ori.put(key, getText("messageConstant.transactionType." + key));
		}

		return ori;
	}

	// @Persist("flash")
	// public abstract Serializable[] getParamList();
	// public abstract void setParamList(Serializable[] paramList);

	@Bean
	public abstract ValidationDelegate getDelegate();

	protected void addError(IValidationDelegate delegate, String componentId, String message,
			ValidationConstraint constraint) {
		IFormComponent component = (IFormComponent) getComponent(componentId);

		delegate.setFormComponent(component);

		try {
			delegate.record(message, constraint);
		} catch (NullPointerException ex) {
			delegate.setFormComponent(null);
			delegate.record(message, constraint);
		}
	}

	protected void addError(IValidationDelegate delegate, IFormComponent component, String message,
			ValidationConstraint constraint) {
		if (component != null)
			delegate.setFormComponent(component);

		delegate.record(message, constraint);
	}

	/**
	 * Convenience method to get the Configuration HashMap from the servlet
	 * context.
	 * 
	 * @return the user's populated form from the session
	 */
	@SuppressWarnings("unchecked")
	protected Map getConfiguration() {
		Map config = (HashMap) getServletContext().getAttribute(Constants.CONFIG);

		// so unit tests don't puke when nothing's been set
		if (config == null) {
			return new HashMap();
		}

		return config;
	}

	/**
	 * Convenience method for unit tests.
	 * 
	 * @return boolean
	 */
	public boolean hasErrors() {
		return (getSession().getAttribute("errors") != null);
	}

	public ServletContext getServletContext() {
		return getSession().getServletContext();
	}

	protected HttpSession getSession() {
		return getRequest().getSession();
	}

	public String getProtectedTextA(String plain, int suffixLen, String mark) {
		return null; // TextProtector.getInstance().getProtectedTextA(plain,
						// suffixLen, mark);
	}

	private static String revaluate(String msg) {
		if (msg == null) {
			return null;
		}

		int size = msg.length();
		int j = 0;

		Map<String, String> subs = new HashMap<String, String>();
		String exp;

		int i = msg.indexOf("${", 0);
		while ((i >= 0) && (i < size)) {
			j = msg.indexOf('}', i + 2);
			if (j < i) {
				break;
			}

			exp = msg.substring(i + 2, j);
			if (exp.startsWith("date:")) {
				String[] parts = exp.split(":");

				switch (parts.length) {
				case 2:
					subs.put("${" + exp + "}", new SimpleDateFormat(parts[1]).format(new Date()));
					break;
				case 3:
					subs.put("${" + exp + "}", new SimpleDateFormat(parts[1], new Locale(parts[2])).format(new Date()));
					break;
				}
			}

			i = msg.indexOf("${", j + 1);
		}

		for (Map.Entry<String, String> entry : subs.entrySet()) {
			msg = msg.replace(entry.getKey(), entry.getValue());
		}

		return msg;
	}

	public String getText(String key) {
		return revaluate(getMessages().getMessage(key));
	}

	public String getText(String key, Object arg) {
		if (arg == null) {
			return revaluate(getText(key));
		}

		if (arg instanceof String) {
			return revaluate(getMessages().format(key, arg));
		}

		if (arg instanceof Object[]) {
			return revaluate(getMessages().format(key, (Object[]) arg));
		}

		if (arg instanceof String[]) {
			return revaluate(getMessages().format(key, (String[]) arg));
		}

		log.error("argument '" + arg + "' not String or Object[] or String[]");
		return "";
	}

	// add
	public abstract boolean isRealRender();

	public abstract void setRealRender(boolean realRender);

	public void pageBeginRender(PageEvent pageEvent) {
		if (!pageEvent.getRequestCycle().isRewinding()) {
			if (getUserLoginFromSession() != null)
				getAuditLogManager().save(ModelConstant.ReffNumType.USER_ACTIVITY, "",
						"auditLog.info.format.accessPage",
						new Object[] { getPageName(), getRequest().getRemoteHost() },
						getUserLoginFromSession().getId(), new Date());
			if (!isRealRender()) {
				setRealRender(true);
			}
		} // else setRealRender(false); //added by Aditya (klo gak ada ini, trus
			// kapan false-nya?)

		// double sumbit
		if (isPreventDoubleSubmit() && !pageEvent.getRequestCycle().isRewinding()) {
			initDoubleSubmitKey();
		}
	}

	/**
	 * 
	 * @param cycle
	 * @param folder
	 * @param pageName
	 * @param pageClass
	 * @return
	 */
	protected IPage getPage(IRequestCycle cycle, String folder, String pageName, Class<?> pageClass) {
		IPage ipage = cycle.getPage(new StringBuilder(folder).append("/").append(pageName).toString());

		if (!pageClass.isInstance(ipage)) {
			// for unit test
			ipage = cycle.getPage(pageName);
		}
		return ipage;
	}

	public User getUserLoginFromSession() {
		return SecurityContextUtil.getCurrentUser();
	}

	public User getUserLoginFromDatabase(boolean initUserProperties) {
		User userLogin = (User) getRequestCacheValue("userLoginFromDatabase", "initUserLoginFromDatabase");
		if (initUserProperties && userLogin.getUserPropertiesObject() == null)
			getUserManager().loadUserProperties(userLogin, true);
		return userLogin;
	}

	public User getUserLoginFromDatabase() {
		return (User) getRequestCacheValue("userLoginFromDatabase", "initUserLoginFromDatabase");
	}

	public User initUserLoginFromDatabase() {
		Long currentUserId = SecurityContextUtil.getCurrentUserId();
		if (currentUserId == null) {
			return null;
		}
		return getUserManager().findById(currentUserId);
	}

	/**
	 * for object looping
	 * 
	 * @return
	 */
	public abstract Object getLoopObject();

	public abstract void setLoopObject(Object loopObject);

	/**
	 * for object looping index
	 * 
	 * @return
	 */
	public abstract int getLoopIndex();

	public abstract void setLoopIndex(int loopIndex);

	/**
	 * Common method for AutohirizationFlagging used to flagging when
	 * confirmation page called from authorization confirmation
	 * 
	 * @param flag
	 */
	public abstract void setAuthorizationFlag(boolean flag);

	public abstract boolean getAuthorizationFlag();

	/**
	 * Common method for Reverse Flag used to flagging when confirmation page
	 * called from authorization confirmation
	 * 
	 * @param flag
	 */
	public abstract void setReverseFlag(boolean flag);

	public abstract boolean getReverseFlag();

	/**
	 * flag if user authorized to view this page
	 * 
	 * @param pageAuthorization
	 */
	@Persist("client")
	public abstract void setPageAuthorization(boolean pageAuthorization);

	public abstract boolean getPageAuthorization();

	/**
	 * cek if user authorized to view this page
	 */

	/**
	 * base URL of this context
	 * 
	 * @return
	 */
	protected String getBaseUrl() {
		StringBuffer result = new StringBuffer();
		try {
			URL baseUrl = null;
			if (getRequest().getServerPort() == 80)
				baseUrl = new java.net.URL(getRequest().getScheme(), getRequest().getServerName(), getRequest()
						.getContextPath());
			else
				baseUrl = new java.net.URL(getRequest().getScheme(), getRequest().getServerName(), getRequest()
						.getServerPort(), getRequest().getContextPath());

			result.append(baseUrl.toString());
		} catch (MalformedURLException e) {
			log.error("", e);
		}
		return result.toString();
	}

	public Format getMoneyFormat() {
		return (Format) getRequestCacheValue("moneyFormat", "initMoneyFormat");
	}

	public Format initMoneyFormat() {
		// DecimalFormat decimalFormat = new DecimalFormat("###,###");

		// Dibuat seperti yg dibawah agar tidak banyak merubah codingan (yg Atas
		// asli-nya)
		DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
		decimalFormat.setNegativePrefix("(");
		decimalFormat.setNegativeSuffix(")");

		DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
		unusualSymbols.setDecimalSeparator(',');
		unusualSymbols.setGroupingSeparator('.');
		decimalFormat.setDecimalFormatSymbols(unusualSymbols);

		return decimalFormat;
	}

	public Format getMoneyFormatDecimal() {
		return (Format) getRequestCacheValue("moneyFormatDecimal", "initMoneyFormatDecimal");
	}

	public Format initMoneyFormatDecimal() {
		DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
		decimalFormat.setNegativePrefix("(");
		decimalFormat.setNegativeSuffix(")");

		DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
		unusualSymbols.setDecimalSeparator(',');
		unusualSymbols.setGroupingSeparator('.');
		decimalFormat.setDecimalFormatSymbols(unusualSymbols);

		return decimalFormat;
	}

	public Format getDateFormat() {
		return (Format) getRequestCacheValue("dateFormat", "initDateFormat");
	}

	public Format getTimeFormat() {
		return (Format) getRequestCacheValue("timeFormat", "initTimeFormat");
	}

	public Format getNumberFormat() {
		return (Format) getRequestCacheValue("numberFormat", "initNumberFormat");
	}

	public Format initDateFormat() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-YYYY");
		return dateFormat;
	}

	public Format initTimeFormat() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		return dateFormat;
	}

	public Format initNumberFormat() {
		DecimalFormat decimalFormat = new DecimalFormat("###,##0");
		decimalFormat.setNegativePrefix("(");
		decimalFormat.setNegativeSuffix(")");

		DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
		unusualSymbols.setGroupingSeparator('.');
		decimalFormat.setDecimalFormatSymbols(unusualSymbols);

		return decimalFormat;
	}

	// form session
	@Persist("session")
	public abstract Map<String, Object> getSessionPropertyValue();

	public abstract void setSessionPropertyValue(Map<String, Object> sessionPropertyValue);

	/**
	 * add session props to sessionPropertyValue
	 * 
	 * @param key
	 * @param value
	 */
	public void addSessionPropertyValue(String key, Object value) {
		if (getSessionPropertyValue() == null) {
			try {
				setSessionPropertyValue(new HashMap<String, Object>());
			} catch (Exception e) {
				// ignore
			}
		}
		if (getSessionPropertyValue() != null) {
			getSessionPropertyValue().put(key, value);
		}
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getSessionPropertyValue(String key) {
		if (getSessionPropertyValue() == null) {
			return null;
		}
		return getSessionPropertyValue().get(key);
	}

	/**
	 * remove session props from sessionPropertyValue
	 * 
	 * @param key
	 */
	public void removeSessionPropertyValue(String key) {
		if (getSessionPropertyValue() == null)
			return;
		getSessionPropertyValue().remove(key);
	}

	/**
	 * synchronize page properties with sessionPropertyValue
	 * 
	 * @param propertyName
	 */
	public void syncSessionPropertyValue(String propertyName) {
		try {
			Object tmpPropertyValue = PropertyUtils.getProperty(this, propertyName);
			if (tmpPropertyValue == null)
				removeSessionPropertyValue(propertyName);
			else
				addSessionPropertyValue(propertyName, tmpPropertyValue);
		} catch (Exception e) {// NOPMD
			// ignore exception may be props not exist
			e.printStackTrace();
		}
	}

	private String errorValue;

	public void setErrorValue(String errorVal) {
		this.errorValue = errorVal;
	}

	public String getErrorValue() {
		return this.errorValue;
	}

	/**
	 * synchronize page properties with sessionPropertyValue
	 * 
	 * @param propertyName
	 */
	public void syncSessionPropertyValue(String[] propertyNameList) {
		if (getSessionPropertyValue() == null && getRequest().getRequestURL().indexOf(".html") != -1)
			return;

		for (String propertyName : propertyNameList) {
			syncSessionPropertyValue(propertyName);
		}
	}

	/**
	 * synchronize page properties with sessionPropertyValue
	 * 
	 * @param propertyName
	 */
	public void syncSessionPropertyValue(String sessionName, String propertyName) {
		try {
			Object tmpPropertyValue = PropertyUtils.getProperty(this, propertyName);
			if (tmpPropertyValue == null)
				removeSessionPropertyValue(sessionName);
			else
				addSessionPropertyValue(sessionName, BeanUtils.cloneBean(tmpPropertyValue));
		} catch (Exception e) {// NOPMD
			// ignore exception may be props not exist
		}
	}

	/**
	 * synchronize page properties with sessionPropertyValue
	 * 
	 * @param propertyName
	 */
	public void syncSessionPropertyValue(String[][] propertySessionNameList) {
		for (String[] propertySessionName : propertySessionNameList) {
			syncSessionPropertyValue(propertySessionName[0], propertySessionName[1]);
		}
	}

	/**
	 * populate sessionPropertyValue to page properties
	 * 
	 * @param propertyName
	 */
	public void populateSessionPropertyValue() {
		if (getSessionPropertyValue() == null)
			return;
		for (Entry<String, Object> entry : getSessionPropertyValue().entrySet()) {
			try {
				PropertyUtils.setSimpleProperty(this, entry.getKey(), entry.getValue());
			} catch (Exception e) {// NOPMD
				// ignore exception if fail set initial props value, may be
				// property does not exist
			}
		}
	}

	// double submit
	public boolean isPreventDoubleSubmit() {
		try {
			IComponent component = getComponent("doubleSubmitKeyField");
			return ((component != null) && (component instanceof Hidden));
		} catch (Exception e) {
			// do nothing
		}

		return false;
	}

	public abstract String getDoubleSubmitKey();

	public abstract void setDoubleSubmitKey(String doubleSubmitKey);

	public void initDoubleSubmitKey() {
		setDoubleSubmitKey(UUID.randomUUID().toString());
		addSessionPropertyValue(getDoubleSubmitKey(), true);
	}

	public void cekDoubleSubmit() {/*
									 * // state tidak valid if
									 * (getDoubleSubmitKey() == null) {
									 * getRequestCycle
									 * ().sendRedirect("doubleSubmit.jsp");
									 * //FIXME: throw new ApplicationException(
									 * "pemanggilan cekDoubleSubmit tetapi doubleSubmitKey null"
									 * ); } else { Boolean cekBoolean =
									 * (Boolean)
									 * getSessionPropertyValue(getDoubleSubmitKey
									 * ()); if (cekBoolean == null) {
									 * getRequestCycle
									 * ().sendRedirect("doubleSubmit.jsp");
									 * //FIXME: throw new
									 * DoubleSubmitException("double submit"); }
									 * // remove
									 * removeSessionPropertyValue(getDoubleSubmitKey
									 * ()); }
									 */
	}

	@Bean
	public abstract EvenOdd getEvenOddTable();

	/**
	 * untuk mencache data pada request
	 */
	public abstract Map<String, Object> getRequestCache();

	public abstract void setRequestCache(Map<String, Object> requestCache);

	protected void addRequestCache(String key, Object value) {
		if (getRequestCache() == null)
			setRequestCache(new HashMap<String, Object>());
		getRequestCache().put(key, value);
	}

	protected Object getRequestCacheValue(String key, String createMethod) {
		if (getRequestCache() == null)
			setRequestCache(new HashMap<String, Object>());

		if (getRequestCache().get(key) == null) {
			try {
				addRequestCache(key, MethodUtils.invokeMethod(this, createMethod, new Object[0]));
			} catch (Exception e) {
				log.error("", e);
			}
		}
		return getRequestCache().get(key);
	}

	/**
	 * userId = 4. getModel().setUserId(4); userId -> getModel().getUserId();
	 * 
	 * @param expression
	 * @return
	 * @throws OgnlException
	 */
	protected Object executeOgnlExpression(String expression, Object object, Class<?> resultType) throws OgnlException {
		Object tree = Ognl.parseExpression(expression);
		return Ognl.getValue(tree, object, resultType);
	}

	public int getPageSize() {
		Integer pageSize = (Integer) getRequestCacheValue("pageSize", "initPageSize");
		if (pageSize != null)
			return pageSize.intValue();
		return 30;
	}

	public int initPageSize() {
		// FIXME:
		return getSystemParameterManager().getIntValue(
				new SystemParameterId(Constants.SystemParameter.Page.GROUP,
						Constants.SystemParameter.Page.TABLE_PAGESIZE), 30);

		// return 30;
	}

	protected void download(String file) {
		log.info("downloading : " + file);
		HttpServletResponse respon = getResponse();
		try {
			String context = getBaseUrl();
			respon.sendRedirect(context + "/download?fileName=" + file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static final public String SESSION_BASKET = "SESSION_BASKET";

	private synchronized HashMap<String, Object> getSessionMap() {
		HashMap<String, Object> map = null;
		Object obj = getSession().getAttribute(SESSION_BASKET);
		if (obj == null) {
			map = new HashMap<String, Object>();
			getSession().setAttribute(SESSION_BASKET, map);
		} else {
			map = (HashMap) obj;
		}
		return map;
	}

	/**
	* Encapsulates save to session function
	* @param object
	* @param Key
	*/
	protected void saveObjectToSession(Object object, String Key) {
		getSessionMap().put(Key, object);
	}

	/**
	 * Encapsulates get object from session
	 * @param key
	 * @return
	 */
	protected Object getObjectfromSession(String key) {
		return getSessionMap().get(key);
	}

	final private String KEY_FIELDS = "FIELDS_MAP";

	public void setFields(Map map) {
		saveObjectToSession(map, KEY_FIELDS);
	}

	/**
	 * Common method for fields (map) now use session to handle the value no longer handled by page
	 * to perform fast render
	 * @param map
	 */
	public Map getFields() {
		return (Map) getObjectfromSession(KEY_FIELDS);
	}
}
