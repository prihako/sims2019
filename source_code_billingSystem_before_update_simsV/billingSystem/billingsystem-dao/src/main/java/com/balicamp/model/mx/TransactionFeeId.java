package com.balicamp.model.mx;

// Generated Dec 13, 2012 12:58:42 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TransactionFeeId generated by hbm2java
 * @version $Id: TransactionFeeId.java 503 2013-05-24 08:13:16Z rudi.sadria $
 */
@Embeddable
public class TransactionFeeId implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int transactionId;
	private int channelId;
	private String identifier;
	private int fee;

	public TransactionFeeId() {
	}

	public TransactionFeeId(int transactionId, int channelId, int fee) {
		this.transactionId = transactionId;
		this.channelId = channelId;
		this.fee = fee;
	}

	public TransactionFeeId(int transactionId, int channelId, String identifier, int fee) {
		this.transactionId = transactionId;
		this.channelId = channelId;
		this.identifier = identifier;
		this.fee = fee;
	}

	@Column(name = "transaction_id", nullable = false)
	public int getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	@Column(name = "channel_id", nullable = false)
	public int getChannelId() {
		return this.channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	@Column(name = "identifier", length = 100)
	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Column(name = "fee", nullable = false)
	public int getFee() {
		return this.fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TransactionFeeId))
			return false;
		TransactionFeeId castOther = (TransactionFeeId) other;

		return (this.getTransactionId() == castOther.getTransactionId())
				&& (this.getChannelId() == castOther.getChannelId())
				&& ((this.getIdentifier() == castOther.getIdentifier()) || (this.getIdentifier() != null
						&& castOther.getIdentifier() != null && this.getIdentifier().equals(castOther.getIdentifier())))
				&& (this.getFee() == castOther.getFee());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getTransactionId();
		result = 37 * result + this.getChannelId();
		result = 37 * result + (getIdentifier() == null ? 0 : this.getIdentifier().hashCode());
		result = 37 * result + this.getFee();
		return result;
	}

	@Override
	public String toString() {
		return "TransactionFeeId [transactionId=" + transactionId + ", channelId=" + channelId + ", identifier="
				+ identifier + ", fee=" + fee + "]";
	}

}
