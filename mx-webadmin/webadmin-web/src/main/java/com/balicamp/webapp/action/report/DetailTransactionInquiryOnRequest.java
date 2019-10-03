package com.balicamp.webapp.action.report;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;

import com.balicamp.model.mx.InquiryReconcileDto;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.webadmin.AnalisaTransactionLogs;

public abstract class DetailTransactionInquiryOnRequest extends AdminBasePage implements PageBeginRenderListener,
		PageEndRenderListener {

	protected final Log log = LogFactory.getLog(AnalisaTransactionLogs.class);

	public abstract void setReconcileDto(InquiryReconcileDto reconcileDto);

	public abstract InquiryReconcileDto getReconcileDto();

	public abstract List<InquiryReconcileDto> getInquiryReconcile();

	public abstract Map<Object, Object> getMapParams();

	public abstract void setMapParams(Map<Object, Object> map);

	public abstract String getInvoiceNo();

	public abstract void setInvoiceNo(String invoiceNo);

	public abstract String getClientId();

	public abstract void setClientId(String clientId);

	public abstract String getChannelCode();

	public abstract void setChannelCode(String channelCode);

	public abstract String getReconcileStatus();

	public abstract void setReconcileStatus(String status);

	public IPage doBack(IRequestCycle cycle) {
		InquiryReconcileDto inquiryReconcileDto = getReconcileDto();
		InquiryOnRequest inquiryOnRequest = (InquiryOnRequest) cycle.getPage("inquiryOnRequest");

		inquiryOnRequest.setNotFirstLoad(true);
		Map<Object, Object> mapParams = getMapParams();
		inquiryOnRequest.setTime(mapParams.get("settled") != null ? mapParams.get("time").toString() : null);
		inquiryOnRequest.setSettled(mapParams.get("settled") != null ? Integer.valueOf(mapParams.get("settled")
				.toString()) : null);
		inquiryOnRequest.setNotSettled(mapParams.get("notSettled") != null ? Integer.valueOf(mapParams
				.get("notSettled").toString()) : null);
		inquiryOnRequest.setSettledReport(mapParams.get("settled") != null ? Integer.valueOf(mapParams.get("settled")
				.toString()) : null);
		inquiryOnRequest.setNotSettledReport(mapParams.get("notSettled") != null ? Integer.valueOf(mapParams.get(
				"notSettled").toString()) : null);

		inquiryOnRequest.setChannelCode(getChannelCode());
		inquiryOnRequest.setClientId(getClientId());
		inquiryOnRequest.setInvoiceNo(getInvoiceNo());
		inquiryOnRequest.setReconcileStatus(getReconcileStatus());

		return inquiryOnRequest;
	}

}
