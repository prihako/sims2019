package com.balicamp.webapp.action.webadmin;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.webapp.action.AdminBasePage;

public abstract class TransactionLogDetail extends AdminBasePage implements PageBeginRenderListener,
		PageEndRenderListener {
	public abstract String getRawDate();

	public abstract void setRawDate(String str);

	public abstract String getRawCompanyCode();

	public abstract void setRawCompanyCode(String str);

	public abstract String getRawTransactionCode();

	public abstract void setRawTransactionCode(String str);

	public abstract String getRawTransactionType();

	public abstract void setRawTransactionType(String str);

	public abstract String getRawRouteId();

	public abstract void setRawRouteId(String str);

	public abstract String getRawClientName();

	public abstract void setRawClientName(String str);

	public abstract String getRawClientID();

	public abstract void setRawClientID(String str);

	public abstract String getRawPeriodBegin();

	public abstract void setRawPeriodBegin(String str);

	public abstract String getRawPeriodEnd();

	public abstract void setRawPeriodEnd(String str);

	public abstract String getRawInvoiceNo();

	public abstract void setRawInvoiceNo(String str);

	public abstract String getRawTransactionDate();

	public abstract void setRawTransactionDate(String str);

	public abstract String getRawTransactionTime();

	public abstract void setRawTransactionTime(String str);

	public abstract String getRawAmountTransaction();

	public abstract void setRawAmountTransaction(String str);

	public abstract String getRawCurrencyCodeTransaction();

	public abstract void setRawCurrencyCodeTransaction(String str);

	public abstract String getRawResponseCode();

	public abstract void setRawResponseCode(String str);

	public abstract String getRawChannelID();

	public abstract void setRawChannelID(String str);

	public abstract String getRawChannelCode();

	public abstract void setRawChannelCode(String str);

	public abstract String getRawTransactionID();

	public abstract void setRawTransactionID(String str);

	@InjectPage("analisaTransactionLogs")
	public abstract AnalisaTransactionLogs getAnalisaTransactionLogs();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {

		super.pageBeginRender(pageEvent);
		System.out.println("User  " + getUserLoginFromSession().getUserName());
		System.out.println("User Role " + getUserLoginFromSession().getUserRoles());
		if (!pageEvent.getRequestCycle().isRewinding()) {
			// if (!isNotFirstLoad()) {
			// setNotFirstLoad(true);
			// if (getFields() == null) {
			// setFields(new HashMap());
			// } else if (getFields() != null) {
			// getFields().remove("TRANSACTION_LOG");
			// }
			// }
		}

	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);

	}

	public IPage doBack() {

		return getAnalisaTransactionLogs();
	}
}
