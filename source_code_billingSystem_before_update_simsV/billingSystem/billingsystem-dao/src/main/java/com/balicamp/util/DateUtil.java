package com.balicamp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import com.balicamp.Constants;

/**
 * Date Utility Class This is used to convert Strings to Dates and Timestamps
 *
 * <p>
 * <a href="DateUtil.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a> Modified by
 *         <a href="mailto:dan@getrolling.com">Dan Kibler </a> to correct time
 *         pattern. Minutes should be mm not MM (MM is month).
 * @version $Revision: 394 $ $Date: 2006-10-03 12:58:45 -0600 (Tue, 03 Oct
 *          2006) $
 */
public class DateUtil {
    // ~ Static fields/initializers
    // =============================================

    private static Log log = LogFactory.getLog(DateUtil.class);

    private static String defaultDatePattern = null;

    private static String timePattern = "HH:mm";

    // ~ Methods
    // ================================================================

    /**
     * Return default datePattern (MM/dd/yyyy)
     *
     * @return a string representing the date pattern on the UI
     */
    public static String getDatePattern() {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            defaultDatePattern = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale).getString("pattern.date");
        } catch (MissingResourceException mse) {
            defaultDatePattern = "dd/MM/yyyy";
        }

        return defaultDatePattern;
    }

    public static String getDateTimePattern() {
        return DateUtil.getDatePattern() + " HH:mm:ss.S";
    }

    /**
     * This method attempts to convert an Oracle-formatted date in the form
     * dd-MMM-yyyy to mm/dd/yyyy.
     *
     * @param aDate
     *            date from database as a string
     * @return formatted string for the ui
     */
    public static final String getDate(Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(getDatePattern());
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date/time in the
     * format you specify on input
     *
     * @param aMask
     *            the date pattern the string is in
     * @param strDate
     *            a string representation of a date
     * @return a converted Date object
     * @see java.text.SimpleDateFormat
     * @throws ParseException
     */
    public static final Date convertStringToDate(String aMask, String strDate) throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(aMask);

        if (log.isDebugEnabled()) {
            log.debug("converting '" + strDate + "' to date with mask '" + aMask + "'");
        }

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            // log.error("ParseException: " + pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return (date);
    }

    /**
     * This method returns the current date time in the format: MM/dd/yyyy HH:MM
     * a
     *
     * @param theTime
     *            the current time
     * @return the current date/time
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(timePattern, theTime);
    }

    /**
     * This method returns the current date in the format: MM/dd/yyyy
     *
     * @return the current date
     * @throws ParseException
     */
    public static Calendar getToday() throws ParseException {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

        // This seems like quite a hack (date -> string -> date),
        // but it works ;-)
        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(convertStringToDate(todayAsString));

        return cal;
    }

    /**
     * This method generates a string representation of a date's date/time in
     * the format you specify on input
     *
     * @param aMask
     *            the date pattern the string is in
     * @param aDate
     *            a date object
     * @return a formatted string representation of the date
     *
     * @see java.text.SimpleDateFormat
     */
    public static final String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
            log.error("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date based on the
     * System Property 'dateFormat' in the format you specify on input
     *
     * @param aDate
     *            A date to convert
     * @return a string representation of the date
     */
    public static final String convertDateToString(Date aDate) {
        return getDateTime(getDatePattern(), aDate);
    }

    public static final String convertDateToString(Date aDate, String pattern) {

        SimpleDateFormat sdf = null;
        try {
            sdf = new SimpleDateFormat(pattern);
        } catch (Exception ex) {
            sdf = new SimpleDateFormat("dd-MM-yyyy");
        }

        return sdf.format(aDate);
    }

    /**
     * This method converts a String to a date using the datePattern
     *
     * @param strDate
     *            the date to convert (in format MM/dd/yyyy)
     * @return a date object
     *
     * @throws ParseException
     */
    public static Date convertStringToDate(String strDate) throws ParseException {
        Date aDate = null;

        try {
            if (log.isDebugEnabled()) {
                log.debug("converting date with pattern: " + getDatePattern());
            }

            aDate = convertStringToDate(getDatePattern(), strDate);
        } catch (ParseException pe) {
            log.error("Could not convert '" + strDate + "' to a date, throwing exception", pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());

        }

        return aDate;
    }

    /**
     * Create date with parametize pattern string, ex. DDMMYYYY of today will be
     * the output
     *
     * @param pattern
     * @return
     */
    public static String getDateAsString(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = new Date();
        String dateAsString = sdf.format(date);

        return dateAsString;
    }

    /**
     *
     * @param dateBackDay
     * @return
     */
    public static Date getDateBasedOnParameterizeDate(int dateBackDay) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);

        cal.add(Calendar.DATE, dateBackDay);

        return cal.getTime();
    }

    /**
     * Nulling time at inserted date, make sure the time is 00:00:00:000000
     *
     * @param date
     * @return
     */
    public static Date getDateNulledTime(Date date) {
        Date dateReturned = null;
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.HOUR_OF_DAY, 0);

            dateReturned = cal.getTime();
        }
        return dateReturned;
    }

    /**
     * Nulling date at inserted date, make sure the date is 00 00 0000
     *
     * @param date
     * @return
     */
    public static Date setTimeAsToday(Date date) {

        Calendar now = Calendar.getInstance();
        now.setTime(new Date());

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, now.get(Calendar.DATE));
        cal.set(Calendar.MONTH, now.get(Calendar.MONTH));
        cal.set(Calendar.YEAR, now.get(Calendar.YEAR));

        return cal.getTime();
    }

    /**
     * get today mmdd
     *
     * @return
     */
    public static String getTodayMMDD() {

        String todayMMMDDrep = null;

        SimpleDateFormat sdf = new SimpleDateFormat("ddMM");
        todayMMMDDrep = sdf.format(new Date());

        return todayMMMDDrep;
    }

    /**
     * get today yymm
     *
     * @return
     */
    public static String getTodayYYMM() {

        String todayYYMMrep = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
        todayYYMMrep = sdf.format(new Date());

        return todayYYMMrep;
    }

    /**
     * get today yymmdd
     *
     * @return
     */
    public static String getTodayYYMMDD() {

        String todayYYMMrep = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        todayYYMMrep = sdf.format(new Date());

        return todayYYMMrep;
    }

    public static String getTodayYYYYMMDDHHmmss() {
        String strval = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        strval = sdf.format(new Date());

        return strval;
    }

    public static String getDateYYYYMMDD(Date date) {
        String todayYYMMrep = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        todayYYMMrep = sdf.format(date);
        return todayYYMMrep;
    }

    /**
     * @author Wayan Arik
     *
     * @param input
     * @return
     * @throws ParseException
     */
    public static Date substractDate(String input, int amount) throws ParseException {
    	Date output = convertStringToDate("MM/dd/yyyy", input);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(output);
		calendar.add(Calendar.DAY_OF_MONTH, -(amount));
		return calendar.getTime();
    }

    public static Date addDate(String input, int amount) throws ParseException {
    	Date output = convertStringToDate("MM/dd/yyyy", input);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(output);
		calendar.add(Calendar.DAY_OF_MONTH, amount);
		return calendar.getTime();
    }

    /**
     * @author arif
     *
     * @param input
     * @return
     */
    public static Date truncDate(Date input) {
        Calendar inputCalendar = Calendar.getInstance();
        inputCalendar.setTime(input);

        Calendar outputCalendar = Calendar.getInstance();
        outputCalendar.set(inputCalendar.get(Calendar.YEAR), inputCalendar.get(Calendar.MONTH), inputCalendar
                .get(Calendar.DATE), 0, 0, 0);
        outputCalendar.set(Calendar.MILLISECOND, 0);

        return outputCalendar.getTime();
    }

    /**
     * @author arif
     *
     * @param input
     * @return
     */
    public static Date generateStartDate(Date input) {
        return truncDate(input);
    }

    /**
     * @author arif
     *
     * @param input
     * @return
     */
    public static Date generateEndDate(Date input) {
        Calendar inputCalendar = Calendar.getInstance();
        inputCalendar.setTime(input);
        inputCalendar.set(Calendar.HOUR_OF_DAY, 0);
        inputCalendar.set(Calendar.MINUTE, 0);
        inputCalendar.set(Calendar.SECOND, 0);
        inputCalendar.set(Calendar.MILLISECOND, 0);

        inputCalendar.add(Calendar.DAY_OF_MONTH, 1);

        return inputCalendar.getTime();

    }

    // public final static DateFormat concatDateFormat = new
    // SimpleDateFormat("yyyyMMdd");
    // public final static DateFormat concatDateTimeFormat = new
    // SimpleDateFormat("yyyyMMddHHmmss");
    // public final static DateFormat concatTimeFormat = new
    // SimpleDateFormat("HHmmss");

    public static boolean cekYearMonthDateEquals(Calendar input1, Calendar input2) {
        if (input1.get(Calendar.YEAR) != input2.get(Calendar.YEAR))
            return false;

        if (input1.get(Calendar.MONTH) != input2.get(Calendar.MONTH))
            return false;

        if (input1.get(Calendar.DATE) != input2.get(Calendar.DATE))
            return false;

        return true;
    }

    public static boolean cekYearMonthDateEquals(Calendar input1, Date date2) {
        Calendar input2 = Calendar.getInstance();
        input2.setTime(date2);

        return cekYearMonthDateEquals(input1, input2);
    }

    public static Date getADayAfter() {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, +1);
		return now.getTime();
	}

}
