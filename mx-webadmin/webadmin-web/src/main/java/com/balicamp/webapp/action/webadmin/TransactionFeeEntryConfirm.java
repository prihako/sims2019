/**
 * 
 */
package com.balicamp.webapp.action.webadmin;

import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mx.TransactionFee;
import com.balicamp.service.TransactionFeeManager;
import com.balicamp.webapp.action.BasePageList;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionFeeEntryConfirm.java 517 2013-06-25 10:13:25Z rudi.sadria $
 */
public abstract class TransactionFeeEntryConfirm extends BasePageList implements PageBeginRenderListener {
	public abstract void setFromPage(String fromPage);

	@Persist("client")
	public abstract String getFromPage();

	@InjectSpring("transactionFeeManagerImpl")
	public abstract TransactionFeeManager getTransactionFeeManager();

	private List<TransactionFee> transactionFee;

	public void setTransactionFee(List<TransactionFee> txFee) {
		this.transactionFee = txFee;
	}

	public List<TransactionFee> getTransactionFee() {
		return transactionFee;
	}

	public abstract Object getLoopObject();

	public abstract void setLoopObject(Object object);

	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
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

	public IPage doSave() {
		getTransactionFeeManager().saveAll(getTransactionFee());
		TransactionFeeEntryNotification notif = (TransactionFeeEntryNotification) getRequestCycle().getPage(
				"transactionFeeEntryNotification");
		notif.setTransactionFee(getTransactionFee());
		return notif;
	}

}
