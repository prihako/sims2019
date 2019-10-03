package com.balicamp.webapp.tapestry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.tapestry.form.IPropertySelectionModel;

import com.balicamp.model.ui.DefaultPropertySelectionData;
import com.balicamp.model.ui.PropertySelectionData;
import com.balicamp.service.common.MessageSourceWrapper;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class GenericPropertySelectionModel implements IPropertySelectionModel {

	public final static String VALUE_NOT_SELECT = null;
	public final static String VALUE_SELECT_ALL = "*";

	private List<PropertySelectionData> dataList = null;

	private PropertySelectionData optionNotSelect;
	private PropertySelectionData optionSelectAll;

	public static PropertySelectionData getOptionNotSelect(MessageSourceWrapper messageSource, Locale locale) {
		String label = "-- Pilih --";
		if (messageSource != null)
			label = messageSource.getMessage("common.label.comboBox.choose", null, label, locale);

		return new DefaultPropertySelectionData(label, VALUE_NOT_SELECT, false);
	}

	public PropertySelectionData getOptionSelectAll(MessageSourceWrapper messageSource, Locale locale) {
		String label = "-- Pilih Semua --";
		if (messageSource != null)
			label = messageSource.getMessage("common.label.comboBox.selectAll", null, label, locale);

		return new DefaultPropertySelectionData(label, VALUE_SELECT_ALL, false);
	}

	public GenericPropertySelectionModel( List<PropertySelectionData> dataList, MessageSourceWrapper messageSource,
			Locale locale ) {
		this(dataList, messageSource, locale, false);
	}

	public GenericPropertySelectionModel( List<PropertySelectionData> dataList, MessageSourceWrapper messageSource,
			Locale locale, boolean defaultNotSelect ) {
		this.dataList = dataList;
		if (defaultNotSelect) {
			optionNotSelect = getOptionNotSelect(messageSource, locale);
			addOption(0, optionNotSelect);
		}
	}

	public GenericPropertySelectionModel( List<PropertySelectionData> dataList, MessageSourceWrapper messageSource,
			Locale locale, boolean defaultNotSelect, boolean selectAllOption ) {
		this(dataList, messageSource, locale, defaultNotSelect);
		if (selectAllOption) {
			int pos = 0;
			if (defaultNotSelect)
				pos = 1;
			optionSelectAll = getOptionSelectAll(messageSource, locale);
			addOption(pos, optionSelectAll);
		}
	}

	public GenericPropertySelectionModel( Map<String, String> dataMap, MessageSourceWrapper messageSource,
			Locale locale, boolean defaultNotSelect, boolean selectAllOption ) {
		this(new ArrayList<PropertySelectionData>(), messageSource, locale, defaultNotSelect, selectAllOption);
		for (Entry<String, String> entry : dataMap.entrySet()) {
			dataList.add(new DefaultPropertySelectionData(entry.getValue(), entry.getKey(), false)); //NOPMD
		}
	}

	public void addOption(int pos, PropertySelectionData data) {
		dataList.add(pos, data);
	}

	public int getOptionCount() {
		return this.dataList.size();
	}

	public PropertySelectionData getOption(int index) {
		return dataList.get(index);
	}

	public String getLabel(int index) {
		return dataList.get(index).getPsdLabel();
	}

	public String getValue(int index) {
		return dataList.get(index).getPsdValue();
	}

	public boolean isDisabled(int index) {
		return dataList.get(index).isPsdDisabled();
	}

	public PropertySelectionData translateValue(String value) {
		for (PropertySelectionData data : dataList) {
			if (data.getPsdValue() != null && data.getPsdValue().equals(value)) {
				return data;
			}
		}
		return null;
	}

	public List<PropertySelectionData> getDataList() {
		return dataList;
	}

}
