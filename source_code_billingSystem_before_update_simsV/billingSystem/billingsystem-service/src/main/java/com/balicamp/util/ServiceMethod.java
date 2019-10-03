package com.balicamp.util;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServiceMethod implements Serializable {

	private static final long serialVersionUID = -8265074896061304251L;

	private String ident;

	private String beanName;

	private String caption;

	private String methodName;

	private volatile String progressInText;

	private volatile float progressInPercent;

	private AtomicBoolean busy;

	private AtomicBoolean error;

	public ServiceMethod() {
		this.ident = UUID.randomUUID().toString().replace("-", "");

		this.busy = new AtomicBoolean(false);
		this.error = new AtomicBoolean(false);
	}

	public ServiceMethod(String beanName, String caption, String methodName) {
		this();

		this.beanName = beanName;
		this.caption = caption;
		this.methodName = methodName;
	}

	public String getIdent() {
		return ident;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getProgressInText() {
		return progressInText;
	}

	public void setProgressInText(String progressInText) {
		this.progressInText = progressInText;
	}

	public float getProgressInPercent() {
		return progressInPercent;
	}

	public void setProgressInPercent(float progressInPercent) {
		this.progressInPercent = progressInPercent;
	}

	public AtomicBoolean getBusy() {
		return busy;
	}

	public void setBusy(AtomicBoolean busy) {
		this.busy = busy;
	}

	public AtomicBoolean getError() {
		return error;
	}

	public void setError(AtomicBoolean error) {
		this.error = error;
	}
}
