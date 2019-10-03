package com.balicamp.model.mx;

import com.balicamp.model.admin.BaseAdminModel;

public class TransactionsDto extends BaseAdminModel {
	private static final long serialVersionUID = 1854321289392929144L;
	
	private String transactionName;
	private String transactionCode;
	private String channelCode;
	private int sukses;
	private int gagal;
	private int timeout;
	private String channelRc;
	private int count;
	
	public String getTransactionName() {
		return transactionName;
	}
	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}
	public String getTransactionCode() {
		return transactionCode;
	}
	
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	
	public String getChannelCode() {
		return channelCode;
	}
	
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	
	public int getSukses() {
		return sukses;
	}
	
	public void setSukses(int sukses) {
		this.sukses = sukses;
	}
	
	public int getGagal() {
		return gagal;
	}
	
	public void setGagal(int gagal) {
		this.gagal = gagal;
	}
	
	public int getTimeout() {
		return timeout;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	@Override
	public String getPKey() {
		return null;
	}
	
	public String getChannelRc() {
		return channelRc;
	}
	public void setChannelRc(String channelRc) {
		this.channelRc = channelRc;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
