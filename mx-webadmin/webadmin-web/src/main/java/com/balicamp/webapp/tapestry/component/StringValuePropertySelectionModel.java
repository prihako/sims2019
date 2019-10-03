package com.balicamp.webapp.tapestry.component;

import java.io.Serializable;

import org.apache.tapestry.form.IPropertySelectionModel;

public class StringValuePropertySelectionModel implements Serializable, IPropertySelectionModel {

	private String[] label;
	private String[] value;
	private boolean[] isDisabled;

	public StringValuePropertySelectionModel(String[] label, String[] value) {
		this.label = label;
		this.value = value;
	}

	public String[] getLabel() {
		return label;
	}

	public void setLabel(String[] label) {
		this.label = label;
	}

	public String[] getValue() {
		return value;
	}

	public void setValue(String[] value) {
		this.value = value;
	}

	public boolean[] getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(boolean[] isDisabled) {
		this.isDisabled = isDisabled;
	}

	@Override
	public int getOptionCount() {
		// TODO Auto-generated method stub
		return value.length;
	}

	@Override
	public Object getOption(int index) {
		// TODO Auto-generated method stub
		return value[index];
	}

	@Override
	public String getLabel(int index) {
		// TODO Auto-generated method stub
		return label[index];
	}

	@Override
	public String getValue(int index) {
		// TODO Auto-generated method stub
		return value[index];
	}

	@Override
	public boolean isDisabled(int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object translateValue(String value) {
		// TODO Auto-generated method stub
		return value;
	}
}
