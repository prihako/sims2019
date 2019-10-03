package com.balicamp.webapp.tapestry.component;

import java.io.Serializable;
import java.util.List;

import org.apache.tapestry.form.IPropertySelectionModel;

import com.balicamp.model.mx.Endpoints;

public class EndpointSelectionModel<T extends Endpoints> implements IPropertySelectionModel, Serializable {
	private static final long serialVersionUID = 8977919717167221957L;

	private List<T> itemList;

	public EndpointSelectionModel(List<T> itemList) {
		this.itemList = itemList;
	}

	public int getOptionCount() {
		return itemList.size();
	}

	public Object getOption(int index) {
		return itemList.get(index);
	}

	public String getLabel(int index) {
		return itemList.get(index).getCode();
	}

	public String getValue(int index) {
		return Integer.toString(itemList.get(index).getId());
	}

	public Object translateValue(String value) {
		return getOption(Integer.parseInt(value));
	}

	@Override
	public boolean isDisabled(int index) {
		return false;
	}

}
