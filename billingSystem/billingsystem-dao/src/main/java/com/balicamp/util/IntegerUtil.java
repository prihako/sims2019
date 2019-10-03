package com.balicamp.util;

public class IntegerUtil {
	public static int parseInt( String input ){
		if ( input == null )
			return 0;
		input = input.trim();
		if ( input.length() == 0 )
			return 0;
		return Integer.parseInt(input);
	}
}
