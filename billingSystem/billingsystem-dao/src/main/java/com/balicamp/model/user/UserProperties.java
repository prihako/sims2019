package com.balicamp.model.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class UserProperties {
	private String previousStatus;
	private String notificationPreference;
	private String pendingEmailAddress;
	private String emailActivationCode;
	private String blockReason;
	private List<Date> loginFailDateList;
	private List<Date> softBlockDateList;
	
	//pln
	private Date plnInquiryDate;
	private Set<String> plnInquiryIdpelSet;

	public UserProperties(){
	}
	
	public String getPreviousStatus() {
		return previousStatus;
	}
	public void setPreviousStatus(String previousStatus) {
		this.previousStatus = previousStatus;
	}
	
	//loginFailDateList
	public List<Date> getLoginFailDateList() {
		return loginFailDateList;
	}
	public void setLoginFailDateList(List<Date> loginFailDateList) {
		this.loginFailDateList = loginFailDateList;
	}
	public void addLoginFailDateList(Date loginFailDate) {
		if( loginFailDateList == null )
			loginFailDateList = new ArrayList<Date>();
		loginFailDateList.add(loginFailDate);
	}

	//softBlockDateList
	public void addSoftBlockDateList(Date softBlockDate) {
		if ( softBlockDateList == null )
			softBlockDateList = new ArrayList<Date>();
		softBlockDateList.add(softBlockDate);
	}
	public List<Date> getSoftBlockDateList() {
		return softBlockDateList;
	}
	public void setSoftBlockDateList(List<Date> softBlockDateList) {
		this.softBlockDateList = softBlockDateList;
	}

	//notificationPreference
	public String getNotificationPreference() {
		return notificationPreference;
	}
	public void setNotificationPreference(String notificationPreference) {
		this.notificationPreference = notificationPreference;
	}

	//pendingEmailAddress
	public String getPendingEmailAddress() {
		return pendingEmailAddress;
	}
	public void setPendingEmailAddress(String pendingEmailAddress) {
		this.pendingEmailAddress = pendingEmailAddress;
	}

	//emailActivationCode
	public String getEmailActivationCode() {
		return emailActivationCode;
	}
	public void setEmailActivationCode(String emailActivationCode) {
		this.emailActivationCode = emailActivationCode;
	}

	//plnInquiryDate
	public Date getPlnInquiryDate() {
		return plnInquiryDate;
	}
	public void setPlnInquiryDate(Date plnInquiryDate) {
		this.plnInquiryDate = plnInquiryDate;
	}

	//plnInquiryIdpelSet
	public Set<String> getPlnInquiryIdpelSet() {
		return plnInquiryIdpelSet;
	}
	public void setPlnInquiryIdpelSet(Set<String> plnInquiryIdpelSet) {
		this.plnInquiryIdpelSet = plnInquiryIdpelSet;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if ( this == object ) return true;
		if (!(object instanceof UserProperties)) return false;
		
		UserProperties rhs = (UserProperties) object;
		return new EqualsBuilder()
			.append(this.previousStatus, rhs.previousStatus)
			.append(this.loginFailDateList, rhs.loginFailDateList)
			.append(this.softBlockDateList, rhs.softBlockDateList)
			.append(this.notificationPreference, rhs.notificationPreference)
			.append(this.pendingEmailAddress, rhs.pendingEmailAddress)
			.append(this.emailActivationCode, rhs.emailActivationCode)
			.append(this.blockReason, rhs.blockReason)
			.isEquals();
	}
	

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(850949817, 1528591847)
			.appendSuper(super.hashCode())
			.append(this.previousStatus)
			.append(this.loginFailDateList)
			.append(this.softBlockDateList)
			.append(this.notificationPreference)
			.append(this.pendingEmailAddress)
			.append(this.emailActivationCode)
			.append(this.blockReason)
			.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("previousStatus",this.previousStatus)
			.append("loginFailDateList",this.loginFailDateList)
			.append("softBlockDateList",this.softBlockDateList)
			.append("notificationPreference", this.notificationPreference)
			.append("pendingEmailAddress", this.pendingEmailAddress)
			.append("emailActivationCode", this.emailActivationCode)
			.append("blockReason", this.blockReason)
			.toString();
	}

	public String getBlockReason() {
		return blockReason;
	}

	public void setBlockReason(String blockReason) {
		this.blockReason = blockReason;
	}

	
}
