package com.balicamp.model.ui;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Default implementation PropertySelectionData
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class DefaultPropertySelectionData 
	implements Serializable, PropertySelectionData 
{
	private static final long serialVersionUID = 8427396029392828842L;
	
	private String psdLabel;
	private String psdValue;
	private boolean psdDisabled;
	
	public DefaultPropertySelectionData(String psdLabel, String psdValue, boolean psdDisabled){
		this.psdLabel = psdLabel;
		this.psdValue = psdValue;
		this.psdDisabled = psdDisabled;
	}

	public DefaultPropertySelectionData(String psdLabelAndValue,boolean psdDisabled){
		this.psdLabel = psdLabelAndValue;
		this.psdValue = psdLabelAndValue;
		this.psdDisabled = psdDisabled;
	}

	public String getPsdLabel() {
		return psdLabel;
	}

	public String getPsdValue() {
		return psdValue;
	}

	public boolean isPsdDisabled() {
		return psdDisabled;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (object == this) return true;
		if ( object == null ) return false;
		if (!(object instanceof DefaultPropertySelectionData)) return false;

		DefaultPropertySelectionData rhs = (DefaultPropertySelectionData) object;
		return new EqualsBuilder()
			.append(this.psdLabel, rhs.psdLabel)
			.append(this.psdValue, rhs.psdValue)
			.append(this.psdDisabled, rhs.psdDisabled)
			.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-1530980243, 1570486375)
			.appendSuper( super.hashCode())
			.append(this.psdLabel)
			.append(this.psdValue)
			.append(this.psdDisabled)
			.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("psdValue",this.psdValue)
			.append("psdLabel", this.psdLabel)
			.append("psdDisabled", this.psdDisabled)
			.toString();
	}
	
}
