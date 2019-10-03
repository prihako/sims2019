/**
 * 
 */
package com.balicamp.webapp.action.webadmin;

import org.apache.tapestry.IRequestCycle;

import com.balicamp.webapp.action.AdminBasePage;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: MessageLogsNotFound.java 260 2013-02-01 07:52:47Z bagus.sugitayasa $
 */
public abstract class MessageLogsNotFound extends AdminBasePage {

	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void onSubmit(IRequestCycle cycle) {
		cycle.activate("messageLogsList");
	}
}
