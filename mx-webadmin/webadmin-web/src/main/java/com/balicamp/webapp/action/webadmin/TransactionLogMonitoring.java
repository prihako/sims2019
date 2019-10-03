package com.balicamp.webapp.action.webadmin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.EventListener;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.balicamp.model.mx.AnalisaTransactionLogsDto;
import com.balicamp.model.mx.TransactionLog;
import com.balicamp.service.TransactionLogsManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.tapestry.PropertySelectionModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class TransactionLogMonitoring extends AdminBasePage implements PageBeginRenderListener,
		PageEndRenderListener {

	public abstract String getCriteria();

	public abstract void setCriteria(String criteria);

	public abstract String getKlienID();

	public abstract void setKlienID(String clientID);

	public abstract String getInvoiceNo();

	public abstract void setInvoiceNo(String invoiceNo);

	public abstract Date getStartDate();

	public abstract void setStartDate(Date startDate);

	public abstract Date getEndDate();

	public abstract void setEndDate(Date endDate);

	public abstract String getTransactionType();

	public abstract void setTransactionType(String transactionType);

	public abstract String getEndpoint();

	public abstract void setEndpoint(String enpoint);

	public abstract String getResponseCode();

	public abstract void setResponseCode(String responseCode);

	public abstract String getRawClientName();

	public abstract void setRawClientName(String str);

	public abstract String getRawClientID();

	public abstract void setRawClientID(String str);

	public abstract String getRawCompanyCode();

	public abstract void setRawCompanyCode(String str);

	public abstract String getRawPeriodBegin();

	public abstract void setRawPeriodBegin(String str);

	public abstract String getRawInvoiceNo();

	public abstract void setRawInvoiceNo(String str);

	public abstract String getRawTransactionCode();

	public abstract void setRawTransactionCode(String str);

	public abstract String getRawTransactionType();

	public abstract void setRawTransactionType(String str);

	public abstract String getRawRouteId();

	public abstract void setRawRouteId(String str);

	public abstract String getRawTransactionDate();

	public abstract void setRawTransactionDate(String str);

	public abstract AnalisaTransactionLogsDto getTransactionLog();

	public abstract void setTransactionLog(AnalisaTransactionLogsDto dto);

	public abstract void setNotFirstLoad(boolean firsloadFlag);

	public abstract boolean isNotFirstLoad();

	public abstract AnalisaTransactionLogsDto getTableRow();

	public abstract void setTableRow(AnalisaTransactionLogsDto row);

	@InjectSpring("transactionLogsManagerImpl")
	public abstract TransactionLogsManager getTransactionLogsManager();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {

		super.pageBeginRender(pageEvent);
		System.out.println("User  " + getUserLoginFromSession().getUserName());
		System.out.println("User Role " + getUserLoginFromSession().getUserRoles());
		if (!pageEvent.getRequestCycle().isRewinding()) {
			if (!isNotFirstLoad()) {
				setNotFirstLoad(true);
				if (getFields() == null) {
					setFields(new HashMap());
				} else if (getFields() != null) {
					getFields().remove("TRANSACTION_LOG");
				}
			}
		}

	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);

	}

	public List<AnalisaTransactionLogsDto> getTransactionList() {
		List<AnalisaTransactionLogsDto> list = (List<AnalisaTransactionLogsDto>) getFields().get("TRANSACTION_LOG");
		return list;
	}

	public IPropertySelectionModel getTransactionTypeModel() {
		HashMap<String, String> map = new HashMap<String, String>();

		if (getUserLoginFromSession().getUserRoles().equals("BHP_FREK")) {
			map.put("00", "BHP Frekuensi");

		} else if (getUserLoginFromSession().getUserRoles().equals("SER_PRKT")) {
			map.put("01", "Sertifikat Perangkat");

		} else if (getUserLoginFromSession().getUserRoles().equals("SKOR")) {
			map.put("02", "SKOR");

		} else if (getUserLoginFromSession().getUserRoles().equals("REOR")) {
			map.put("03", "REOR");

		} else {
			map.put("0", "All");
			map.put("00", "BHP Frekuensi");
			map.put("01", "Sertifikat Perangkat");
			map.put("02", "SKOR");
			map.put("03", "REOR");
		}

		return new PropertySelectionModel(getLocale(), map, false, false);
	}

	public IPropertySelectionModel getEndpointModel() {

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("All", "All");
		map.put("BMRI", "Bank Mandiri");
		map.put("BHP Frekuensi", "BHP Frekuensi");
		map.put("Sertifikat Perangkat", "Sertifikat Perangkat");
		map.put("SKOR", "SKOR");
		map.put("REOR", "REOR");

		return new PropertySelectionModel(getLocale(), map, false, false);
	}

	public IPropertySelectionModel getResponseCodeModel() {

		if (getResponseModel != null) {
			return getResponseModel;
		} else {
			HashMap<String, String> map = new HashMap<String, String>();

			map.put("0", "All");
			map.put("00", "Sukses");
			map.put("01", "Gagal");
			map.put("13", "Nilai tagihan tidak valid");
			map.put("50", "Reversal Gagal");
			map.put("25", "No Invoice tidak valid");
			map.put("26", "Kode Klien tidak valid");
			map.put("97", "Tagihan sudah dibayar");
			map.put("ZZ", "Timeout");
			map.put("SU", "Service Unavailable");
			return new PropertySelectionModel(getLocale(), map, false, false);

		}

	}

	public IPropertySelectionModel getResponseModel = null;

	public void setResponseCodeModel(String responseCode) {
		HashMap<String, String> map = new HashMap<String, String>();

		if (responseCode == "BMRI") {
			map.put("0", "All");
			map.put("00", "00 - Sukses");
			map.put("01", "01 - System tidak bisa melayani transaksi");
			map.put("B5", "B5 - Tagihan tidak ditemukan/No referensi salah");
			map.put("B8", "B8 - Tagihan sudah dibayar");
			map.put("13", "13 - Nilai tagihan tidak valid");
			map.put("89", "89 - Timeout");
			map.put("91", "91 - Link down");

		} else {
			map.put("0", "All");
			map.put("00", "00 - Sukses");
			map.put("01", "01 - Gagal");
			map.put("13", "13 - Nilai tagihan tidak valid");
			map.put("50", "50 - Reversal Gagal");
			map.put("25", "25 - No Invoice tidak valid");
			map.put("26", "26 - Kode Klien tidak valid");
			map.put("97", "97 - Tagihan sudah dibayar");
			map.put("ZZ", "ZZ - Timeout");
			map.put("SU", "SU - Service Unavailable");
		}

		getResponseModel = new PropertySelectionModel(getLocale(), map, false, false);

	}

	@EventListener(targets = "endpoint", events = "onchange", submitForm = "myForm")
	public void watchText(IRequestCycle cycle) {

		if (getEndpoint() != null) {

			setResponseCodeModel(getEndpoint());
			cycle.getResponseBuilder().updateComponent("responseCode");
		} else {
			getResponseModel = null;
			cycle.getResponseBuilder().updateComponent("responseCode");
		}
	}

	public void logDetail(IRequestCycle cycle, String raw) {

		TransactionLogDetail logDetail = (TransactionLogDetail) cycle.getPage("transactionLogDetail");

		getRawMessageInvoiceNo(raw);// INVOICENO
		getRawMessageClientID(raw);// CLIENTID
		getRawMessageTransactionCode(raw); // RESPONSECODE
		getRawMessageTransactionType(raw);// TRANSACTION TYPE
		getRawMessagePeriodBegin(raw);// PERIODBEGIN
		getRawMessagePeriodEnd(raw);// PERIODEND
		getRawMessageRouteId(raw);// ROUTEID
		getRawMessageClientName(raw);// CLIENTNAME
		getRawMessageTransactionDate(raw);// TRANSACTIONDATE

		logDetail.setRawClientID(getRawMessageClientID(raw));
		logDetail.setRawClientName(getRawMessageClientName(raw));
		logDetail.setRawInvoiceNo(getRawMessageInvoiceNo(raw));
		logDetail.setRawTransactionCode(getRawMessageTransactionCode(raw));
		logDetail.setRawTransactionType(getRawMessageTransactionType(raw));
		logDetail.setRawTransactionDate(getRawMessageTransactionDate(raw));
		logDetail.setRawRouteId(getRawMessageRouteId(raw));
		logDetail.setRawPeriodBegin(getRawMessagePeriodBegin(raw));
		logDetail.setRawPeriodEnd(getRawMessagePeriodEnd(raw));
		cycle.activate(logDetail);

	}

	public String getRawMessageInvoiceNo(String raw) {
		String invoiceNoRAW = raw.substring(raw.lastIndexOf("/custom/invoiceNumber/text()="),
				raw.lastIndexOf("custom/channelID/text()"));
		System.out.println("RAW invoice no  " + invoiceNoRAW);

		String invoiceNo = invoiceNoRAW.substring(invoiceNoRAW.lastIndexOf("=") + 1, invoiceNoRAW.length() - 1);
		System.out.println("invoice no  " + invoiceNo);

		setRawInvoiceNo(invoiceNo);
		return invoiceNo;
	}

	public String getRawMessageClientID(String raw) {
		String clientIdRAW = raw.substring(raw.lastIndexOf("/custom/clientID/text()="),
				raw.lastIndexOf("info/result/order/text()"));
		System.out.println("RAW Client ID  " + clientIdRAW);

		String clientID = clientIdRAW.substring(clientIdRAW.lastIndexOf("=") + 1, clientIdRAW.length() - 1);
		System.out.println("client id  " + clientID);

		setRawClientID(clientID);

		return clientID;
	}

	public String getRawMessageCompanyCode(String raw) {
		String companycodeRAW = raw.substring(raw.lastIndexOf("/custom/companyCode/text()="),
				raw.lastIndexOf("info/backToChannel/text()"));
		System.out.println("RAW Company Code  " + companycodeRAW);

		String companycode = companycodeRAW.substring(companycodeRAW.lastIndexOf("=") + 1, companycodeRAW.length() - 1);
		System.out.println("companyCode  " + companycode);

		setRawCompanyCode(companycode);

		return companycode;
	}

	public String getRawMessageTransactionCode(String raw) {
		String transactioncodeRAW = raw.substring(raw.lastIndexOf("/info/transactionCode/text()="),
				raw.lastIndexOf("custom/billName/text()"));
		System.out.println("RAW Transaction Code  " + transactioncodeRAW);

		String transactioncode = transactioncodeRAW.substring(transactioncodeRAW.lastIndexOf("=") + 1,
				transactioncodeRAW.length() - 1);
		System.out.println("transactionCode  " + transactioncode);

		setRawTransactionCode(transactioncode);
		return transactioncode;
	}

	public String getRawMessageTransactionType(String raw) {
		String transactiontypeRAW = raw.substring(raw.lastIndexOf("/info/transactionType/text()="),
				raw.lastIndexOf("data/amountTransaction/text()"));
		System.out.println("RAW Transaction type  " + transactiontypeRAW);

		String transactiontype = transactiontypeRAW.substring(transactiontypeRAW.lastIndexOf("=") + 1,
				transactiontypeRAW.length() - 1);
		System.out.println("transactionType  " + transactiontype);

		setRawTransactionType(transactiontype);

		return transactiontype;
	}

	public String getRawMessagePeriodBegin(String raw) {
		String periodBeginRAW = raw.substring(raw.lastIndexOf("/custom/periodBegin/text()"),
				raw.lastIndexOf("info/channelMappingCode/text()"));
		System.out.println("RAW Period Begin  " + periodBeginRAW);

		String periodbegin = periodBeginRAW.substring(periodBeginRAW.lastIndexOf("=") + 1, periodBeginRAW.length() - 1);
		System.out.println("periodBegin  " + periodbegin);

		setRawPeriodBegin(periodbegin);

		return periodbegin;
	}

	public String getRawMessagePeriodEnd(String raw) {
		String periodEndRAW = raw.substring(raw.lastIndexOf("/custom/periodEnd/text()="),
				raw.lastIndexOf("info/project/text()"));
		System.out.println("RAW Period End  " + periodEndRAW);

		String periodEnd = periodEndRAW.substring(periodEndRAW.lastIndexOf("=") + 1, periodEndRAW.length() - 1);
		System.out.println("periodEnd  " + periodEnd);

		setRawPeriodBegin(periodEnd);

		return periodEnd;
	}

	public String getRawMessageRouteId(String raw) {
		String routeIdRAW = raw.substring(raw.lastIndexOf("/info/routeId/text()="),
				raw.lastIndexOf("data/transactionTime/text()"));
		System.out.println("RAW Route Id  " + routeIdRAW);

		String routeid = routeIdRAW.substring(routeIdRAW.lastIndexOf("=") + 1, routeIdRAW.length() - 1);
		System.out.println("routeId  " + routeid);

		setRawRouteId(routeid);

		return routeid;
	}

	public String getRawMessageClientName(String raw) {
		String clientNameRAW = raw.substring(raw.lastIndexOf("/custom/clientName/text()="),
				raw.lastIndexOf("data/currencyCodeTransaction/text()"));
		System.out.println("RAW Client Name  " + clientNameRAW);

		String clientName = clientNameRAW.substring(clientNameRAW.lastIndexOf("=") + 1, clientNameRAW.length() - 1);
		System.out.println("clientName  " + clientName);

		setRawClientName(clientName);
		return clientName;
	}

	public String getRawMessageTransactionDate(String raw) {
		String transactionDateRAW = raw.substring(raw.lastIndexOf("/data/transactionDate/text()="),
				raw.lastIndexOf("custom/periodEnd/text()"));
		System.out.println("RAW Transaction Date  " + transactionDateRAW);

		String transactionDate = transactionDateRAW.substring(transactionDateRAW.lastIndexOf("=") + 1,
				transactionDateRAW.length() - 1);
		System.out.println("transactionDate  " + transactionDate);

		setRawTransactionDate(transactionDate);

		return transactionDate;
	}

	public void doSearch() {

		List<Object> list;

		if (getKlienID() != null && getInvoiceNo() == null) {

		} else if (getInvoiceNo() != null && getKlienID() == null) {

		}

		TransactionLog tl = new TransactionLog();
		System.out.println("client ID = " + getKlienID());
		System.out.println("invoice NO = " + getInvoiceNo());
		System.out.println("startDate = " + getStartDate());
		System.out.println("endDate = " + getEndDate());
		System.out.println("tipe TRANSAKSI = " + getTransactionType());
		System.out.println("endpoint = " + getEndpoint());
		System.out.println("response code = " + getResponseCode());;

		list = getTransactionLogsManager().findByTransaction(getKlienID(), getInvoiceNo(), getTransactionType(),
				getEndpoint(), getResponseCode(), getStartDate(), getEndDate());

		List<AnalisaTransactionLogsDto> result = new ArrayList<AnalisaTransactionLogsDto>();
		for (Object o : list) {

			Object[] row = (Object[]) o;
			AnalisaTransactionLogsDto dto = new AnalisaTransactionLogsDto();
			dto.setTrxId((String) row[0]);
			dto.setTrxTime((String) row[1]);
			dto.setTrxCode((String) row[2]);
			dto.setTrxDesc((String) row[3]);
			dto.setChannelCode((String) row[4]);
			dto.setChannelRc((String) row[5]);
			dto.setMxRc((String) row[8]);
			dto.setRcDesc((String) row[6]);
			dto.setRaw((String) row[7]);
			dto.setKlienID((String) row[7], (String) row[5], (String) row[2], (String) row[2]);
			dto.setClientName((String) row[7], (String) row[5], (String) row[2]);
			dto.setInvoiceNo((String) row[7], (String) row[5], (String) row[2], (String) row[2]);
			dto.setEndpoint((String) row[7], (String) row[5], (String) row[2]);

			if (dto.getEndpoint().equals(getEndpoint()) && dto.getChannelRc().equals(getResponseCode())) {
				System.out.println("endpoint & response ->" + getResponseCode());

				result.add(dto);

			} else if (dto.getEndpoint().equals(getEndpoint()) && !dto.getChannelRc().equals(getResponseCode())) {
				System.out.println("endpoint & response all->" + getResponseCode());

				result.add(dto);

			} else {
				System.out.println("endpoint & response null" + getResponseCode());

				result.add(new AnalisaTransactionLogsDto());
			}

			getFields().put("TRANSACTION_LOG", list);

		}
	}

}
