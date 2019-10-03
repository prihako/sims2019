package id.co.sigma.mx.project.ftpreconcile.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class UserSequences {
	private String sequenceName;

	private BigDecimal minValue;

	private BigDecimal maxValue;

	private BigDecimal incrementBy;

	private String cycleFlag;

	private String orderFlag;

	private BigDecimal cacheSize;

	private BigDecimal lastNumber;

	public BigDecimal getCacheSize() {
		return cacheSize;
	}

	public void setCacheSize(BigDecimal cacheSize) {
		this.cacheSize = cacheSize;
	}

	public String getCycleFlag() {
		return cycleFlag;
	}

	public void setCycleFlag(String cycleFlag) {
		this.cycleFlag = cycleFlag;
	}

	public BigDecimal getIncrementBy() {
		return incrementBy;
	}

	public void setIncrementBy(BigDecimal incrementBy) {
		this.incrementBy = incrementBy;
	}

	public BigDecimal getLastNumber() {
		return lastNumber;
	}

	public void setLastNumber(BigDecimal lastNumber) {
		this.lastNumber = lastNumber;
	}

	public BigDecimal getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}

	public BigDecimal getMinValue() {
		return minValue;
	}

	public void setMinValue(BigDecimal minValue) {
		this.minValue = minValue;
	}

	public String getOrderFlag() {
		return orderFlag;
	}

	public void setOrderFlag(String orderFlag) {
		this.orderFlag = orderFlag;
	}

	public String getSequenceName() {
		return sequenceName;
	}

	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
