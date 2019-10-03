package com.balicamp.model.common;

import org.apache.commons.lang.builder.ToStringBuilder;

public class MethodResponse<T> {
	protected boolean status;
	protected String code;
	protected String messageKey;
	protected String defaultMessage;
	protected String referenceNumber;
	protected String componentName;
	protected Object[] messageParam;

	protected T data;

	public MethodResponse(){
	}

	public MethodResponse(boolean status, String code, String messageKey, String defaultMessage){
		setStatus(status);
		setCode(code);
		setMessageKey(messageKey);
		setDefaultMessage(defaultMessage);
	}

	public MethodResponse(boolean status, String code, String messageKey, String defaultMessage, Object[] messageParam){
		this(status, code, messageKey, defaultMessage);
		setMessageParam(messageParam);
	}

	public String getMessageKey() {
		return messageKey;
	}
	
	public String getDefaultMessage() {
		return defaultMessage;
	}
	
	//status
	public void setStatus(boolean status) {
		this.status = status;
	}
	public boolean isStatus() {
		return status;
	}

	//code
	public void setCode(String code) {
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	
	//messageKey
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
	public void setDefaultMessage(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}
	
	public String getComponentName() {
		return componentName;
	}
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	//referenceNumber
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	//messageParam
	public Object[] getMessageParam() {
		return messageParam;			//NOPMD
	}
	public void setMessageParam(Object[] messageParam) {
		this.messageParam = messageParam; 			//NOPMD
	}

	//data
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("messageKey", this.messageKey)
			.append("data", this.data)
			.append("defaultMessage",this.defaultMessage)
			.append("code", this.code)
			.append("referenceNumber", this.referenceNumber)
			.append("status", this.status)
			.append("messageParam", this.messageParam)
			.toString();
	}
	
	
}
