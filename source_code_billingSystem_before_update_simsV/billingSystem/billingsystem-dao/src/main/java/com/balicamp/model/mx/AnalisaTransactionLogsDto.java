package com.balicamp.model.mx;

import java.util.Date;

import com.balicamp.model.admin.BaseAdminModel;

public class AnalisaTransactionLogsDto extends BaseAdminModel {
	private static final long serialVersionUID = 1049160728433482866L;
	
	private String trxId;
	private Date trxTime;
	private String trxCode;
	private String trxDesc;
	private String channelCode;
	private String channelRc;
	private String rcDesc;

	public String getTrxId() {
		return trxId;
	}

	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}

	public Date getTrxTime() {
		return trxTime;
	}

	public void setTrxTime(Date trxTime) {
		this.trxTime = trxTime;
	}

	public String getTrxCode() {
		return trxCode;
	}

	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}

	public String getTrxDesc() {
		return trxDesc;
	}

	public void setTrxDesc(String trxDesc) {
		this.trxDesc = trxDesc;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getChannelRc() {
		return channelRc;
	}

	public void setChannelRc(String channelRc) {
		this.channelRc = channelRc;
	}

	public String getRcDesc() {
		return rcDesc;
	}

	public void setRcDesc(String rcDesc) {
		this.rcDesc = rcDesc;
	}

	@Override
	public Object getPKey() {
		return trxId;
	}

	@Override
	public String toString() {
		return "AnalisaTransactionLogsDto [trxId=" + trxId + ", trxTime="
				+ trxTime + ", trxCode=" + trxCode + ", trxDesc=" + trxDesc
				+ ", channelCode=" + channelCode + ", channelRc=" + channelRc
				+ ", rcDesc=" + rcDesc + "]";
	}

}
