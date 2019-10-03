/**
 * 
 */
package com.balicamp.webapp.action.webadmin;

import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mx.TransactionFee;
import com.balicamp.webapp.action.BasePageList;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionFeeEntryConfirm.java 505 2013-05-24 08:15:51Z rudi.sadria $
 */
public abstract class TransactionFeeEntryNotification extends BasePageList implements PageBeginRenderListener {

	private List<TransactionFee> transactionFee;

	public List<TransactionFee> getTransactionFee() {
		return transactionFee;
	}

	public void setTransactionFee(List<TransactionFee> txFee) {
		this.transactionFee = txFee;
	}

	public abstract Object getLoopObject();

	public abstract void setLoopObject(Object object);

	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		// setMessage(getText("transactionFeeEntryNotification.title.success"));
	}

	public IBasicTableModel getTableModel() {
		return new IBasicTableModel() {

			@Override
			public int getRowCount() {
				return getTransactionFee().size();
			}

			@Override
			public Iterator<TransactionFee> getCurrentPageRows(int nFirst, int nPageSize, ITableColumn objSortColumn,
					boolean bSortOrder) {
				return getTransactionFee().iterator();
			}
		};
	}

	public void setTableModel(IBasicTableModel model) {
	}
}
