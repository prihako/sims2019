package com.balicamp.webapp.action.webadmin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.EventListener;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.BeanPropertySelectionModel;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mx.AnalisaTransactionLogsDto;
import com.balicamp.model.mx.EndpointRcs;
import com.balicamp.model.mx.Endpoints;
import com.balicamp.model.mx.Transactions;
import com.balicamp.model.user.Role;
import com.balicamp.service.TransactionLogsManager;
import com.balicamp.service.TransactionsManager;
import com.balicamp.service.impl.mx.EndpointRcsManagerHibernate;
import com.balicamp.service.impl.mx.EndpointsManagerImpl;
import com.balicamp.service.impl.mx.RoutesManagerImpl;
import com.balicamp.util.DateUtil;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.action.report.ReportAnalisaTransactionLogsAction;
import com.balicamp.webapp.tapestry.PropertySelectionModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version 
 */
public abstract class AnalisaTransactionLogs extends BasePageList implements PageBeginRenderListener {

	protected final Log log = LogFactory.getLog(AnalisaTransactionLogs.class);

	@InjectSpring("transactionLogsManagerImpl")
	public abstract TransactionLogsManager getGridManager();

	@InjectSpring("transactionsManagerImpl")
	public abstract TransactionsManager getTransactionsManager();

	@InjectSpring("endpointsManagerImpl")
	public abstract EndpointsManagerImpl getEndpointsManager();

	@InjectSpring("routesManagerImpl")
	public abstract RoutesManagerImpl getRoutesManager();

	@InjectSpring("endpointRcsManagerImpl")
	public abstract EndpointRcsManagerHibernate getEndpointRcsManager();

	public abstract void setTransactionCode(Transactions code);

	public abstract Transactions getTransactionCode();

	public abstract void setResponseCodeChannel(EndpointRcs eRcs);

	public abstract EndpointRcs getResponseCodeChannel();

	public abstract void setResponseBillerCode(EndpointRcs eRcs);

	public abstract EndpointRcs getResponseBillerCode();

	public abstract void setChannelCode(String channelCode);

	public abstract String getChannelCode();

	public abstract Date getStartDate();

	public abstract void setStartDate(Date startDate);

	public abstract void setStartHour(String end);

	public abstract String getStartHour();

	public abstract void setStartMinute(String min);

	public abstract String getStartMinute();

	public abstract void setStartSecond(String sec);

	public abstract String getStartSecond();

	public abstract Date getEndDate();

	public abstract void setEndDate(Date endDate);

	public abstract void setEndHour(String end);

	public abstract String getEndHour();

	public abstract void setEndMinute(String min);

	public abstract String getEndMinute();

	public abstract void setEndSecond(String sec);

	public abstract String getEndSecond();

	public abstract String getKlienID();

	public abstract void setKlienID(String clientID);

	public abstract String getKlienName();

	public abstract void setKlienName(String clientName);

	public abstract String getInvoiceNo();

	public abstract void setInvoiceNo(String invoiceNo);

	public abstract String getEndpoint();

	public abstract void setEndpoint(String enpoint);

	public abstract String getBillerCode();

	public abstract void setBillerCode(String b);

	public abstract String getResponseCode();

	public abstract void setResponseCode(String responseCode);

	public abstract String getResponseBiller();

	public abstract void setResponseBiller(String responseBiller);

	public abstract void setTransactionId(String id);

	public abstract String getTransactionId();

	public abstract AnalisaTransactionLogsDto getTableRow();

	public abstract void setTableRow(AnalisaTransactionLogsDto row);

	public abstract void setFormattedRaw(String formattedRaw);

	public abstract String getFormattedRaw();

	public abstract void setRawKey(String key);

	public abstract BeanPropertySelectionModel getTransactionCodeOption();

	public abstract void setTransactionCodeOption(BeanPropertySelectionModel transactionCodeOption);

	public abstract BeanPropertySelectionModel getResponseCodeMandiriOption();

	public abstract void setResponseCodeMandiriOption(BeanPropertySelectionModel responseCodeMandiriOption);

	public abstract String getRawKey();

	public abstract AnalisaTransactionLogsDto getTransactionReport();

	public abstract void setNotFirstLoad(boolean firsloadFlag);

	public abstract boolean isNotFirstLoad();

	public abstract void setTestDate(Date dt);

	public abstract Date getTestDate();

	public List getTransactionLogs() {
		List<AnalisaTransactionLogsDto> list = (List<AnalisaTransactionLogsDto>) getFields().get("TRANSACTION_LOGS");
		return list;
	}

	public void doSearch2() {

		String errorMessage = validation();

		if (errorMessage != null) {
			addError(getDelegate(), "errorShadow", errorMessage, ValidationConstraint.CONSISTENCY);
			getFields().put("TRANSACTION_LOGS", new ArrayList<AnalisaTransactionLogsDto>());
			return;
		}

		String specialCharactersValidation = validationSpecialCharacters();
		if (specialCharactersValidation != null) {
			addError(getDelegate(), "errorShadow", specialCharactersValidation, ValidationConstraint.CONSISTENCY);
			getFields().put("TRANSACTION_LOGS", new ArrayList<AnalisaTransactionLogsDto>());
			return;
		}

		Date timestampStart = null;
		Date timestampEnd = null;
		if (getStartDate() != null && getEndDate() != null) {
			try {
				String startDate = DateUtil.convertDateToString(getStartDate(), "dd-MM-yyyy");
				if (getStartMinute() == null) {
					setStartMinute("00");
				}
				if (getStartHour() == null) {
					setStartHour("00");
				}
				if (getStartSecond() == null) {
					setStartSecond("00");
				}
				timestampStart = DateUtil.convertStringToDate("dd-MM-yyyy HH:mm:ss", startDate + " " + getStartHour()
						+ ":" + getStartMinute() + ":" + getStartSecond());

				String endDate = DateUtil.convertDateToString(getEndDate(), "dd-MM-yyyy");
				if (getEndMinute() == null) {
					setEndMinute("00");
				}
				if (getEndHour() == null) {
					setEndHour("00");
				}
				if (getEndSecond() == null) {
					setEndSecond("00");
				}
				timestampEnd = DateUtil.convertStringToDate("dd-MM-yyyy HH:mm:ss", endDate + " " + getEndHour() + ":"
						+ getEndMinute() + ":" + getEndSecond());
			} catch (Exception e) {
				// Untuk handle error saat parsing tanggal
				log.trace(e);
				String error = getText("leftmenu.analisaTransactionLogs.hourMinuteSecond.convertProses");
				addError(getDelegate(), "errorShadow", error, ValidationConstraint.CONSISTENCY);
				getFields().put("TRANSACTION_LOGS", new ArrayList<AnalisaTransactionLogsDto>());
				return;
			}
		}

		if (getTransactionId() == null && getChannelCode() == null && getResponseCode() == null)
			getFields().put("TRANSACTION_LOGS", new ArrayList<AnalisaTransactionLogsDto>());

		String trxCode = null;
		trxCode = getTransactionCode().getCode();

		String chanCode = getChannelCode();
		String respCodeChan = getResponseCodeChannel().getRc();
		if (chanCode.equalsIgnoreCase("All")) {
			chanCode = "";
		}
		if (respCodeChan.equalsIgnoreCase("All")) {
			respCodeChan = "";
		}

		EndpointRcs endRc = getResponseBillerCode();

		if (getCacheTableData() == null) {

			Set<String> invoice = null;
			if (getInvoiceNo() != null) {
				invoice = new HashSet<String>();
				invoice.add(getInvoiceNo());
			}
			List<AnalisaTransactionLogsDto> dataTransaction = getGridManager().findTransactionsPlusRcWithMap(
					getBillerCode(), trxCode, getKlienID(), invoice, timestampStart, timestampEnd, endRc, chanCode,
					respCodeChan);

			getFields().put("TRANSACTION_LOGS", dataTransaction);
		}

		if (getFields().get("TRANSACTION_LOGS") == null
				|| (((List<AnalisaTransactionLogsDto>) getFields().get("TRANSACTION_LOGS"))).size() < 1) {
			addError(getDelegate(), "errorShadow",
					getText("leftmenu.analisaTransactionLogs.errorMessage.emptySearchResult"),
					ValidationConstraint.CONSISTENCY);
		}

	}

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if (!pageEvent.getRequestCycle().isRewinding()) {
			if (!isNotFirstLoad()) {
				setNotFirstLoad(true);
				if (getFields() == null) {
					setFields(new HashMap());
				} else {
					if (getFields().get("viewLogs") == null) {
						getFields().remove("TRANSACTION_LOGS");
						responseCodeMandiriModel = null;
						transactionCodeModel = null;
						responseBillerCodeModel = null;
					} else {
						getFields().remove("viewLogs");
					}
				}
			}
		}

		/*
		 * // setting back all checkbox to all after user leave page
		 * if (!pageEvent.getRequestCycle().isRewinding()) {
		 * if (isNotFirstLoad()) {
		 * 
		 * if (getChannelCode() == null) {
		 * responseCodeMandiriModel = null;
		 * }
		 * 
		 * if (getBillerCode() == null) {
		 * transactionCodeModel = null;
		 * responseBillerCodeModel = null;
		 * }
		 * }
		 * }
		 */
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);
	}

	public IPropertySelectionModel responseCodeMandiriModel = null;

	public IPropertySelectionModel responseBillerCodeModel = null;

	public IPropertySelectionModel getResponseCodeMandiriModelOption() {
		List<EndpointRcs> listRcs = new ArrayList<EndpointRcs>();
		EndpointRcs temp = new EndpointRcs();
		temp.setRc("All");
		temp.setDescription("All");
		listRcs.add(temp);
		listRcs.addAll(getEndpointRcsManager().findByEndpointId(47));
		responseCodeMandiriModel = new BeanPropertySelectionModel(listRcs, "description");
		return responseCodeMandiriModel;
	}

	public IPropertySelectionModel getResponseBillerCodeModelOption() {
		List<EndpointRcs> listRcs = new ArrayList<EndpointRcs>();
		EndpointRcs temp = new EndpointRcs();
		temp.setRc("All");
		temp.setDescription("All");
		listRcs.add(temp);
		listRcs.addAll(getEndpointRcsManager().findByEndpointId(57));
		responseCodeMandiriModel = new BeanPropertySelectionModel(listRcs, "description");
		return responseCodeMandiriModel;
	}

	public IPropertySelectionModel transactionCodeModel = null;

	public IPropertySelectionModel getTransactionCodeModelOption() {

		if (transactionCodeModel != null) {
			return transactionCodeModel;
		} else {

			transactionCodeModel = new BeanPropertySelectionModel(getTransactionCodeWithSessionCriteria(), "name");

			return transactionCodeModel;
		}

	}

	public IPropertySelectionModel getResponseCodeMandiriModelOption2() {

		if (responseCodeMandiriModel != null) {
			return responseCodeMandiriModel;
		} else {
			EndpointRcs temp = new EndpointRcs();
			temp.setRc("All");
			temp.setDescription("All");
			// StringBuffer strBill = new StringBuffer("BIL");
			// StringBuffer str01 = new StringBuffer("01");
			List<EndpointRcs> listChannel = new ArrayList<EndpointRcs>();
			listChannel.add(temp);
			listChannel.addAll(getEndpointRcsManager().findAllChannelRc());
			for (int i = 0; i < listChannel.size(); i++) {
				if (listChannel.get(i).getRc() == null || listChannel.get(i).getRc() == "") {
					listChannel.remove(i);
				}
			}

			// List<String> roleNames = new ArrayList<String>();
			// Set<Role> roles = getUserLoginFromDatabase().getRoleSet();
			// for (Role tempRole : roles) {
			// roleNames.add(tempRole.getName());
			// }
			//
			// for (String roleName : roleNames) {
			// if (roleName.contains(strBill)) {
			// listChannel.addAll(getEndpointRcsManager().findAll());
			// } else if (roleName.contains(str01)) {
			// roleName = roleName.substring(0, roleName.length() - 2);
			// listChannel.addAll(getEndpointRcsManager().findAll());
			// } else {
			// listChannel.addAll(getEndpointRcsManager().findAll());
			// }
			// }

			responseCodeMandiriModel = new BeanPropertySelectionModel(listChannel, "description");
			return responseCodeMandiriModel;
		}

	}

	public IPropertySelectionModel getResponseBillerCodeModelOption2() {

		if (responseBillerCodeModel != null) {
			return responseBillerCodeModel;
		} else {
			responseBillerCodeModel = new BeanPropertySelectionModel(getResponseCodeBillerWithSessionCriteria(),
					"description");
			return responseBillerCodeModel;
		}
	}

	public void setTransactionCodeModel(String transactionType) {

		Transactions temp = new Transactions();
		temp.setCode("All");
		temp.setName("All");

		StringBuffer strBill = new StringBuffer("bil");
		StringBuffer str01 = new StringBuffer("01");
		List<Transactions> listTransaction = new ArrayList<Transactions>();
		listTransaction.add(temp);

		if (transactionType.contains(strBill)) {
			listTransaction.addAll(getTransactionsManager().findByCode("BILL"));
		} else if (transactionType.contains(str01)) {
			transactionType = transactionType.substring(0, transactionType.length() - 2);
			listTransaction.addAll(getTransactionsManager().findByCode(transactionType.toUpperCase()));
		} else if (transactionType.equalsIgnoreCase("all")) {
			listTransaction.addAll(getTransactionCodeWithSessionCriteria());
		} else {
			listTransaction.addAll(getTransactionsManager().findByCode(transactionType.toUpperCase()));
		}

		if (listTransaction.size() < 2) {
			listTransaction.addAll(getTransactionsManager().findAll());
		}

		transactionCodeModel = new BeanPropertySelectionModel(listTransaction, "name");
	}

	public List<Transactions> getTransactionCodeWithSessionCriteria() {
		Transactions temp = new Transactions();
		temp.setCode("All");
		temp.setName("All");

		StringBuffer strBill = new StringBuffer("BIL");
		StringBuffer str01 = new StringBuffer("01");
		List<Transactions> listTransaction = new ArrayList<Transactions>();
		listTransaction.add(temp);

		List<String> roleNames = new ArrayList<String>();
		Set<Role> roles = getUserLoginFromSession().getRoleSet();
		for (Role tempRole : roles) {
			roleNames.add(tempRole.getName());
		}

		for (String roleName : roleNames) {
			if (roleName.contains(strBill)) {
				listTransaction.addAll(getTransactionsManager().findByCode("BILL"));
			} else if (roleName.contains(str01)) {
				roleName = roleName.substring(0, roleName.length() - 2);
				listTransaction.addAll(getTransactionsManager().findByCode(roleName));
			} else {
				listTransaction.addAll(getTransactionsManager().findByCode(roleName));
			}
		}

		if (listTransaction.size() < 2) {
			listTransaction.addAll(getTransactionsManager().findAll());
		}

		return listTransaction;
	}

	public List<EndpointRcs> getResponseCodeBillerWithSessionCriteria() {
		List<EndpointRcs> listBiller = new ArrayList<EndpointRcs>();
		EndpointRcs temp = new EndpointRcs();
		listBiller.add(temp);
		temp.setRc("All");
		temp.setDescription("All");

		List<String> strBill = Arrays.asList("SKOR01", "BILS0", "REOR01", "PER01", "UNAR", "IAR", "PAP", "IKRAP");

		List<String> roleNames = new ArrayList<String>();
		Set<Role> roles = getUserLoginFromSession().getRoleSet();
		for (Role tempRole : roles) {
			roleNames.add(tempRole.getName());
		}

		for (String roleName : roleNames) {
			if (roleName.equals("ADMIN")) {
				listBiller.addAll(getEndpointRcsManager().findAllBillerRc());
			} else if (strBill.contains(roleName)) {
				listBiller.addAll(getEndpointRcsManager().findByEndpointCode(roleName.toLowerCase()));
			}
		}

		for (int i = 0; i < listBiller.size(); i++) {
			if (listBiller.get(i).getRc() == null || listBiller.get(i).getRc() == "") {
				listBiller.remove(i);
			}
		}
		return listBiller;
	}

	public void setResponseCodeBillerModel(String billerCode) {
		EndpointRcs rcs = new EndpointRcs();
		rcs.setRc("All");
		rcs.setDescription("All");
		List<EndpointRcs> listRescodeBiller = new ArrayList<EndpointRcs>();
		listRescodeBiller.add(rcs);
		Endpoints bc = new Endpoints();

		if (billerCode.equalsIgnoreCase("all")) {
			listRescodeBiller.addAll(getResponseCodeBillerWithSessionCriteria());
		} else {
			bc = getEndpointsManager().findEndpointsByCode(billerCode);
			listRescodeBiller.addAll(getEndpointRcsManager().findByEndpointId(bc.getId(), "N"));
			for (int i = 0; i < listRescodeBiller.size(); i++) {
				if (listRescodeBiller.get(i).getRc() == null || listRescodeBiller.get(i).getRc() == "") {
					listRescodeBiller.remove(i);
				}
			}
		}
		responseBillerCodeModel = new BeanPropertySelectionModel(listRescodeBiller, "description");
		System.out.println("Biller ID: " + bc.getId());
	}

	public void setResponseCodeChannelModel(String channelCode) {
		EndpointRcs rcs = new EndpointRcs();
		rcs.setRc("All");
		rcs.setDescription("All");
		List<EndpointRcs> listRescodeChannel = new ArrayList<EndpointRcs>();
		listRescodeChannel.add(rcs);
		Endpoints bc = new Endpoints();

		if (channelCode.equalsIgnoreCase("all")) {
			listRescodeChannel.addAll(getEndpointRcsManager().findAllChannelRc());
			for (int i = 0; i < listRescodeChannel.size(); i++) {
				if (listRescodeChannel.get(i).getRc() == null || listRescodeChannel.get(i).getRc() == "") {
					listRescodeChannel.remove(i);
				}
			}
		} else {
			bc = getEndpointsManager().findEndpointsByCode(channelCode);
			listRescodeChannel.addAll(getEndpointRcsManager().findByEndpointId(bc.getId(), "N"));
			for (int i = 0; i < listRescodeChannel.size(); i++) {
				if (listRescodeChannel.get(i).getRc() == null || listRescodeChannel.get(i).getRc() == "") {
					listRescodeChannel.remove(i);
				}
			}
		}
		responseCodeMandiriModel = new BeanPropertySelectionModel(listRescodeChannel, "description");
	}

	public List<AnalisaTransactionLogsDto> getTransactionList() {
		List<AnalisaTransactionLogsDto> list = (List<AnalisaTransactionLogsDto>) getFields().get("TRANSACTION_LOG");
		return list;
	}

	public IPropertySelectionModel getTransactionTypeModel() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("All", "All");

		List<Endpoints> channel = new ArrayList<Endpoints>();
		channel.addAll(getEndpointsManager().getAllEndpointsByType("channel"));

		for (Endpoints tempEndpoint : channel) {
			map.put(tempEndpoint.getCode(), tempEndpoint.getName());
		}

		return new PropertySelectionModel(getLocale(), map, false, false);
	}

	public IPropertySelectionModel getTransactionTypeModelBiller() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		Map<String, String> mapAll = new LinkedHashMap<String, String>();
		
		List<Endpoints> endpoints = new ArrayList<Endpoints>();

		// Check if session is null
		if (getUserLoginFromSession() == null) {
			try {
				getResponse().sendRedirect(getBaseUrl() + "/main.html");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Set<Role> roles = getUserLoginFromSession().getRoleSet();
		for (Role tempRole : roles) {
			if (getEndpointsManager().findEndpointsByCode(tempRole.getName().toLowerCase()) != null) {
				endpoints.add(getEndpointsManager().findEndpointsByCode(tempRole.getName().toLowerCase()));
			}
		}

		if (endpoints.size() > 0) {
			for (Endpoints tempEnd : endpoints) {
				map.put(tempEnd.getCode(), tempEnd.getName());
			}
		} else {
			List<Endpoints> endpointList = getEndpointsManager().getAllEndpointsByType("biller");
			// List<Endpoints> endpointList =
			// getEndpointsManager().getAllEndpointsByState("ready");
			for (Endpoints tempEndpoint : endpointList) {
				if (!tempEndpoint.getCode().equalsIgnoreCase("chws")
						&& !tempEndpoint.getCode().equalsIgnoreCase("chws2")) {
					map.put(tempEndpoint.getCode(), tempEndpoint.getName());
				}
			}

		}
		Map<String, String> mapSorted = sortByComparatorEndpointName(map);
		
		if(mapSorted.keySet().size() > 1){
			mapAll.put("All", "All");
			mapAll.putAll(mapSorted);
		}else{
			mapAll.putAll(mapSorted);
		}
		
		return new PropertySelectionModel(getLocale(), mapAll, false, false, "Y");
	}
	
	public static Map<String, String> sortByComparatorEndpointName(Map<String, String> unsortMap) {
		// Convert Map to List
		List<Map.Entry<String, String>> list = new LinkedList<Map.Entry<String, String>>(unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String,String>>() {
			public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
				return (o1.getValue().toString()).compareTo(o2.getValue().toString());
			}
		});

		// Convert sorted map back to a Map
		Map<String, String> sortedMap = new LinkedHashMap<String, String>();
		for (Iterator<Map.Entry<String, String>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, String> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	public static void printMap(Map<String, String> map) {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println("[Key] : " + entry.getKey() + " [Value] : " + entry.getValue());
		}
	}

	@EventListener(targets = "transactionTypeBiller", events = "onchange", submitForm = "monitoringTxForm2")
	public void changeTrxCode(IRequestCycle cycle) {

		// System.out.println("transactionTypeBiller called");
		// System.out.println("Transaction Type Biller: " + getBillerCode());
		// setResponseCodeBillerModel(getBillerCode());
		// setTransactionCodeModel(getBillerCode());
		// cycle.getResponseBuilder().updateComponent("transactionCodeField");
		// cycle.getResponseBuilder().updateComponent("responseBillerCodeField");

		if (!getBillerCode().equalsIgnoreCase("all")) {
			System.out.println("Transaction Type Biller: " + getBillerCode());
			setResponseCodeBillerModel(getBillerCode());
			setTransactionCodeModel(getBillerCode());

			cycle.getResponseBuilder().updateComponent("transactionCodeField");

			cycle.getResponseBuilder().updateComponent("responseBillerCodeField");
		} else {
			transactionCodeModel = null;

			setResponseCodeBillerModel(getBillerCode());
			setTransactionCodeModel(getBillerCode());

			cycle.getResponseBuilder().updateComponent("transactionCodeField");

			cycle.getResponseBuilder().updateComponent("responseBillerCodeField");
		}
	}

	@EventListener(targets = "transactionType", events = "onchange", submitForm = "monitoringTxForm2")
	public void changeChannelCode(IRequestCycle cycle) {

		System.out.println("transactionTypeChannel called");
		setResponseCodeChannelModel(getChannelCode());
		cycle.getResponseBuilder().updateComponent("responseCodeFieldMandiri");
		// if (!getChannelCode().equalsIgnoreCase("zall")) {
		// setResponseCodeChannelModel(getChannelCode());
		// cycle.getResponseBuilder().updateComponent("responseCodeFieldMandiri");
		// } else {
		// responseCodeMandiriModel = null;
		// cycle.getResponseBuilder().updateComponent("responseCodeFieldMandiri");
		// }
	}

	public IPage additionalLinkListener2(IRequestCycle cycle, String trxId, String klienId, String invoiceNo,
			Date startdate, Date endDate, String transactionType, Transactions transactionCode) {

		MessageLogsList messageLogsList = (MessageLogsList) cycle.getPage("messageLogsList");
		messageLogsList.setEditorState("visible");

		if (getStartDate() != null && getEndDate() != null) {
			Calendar calStart = Calendar.getInstance();
			Calendar calEnd = Calendar.getInstance();
			try {
				if (getStartDate() == null) {
					calStart.setTime(new Date());
				} else {
					calStart.setTime(getStartDate());
				}
				calStart.set(Calendar.HOUR_OF_DAY, Integer.valueOf(getStartHour()));
				calStart.set(Calendar.MINUTE, Integer.valueOf(getStartMinute()));
				calStart.set(Calendar.SECOND, Integer.valueOf(getStartSecond()));

				if (getEndDate() == null) {
					calEnd.setTime(new Date());
				} else {
					calEnd.setTime(getEndDate());
				}
				calEnd.set(Calendar.HOUR_OF_DAY, Integer.valueOf(getEndHour()));
				calEnd.set(Calendar.MINUTE, Integer.valueOf(getEndMinute()));
				calEnd.set(Calendar.SECOND, Integer.valueOf(getEndSecond()));
			} catch (Exception e) {
				calStart.setTime(new Date());
				calEnd.setTime(new Date());
			}

			messageLogsList.setStartDate2(calStart.getTime());
			messageLogsList.setEndDate2(calEnd.getTime());

			getFields().put("startDate2", calStart.getTime());
			getFields().put("endDate2", calEnd.getTime());
		}

		messageLogsList.setTransactionLogList((List<AnalisaTransactionLogsDto>) getFields().get("TRANSACTION_LOGS"));
		messageLogsList.setKlienID(getKlienID());
		messageLogsList.setInvoiceNo(getInvoiceNo());

		messageLogsList.setTransactionCode(transactionCode);
		messageLogsList.setTransactionType(transactionType);

		messageLogsList.setTransactionId(trxId);

		getFields().put("klienId", getKlienID());
		getFields().put("invoiceNo", getInvoiceNo());
		getFields().put("transactionCode", transactionCode);
		getFields().put("transactionType", transactionType);
		getFields().put("transactionLogList", (List<AnalisaTransactionLogsDto>) getFields().get("TRANSACTION_LOGS"));
		getFields().put("transactionId", trxId);

		System.out.println("klienId : " + getKlienID());
		System.out.println("invoiceNo : " + getInvoiceNo());

		return messageLogsList;
	}

	// public IPage additionalLinkListener(IRequestCycle cycle, String trxId) {
	//
	// MessageLogsList messageLogsList = (MessageLogsList)
	// cycle.getPage("messageLogsList");
	// messageLogsList.setEditorState("visible");
	// messageLogsList.setTransactionId(trxId);
	//
	// return messageLogsList;
	// }

	public IPage viewRawData(IRequestCycle cycle, String trxId) {
		RawTransactionView rtv = (RawTransactionView) cycle.getPage("rawTransactionView");
		List<String> msgLogs = getGridManager().findMessageLogsByTxId(trxId);
		StringBuffer buffer = new StringBuffer();

		TransactionLogDetail logDetail = (TransactionLogDetail) cycle.getPage("transactionLogDetail");

		for (String raw : msgLogs) {

			Properties prop = new Properties();
			try {
				prop.load(new StringReader(raw));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// System.out.println("VIEW RAW  CLIEN ID " +
			// prop.getProperty("/custom/clientID/text()"));
			// System.out.println("VIEW RAW COMPANY CODE " +
			// prop.getProperty("/custom/companyCode/text()"));
			// System.out.println("VIEW RAW TANGGAL " + prop.getProperty("#"));

			String transactionDateRAW = raw.substring(raw.indexOf("#"), raw.lastIndexOf("/"));
			String daateRAW = raw.substring(0, 30);

			// System.out.println("RAW Transaction Date  " +
			// transactionDateRAW);
			// System.out.println("View Raw Date  " + daateRAW);

			logDetail.setRawDate(daateRAW);
			logDetail.setRawCompanyCode(prop.getProperty("/custom/companyCode/text()"));
			logDetail.setRawTransactionCode(prop.getProperty("/info/transactionCode/text()"));
			logDetail.setRawTransactionType(prop.getProperty("/custom/transactionType/text()"));
			logDetail.setRawClientID(prop.getProperty("/custom/clientID/text()"));
			logDetail.setRawClientName(prop.getProperty("/custom/clientName/text()"));
			logDetail.setRawRouteId(prop.getProperty("/info/routeId/text()"));
			logDetail.setRawInvoiceNo(prop.getProperty("/custom/invoiceNumber/text()"));
			logDetail.setRawPeriodBegin(prop.getProperty("/custom/periodBegin/text()"));
			logDetail.setRawPeriodEnd(prop.getProperty("/custom/periodEnd/text()"));
			logDetail.setRawTransactionDate(prop.getProperty("/data/transactionDate/text()"));
			logDetail.setRawTransactionTime(prop.getProperty("/data/transactionTime/text()"));
			logDetail.setRawAmountTransaction(prop.getProperty("/data/amountTransaction/text()"));
			logDetail.setRawCurrencyCodeTransaction(prop.getProperty("/data/currencyCodeTransaction/text()"));
			logDetail.setRawResponseCode(prop.getProperty("/data/responseCode/text()"));
			logDetail.setRawChannelID(prop.getProperty("/custom/channelID/text()"));
			logDetail.setRawChannelCode(prop.getProperty("/info/channelCode/text()"));
			logDetail.setRawTransactionID(prop.getProperty("/info/transactionId/text()"));
			StringReader stringReader = new StringReader(raw);
			BufferedReader reader = new BufferedReader(stringReader);
			String str;
			buffer.append("<table  border='0' class='data'>");
			try {
				while ((str = reader.readLine()) != null) {
					String[] line = str.split("[=]", 2);
					if (line != null && line.length > 1) {
						line[0] = line[0].replaceFirst("/data/|/info/|/custom/", "");
						line[0] = line[0].replaceFirst("/text().*$", "");
						buffer.append("<tr><td><b>" + line[0] + "</b></td><td>=</td><td>"
								+ line[1].replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</td></tr>");
					} else if (line != null && line.length == 1) {
						buffer.append("<tr><td colspan='3'><b>" + str + "</b></td></tr>");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			buffer.append("<table>");
		}
		rtv.setFormattedRaw(buffer.toString());
		return logDetail;
	}

	public abstract void setSortedUniqueSet(List<AnalisaTransactionLogsDto> selectedIds);

	/**
	 * @return Data yg diseleksi pada grid.
	 */
	// public abstract Set<T> getUniqueSet();

	public abstract List<AnalisaTransactionLogsDto> getSortedUniqueSet();

	public void checkSelect(IRequestCycle cycle) {
		// if (getPage().getRequestCycle().isRewinding()) {
		// AnalisaTransactionLogsDto row = (AnalisaTransactionLogsDto)
		// getTableRow();
		// if (row.getSelected()) {
		// // getUniqueSet().add(row);
		// getSortedUniqueSet().add(row);
		// }
		// }
	}

	public IPage doExport() {
		if (getSortedUniqueSet() == null || getSortedUniqueSet().size() == 0) {
			addError(getDelegate(), (IFormComponent) null, getText("common.message.validation.noDataSelected"),
					ValidationConstraint.CONSISTENCY);
			return null;
		}
		if (getDelegate().getHasErrors())
			return null;
		String fileName = getGridManager().exportToWord(getSortedUniqueSet(), getRequest().getRealPath("/"),
				getUserLoginFromSession());
		download(fileName);
		return null;
	}

	public String validation() {

		String errorMessage = null;

		if (getStartDate() != null && getEndDate() == null) {

			errorMessage = getText("leftmenu.analisaTransactionLogs.endDate.empty");

		} else if (getStartDate() == null && getEndDate() != null) {

			errorMessage = getText("leftmenu.analisaTransactionLogs.startDate.empty");

		} else if (getStartDate() != null && getEndDate() != null) {

			try {
				String startDate = DateUtil.convertDateToString(getStartDate(), "dd-MM-yyyy");
				if (getStartMinute() == null) {
					setStartMinute("00");
				}
				if (getStartHour() == null) {
					setStartHour("00");
				}
				if (getStartSecond() == null) {
					setStartSecond("00");
				}

				Date timestampStart = DateUtil.convertStringToDate("dd-MM-yyyy HH:mm:ss", startDate + " "
						+ getStartHour() + ":" + getStartMinute() + ":" + getStartSecond());

				String endDate = DateUtil.convertDateToString(getEndDate(), "dd-MM-yyyy");
				if (getEndMinute() == null) {
					setEndMinute("00");
				}
				if (getEndHour() == null) {
					setEndHour("00");
				}
				if (getEndSecond() == null) {
					setEndSecond("00");
				}

				if (validationHourSecondsMinutes() != null) {
					return validationHourSecondsMinutes();
				}

				Date timestampEnd = DateUtil.convertStringToDate("dd-MM-yyyy HH:mm:ss", endDate + " " + getEndHour()
						+ ":" + getEndMinute() + ":" + getEndSecond());

				Calendar cal = Calendar.getInstance();
				cal.setTime(getStartDate());
				cal.add(Calendar.MONTH, 3);

				// if (!getEndDate().after(getStartDate())) {
				if ((timestampStart.getTime() - timestampEnd.getTime()) > 1) {

					errorMessage = getText("leftmenu.analisaTransactionLogs.endDateBiggerThanStartDate");

				} else if (getEndDate().after(cal.getTime())) {

					errorMessage = getText("leftmenu.analisaTransactionLogs.startDateEndDate.biggerThan3Month");

				}
			} catch (Exception e) {
				e.printStackTrace();
				errorMessage = getText("leftmenu.analisaTransactionLogs.timeFormat.wrongInput");
			}

		} else if (getKlienID() == null
				&& getInvoiceNo() == null
				&& getStartDate() == null
				&& getEndDate() == null
				&& ((getChannelCode() != null && getChannelCode().equalsIgnoreCase("all")) || getChannelCode() == null)
				&& ((getResponseCodeChannel() != null && getResponseCodeChannel().getDescription().equalsIgnoreCase(
						"all")) || getResponseCodeChannel() == null)
				&& ((getBillerCode() != null && getBillerCode().equalsIgnoreCase("all")) || getBillerCode() == null)
				&& ((getResponseBillerCode() != null && getResponseBillerCode().getDescription()
						.equalsIgnoreCase("all")) || getResponseBillerCode() == null)
				&& ((getTransactionCode() != null && getTransactionCode().getName().equalsIgnoreCase("all")) || getTransactionCode() == null)) {
			errorMessage = getText("leftmenu.analisaTransactionLogs.errorMessage.allFieldEmpty");
		}

		return errorMessage;

	}

	public String validationSpecialCharacters() {
		String errorMessage = null;
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);

		if (getInvoiceNo() != null) {
			if (p.matcher(getInvoiceNo()).find()) {
				errorMessage = getText("leftmenu.analisaTransactionLogs.invoice.containsSpecialCharacter");
			}
		}

		if (getClientId() != null) {
			if (p.matcher(getClientId()).find()) {
				if (errorMessage != null)
					errorMessage += getText("leftmenu.analisaTransactionLogs.clienId.containsSpecialCharacter");
				else
					errorMessage = getText("leftmenu.analisaTransactionLogs.clienId.containsSpecialCharacter");
			}
		}

		return errorMessage;
	}

	public List<Endpoints> sortEndpoints(List<Endpoints> list) {
		if (list.size() > 1) {
			Collections.sort(list, new Comparator<Endpoints>() {

				@Override
				public int compare(Endpoints end1, Endpoints end2) {
					return end1.getName().compareTo(end2.getName());
				}

			});
			return list;
		} else {
			return list;
		}
	}

	@SuppressWarnings("rawtypes")
	public Map sortByValue(Map unsortMap) {
		List list = new LinkedList(unsortMap.entrySet());

		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	public void exportToExcel(IRequestCycle cycle) throws JRException, IOException {

		List<AnalisaTransactionLogsDto> listData = getTransactionLogs();
		ReportAnalisaTransactionLogsAction reportAnalisaTransactionLogsAction = new ReportAnalisaTransactionLogsAction();
		String xlsReportPath = reportAnalisaTransactionLogsAction.createReport(putParams(), listData, "xls",
				getServletContext(), getGridManager().findParamValueByParamName());
		// download(xlsReportPath);
		String context = getBaseUrl();
		System.out.println("DOWNLOAD --> " + context + "/download?fileName=" + xlsReportPath + "&mode=simple");
		cycle.sendRedirect(context + "/download?fileName=" + xlsReportPath + "&mode=simple");

	}

	public void exportToPdf(IRequestCycle cycle) throws JRException, IOException {

		List<AnalisaTransactionLogsDto> listData = getTransactionLogs();
		ReportAnalisaTransactionLogsAction reportAnalisaTransactionLogsAction = new ReportAnalisaTransactionLogsAction();
		String pdfReportPath = reportAnalisaTransactionLogsAction.createReport(putParams(), listData, "pdf",
				getServletContext(), getGridManager().findParamValueByParamName());
		// download(pdfReportPath);
		String context = getBaseUrl();
		System.out.println("DOWNLOAD --> " + context + "/download?fileName=" + pdfReportPath + "&mode=simple");
		cycle.sendRedirect(context + "/download?fileName=" + pdfReportPath + "&mode=simple");

	}

	public Map putParams() {
		Map params = new HashMap<String, String>();

		Endpoints channel = getEndpointsManager().findEndpointsByCode(getChannelCode());
		Endpoints biller = getEndpointsManager().findEndpointsByCode(getBillerCode());

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		params.put("klienId", getKlienID());
		params.put("noInvoice", getInvoiceNo());
		// params.put("startDate", getStartDate().toString());
		params.put("startDate", getStartDate() == null ? "" : dateFormat.format(getStartDate()));
		params.put("startHour", getStartHour());
		params.put("startMinute", getStartMinute());
		params.put("startSecond", getStartSecond());
		// params.put("endDate", getEndDate().toString());
		params.put("endDate", getEndDate() == null ? "" : dateFormat.format(getEndDate()));
		params.put("endHour", getEndHour());
		params.put("endMinute", getEndMinute());
		params.put("endSecond", getEndSecond());
		if (channel != null) {
			params.put("trxType", channel.getName());
		} else {
			params.put("trxType", getChannelCode());
		}
		params.put("respCodeMandiri", getResponseCodeChannel().getDescription());
		if (biller != null) {
			params.put("trxTypeBiller", biller.getName());
		} else {
			params.put("trxTypeBiller", getBillerCode());
		}
		params.put("respBillerCode", getResponseBillerCode().getDescription());
		params.put("trxCode", getTransactionCode().getName());
		// ngambil gambar
		InputStream imgStream = getServletContext().getResourceAsStream("/WEB-INF/report/kominfo_2.png");
		params.put("realPath", imgStream);
		// params.put("realPath",
		// getServletContext().getRealPath("/WEB-INF/report/"));
		return params;
	}

	public String validationHourSecondsMinutes() {

		String errorMessage = null;

		if ((Integer.valueOf(getStartHour()) > 23) || (Integer.valueOf(getStartMinute()) > 59)
				|| (Integer.valueOf(getStartSecond()) > 59)) {
			errorMessage = getText("leftmenu.analisaTransactionLogs.hourMinuteSecond.outOfRange");
		}

		if ((Integer.valueOf(getEndHour()) > 23) || (Integer.valueOf(getEndMinute()) > 59)
				|| (Integer.valueOf(getEndSecond()) > 59)) {
			errorMessage = getText("leftmenu.analisaTransactionLogs.hourMinuteSecond.outOfRange");
		}

		return errorMessage;
	}

}
