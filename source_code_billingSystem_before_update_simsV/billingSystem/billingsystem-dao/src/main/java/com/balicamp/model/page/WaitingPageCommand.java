package com.balicamp.model.page;

import java.io.Serializable;

import org.springframework.context.annotation.Bean;

public class WaitingPageCommand implements Serializable {
	private static final long serialVersionUID = -1578609344118508444L;

	private String deliveryChannelId;

	private Bean response;

	private String nextPage;

	private String previousPage;

	private String timeoutPage;

	private String errorPage;

	private String nextPageInitMethod;

	private String previousPageInitMethod;

	private String timeoutPageInitMethod;

	private String errorPageInitMethod;

	private Object paramList;

	public String getDeliveryChannelId() {
		return deliveryChannelId;
	}

	public void setDeliveryChannelId(String deliveryChannelId) {
		this.deliveryChannelId = deliveryChannelId;
	}

	public Bean getResponse() {
		return response;
	}

	public void setResponse(Bean response) {
		this.response = response;
	}

	public String getNextPage() {
		return nextPage;
	}

	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

	public String getPreviousPage() {
		return previousPage;
	}

	public void setPreviousPage(String previousPage) {
		this.previousPage = previousPage;
	}

	public String getTimeoutPage() {
		return timeoutPage;
	}

	public void setTimeoutPage(String timeoutPage) {
		this.timeoutPage = timeoutPage;
	}

	public String getErrorPage() {
		return errorPage;
	}

	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}

	public String getNextPageInitMethod() {
		return nextPageInitMethod;
	}

	public void setNextPageInitMethod(String nextPageInitMethod) {
		this.nextPageInitMethod = nextPageInitMethod;
	}

	public String getPreviousPageInitMethod() {
		return previousPageInitMethod;
	}

	public void setPreviousPageInitMethod(String previousPageInitMethod) {
		this.previousPageInitMethod = previousPageInitMethod;
	}

	public String getTimeoutPageInitMethod() {
		return timeoutPageInitMethod;
	}

	public void setTimeoutPageInitMethod(String timeoutPageInitMethod) {
		this.timeoutPageInitMethod = timeoutPageInitMethod;
	}

	public String getErrorPageInitMethod() {
		return errorPageInitMethod;
	}

	public void setErrorPageInitMethod(String errorPageInitMethod) {
		this.errorPageInitMethod = errorPageInitMethod;
	}

	public Object getParamList() {
		return paramList;
	}

	public void setParamList(Object paramList) {
		this.paramList = paramList;
	}
}
