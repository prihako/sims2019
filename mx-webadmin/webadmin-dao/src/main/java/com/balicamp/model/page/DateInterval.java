package com.balicamp.model.page;

import java.util.Date;


/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class DateInterval {

	public final static String INTERVAL_TYPE_NOW = "now";
	public final static String INTERVAL_TYPE_DATE = "date";
	
	private String intervalType;
	private Date startDate;
	private Date endDate;
	
	public Date getRealStartDate() {
		Date result = startDate;
		if ( getIntervalType() != null && getIntervalType().equals(INTERVAL_TYPE_NOW) )
			result = new Date();
		return result;
	}
	public Date getRealEndDate() {
		Date result = endDate;
		if ( getIntervalType() != null && getIntervalType().equals(INTERVAL_TYPE_NOW) )
			result = new Date();
		return result;
	}
	
	//getter setter
	public String getIntervalType() {
		return intervalType;
	}
	public void setIntervalType(String intervalType) {
		this.intervalType = intervalType;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	
}
