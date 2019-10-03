package com.balicamp.model.mx;

import java.io.Serializable;
import java.util.List;

public class TransactionFeeDto implements Serializable {
	private static final long serialVersionUID = 2594347500892149079L;
	
	private List<Integer> transactionIds;
	private String channelCode;
	private String identifier;
	
	public TransactionFeeDto() {
	}
	
	public TransactionFeeDto(List<Integer> transactionCode, String channelCode,
			String identifier) {
		super();
		this.transactionIds = transactionCode;
		this.channelCode = channelCode;
		this.identifier = identifier;
	}

	public List<Integer> getTransactionIds() {
		return transactionIds;
	}

	public void setTransactionIds(List<Integer> trxIds) {
		this.transactionIds = trxIds;
	}
	
	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public String toString() {
		return "TransactionFeeDto [transactionCode=" + transactionIds
				+ ", channelCode=" + channelCode + ", identifier=" + identifier
				+ "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((channelCode == null) ? 0 : channelCode.hashCode());
		result = prime * result
				+ ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result
				+ ((transactionIds == null) ? 0 : transactionIds.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransactionFeeDto other = (TransactionFeeDto) obj;
		if (channelCode == null) {
			if (other.channelCode != null)
				return false;
		} else if (!channelCode.equals(other.channelCode))
			return false;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (transactionIds == null) {
			if (other.transactionIds != null)
				return false;
		} else if (!transactionIds.equals(other.transactionIds))
			return false;
		return true;
	}
	
	
	

}
