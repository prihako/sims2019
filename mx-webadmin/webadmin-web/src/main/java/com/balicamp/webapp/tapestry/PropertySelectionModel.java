/**
 * 
 */
package com.balicamp.webapp.tapestry;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.tapestry.form.IPropertySelectionModel;

import com.balicamp.webapp.constant.WebConstant;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: PropertySelectionModel.java 114 2012-12-12 04:18:29Z bagus.sugitayasa $
 */
public class PropertySelectionModel implements IPropertySelectionModel, Serializable {
	private static final long serialVersionUID = -209877350737319261L;

	private List<Entry> selectionList;

	private String SELECT_ONE = "--Pilih satu--";
	private String SELECT_ALL = "--Pilih semua--";

	private final static String EMPTY_STR = "";

	private final static String DATE_ZERO = "--";

	private final static String ALL_STR_MARK = "*";

	private Locale locale;

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Locale getLocale() {
		return locale;
	}

	public PropertySelectionModel(Locale locale) {
		this.locale = locale;
		SELECT_ONE = getText("common.label.comboBox.choose");
		SELECT_ALL = getText("common.label.comboBox.selectAll");

		selectionList = new ArrayList<Entry>();
		add(EMPTY_STR, SELECT_ONE, null);
	}

	public PropertySelectionModel(Locale locale, Map mapOfDropDownContent) {
		this.locale = locale;
		SELECT_ONE = getText("common.label.comboBox.choose");
		SELECT_ALL = getText("common.label.comboBox.selectAll");

		selectionList = new ArrayList<Entry>();
		load(mapOfDropDownContent);
	}

	public PropertySelectionModel(Locale locale, Map mapOfDropDownContent, boolean enableSelectOne,
			boolean enableSelectAll) {
		this.locale = locale;
		SELECT_ONE = getText("common.label.comboBox.choose");
		SELECT_ALL = getText("common.label.comboBox.selectAll");

		selectionList = new ArrayList<Entry>();
		if (enableSelectOne)
			add(EMPTY_STR, SELECT_ONE, null);
		if (enableSelectAll)
			add(ALL_STR_MARK, SELECT_ALL, ALL_STR_MARK);
		load(mapOfDropDownContent);
	}
	
	public PropertySelectionModel(Locale locale, Map mapOfDropDownContent, boolean enableSelectOne,
			boolean enableSelectAll, String sortedFlag) {
		this.locale = locale;
		SELECT_ONE = getText("common.label.comboBox.choose");
		SELECT_ALL = getText("common.label.comboBox.selectAll");

		selectionList = new ArrayList<Entry>();
		if (enableSelectOne)
			add(EMPTY_STR, SELECT_ONE, null);
		if (enableSelectAll)
			add(ALL_STR_MARK, SELECT_ALL, ALL_STR_MARK);
		loadNotSorted(mapOfDropDownContent);
	}

	public PropertySelectionModel(Locale locale, Map mapOfDropDownContent, boolean enableSelectOne,
			boolean enableSelectAll, boolean sorted) { // NOPMD
		this.locale = locale;
		SELECT_ONE = getText("common.label.comboBox.choose");
		SELECT_ALL = getText("common.label.comboBox.selectAll");

		selectionList = new ArrayList<Entry>();
		if (enableSelectOne)
			add(EMPTY_STR, SELECT_ONE, null);
		if (enableSelectAll)
			add(ALL_STR_MARK, SELECT_ALL, ALL_STR_MARK);
		loadIntegerKeyAddSorted(listOfIntegerKeyValue(mapOfDropDownContent), mapOfDropDownContent);
	}

	public PropertySelectionModel(Locale locale, Map mapOfDropDownContent, boolean integerSorted) { // NOPMD
		this.locale = locale;
		SELECT_ONE = getText("common.label.comboBox.choose");
		SELECT_ALL = getText("common.label.comboBox.selectAll");

		selectionList = new ArrayList<Entry>();
		add(EMPTY_STR, DATE_ZERO, null);
		loadIntegerKeyAddSorted(listOfIntegerKeyValue(mapOfDropDownContent), mapOfDropDownContent);
	}

	public PropertySelectionModel(Locale locale, int initialCapacity) {
		this.locale = locale;
		SELECT_ONE = getText("common.label.comboBox.choose");
		SELECT_ALL = getText("common.label.comboBox.selectAll");

		selectionList = new ArrayList<Entry>(initialCapacity + 1);
		add(EMPTY_STR, SELECT_ONE, null);
	}

	public PropertySelectionModel(Locale locale, int initialCapacity, boolean enableSelectOne) {
		this.locale = locale;
		SELECT_ONE = getText("common.label.comboBox.choose");
		SELECT_ALL = getText("common.label.comboBox.selectAll");

		if (enableSelectOne) {
			selectionList = new ArrayList<Entry>(initialCapacity + 1);
			add(EMPTY_STR, SELECT_ONE, null);
		} else {
			selectionList = new ArrayList<Entry>(initialCapacity);
		}
	}

	/**
	 * @param id   String of id/value
	 * @param text String of text/label
	 * @param obj  Object
	 */
	public void add(String id, String text, Object obj) {
		selectionList.add(new Entry(id, text, obj));
	}

	/**
	 * @param index int index
	 * @param id    String of id/value
	 * @param text  String of text/label
	 * @param obj   Object
	 */
	public void add(int index, String id, String text, Object obj) {
		selectionList.add(index, new Entry(id, text, obj));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tapestry.form.IPropertySelectionModel#getLabel(int)
	 */
	public String getLabel(int index) {
		return selectionList.get(index).getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tapestry.form.IPropertySelectionModel#getOption(int)
	 */
	public Object getOption(int index) {
		if (index == -1)
			return null;
		return selectionList.get(index).getObj();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tapestry.form.IPropertySelectionModel#getOptionCount()
	 */
	public int getOptionCount() {
		return selectionList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tapestry.form.IPropertySelectionModel#getValue(int)
	 */
	public String getValue(int index) {
		return selectionList.get(index).getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.tapestry.form.IPropertySelectionModel#translateValue(java.
	 * lang.String)
	 */
	public Object translateValue(String value) {
		return getOption(selectionList.indexOf(new Entry(value, null, null)));
	}

	class Entry implements Serializable {
		String id;
		String text;
		Object obj;

		public Entry(String id, String text, Object obj) {
			this.id = id;
			this.text = text;
			this.obj = obj;
		}

		/**
		 * @return Returns the id.
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param id The id to set.
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @return Returns the text.
		 */
		public String getText() {
			return text;
		}

		/**
		 * @param text The text to set.
		 */
		public void setText(String text) {
			this.text = text;
		}

		/**
		 * @return Returns the obj.
		 */
		public Object getObj() {
			return obj;
		}

		/**
		 * @param obj The obj to set.
		 */
		public void setObj(Object obj) {
			this.obj = obj;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj == null || id == null || ((Entry) obj).getId() == null)
				return false;
			return obj instanceof Entry && id.equals(((Entry) obj).getId());
		}
	}

	private void load(Map mapOfDropdownContent) {
		if (mapOfDropdownContent != null) {
			Set setOfContent = mapOfDropdownContent.keySet();
			TreeSet treeOfContent = new TreeSet();
			treeOfContent.addAll(setOfContent);
			for (Object aTreeOfContent : treeOfContent) {
				String id = null;
				String value = null;
				Object idObject;
				idObject = aTreeOfContent;
				if (idObject instanceof Integer) {
					id = String.valueOf((Integer) idObject);
					value = String.valueOf(mapOfDropdownContent.get((Integer) idObject));
				} else {
					id = (String) idObject;
					value = String.valueOf(mapOfDropdownContent.get(id));
				}
				add(id, value, id);
			}
		}
	}
	
	private void loadNotSorted(Map mapOfDropdownContent) {
		if (mapOfDropdownContent != null) {
			Set setOfContent = mapOfDropdownContent.keySet();
			Set treeOfContent = new LinkedHashSet();
			treeOfContent.addAll(setOfContent);
			for (Object aTreeOfContent : treeOfContent) {
				String id = null;
				String value = null;
				Object idObject;
				idObject = aTreeOfContent;
				if (idObject instanceof Integer) {
					id = String.valueOf((Integer) idObject);
					value = String.valueOf(mapOfDropdownContent.get((Integer) idObject));
				} else {
					id = (String) idObject;
					value = String.valueOf(mapOfDropdownContent.get(id));
				}
				add(id, value, id);
			}
		}
	}

	private void loadAddSorted(List list) { // NOPMD
		for (Object aList : list) {
			String element = (String) aList;
			String[] value = element.split("#");
			add(value[1].trim(), value[0].trim(), value[1].trim());
		}
	}

	private List listOfValue(Map map) { // NOPMD
		List list = null;

		if (map != null) {
			list = new ArrayList();
			Set set = map.keySet();
			for (Object aSet : set) {
				String keyElement = (String) aSet;
				list.add(map.get(keyElement) + "#" + keyElement);
			}
		}
		Collections.sort(list);
		return list;
	}

	private void loadIntegerKeyAddSorted(List list, Map mapOfContent) {
		for (Object aList : list) {
			String keyInt = String.valueOf(aList);
			add(keyInt, String.valueOf(mapOfContent.get(keyInt)), keyInt);
		}
	}

	private List listOfIntegerKeyValue(Map map) {
		List list = null;

		if (map != null) {
			list = new ArrayList();
			Set set = map.keySet();
			for (Object aSet : set) {
				String keyElement = (String) aSet;
				list.add(Integer.parseInt(String.valueOf(keyElement)));
			}
		}
		Collections.sort(list);
		return list;
	}

	public boolean isDisabled(int arg0) {
		return false;
	}

	/**
	 * Build error message from properties
	 *
	 * @param key
	 * @return
	 */
	private String getText(String key) {
		return MessageFormat.format(WebConstant.extractLocalizedMessage(key, getLocale()), new Object[] {});
	}
}
