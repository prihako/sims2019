package com.balicamp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InquiryPaymentStatusUtil {
	public static void main(String[] args) {
		Map<String, Object[]> map = new HashMap<String, Object[]>();
		map.put("chws-df560-0912.181828-1",
				new String[] { "chws-df560-0912.181828-1", "49571", "2014", "0262975", "Billing System Payment", "5", "00", "00", "12-09-2014 18:18:28", "Bank Mandiri", "SpectraPlus: Approved", "WebService: Approved" });
		map.put("chws-df560-0904.154847-28",
				new String[] { "chws-df560-0904.154847-28", "8608", "2014", "0372618", "Billing System Payment", "5", "00", "00", "04-09-2014 15:48:47", "Bank Mandiri", "SpectraPlus: Approved", "WebService: Approved" });
		map.put("chws-df560-0902.154448-8",
				new String[] { "chws-df560-0902.154448-8", "8071", "2014", "0281603", "Billing System Payment", "5", "10", "B5", "02-09-2016 15:44:48", "Bank Mandiri", "SpectraPlus: Data Transaction Issuer dan SPECTRA tidak cocok", "WebService: Tagihan tidak ditemukan / Nomor Referensi salah" });
		map.put("chws-df560-0902.154639-10",
				new String[] { "chws-df560-0902.154639-10", "8071", "2014", "0281603", "Billing System Payment", "5", "97", "B8", "02-09-2014 15:46:39", "Bank Mandiri", "SpectraPlus: Invoice already paid ", "WebService: Tagihan sudah dibayar" });
		map.put("chws-df560-0902.154527-9",
				new String[] { "chws-df560-0902.154527-9", "8071", "2014", "5517558", "Billing System Payment", "5", "10", "B5", "02-09-2014 15:45:27", "Bank Mandiri", "SpectraPlus: Approved", "WebService: Approved" });
		map.put("chws-df560-0903.142500-17",
				new String[] { "chws-df560-0903.142500-17", "8071", "2014", "0281603", "Billing System Payment", "5", "00", "00", "19-08-2015 14:25:00", "Bank Mandiri", "SpectraPlus: Approved", "WebService: Approved" });

		System.out.println("Before sort : ");
		InquiryPaymentStatusUtil.printMap(map);

		System.out.println("After Sort : ");
		Map<String, Object[]> sortedMap = sortByComparatorInvoiceNo(map);
		InquiryPaymentStatusUtil.printMap(sortedMap);

		System.out.println("After grouping : ");
		Map<String, Map<String, Object[]>> groupMap = groupingMapByInvoiceNo(sortedMap);
		InquiryPaymentStatusUtil.printMap2(groupMap);

		System.out.println("Remove Duplicate Element : ");
		Map<String, Object[]> result = removeDuplicate(groupMap);
		InquiryPaymentStatusUtil.printMap(result);

	}

	public static Map<String, Object[]> sortByComparatorInvoiceNo(Map<String, Object[]> unsortMap) {
		// Convert Map to List
		List<Map.Entry<String, Object[]>> list = new LinkedList<Map.Entry<String, Object[]>>(unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Object[]>>() {
			public int compare(Map.Entry<String, Object[]> o1, Map.Entry<String, Object[]> o2) {
				return (o1.getValue()[3].toString()).compareTo(o2.getValue()[3].toString());
			}
		});

		// Convert sorted map back to a Map
		Map<String, Object[]> sortedMap = new LinkedHashMap<String, Object[]>();
		for (Iterator<Map.Entry<String, Object[]>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Object[]> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	public static Map<String, Object[]> sortByTransactionTime(Map<String, Object[]> unsortMap) {

		final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		// Convert Map to List
		List<Map.Entry<String, Object[]>> list = new LinkedList<Map.Entry<String, Object[]>>(unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Object[]>>() {
			public int compare(Map.Entry<String, Object[]> o1, Map.Entry<String, Object[]> o2) {
				Date date1 = new Date();
				Date date2 = new Date();
				try {
					date1 = formatter.parse(o1.getValue()[8].toString());
					date2 = formatter.parse(o2.getValue()[8].toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}

				return (date1).compareTo(date2);
			}
		});

		// Convert sorted map back to a Map
		Map<String, Object[]> sortedMap = new LinkedHashMap<String, Object[]>();
		for (Iterator<Map.Entry<String, Object[]>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Object[]> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	public static Map<String, Map<String, Object[]>> groupingMapByInvoiceNo(Map<String, Object[]> map) {
		Map<String, Map<String, Object[]>> result = new HashMap<String, Map<String, Object[]>>();

		String tempInvoiceNo = "XXXXXX";
		for (Map.Entry<String, Object[]> entry : map.entrySet()) {

			if (tempInvoiceNo.equalsIgnoreCase(entry.getValue()[3].toString())) {
				result.get(entry.getValue()[3].toString()).put(entry.getKey(), entry.getValue());
			} else {
				Map<String, Object[]> singleMap = new HashMap<String, Object[]>();
				singleMap.put(entry.getKey(), entry.getValue());
				result.put(entry.getValue()[3].toString(), singleMap);
				tempInvoiceNo = entry.getValue()[3].toString();
			}
		}

		return result;
	}

	public static Map<String, Object[]> removeDuplicate(Map<String, Map<String, Object[]>> map) {

		Map<String, Object[]> resultMap = new HashMap<String, Object[]>();
		for (Map.Entry<String, Map<String, Object[]>> entry : map.entrySet()) {

			if (entry.getValue().entrySet().size() > 1) {
				Map<String, Object[]> sortedByDate = sortByTransactionTime(entry.getValue());

				if (getSuccessOnly(sortedByDate) != null) {
					resultMap.putAll(getSuccessOnly(sortedByDate));
				} else {
					resultMap.putAll(getLastElement(sortedByDate));
				}

			} else {
				Map<String, Object[]> tempMap = entry.getValue();
				for (Map.Entry<String, Object[]> entryTemp : tempMap.entrySet()) {
					resultMap.put(entryTemp.getValue()[3].toString(), entryTemp.getValue());
				}
			}
		}

		return resultMap;

	}

	public static Map<String, Object[]> getSuccessOnly(Map<String, Object[]> map) {

		if (map != null && map.entrySet().size() > 0) {
			Map<String, Object[]> resultMap = new HashMap<String, Object[]>();
			for (Map.Entry<String, Object[]> entry : map.entrySet()) {
				if (entry.getValue()[7].toString().equalsIgnoreCase("00")) {
					resultMap.put(entry.getValue()[3].toString(), entry.getValue());
				}
			}

			return resultMap;
		}

		return null;
	}

	public static Map<String, Object[]> getLastElement(Map<String, Object[]> map) {

		if (map != null && map.entrySet().size() > 0) {
			Map<String, Object[]> resultMap = new HashMap<String, Object[]>();
			for (Map.Entry<String, Object[]> entry : map.entrySet()) {
				resultMap.put(entry.getValue()[3].toString(), entry.getValue());
			}

			return resultMap;
		}

		return null;
	}

	public static void printMap(Map<String, Object[]> map) {
		for (Map.Entry<String, Object[]> entry : map.entrySet()) {
			System.out.println("[Key] : " + entry.getKey() + " [Value] : " + entry.getValue()[3] + ", "
					+ entry.getValue()[7] + ", " + entry.getValue()[8]);
		}
	}

	public static void printMap2(Map<String, Map<String, Object[]>> map) {
		for (Map.Entry<String, Map<String, Object[]>> entry : map.entrySet()) {
			System.out.println("[Key] : " + entry.getKey());
			for (Map.Entry<String, Object[]> entryInner : entry.getValue().entrySet()) {
				System.out.println("[Key] : " + entryInner.getKey() + " [Value] : " + entryInner.getValue()[3]);
			}
		}
	}
}
