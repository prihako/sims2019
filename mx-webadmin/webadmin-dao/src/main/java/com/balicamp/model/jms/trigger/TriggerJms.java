package com.balicamp.model.jms.trigger;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class TriggerJms implements Serializable {
	private static final long serialVersionUID = -6696762025551274888L;
	
	public static String JMS_SOURCE_WEB = "WEB";
	public static String JMS_SOURCE_SCHEDULER = "SCHEDULER";
	public static String JMS_SOURCE_ADMIN = "ADMIN"; 
	
	protected String id;
	protected String source;
	protected boolean waitResponse;
	protected long coreTimeOut = -1;
	
	//id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	//source
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	//waitResponse
	public boolean isWaitResponse() {
		return waitResponse;
	}
	public void setWaitResponse(boolean waitResponse) {
		this.waitResponse = waitResponse;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if ( this == object ) return true;
		if (!(object instanceof TriggerJms)) return false;
		
		TriggerJms rhs = (TriggerJms) object;
		return new EqualsBuilder()
			.append(this.id, rhs.id)
			.append(this.source, rhs.source)
			.append(this.waitResponse, rhs.waitResponse)
			.isEquals();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(1734752251, 251471021)
			.appendSuper(super.hashCode())
			.append(this.id)
			.append(this.source)
			.append(this.waitResponse)
			.toHashCode();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("id", this.id)
			.append("source", this.source)
			.append("waitResponse", this.waitResponse)
			.toString();
	}
	
	//coreTimeOut
	public long getCoreTimeOut() {
		return coreTimeOut;
	}
	public void setCoreTimeOut(long coreTimeOut) {
		this.coreTimeOut = coreTimeOut;
	}
	
}
