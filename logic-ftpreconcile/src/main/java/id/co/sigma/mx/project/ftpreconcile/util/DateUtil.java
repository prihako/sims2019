package id.co.sigma.mx.project.ftpreconcile.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;


public class DateUtil {
	
	private static Logger log = Logger.getLogger(DateUtil.class);
	public static String YYYYMMDD_DatePattern ="yyyyMMdd";
	public static String YYMMDD_DatePattern ="yyMMdd";
	
	public static void main(String[] args) {
//		try {
//			Date a = convertStringToDate("201307fd");
//			System.out.println(a);
//		} catch (ParseException e) {
//			System.out.println(e);
//			e.printStackTrace();
//		}
		try {
			Date date = convertStringToDate( "130720", YYMMDD_DatePattern, false);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			System.out.println(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Date convertStringToDate(String maybeDate, String format, boolean lenient) throws ParseException {
	    Date date = null;

	    // test date string matches format structure using regex
	    // - weed out illegal characters and enforce 4-digit year
	    // - create the regex based on the local format string
		String reFormat = Pattern.compile("d+|M+").matcher(Matcher.quoteReplacement(format))
				.replaceAll("\\\\d{1,2}");
		reFormat = Pattern.compile("y+").matcher(reFormat).replaceAll("\\\\d{4}");
		if (Pattern.compile(reFormat).matcher(maybeDate).matches()) {
			// date string matches format structure,
			// - now test it can be converted to a valid date
			SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getDateInstance();
			sdf.applyPattern(format);
			sdf.setLenient(lenient);
			try {
				date = sdf.parse(maybeDate);
			} catch (ParseException pe) {
				log.error("Could not convert '" + maybeDate
						+ "' to a date, throwing exception");
				pe.printStackTrace();
				throw new ParseException(pe.getMessage(), pe.getErrorOffset());
			}
		}
		else {
			log.error("Could not convert '" + maybeDate
					+ "' to a date, throwing exception");
			throw new ParseException("Unparseable date: " + maybeDate, 0);
		}
		return date;
	  }
	
	public static Date convertStringToDate(String strDate) throws ParseException {
      Date aDate = null;

      try {
          if (log.isDebugEnabled()) {
              log.debug("converting date with pattern: " + YYYYMMDD_DatePattern);
          }

          aDate = convertStringToDate(YYYYMMDD_DatePattern, strDate);
      } catch (ParseException pe) {
          log.error("Could not convert '" + strDate
                    + "' to a date, throwing exception");
          pe.printStackTrace();
          throw new ParseException(pe.getMessage(),
                                   pe.getErrorOffset());
                  
      }

      return aDate;
	}
	
	public static Date convertStringToDate(String aMask, String strDate) throws ParseException {
      SimpleDateFormat df = null;
      Date date = null;
      df = new SimpleDateFormat(aMask);
      df.setLenient(false);
      if (log.isDebugEnabled()) {
          log.debug("converting '" + strDate + "' to date with mask '"
                    + aMask + "'");
      }

      try {
          date = df.parse(strDate);
      } catch (ParseException pe) {
          //log.error("ParseException: " + pe);
          throw new ParseException(pe.getMessage(), pe.getErrorOffset());
      }

      return date;
	}
	
	

}
