package com.balicamp.webapp.tapestry;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.form.IPropertySelectionModel;

public class BooleanPropertySelectionModel implements IPropertySelectionModel {

	private List<Boolean> dataList = null;

	public BooleanPropertySelectionModel() {
		dataList = new ArrayList<Boolean>();
		dataList.add(new Boolean(true));
		dataList.add(new Boolean(false));
	}

	@Override
	public int getOptionCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getOption(int index) {
		// TODO Auto-generated method stub
		return dataList.get(index);
	}

	@Override
	public String getLabel(int index) {
		if (dataList.get(index) == true)
			return "Ya";
		return "Tidak";
	}

	@Override
	public String getValue(int index) {
		// TODO Auto-generated method stub
		if (dataList.get(index) == true)
			return "Ya";
		return "Tidak";
	}

	@Override
	public boolean isDisabled(int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object translateValue(String value) {
		if (value.equalsIgnoreCase("ya") || value.equalsIgnoreCase("true"))
			return true;
		return false;
	}

}
