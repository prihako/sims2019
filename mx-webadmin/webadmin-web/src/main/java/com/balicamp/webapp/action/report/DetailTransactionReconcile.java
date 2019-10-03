package com.balicamp.webapp.action.report;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.valid.ValidationConstraint;

import test.Constants;

import com.balicamp.model.mx.ReconcileDto;
import com.balicamp.service.TransactionLogsManager;
import com.balicamp.service.impl.ebs.ExternalBillingSystemManagerImpl;
import com.balicamp.service.impl.mx.EndpointsManagerImpl;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.service.sims.BillingManager;
import com.balicamp.util.DateUtil;
import com.balicamp.webapp.action.AdminBasePage;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class DetailTransactionReconcile extends AdminBasePage implements PageBeginRenderListener, PageEndRenderListener {

	protected final Log log = LogFactory.getLog(Reconcile.class);

	@InjectSpring("endpointsManagerImpl")
	public abstract EndpointsManagerImpl getEndpointsManager();

	@InjectSpring("transactionLogsManagerImpl")
	public abstract TransactionLogsManager getTransactionLogsManager();

	@InjectSpring("billingManagerImpl")
	public abstract BillingManager getBillingManager();

	@InjectSpring("systemParameterManager")
	public abstract SystemParameterManager getSystemParameter();

	@InjectSpring("externalBillingSystemManagerImpl")
	public abstract ExternalBillingSystemManagerImpl getExternalBillingSystem();

	public abstract void setReconcileDto(ReconcileDto reconcileDto);

	public abstract void setNotFirstLoad(boolean firsloadFlag);

	public abstract boolean isNotFirstLoad();

	public abstract ReconcileDto getReconcileDto();

	public abstract Map<Object, Object> getMapParams();

	public abstract void setMapParams(Map<Object, Object> map);

	public abstract String getRemarks();

	public abstract void setRemarks(String remarks);

	public abstract Date getTrxDate();

	public abstract void setTrxDate(Date getTrxDate);

	public abstract String getChannelCode();

	public abstract void setChannelCode(String channelCode);

	public abstract String getReconcileStatus();

	public abstract void setReconcileStatus(String reconcileStatus);

	public abstract Integer getSettled();

	public abstract void setSettled(Integer settled);

	public abstract Integer getNotSettled();

	public abstract void setNotSettled(Integer notSettled);

	public abstract Integer getUnconfirmed();

	public abstract void setUnconfirmed(Integer unconfirmed);

	public abstract Integer getSettledReport();

	public abstract void setSettledReport(Integer settled);

	public abstract Integer getNotSettledReport();

	public abstract void setNotSettledReport(Integer notSettled);

	public abstract Integer getUnconfirmedReport();

	public abstract void setUnconfirmedReport(Integer unconfirmed);

	public abstract Long getAmountSettled();

	public abstract void setAmountSettled(Long amountSettled);

	public abstract Long getAmountNotSettled();

	public abstract void setAmountNotSettled(Long amountNotSettled);

	public abstract Long getAmountUnconfirmed();

	public abstract void setAmountUnconfirmed(Long amountUnconfirmed);

	public abstract Long getAmountSettledReport();

	public abstract void setAmountSettledReport(Long amountSettled);

	public abstract Long getAmountNotSettledReport();

	public abstract void setAmountNotSettledReport(Long amountNotSettled);

	public abstract Long getAmountUnconfirmedReport();

	public abstract void setAmountUnconfirmedReport(Long amountUnconfirmed);

	public abstract Long getTotalAmount();

	public abstract void setTotalAmount(Long totalAmount);

	public abstract Long getTotalAmountReport();

	public abstract void setTotalAmountReport(Long totalAmount);

	public abstract Long getTotalAmountDenda();

	public abstract void setTotalAmountDenda(Long totalAmountDenda);

	public abstract Long getTotalAmountDendaReport();

	public abstract void setTotalAmountDendaReport(Long totalAmountDendaReport);

	public abstract String getDoReconcileStatus();

	public abstract void setDoReconcileStatus(String doReconcileStatus);

	public abstract String getTransactionType();

	public abstract void setTransactionType(String transactionType);

	public IPage doBack(IRequestCycle cycle) {
		ReconcileDto reconcileDto = getReconcileDto();
		Reconcile reconcile = (Reconcile) cycle.getPage("reconcile");

		reconcile.getReconcileList();

		System.out.println(isNotFirstLoad());
		// getFields().put("TRANSACTION_LOGS_WEBADMIN", (List<ReconcileDto>)
		// get());

		reconcile.setNotFirstLoad(true);

		Map<Object, Object> mapParams = getMapParams();

		reconcile.setSettled(getSettledReport());
		reconcile.setNotSettled(getNotSettledReport());
		reconcile.setUnconfirmed(getUnconfirmedReport());
		reconcile.setSettledReport(getSettledReport());
		reconcile.setNotSettledReport(getNotSettledReport());
		reconcile.setUnconfirmedReport(getUnconfirmedReport());

		reconcile.setAmountSettled(getAmountSettled());
		reconcile.setAmountNotSettled(getAmountNotSettled());
		reconcile.setAmountUnconfirmed(getAmountUnconfirmed());
		reconcile.setTotalAmount(getTotalAmount());
		reconcile.setTotalAmountDenda(getTotalAmountDenda());

		reconcile.setAmountSettledReport(getAmountSettledReport());
		reconcile.setAmountNotSettledReport(getAmountNotSettledReport());
		reconcile.setAmountUnconfirmedReport(getAmountUnconfirmedReport());
		reconcile.setTotalAmountReport(getTotalAmountReport());
		reconcile.setTotalAmountDendaReport(getTotalAmountDendaReport());

		reconcile.setChannelCode(getChannelCode());

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date trxDate = null;
		try {
			trxDate = formatter.parse(mapParams.get("trxDate").toString());
			reconcile.setTrxDate(trxDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		reconcile.setReconcileStatus(mapParams.get("reconcileStatus").toString());
		reconcile.setChannelCode(mapParams.get("channelCode").toString());
		reconcile.setTransactionType(mapParams.get("transactionType").toString());
		reconcile.setTrxDate(trxDate);

		reconcile.doGetReconcileListByMt940();

		/*
		 * Map<Object, Object> mapParams = getMapParams(); reconcile.setTime(mapParams.get("time").toString()); reconcile.setSettled (Integer.valueOf(mapParams.get("settled").toString ()));
		 * reconcile.setNotSettled(Integer.valueOf(mapParams.get("notSettled"). toString()));
		 */

		return reconcile;
	}

	public IPage doUpdateInvoicePaid(IRequestCycle cycle, ReconcileDto reconcileDto, Date trxDate, String channelCode, String reconcileStatus, String invoiceNo) {

		DetailTransactionReconcile detailTransaction = (DetailTransactionReconcile) cycle.getPage("detailTransactionReconcile");

		Map<Object, Object> mapParams = getMapParams();
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date paymentDate = null;
		Date dueDate = null;
		try {
			paymentDate = format.parse(mapParams.get("trxDate").toString());
			dueDate = format.parse(reconcileDto.getInvoiceDueDate());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (getRemarks() == null || getRemarks().equalsIgnoreCase("-")) {
			addError(getDelegate(), "errorShadow", getText("leftmenu.reconcileDetail.remarksIsNull"), ValidationConstraint.CONSISTENCY);
			return detailTransaction;
		}

		String remarks = getRemarks();

		String username = getUserLoginFromSession().getUsername();

		if (getExternalBillingSystem().updateInvoicePaid(reconcileDto.getInvoiceNo(), paymentDate, remarks, username) == true) {

			reconcileDto.setPaymentDateSims(DateUtil.convertDateToString(new Date(), "dd-MM-yyyy"));
			if (dueDate.compareTo(paymentDate) >= 0) {
				reconcileDto.setReconcileStatus("Settled");
				detailTransaction.setSettled(Integer.parseInt(mapParams.get("settled").toString()) + 1);
				detailTransaction.setNotSettled(Integer.parseInt(mapParams.get("notSettled").toString()) - 1);
				detailTransaction.setSettledReport(Integer.parseInt(mapParams.get("settled").toString()) + 1);
				detailTransaction.setNotSettledReport(Integer.parseInt(mapParams.get("notSettled").toString()) - 1);

				if (reconcileDto.getInvoiceDendaNo() != null && !reconcileDto.getInvoiceDendaNo().equalsIgnoreCase("-")) {
					reconcileDto.setStatusDenda("Cancelled");

					detailTransaction.setAmountSettled(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
					detailTransaction.setAmountNotSettled(Long.parseLong(mapParams.get("amountNotSettled").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
					detailTransaction.setAmountSettledReport(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
					detailTransaction.setAmountNotSettledReport(Long.parseLong(mapParams.get("amountNotSettled").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));

					detailTransaction.setTotalAmountDenda(Long.parseLong(mapParams.get("totalAmountDenda").toString()) - Long.parseLong(reconcileDto.getTrxAmountDenda().replaceAll("(\\D)", "")));
					detailTransaction.setTotalAmountDendaReport(Long.parseLong(mapParams.get("totalAmountDenda").toString()) - Long.parseLong(reconcileDto.getTrxAmountDenda().replaceAll("(\\D)", "")));
				} else {
					detailTransaction.setAmountSettled(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
					detailTransaction.setAmountNotSettled(Long.parseLong(mapParams.get("amountNotSettled").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
					detailTransaction.setAmountSettledReport(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
					detailTransaction.setAmountNotSettledReport(Long.parseLong(mapParams.get("amountNotSettled").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
				}

				detailTransaction.setDoReconcileStatus("Success");
				reconcileDto.setSimsStatus("Paid");
			}
		} else {
			detailTransaction.setDoReconcileStatus("Fail");
		}

		/*
		 * getBillingManager().addRemarks(invoiceNo, paymentDate, new String[] { "8", "9", "10", "11", "38", "48" }, checkIfNull(getRemarks()));
		 */
		detailTransaction.setMapParams(mapParams);

		detailTransaction.setChannelCode(mapParams.get("channelCode").toString());
		detailTransaction.setTrxDate(paymentDate);
		detailTransaction.setReconcileStatus(mapParams.get("reconcileStatus").toString());
		detailTransaction.setTransactionType(mapParams.get("transactionType").toString());

		detailTransaction.setReconcileDto(reconcileDto);
		/*
		 * Map<Object, Object> mapParams = new HashMap<Object, Object>(); mapParams.put("settled", settle); mapParams.put("notSettled", notSettle); mapParams.put("time", time);
		 * detailTransaction.setMapParams(mapParams);
		 * 
		 * System.out.println(mapParams);
		 */

		return detailTransaction;
	}

	public IPage doUpdateInvoiceUnpaid(IRequestCycle cycle, ReconcileDto reconcileDto, Date trxDate, String channelCode, String reconcileStatus, String invoiceNo) {
		DetailTransactionReconcile detailTransaction = (DetailTransactionReconcile) cycle.getPage("detailTransactionReconcile");

		Map<Object, Object> mapParams = getMapParams();
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date paymentDate = null;
		Date transactionTimeDate = null;
		Date dueDate = null;

		if (getRemarks() == null || getRemarks().equalsIgnoreCase("-")) {
			addError(getDelegate(), "errorShadow", getText("leftmenu.reconcileDetail.remarksIsNull"), ValidationConstraint.CONSISTENCY);
			return detailTransaction;
		}

		String remarks = getRemarks();

		String trxCode = null;
		if (mapParams.get("transactionType").toString().equalsIgnoreCase(Constants.BillerConstants.BHP_CODE)) {
			trxCode = Constants.EndpointCode.BHP_CODE;
		} else if (mapParams.get("transactionType").toString().equalsIgnoreCase(Constants.BillerConstants.PERANGKAT_CODE)) {
			trxCode = Constants.EndpointCode.PERANGKAT_CODE;
		} else if (mapParams.get("transactionType").toString().equalsIgnoreCase(Constants.BillerConstants.SKOR_CODE)) {
			trxCode = Constants.EndpointCode.SKOR_CODE;
		} else if (mapParams.get("transactionType").toString().equalsIgnoreCase(Constants.BillerConstants.REOR_CODE)) {
			trxCode = Constants.EndpointCode.REOR_CODE;
		} else if (mapParams.get("transactionType").toString().equalsIgnoreCase(Constants.BillerConstants.IAR_CODE)) {
			trxCode = Constants.EndpointCode.IAR_CODE;
		} else if (mapParams.get("transactionType").toString().equalsIgnoreCase(Constants.BillerConstants.UNAR_CODE)) {
			trxCode = Constants.EndpointCode.UNAR_CODE;
		} else if (mapParams.get("transactionType").toString().equalsIgnoreCase(Constants.BillerConstants.IKRAP_CODE)) {
			trxCode = Constants.EndpointCode.IKRAP_CODE;
		} else if (mapParams.get("transactionType").toString().equalsIgnoreCase(Constants.BillerConstants.PAP_CODE)) {
			trxCode = Constants.EndpointCode.PAP_CODE;
		}
		trxCode += ".PAY";

		boolean isEODTime = false;

		String username = getUserLoginFromSession().getUsername();

		try {
			paymentDate = format.parse(mapParams.get("trxDate").toString());
			transactionTimeDate = format.parse(reconcileDto.getTransactionTime());
			dueDate = format.parse(reconcileDto.getInvoiceDueDate());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			isEODTime = checkEODTime(reconcileDto.getTransactionTime(), mapParams.get("channelCode").toString(), paymentDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;

		if (isEODTime == true) {
			if (transactionTimeDate.compareTo(dueDate) < 0 || transactionTimeDate.compareTo(dueDate) > 0) {
				if (getExternalBillingSystem().updateInvoiceUnpaid(reconcileDto.getInvoiceNo()) == true) {
					detailTransaction.setDoReconcileStatus("Success");
					reconcileDto.setSimsStatus("Unpaid");
					reconcileDto.setReconcileStatus("Need Confirmation/Manual Payment");

					detailTransaction.setNotSettled(Integer.parseInt(mapParams.get("notSettled").toString()) - 1);
					detailTransaction.setNotSettledReport(Integer.parseInt(mapParams.get("notSettled").toString()) - 1);

					if (reconcileDto.getInvoiceDendaNo() != null && !reconcileDto.getInvoiceDendaNo().equalsIgnoreCase("-")) {
						reconcileDto.setStatusDenda("Cancelled");

						detailTransaction.setAmountNotSettled(Long.parseLong(mapParams.get("amountNotSettled").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						detailTransaction.setAmountNotSettledReport(Long.parseLong(mapParams.get("amountNotSettled").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						detailTransaction.setTotalAmount(Long.parseLong(mapParams.get("totalAmount").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						detailTransaction.setTotalAmountReport(Long.parseLong(mapParams.get("totalAmount").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));

						detailTransaction.setTotalAmountDenda(Long.parseLong(mapParams.get("totalAmountDenda").toString()) - Long.parseLong(reconcileDto.getTrxAmountDenda().replaceAll("(\\D)", "")));
						detailTransaction.setTotalAmountDendaReport(Long.parseLong(mapParams.get("totalAmountDenda").toString()) - Long.parseLong(reconcileDto.getTrxAmountDenda().replaceAll("(\\D)", "")));
					} else {
						detailTransaction.setAmountNotSettled(Long.parseLong(mapParams.get("amountNotSettled").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						detailTransaction.setAmountNotSettledReport(Long.parseLong(mapParams.get("amountNotSettled").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						detailTransaction.setTotalAmount(Long.parseLong(mapParams.get("totalAmount").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						detailTransaction.setTotalAmountReport(Long.parseLong(mapParams.get("totalAmount").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
					}

					getBillingManager().addRemarks(reconcileDto.getInvoiceNo(), new Date(), new String[] { "8", "9", "10", "11", "38", "48" }, "RECONCILED BY " + username + " - " + remarks);
				}
			} else if (transactionTimeDate.compareTo(dueDate) == 0) {
				if (getExternalBillingSystem().updateInvoiceUnpaid(reconcileDto.getInvoiceNo()) == true) {
					if (reconcileDto.getMt940Status().equalsIgnoreCase("Unpaid")) {
						getTransactionLogsManager().updateReconFlag(reconcileDto.getInvoiceNo(), "SIMS", "00", "00", trxCode);
					}
					detailTransaction.setDoReconcileStatus("Success");
					reconcileDto.setSimsStatus("Unpaid");
					reconcileDto.setReconcileStatus("Settled");
					detailTransaction.setNotSettled(Integer.parseInt(mapParams.get("notSettled").toString()) - 1);
					detailTransaction.setNotSettledReport(Integer.parseInt(mapParams.get("notSettled").toString()) - 1);

					if (reconcileDto.getInvoiceDendaNo() != null && !reconcileDto.getInvoiceDendaNo().equalsIgnoreCase("-")) {
						reconcileDto.setStatusDenda("Cancelled");

						detailTransaction.setAmountNotSettled(Long.parseLong(mapParams.get("amountNotSettled").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						detailTransaction.setAmountNotSettledReport(Long.parseLong(mapParams.get("amountNotSettled").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						detailTransaction.setTotalAmount(Long.parseLong(mapParams.get("totalAmount").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						detailTransaction.setTotalAmountReport(Long.parseLong(mapParams.get("totalAmount").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));

						detailTransaction.setTotalAmountDenda(Long.parseLong(mapParams.get("totalAmountDenda").toString()) - Long.parseLong(reconcileDto.getTrxAmountDenda().replaceAll("(\\D)", "")));
						detailTransaction.setTotalAmountDendaReport(Long.parseLong(mapParams.get("totalAmountDenda").toString()) - Long.parseLong(reconcileDto.getTrxAmountDenda().replaceAll("(\\D)", "")));
					} else {
						detailTransaction.setAmountNotSettled(Long.parseLong(mapParams.get("amountNotSettled").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						detailTransaction.setAmountNotSettledReport(Long.parseLong(mapParams.get("amountNotSettled").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						detailTransaction.setTotalAmount(Long.parseLong(mapParams.get("totalAmount").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						detailTransaction.setTotalAmountReport(Long.parseLong(mapParams.get("totalAmount").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
					}

					getBillingManager().addRemarks(reconcileDto.getInvoiceNo(), new Date(), new String[] { "8", "9", "10", "11", "38", "48" }, "RECONCILED BY " + username + " - " + remarks);
				} else {
					detailTransaction.setDoReconcileStatus("Fail");
				}
			}
		} else {
			detailTransaction.setDoReconcileStatus("FailIsNotEOD");
		}

		detailTransaction.setMapParams(mapParams);

		detailTransaction.setChannelCode(mapParams.get("channelCode").toString());
		detailTransaction.setTrxDate(paymentDate);
		detailTransaction.setReconcileStatus(mapParams.get("reconcileStatus").toString());
		detailTransaction.setTransactionType(mapParams.get("transactionType").toString());

		detailTransaction.setReconcileDto(reconcileDto);

		/*
		 * Map<Object, Object> mapParams = new HashMap<Object, Object>(); mapParams.put("settled", settle); mapParams.put("notSettled", notSettle); mapParams.put("time", time);
		 * detailTransaction.setMapParams(mapParams);
		 * 
		 * System.out.println(mapParams);
		 */

		return detailTransaction;
	}

	public IPage doForceSettled(IRequestCycle cycle, ReconcileDto reconcileDto, Date trxDate, String channelCode, String reconcileStatus, String invoiceNo) {
		DetailTransactionReconcile detailTransaction = (DetailTransactionReconcile) cycle.getPage("detailTransactionReconcile");

		Map<Object, Object> mapParams = getMapParams();
		String trxCode = null;
		String username = getUserLoginFromSession().getUsername();
		Date transactionDate = null;
		Date dueDate = null;

		DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		try {
			transactionDate = format.parse(mapParams.get("trxDate").toString());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (getRemarks() == null || getRemarks().equalsIgnoreCase("-")) {
			addError(getDelegate(), "errorShadow", getText("leftmenu.reconcileDetail.remarksIsNull"), ValidationConstraint.CONSISTENCY);
			return detailTransaction;
		}

		String remarks = getRemarks();

		if (mapParams.get("transactionType").toString().equalsIgnoreCase(Constants.BillerConstants.BHP_CODE)) {
			trxCode = Constants.EndpointCode.BHP_CODE;
		} else if (mapParams.get("transactionType").toString().equalsIgnoreCase(Constants.BillerConstants.PERANGKAT_CODE)) {
			trxCode = Constants.EndpointCode.PERANGKAT_CODE;
		} else if (mapParams.get("transactionType").toString().equalsIgnoreCase(Constants.BillerConstants.SKOR_CODE)) {
			trxCode = Constants.EndpointCode.SKOR_CODE;
		} else if (mapParams.get("transactionType").toString().equalsIgnoreCase(Constants.BillerConstants.REOR_CODE)) {
			trxCode = Constants.EndpointCode.REOR_CODE;
		} else if (mapParams.get("transactionType").toString().equalsIgnoreCase(Constants.BillerConstants.IAR_CODE)) {
			trxCode = Constants.EndpointCode.IAR_CODE;
		} else if (mapParams.get("transactionType").toString().equalsIgnoreCase(Constants.BillerConstants.UNAR_CODE)) {
			trxCode = Constants.EndpointCode.UNAR_CODE;
		} else if (mapParams.get("transactionType").toString().equalsIgnoreCase(Constants.BillerConstants.IKRAP_CODE)) {
			trxCode = Constants.EndpointCode.IKRAP_CODE;
		} else if (mapParams.get("transactionType").toString().equalsIgnoreCase(Constants.BillerConstants.PAP_CODE)) {
			trxCode = Constants.EndpointCode.PAP_CODE;
		}
		trxCode += ".PAY";
		if (reconcileDto.getReconcileStatus().equalsIgnoreCase("Need Confirmation/Manual Payment")) {
			if (reconcileDto.getMt940Status().equalsIgnoreCase("Paid") && reconcileDto.getSimsStatus().equalsIgnoreCase("Paid")) {
				if (reconcileDto.getTrxId().equalsIgnoreCase("-")) {

					if (reconcileDto.getInvoiceNo() != null) {
						getTransactionLogsManager().insertDummyDataWebadminReconcile(trxCode, mapParams.get("channelCode").toString(), mapParams.get("transactionType").toString(), reconcileDto.getInvoiceNo(), "MX", transactionDate);
						detailTransaction.setDoReconcileStatus("Success");

						reconcileDto.setReconcileStatus("Settled");
						detailTransaction.setSettled(Integer.parseInt(mapParams.get("settled").toString()) + 1);
						detailTransaction.setUnconfirmed(Integer.parseInt(mapParams.get("unconfirmed").toString()) - 1);
						detailTransaction.setSettledReport(Integer.parseInt(mapParams.get("settled").toString()) + 1);
						detailTransaction.setUnconfirmedReport(Integer.parseInt(mapParams.get("unconfirmed").toString()) - 1);

						if (reconcileDto.getInvoiceDendaNo() != null && !reconcileDto.getInvoiceDendaNo().equalsIgnoreCase("-")) {
							reconcileDto.setStatusDenda("Cancelled");

							detailTransaction.setAmountSettled(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountUnconfirmed(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountSettledReport(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountUnconfirmedReport(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));

							detailTransaction.setTotalAmountDenda(Long.parseLong(mapParams.get("totalAmountDenda").toString()) - Long.parseLong(reconcileDto.getTrxAmountDenda().replaceAll("(\\D)", "")));
							detailTransaction.setTotalAmountDendaReport(Long.parseLong(mapParams.get("totalAmountDenda").toString()) - Long.parseLong(reconcileDto.getTrxAmountDenda().replaceAll("(\\D)", "")));
						} else {
							detailTransaction.setAmountSettled(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountUnconfirmed(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountSettledReport(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountUnconfirmedReport(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						}

						getBillingManager().addRemarks(reconcileDto.getInvoiceNo(), new Date(), new String[] { "8", "9", "10", "11", "38", "48" }, "RECONCILED BY " + username + " - " + remarks);

					}

				} else {
					if (reconcileDto.getInvoiceNo() != null) {
						getTransactionLogsManager().updateReconFlag(reconcileDto.getInvoiceNo(), "TGL_BYR>TGL_JT", "00", "00", trxCode);
						detailTransaction.setDoReconcileStatus("Success");
						reconcileDto.setReconcileStatus("Settled");
						detailTransaction.setSettled(Integer.parseInt(mapParams.get("settled").toString()) + 1);
						detailTransaction.setUnconfirmed(Integer.parseInt(mapParams.get("unconfirmed").toString()) - 1);
						detailTransaction.setSettledReport(Integer.parseInt(mapParams.get("settled").toString()) + 1);
						detailTransaction.setUnconfirmedReport(Integer.parseInt(mapParams.get("unconfirmed").toString()) - 1);

						if (reconcileDto.getInvoiceDendaNo() != null && !reconcileDto.getInvoiceDendaNo().equalsIgnoreCase("-")) {
							reconcileDto.setStatusDenda("Cancelled");

							detailTransaction.setAmountSettled(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountUnconfirmed(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountSettledReport(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountUnconfirmedReport(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));

							detailTransaction.setTotalAmountDenda(Long.parseLong(mapParams.get("totalAmountDenda").toString()) - Long.parseLong(reconcileDto.getTrxAmountDenda().replaceAll("(\\D)", "")));
							detailTransaction.setTotalAmountDendaReport(Long.parseLong(mapParams.get("totalAmountDenda").toString()) - Long.parseLong(reconcileDto.getTrxAmountDenda().replaceAll("(\\D)", "")));
						} else {
							detailTransaction.setAmountSettled(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountUnconfirmed(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountSettledReport(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountUnconfirmedReport(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						}

						getBillingManager().addRemarks(reconcileDto.getInvoiceNo(), new Date(), new String[] { "8", "9", "10", "11", "38", "48" }, "RECONCILED BY " + username + " - " + remarks);

					}
				}
			} else if (reconcileDto.getMt940Status().equalsIgnoreCase("Paid") && (reconcileDto.getSimsStatus().equalsIgnoreCase("Unpaid") || reconcileDto.getSimsStatus().equalsIgnoreCase("Cancelled")) && reconcileDto.getTrxId().equalsIgnoreCase("-")) {

				if (reconcileDto.getInvoiceNo() != null) {
					if (getExternalBillingSystem().updateInvoicePaid(reconcileDto.getInvoiceNo(), transactionDate, remarks, username) == true) {
						getTransactionLogsManager().insertDummyDataWebadminReconcile(trxCode, mapParams.get("channelCode").toString(), mapParams.get("transactionType").toString(), reconcileDto.getInvoiceNo(), "SIMS_MX", transactionDate);
						detailTransaction.setDoReconcileStatus("Success");
						reconcileDto.setSimsStatus("Paid");
						reconcileDto.setReconcileStatus("Settled");
						detailTransaction.setSettled(Integer.parseInt(mapParams.get("settled").toString()) + 1);
						detailTransaction.setUnconfirmed(Integer.parseInt(mapParams.get("unconfirmed").toString()) - 1);
						detailTransaction.setSettledReport(Integer.parseInt(mapParams.get("settled").toString()) + 1);
						detailTransaction.setUnconfirmedReport(Integer.parseInt(mapParams.get("unconfirmed").toString()) - 1);

						if (reconcileDto.getInvoiceDendaNo() != null && !reconcileDto.getInvoiceDendaNo().equalsIgnoreCase("-")) {
							reconcileDto.setStatusDenda("Cancelled");

							detailTransaction.setAmountSettled(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountUnconfirmed(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountSettledReport(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountUnconfirmedReport(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));

							detailTransaction.setTotalAmountDenda(Long.parseLong(mapParams.get("totalAmountDenda").toString()) - Long.parseLong(reconcileDto.getTrxAmountDenda().replaceAll("(\\D)", "")));
							detailTransaction.setTotalAmountDendaReport(Long.parseLong(mapParams.get("totalAmountDenda").toString()) - Long.parseLong(reconcileDto.getTrxAmountDenda().replaceAll("(\\D)", "")));
						} else {
							detailTransaction.setAmountSettled(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountUnconfirmed(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountSettledReport(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountUnconfirmedReport(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						}

					}
				}
			} else if (reconcileDto.getMt940Status().equalsIgnoreCase("Unpaid") && (reconcileDto.getSimsStatus().equalsIgnoreCase("Paid") || reconcileDto.getSimsStatus().equalsIgnoreCase("Cancelled")) && reconcileDto.getTrxId().equalsIgnoreCase("-")) {

				if (reconcileDto.getInvoiceNo() != null) {
					getTransactionLogsManager().insertDummyDataWebadminReconcile(trxCode, mapParams.get("channelCode").toString(), mapParams.get("transactionType").toString(), reconcileDto.getInvoiceNo(), "MT_MX", transactionDate);
					detailTransaction.setDoReconcileStatus("Success");
					reconcileDto.setReconcileStatus("Settled");
					detailTransaction.setSettled(Integer.parseInt(mapParams.get("settled").toString()) + 1);
					detailTransaction.setUnconfirmed(Integer.parseInt(mapParams.get("unconfirmed").toString()) - 1);
					detailTransaction.setSettledReport(Integer.parseInt(mapParams.get("settled").toString()) + 1);
					detailTransaction.setUnconfirmedReport(Integer.parseInt(mapParams.get("unconfirmed").toString()) - 1);

					if (reconcileDto.getInvoiceDendaNo() != null && !reconcileDto.getInvoiceDendaNo().equalsIgnoreCase("-")) {
						reconcileDto.setStatusDenda("Cancelled");

						detailTransaction.setAmountSettled(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						detailTransaction.setAmountUnconfirmed(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						detailTransaction.setAmountSettledReport(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						detailTransaction.setAmountUnconfirmedReport(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));

						detailTransaction.setTotalAmountDenda(Long.parseLong(mapParams.get("totalAmountDenda").toString()) - Long.parseLong(reconcileDto.getTrxAmountDenda().replaceAll("(\\D)", "")));
						detailTransaction.setTotalAmountDendaReport(Long.parseLong(mapParams.get("totalAmountDenda").toString()) - Long.parseLong(reconcileDto.getTrxAmountDenda().replaceAll("(\\D)", "")));
					} else {
						detailTransaction.setAmountSettled(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						detailTransaction.setAmountUnconfirmed(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						detailTransaction.setAmountSettledReport(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						detailTransaction.setAmountUnconfirmedReport(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
					}

					getBillingManager().addRemarks(reconcileDto.getInvoiceNo(), new Date(), new String[] { "8", "9", "10", "11", "38", "48" }, "RECONCILED BY " + username + " - " + remarks);

				}
			} else if (reconcileDto.getMt940Status().equalsIgnoreCase("Unpaid") && (reconcileDto.getSimsStatus().equalsIgnoreCase("Unpaid") || reconcileDto.getSimsStatus().equalsIgnoreCase("Cancelled")) && !reconcileDto.getTrxId().equalsIgnoreCase("-")) {

				if (reconcileDto.getInvoiceNo() != null) {

					if (getExternalBillingSystem().updateInvoicePaid(reconcileDto.getInvoiceNo(), transactionDate, remarks, username) == true) {
						getTransactionLogsManager().updateReconFlag(reconcileDto.getInvoiceNo(), "SIMS", "00", "00", trxCode);
						detailTransaction.setDoReconcileStatus("Success");
						reconcileDto.setSimsStatus("Paid");
						reconcileDto.setReconcileStatus("Settled");
						detailTransaction.setSettled(Integer.parseInt(mapParams.get("settled").toString()) + 1);
						detailTransaction.setUnconfirmed(Integer.parseInt(mapParams.get("unconfirmed").toString()) - 1);
						detailTransaction.setSettledReport(Integer.parseInt(mapParams.get("settled").toString()) + 1);
						detailTransaction.setUnconfirmedReport(Integer.parseInt(mapParams.get("unconfirmed").toString()) - 1);

						if (reconcileDto.getInvoiceDendaNo() != null && !reconcileDto.getInvoiceDendaNo().equalsIgnoreCase("-")) {
							reconcileDto.setStatusDenda("Cancelled");

							detailTransaction.setAmountSettled(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountUnconfirmed(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountSettledReport(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountUnconfirmedReport(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));

							detailTransaction.setTotalAmountDenda(Long.parseLong(mapParams.get("totalAmountDenda").toString()) - Long.parseLong(reconcileDto.getTrxAmountDenda().replaceAll("(\\D)", "")));
							detailTransaction.setTotalAmountDendaReport(Long.parseLong(mapParams.get("totalAmountDenda").toString()) - Long.parseLong(reconcileDto.getTrxAmountDenda().replaceAll("(\\D)", "")));
						} else {
							detailTransaction.setAmountSettled(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountUnconfirmed(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountSettledReport(Long.parseLong(mapParams.get("amountSettled").toString()) + Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
							detailTransaction.setAmountUnconfirmedReport(Long.parseLong(mapParams.get("amountUnconfirmed").toString()) - Long.parseLong(reconcileDto.getTrxAmount().replaceAll("(\\D)", "")));
						}

						getBillingManager().addRemarks(reconcileDto.getInvoiceNo(), new Date(), new String[] { "8", "9", "10", "11", "38", "48" }, "RECONCILED BY " + username + " - " + remarks);

					}
				}
			}
		}

		detailTransaction.setMapParams(mapParams);
		detailTransaction.setChannelCode(mapParams.get("channelCode").toString());
		detailTransaction.setReconcileStatus(mapParams.get("reconcileStatus").toString());
		detailTransaction.setTransactionType(mapParams.get("transactionType").toString());

		detailTransaction.setReconcileDto(reconcileDto);

		return detailTransaction;
	}

	public String checkIfNull(Object obj) {
		String str = null;
		if (obj == null) {
			str = "";
		} else {
			str = obj.toString();
		}

		return str;
	}

	public static final boolean checkEODTime(String payDate, String channel, Date trxDate) throws ParseException {

		// String[] payTime = payDate.split(" ");

		// paydate is before trxdate jam 21:00:00-23:59:59 and paydate after
		// trxdate-1 jm 00:00:00-20:59:59?

		// DateFormat df = new SimpleDateFormat("HH:mm:ss");
		DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
		String pattern = "dd-MM-yyyy HH:mm:ss";
		// String hourPattern="HH:mm:ss";
		String datePattern = "dd-MM-yyyy";

		// Date pd = df.parse(payTime[1]);
		Date pd = DateUtil.convertStringToDate(pattern, payDate);
		// Date pdHour=convertStringToDate(hourPattern, df.format(pd));
		Date pdDate = DateUtil.convertStringToDate(datePattern, df2.format(pd));
		Date startHourMinD = null;
		Date endHourMinD = null;
		Date startHourD = null;
		Date endHourD = null;

		boolean checkEODTimeResult = false;

		// pd.after(subtractDay(trxDate));
		// pd.before(trxDate);

		if (channel.equalsIgnoreCase("chws")) {
			startHourD = DateUtil.convertStringToDate(pattern, df2.format(subtractDay(trxDate)) + " 21:00:00");
			endHourD = DateUtil.convertStringToDate(pattern, df2.format(subtractDay(trxDate)) + " 23:59:59");
			startHourMinD = DateUtil.convertStringToDate(pattern, df2.format(trxDate) + " 00:00:00");
			endHourMinD = DateUtil.convertStringToDate(pattern, df2.format(trxDate) + " 20:59:59");
			if (pdDate.after(subtractDay(trxDate))) {
				checkEODTimeResult = pd.after(startHourMinD) && pd.before(endHourMinD);
			} else if (pdDate.before(trxDate)) {
				checkEODTimeResult = pd.after(startHourD) && pd.before(endHourD);
			}
		} else if (channel.equalsIgnoreCase("chws2")) {
			startHourD = DateUtil.convertStringToDate(pattern, df2.format(subtractDay(trxDate)) + " 21:00:00");
			endHourD = DateUtil.convertStringToDate(pattern, df2.format(subtractDay(trxDate)) + " 23:59:59");
			startHourMinD = DateUtil.convertStringToDate(pattern, df2.format(trxDate) + " 00:00:00");
			endHourMinD = DateUtil.convertStringToDate(pattern, df2.format(trxDate) + " 20:59:59");

			if (pdDate.after(subtractDay(trxDate))) {
				checkEODTimeResult = pd.after(startHourMinD) && pd.before(endHourMinD);
			} else if (pdDate.before(trxDate)) {
				checkEODTimeResult = pd.after(startHourD) && pd.before(endHourD);
			}
		}
		return checkEODTimeResult;

	}

	public static Date subtractDay(Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}
}
