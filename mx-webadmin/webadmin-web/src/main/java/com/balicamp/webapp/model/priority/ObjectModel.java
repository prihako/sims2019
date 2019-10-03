package com.balicamp.webapp.model.priority;

import java.io.Serializable;

public class ObjectModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private String key;
	private Object obj;

	public ObjectModel() {
		super();
	}

	public ObjectModel(String key, Object obj) {
		super();
		this.key = key;
		this.obj = obj;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
}
