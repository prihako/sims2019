package com.balicamp.util;

import java.text.DateFormatSymbols;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class DateFormatSymbolsInPln 
	extends DateFormatSymbols
{	
	private static final long serialVersionUID = -5278961187250802820L;
	
	public static final String [] months 		= {"Januari",	"Pebruari",	"Maret",	"April",	"Mei",	"Juni",	"Juli",	"Agustus",	"September",	"Oktober",	"Nopember",	"Desember"};
	public static final String [] shortMonths	= {"Jan",		"Peb",		"Mar",		"Apr",		"Mei",	"Jun",	"Jul",	"Ags",		"Sep",			"Okt",		"Nop",		"Des"};
	public static final String [] weekDays 		= {"","Minggu","Senin","Selasa","Rabu","Kamis","Jumat","Sabtu"};
	public static final String [] shortWeekDays = {"","Min","Sen","Sel","Rab","Kam","Jum","Sab"};


	public DateFormatSymbolsInPln(){
		super();		
		this.setMonths(months);
		this.setShortMonths(shortMonths);
		this.setWeekdays(weekDays);
		this.setShortWeekdays(shortWeekDays);
	}
	
}
