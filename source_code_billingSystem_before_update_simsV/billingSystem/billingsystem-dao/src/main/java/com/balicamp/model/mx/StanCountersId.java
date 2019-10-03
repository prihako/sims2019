package com.balicamp.model.mx;

// Generated Dec 13, 2012 12:58:42 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * StanCountersId generated by hbm2java
 * @version $Id: StanCountersId.java 503 2013-05-24 08:13:16Z rudi.sadria $
 */
@Embeddable
public class StanCountersId implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int endpointId;
	private int value;
	private long maxValue;

	public StanCountersId() {
	}

	public StanCountersId(int endpointId, int value, long maxValue) {
		this.endpointId = endpointId;
		this.value = value;
		this.maxValue = maxValue;
	}

	@Column(name = "endpoint_id", unique = true, nullable = false)
	public int getEndpointId() {
		return this.endpointId;
	}

	public void setEndpointId(int endpointId) {
		this.endpointId = endpointId;
	}

	@Column(name = "value", nullable = false)
	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Column(name = "max_value", nullable = false)
	public long getMaxValue() {
		return this.maxValue;
	}

	public void setMaxValue(long maxValue) {
		this.maxValue = maxValue;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof StanCountersId))
			return false;
		StanCountersId castOther = (StanCountersId) other;

		return (this.getEndpointId() == castOther.getEndpointId()) && (this.getValue() == castOther.getValue())
				&& (this.getMaxValue() == castOther.getMaxValue());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getEndpointId();
		result = 37 * result + this.getValue();
		result = 37 * result + (int) this.getMaxValue();
		return result;
	}

}
