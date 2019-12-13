package com.balicamp.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LogHelper {
	public static String toString(Object[] array) {

		if (array != null && array.length > 0) {
			StringBuffer buff = new StringBuffer();
			for (Object temp : array) {
				if (temp == null) {
					buff.append("null");
				} else {
					buff.append(temp.toString());
				}

				buff.append("|");
			}

			return buff.toString();
		} else {
			return "";
		}
	}
	
	public static String toString(Set<String> array) {

		if (array != null && array.size() > 0) {
			StringBuffer buff = new StringBuffer();
			for (String temp : array) {
				if (temp == null) {
					buff.append("null");
				} else {
					buff.append(temp);
				}

				buff.append("|");
			}

			return buff.toString();
		} else {
			return "";
		}
	}

	public static String toString(List<Object[]> array) {

		if (array != null && array.size() > 0) {
			StringBuffer buff = new StringBuffer();

			for (Object[] outer : array) {
				for (Object inner : outer) {
					if (inner == null) {
						buff.append("null");
					} else {
						buff.append(inner.toString());
					}

					buff.append("|");
				}
				buff.append("\n");
			}

			return buff.toString();
		} else {
			return "";
		}
	}

	public static String toString(HashMap<String, Object[]> array) {

		StringBuffer buff = new StringBuffer();
		if (array != null) {
			for (Map.Entry<String, Object[]> entry : array.entrySet()) {
				Object[] inner = entry.getValue();
				buff.append(entry.getKey() + "==>");
				for (Object temp : inner) {
					if (temp == null) {
						buff.append("null");
					} else {
						buff.append(temp.toString());
					}

					buff.append("|");
				}
				buff.append("\n");
			}
			
			return buff.toString();

		} else {
			return "";
		}
	}
}
