/**
 * 
 */
package com.balicamp.model.mx;

import java.io.Serializable;
import java.util.Date;

import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.util.DateUtil;

/**
 * Data Transfer Object for Message Log 
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: MessageLogsDto.java 503 2013-05-24 08:13:16Z rudi.sadria $
 */
public class MessageLogsDto extends BaseAdminModel implements Serializable, Comparable<MessageLogsDto> {

	private static final long serialVersionUID = 1L;

	private String transactionId;
	private String transactionCode;
	private String transactionName;
	private String endpointCode;
	private String endpointId;
	private Date conversionTime;
	private Date transactionDateTime;
	private String rc;
	private String rcDesc;
	private String mappingCode;
	private boolean isRequest;
	private String raw;
	private String formatedMessage;

	public MessageLogsDto() {
	}

	public MessageLogsDto(String transactionId, String transactionCode, String transactionName, String endpointCode,
			Date transactionDateTime, String rc, String rcDesc, String mappingCode, boolean isRequest,
			Date conversionTime) {
		super();
		this.transactionId = transactionId;
		this.transactionCode = transactionCode;
		this.transactionName = transactionName;
		this.endpointCode = endpointCode;
		this.transactionDateTime = transactionDateTime;
		this.rc = rc;
		this.rcDesc = rcDesc;
		this.mappingCode = mappingCode;
		this.isRequest = isRequest;
		this.conversionTime = conversionTime;
	}

	public String getEndpointId() {
		return endpointId;
	}

	public void setEndpointId(String endpointId) {
		this.endpointId = endpointId;
	}

	public MessageLogsDto(String transactionId, String transactionCode, String transactionName, String endpointCode,
			Date transactionDateTime, String rc, String rcDesc, String mappingCode, boolean isRequest, String raw) {
		super();
		this.transactionId = transactionId;
		this.transactionCode = transactionCode;
		this.transactionName = transactionName;
		this.endpointCode = endpointCode;
		this.transactionDateTime = transactionDateTime;
		this.rc = rc;
		this.rcDesc = rcDesc;
		this.mappingCode = mappingCode;
		this.isRequest = isRequest;
		this.raw = raw;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getTransactionName() {
		return transactionName;
	}

	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}

	public String getEndpointCode() {
		return endpointCode;
	}

	public void setEndpointCode(String endpointCode) {
		this.endpointCode = endpointCode;
	}

	public Date getTransactionDateTime() {
		return transactionDateTime;
	}

	public void setTransactionDateTime(Date transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}

	public String getRc() {
		return rc;
	}

	public void setRc(String rc) {
		this.rc = rc;
	}

	public String getRcDesc() {
		return rcDesc;
	}

	public void setRcDesc(String rcDesc) {
		this.rcDesc = rcDesc;
	}

	public String getMappingCode() {
		return mappingCode;
	}

	public void setMappingCode(String mappingCode) {
		this.mappingCode = mappingCode;
	}

	public boolean isRequest() {
		return isRequest;
	}

	public void setRequest(boolean isRequest) {
		this.isRequest = isRequest;
	}

	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}

	public Date getConversionTime() {
		return conversionTime;
	}

	public void setConversionTime(Date conversionTime) {
		this.conversionTime = conversionTime;
	}

	public String getFormatedMessage() {
		return formatedMessage;
	}

	public void setFormatedMessage(String formatedMessage) {
		this.formatedMessage = formatedMessage;
	}

	public String getFormatedTransDate() {
		return DateUtil.convertDateToString(this.transactionDateTime, "dd MMM yyyy HH:mm:ss");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((conversionTime == null) ? 0 : conversionTime.hashCode());
		result = prime * result + ((endpointCode == null) ? 0 : endpointCode.hashCode());
		result = prime * result + ((formatedMessage == null) ? 0 : formatedMessage.hashCode());
		result = prime * result + (isRequest ? 1231 : 1237);
		result = prime * result + ((mappingCode == null) ? 0 : mappingCode.hashCode());
		result = prime * result + ((raw == null) ? 0 : raw.hashCode());
		result = prime * result + ((rc == null) ? 0 : rc.hashCode());
		result = prime * result + ((rcDesc == null) ? 0 : rcDesc.hashCode());
		result = prime * result + ((transactionCode == null) ? 0 : transactionCode.hashCode());
		result = prime * result + ((transactionDateTime == null) ? 0 : transactionDateTime.hashCode());
		result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
		result = prime * result + ((transactionName == null) ? 0 : transactionName.hashCode());
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
		MessageLogsDto other = (MessageLogsDto) obj;
		if (conversionTime == null) {
			if (other.conversionTime != null)
				return false;
		} else if (!conversionTime.equals(other.conversionTime))
			return false;
		if (endpointCode == null) {
			if (other.endpointCode != null)
				return false;
		} else if (!endpointCode.equals(other.endpointCode))
			return false;
		if (formatedMessage == null) {
			if (other.formatedMessage != null)
				return false;
		} else if (!formatedMessage.equals(other.formatedMessage))
			return false;
		if (isRequest != other.isRequest)
			return false;
		if (mappingCode == null) {
			if (other.mappingCode != null)
				return false;
		} else if (!mappingCode.equals(other.mappingCode))
			return false;
		if (raw == null) {
			if (other.raw != null)
				return false;
		} else if (!raw.equals(other.raw))
			return false;
		if (rc == null) {
			if (other.rc != null)
				return false;
		} else if (!rc.equals(other.rc))
			return false;
		if (rcDesc == null) {
			if (other.rcDesc != null)
				return false;
		} else if (!rcDesc.equals(other.rcDesc))
			return false;
		if (transactionCode == null) {
			if (other.transactionCode != null)
				return false;
		} else if (!transactionCode.equals(other.transactionCode))
			return false;
		if (transactionDateTime == null) {
			if (other.transactionDateTime != null)
				return false;
		} else if (!transactionDateTime.equals(other.transactionDateTime))
			return false;
		if (transactionId == null) {
			if (other.transactionId != null)
				return false;
		} else if (!transactionId.equals(other.transactionId))
			return false;
		if (transactionName == null) {
			if (other.transactionName != null)
				return false;
		} else if (!transactionName.equals(other.transactionName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MessageLogDisplay [transactionId=" + transactionId + ", transactionCode=" + transactionCode
				+ ", transactionName=" + transactionName + ", endpointCode=" + endpointCode + ", conversionTime="
				+ conversionTime + ", transactionDateTime=" + transactionDateTime + ", rc=" + rc + ", rcDesc=" + rcDesc
				+ ", mappingCode=" + mappingCode + ", isRequest=" + isRequest + ", raw=" + raw + "]";
	}

	@Override
	public MessageLogsDto getPKey() {
		return new MessageLogsDto(transactionId, transactionCode, transactionName, endpointCode,
				transactionDateTime, rc, rcDesc, mappingCode, isRequest, conversionTime);
	}

	@Override
	public int compareTo(MessageLogsDto mld) {
		return this.hashCode() - mld.hashCode();
	}
}
