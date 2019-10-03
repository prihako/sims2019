package com.balicamp.webapp.action.webadmin;

import java.util.List;

import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mx.TransactionFee;
import com.balicamp.webapp.action.BasePageList;

public abstract class TransactionFeeAlreadyExists extends BasePageList implements PageBeginRenderListener {

	public abstract void setTransactionFee(List<TransactionFee> txFee);

	public abstract List<TransactionFee> getTransactionFee();

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	public int getGridRowCount() {
		return getTransactionFee().size();
	}

}
