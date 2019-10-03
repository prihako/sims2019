/**
 * 
 */
package com.balicamp.webapp.action.webadmin;

import org.apache.tapestry.IRequestCycle;

import com.balicamp.webapp.action.AdminBasePage;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionFeeNotFound.java 135 2013-01-03 04:20:57Z bagus.sugitayasa $
 */
public abstract class TransactionFeeNotFound extends AdminBasePage {

	private String dName;

	public String getdName() {
		return dName;
	}

	public void setdName(String dName) {
		this.dName = dName;
	}

	public void onSubmit(IRequestCycle cycle) {
		cycle.activate("transactionFeeList");
	}
}
