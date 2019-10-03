package com.balicamp.model.jms.trigger;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Holder for TriggerJms
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class TriggerJmsHolder {
	private Long timestamp;
	private long timeOutPeriode = -1l;
	private TriggerJms request;
	private TriggerJms response;
	
	public TriggerJmsHolder(){
	}
	
	public Long getTimestamp() {
		return timestamp;
	}
	public TriggerJms getRequest() {
		return request;
	}
	public TriggerJms getResponse() {
		return response;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public void setRequest(TriggerJms request) {
		this.request = request;
	}
	public void setResponse(TriggerJms response) {
		this.response = response;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("timestamp", this.timestamp)
		.append("response", this.response)
		.append("request", this.request)
		.toString();
	}

	public long getTimeOutPeriode() {
		return timeOutPeriode;
	}

	public void setTimeOutPeriode(long timeOutPeriode) {
		this.timeOutPeriode = timeOutPeriode;
	}

	
	
}
