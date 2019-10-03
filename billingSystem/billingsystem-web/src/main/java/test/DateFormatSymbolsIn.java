package test;

import java.text.DateFormatSymbols;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class DateFormatSymbolsIn 
	extends DateFormatSymbols
{	
	private static final long serialVersionUID = -5278961187250802820L;
	
	public static final String [] months 		= {"Januari",	"Februari",	"Maret",	"April",	"Mei",	"Juni",	"Juli",	"Agustus",	"September",	"Oktober",	"November",	"Desember"};
	public static final String [] shortMonths	= {"Jan",		"Feb",		"Mar",		"Apr",		"Mei",	"Jun",	"Jul",	"Ags",		"Sep",			"Okt",		"Nov",		"Des"};
	public static final String [] weekDays 		= {"","Minggu","Senin","Selasa","Rabu","Kamis","Jumat","Sabtu"};
	public static final String [] shortWeekDays = {"","Min","Sen","Sel","Rab","Kam","Jum","Sab"};


	public DateFormatSymbolsIn(){
		super();		
		this.setMonths(months);
		this.setShortMonths(shortMonths);
		this.setWeekdays(weekDays);
		this.setShortWeekdays(shortWeekDays);
	}
	
}
