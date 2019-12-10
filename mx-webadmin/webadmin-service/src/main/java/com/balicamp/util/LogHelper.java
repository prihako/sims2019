package com.balicamp.util;

import java.util.List;

public class LogHelper {
	public static String toString(Object [] array) {
		
		StringBuffer buff = new StringBuffer();
		for(Object temp : array) {
			if(temp == null) {
				buff.append("null");
			}else {
				buff.append(temp.toString());
			}
			
			buff.append("|");
		}
		
		return buff.toString();
	}
	
	public static String toString(List<Object[]> array) {
		
		StringBuffer buff = new StringBuffer();
		
		for(Object [] outer : array) {
			for(Object inner : outer) {
				if(inner == null) {
					buff.append("null");
				}else {
					buff.append(inner.toString());
				}
				
				buff.append("|");
			}
		}
		
		return buff.toString();
	}
}
