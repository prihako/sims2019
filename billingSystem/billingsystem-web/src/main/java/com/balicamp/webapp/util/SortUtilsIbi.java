package com.balicamp.webapp.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Sorting Class
 * 
 * @author Rizal
 * @version $Id: SortUtilsIbi.java 114 2012-12-12 04:18:29Z bagus.sugitayasa $
 */
public class SortUtilsIbi {

	/**
	 * Menangkap Harga Jual dari OGNL
	 * 
	 * @param ognl
	 * @return
	 */
	public String getHargaJualOgnl(String ognl) {
		String[] temp;
		String sparatorKoma = "\\,";
		String sparatorEquals = "\\=";
		temp = ognl.split(sparatorKoma);

		String[] temp2 = temp[1].split(sparatorEquals, 0);

		return temp2[1];
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map sortByComparator(Map unsortMap) {

		List list = new LinkedList(unsortMap.entrySet());

		// sort list based on comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
						.compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		// put sorted list into map again
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
}
