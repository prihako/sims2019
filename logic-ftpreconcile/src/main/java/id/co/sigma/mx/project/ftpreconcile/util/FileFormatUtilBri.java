package id.co.sigma.mx.project.ftpreconcile.util;

import id.co.sigma.mx.project.ftpreconcile.constant.PostelConstant;
import id.co.sigma.mx.project.ftpreconcile.process.FTPManager;
import id.co.sigma.mx.project.ftpreconcile.process.FTPManagerBri;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: ferdi
 * Date: Sep 7, 2006
 * Time: 10:19:14 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * this is file format utility class, handle file transaction format
 */
public class FileFormatUtilBri {

    //contoh file transaction : 1030061777773MT94020060910
//    private static String FILE_PATTERN="1030061777773MT940${DATE:yyyyMMdd}";

    private static String HEAD_FILE ;
    private static String DATE_PATTERN ;
    private static String FILE_PATTERN;
    private static boolean isInit=false;

    private static Logger logger = Logger.getLogger(FileFormatUtilBri.class);

    /**
     * do initial to get head file name
     */
    public static void init() {
		try {
			FILE_PATTERN = FTPManagerBri.getFilePattern();

	        int index = FILE_PATTERN.indexOf("${DATE:");
	        if (index > -1) {
	            int curl = FILE_PATTERN.indexOf("}", index);
	            if (curl != -1) {
	                DATE_PATTERN=FILE_PATTERN.substring(index + 7, curl);
	                HEAD_FILE = FILE_PATTERN.substring(0,index);
	                isInit=true;
	            } else {
	                logger.info("bri, [FileFormatUtil.init] Error parsing file transaction : Cant parse format ${DATE:yyyyMMdd} from file pattern " + FILE_PATTERN);
	            }
	        } else {
	            logger.info("bri, [FileFormatUtil.init] Error parsing file transaction : Cant parse format ${DATE:yyyyMMdd} from file pattern " + FILE_PATTERN);
	        }
		} catch (Exception e) {
			logger.error("bri, could not init ftp configuration, " + e.getMessage());
		}
    }
    
    public static void init(int type) {
    	
		try {
				if(type == 0){
					FILE_PATTERN = FTPManager.getFilePattern();
				}

	        int index = FILE_PATTERN.indexOf("${DATE:");
	        if (index > -1) {
	            int curl = FILE_PATTERN.indexOf("}", index);
	            if (curl != -1) {
	                DATE_PATTERN=FILE_PATTERN.substring(index + 7, curl);
	                HEAD_FILE = FILE_PATTERN.substring(0,index);
	                isInit=true;
	            } else {
	                logger.info("[FileFormatUtil.init] Error parsing file transaction : Cant parse format ${DATE:yyyyMMdd} from file pattern " + FILE_PATTERN);
	            }
	        } else {
	            logger.info("[FileFormatUtil.init] Error parsing file transaction : Cant parse format ${DATE:yyyyMMdd} from file pattern " + FILE_PATTERN);
	        }
		} catch (Exception e) {
			logger.error("could not init ftp configuration, " + e.getMessage());
		}
    }
    
    public static void init(String filePattern) {
		try {
			FILE_PATTERN = filePattern;

	        int index = FILE_PATTERN.indexOf("${DATE:");
	        if (index > -1) {
	            int curl = FILE_PATTERN.indexOf("}", index);
	            if (curl != -1) {
	                DATE_PATTERN=FILE_PATTERN.substring(index + 7, curl);
	                HEAD_FILE = FILE_PATTERN.substring(0,index-1);
	                isInit=true;
	            } else {
	                logger.info("[FileFormatUtil.init] Error parsing file transaction : Cant parse format ${DATE:yyyyMMdd} from file pattern " + FILE_PATTERN);
	            }
	        } else {
	            logger.info("[FileFormatUtil.init] Error parsing file transaction : Cant parse format ${DATE:yyyyMMdd} from file pattern " + FILE_PATTERN);
	        }
	        
	        logger.info("bri, init, FILE_PATTERN " + FILE_PATTERN);
	        logger.info("bri, init, HEAD_FILE " + HEAD_FILE);
	        logger.info("bri, init, DATE_PATTERN " + DATE_PATTERN);
		} catch (Exception e) {
			logger.error("bri, could not init ftp configuration, " + e.getMessage());
		}
    }

    /**
     * used to get filename transaction, if count 0 = current date
     *
     * @param count
     * @return filename
     */
    public static String getFileName(int count) {
        if (!isInit) init();

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);

        if (count==0) {// Format the current time.
            Date currentTime = new Date();
            return HEAD_FILE + formatter.format(currentTime);
        } else {
            Calendar cal=Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, count);
            return HEAD_FILE + formatter.format(cal.getTime());
        }
    }
    
    public static String getFileName(int count, int type) {
//        if (!isInit) init(type);
    	init(type);

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);

        if (count==0) {// Format the current time.
            Date currentTime = new Date();
            return HEAD_FILE + formatter.format(currentTime);
        } else {
            Calendar cal=Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, count);
            return HEAD_FILE + formatter.format(cal.getTime());
        }
    }
    
    public static String getFileName(int count, String filePattern) {
      
      init(filePattern);
    	
      SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);

      if (count==0) {// Format the current time.
          Date currentTime = new Date();
          return HEAD_FILE + "+" + formatter.format(currentTime);
      } else {
          Calendar cal=Calendar.getInstance();
          cal.add(Calendar.DAY_OF_YEAR, count);
          return HEAD_FILE + "+" + formatter.format(cal.getTime());
      }
      
  }

    /**
     * get string of calendar transaction from fileName
     *
     * @param fileName
     * @return string transaction date (ex : 20061027)
     */
    public static String getStrTransactionDate(String fileName) {
        Calendar cal=getTrasactionDate(fileName);

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
        return formatter.format(cal.getTime());
    }

    /**
     * get string of calendar transaction
     *
     * @param calendar
     * @return string transaction date (ex : 20061027)
     */
    public static String getStrTransactionDate(Calendar calendar) {

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
        return formatter.format(calendar.getTime());
    }

    /**
     * used to get transaction date from name of file transaction
     *
     * @param fileName
     * @return calendar
     */
    public static Calendar getTrasactionDate(String fileName) {
        if (!isInit) init();

        Calendar cal=Calendar.getInstance();
        int idx = fileName.indexOf(HEAD_FILE);
        if (idx != -1) {
            String dateFile = fileName.substring(HEAD_FILE.length());
            try {
                SimpleDateFormat yyyyMMdd = new SimpleDateFormat(DATE_PATTERN);
                Date transactionDate=yyyyMMdd.parse(dateFile);
                cal.setTime(transactionDate);
            } catch (ParseException e) {
                logger.error("[FileFormatUtil.getTrasactionDate] Error parsing file transaction : ", e);
            }
        }

        return cal;
    }
    
    public static Calendar getTrasactionDate(String filenameDatePlusAccountNo, String datePattern, int count) {
		Calendar cal = Calendar.getInstance();
		String filenameDate = filenameDatePlusAccountNo.substring(filenameDatePlusAccountNo.indexOf("+") + 1);
		try {
			SimpleDateFormat yyyyMMdd = new SimpleDateFormat(datePattern);
			Date transactionDate = yyyyMMdd.parse(filenameDate);
			cal.setTime(transactionDate);
			if(count != 0){
				cal.add(Calendar.DAY_OF_MONTH, count);
			}
		} catch (ParseException e) {
			logger.error(
					"Bri, [FileFormatUtil.getTrasactionDate] Error parsing file transaction : ",
					e);
		}
		return cal;
	}
    
    public static Calendar getTrasactionDate(String accountNoPlusDate, String datePattern) {
    	
    	Calendar cal = Calendar.getInstance();
		String filenameDate = accountNoPlusDate.substring(accountNoPlusDate.indexOf("+")+1);
		try {
			SimpleDateFormat yyyyMMdd = new SimpleDateFormat(datePattern);
			Date transactionDate = yyyyMMdd.parse(filenameDate);
			cal.setTime(transactionDate);
		} catch (ParseException e) {
			logger.error(
					"Bri, [FileFormatUtil.getTrasactionDate] Error parsing file transaction : ",
					e);
		}
		return cal;
    }

    public static String getFilePattern() {
        if (!isInit) 
        	init();
        return FILE_PATTERN;
    }
    
    public static String getDatePattern(String filePattern){
		int index = filePattern.indexOf("${DATE:");
		String datePattern = null;
		if (index > -1) {
			int curl = filePattern.indexOf("}", index);
			if (curl != -1) {
				datePattern = filePattern.substring(index + 7, curl);
			}
		}
		
		return datePattern;
	}

    /**
     * check is new month or not
     *
     * @param fileName
     * @return  flag -> true is new month, false otherwise
     */
    public static boolean isNewMonthTransaction(String fileName) {

        int day = 0;
        try {
            day = Integer.parseInt(fileName.substring(fileName.length() - 2));
        } catch (NumberFormatException e) {
            logger.error("[FileFormatUtil.isNewMonthTransaction] Error parse : ", e);
        }

        if (day == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * overloading from last method, to get new month with input param calendar
     *
     * @param transactionDate
     * @return flag -> true if new month, false otherwise
     */
    public static boolean isNewMonthTransaction(Calendar transactionDate) {

        int day = transactionDate.get(Calendar.DAY_OF_MONTH);

        if (day == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * check is valid transaction file or not
     *
     * @param fileName
     * @return flag -> true is valid transaction file, else false
     */
    public static boolean isTransactionFile(String fileName) {
        if (!isInit) init();

        List<String> listDay=new ArrayList<String>(7);
        listDay.add("sunday");
        listDay.add("monday");
        listDay.add("tuesday");
        listDay.add("wednesday");
        listDay.add("thursday");
        listDay.add("friday");
        listDay.add("saturday");

        boolean flag;
        int idx = fileName.indexOf(HEAD_FILE);
        if (idx != -1) {
            String dateFile = fileName.substring(HEAD_FILE.length());
            if (listDay.contains(dateFile.toLowerCase())) {
                flag=true;
            } else {
                try {
                    SimpleDateFormat yyyyMMdd = new SimpleDateFormat(DATE_PATTERN);
                    yyyyMMdd.parse(dateFile);
                    flag=true;
                } catch (ParseException e) {
                    logger.error("[FileFormatUtil.isTransactionFile] Error file transaction : ", e);
                    flag = false;
                }
            }
        } else {
            flag = false;
        }

        return flag;
    }

    /**
     * get day of transaction file
     *
     * @param fileName
     * @return day of transaction file
     */
    public static String getDayFileTransaction(String fileName) {
        if (!isInit) init();

        Calendar calendar = getTrasactionDate(fileName);

        int day=calendar.get(Calendar.DAY_OF_WEEK);

        String nameDay="";

        switch(day){
            case 0 : nameDay="sunday";break;
            case 1 : nameDay="monday";break;
            case 2 : nameDay="tuesday";break;
            case 3 : nameDay="wednesday";break;
            case 4 : nameDay="thursday";break;
            case 5 : nameDay="friday";break;
            case 6 : nameDay="saturday";break;
            default:
        }

        StringBuilder sb = new StringBuilder();
        sb.append(HEAD_FILE).append(nameDay.toUpperCase());

        return sb.toString();
    }

    /**
     * overloading from last method, to get day of transaction file from calendar
     *
     * @param transactionDate
     * @return day of transaction file
     */
    public static String getDayFileTransaction(Calendar transactionDate) {
        if (!isInit) init();

        int day=transactionDate.get(Calendar.DAY_OF_WEEK);

        String nameDay="";

        switch(day){
            case 0 : nameDay="sunday";break;
            case 1 : nameDay="monday";break;
            case 2 : nameDay="tuesday";break;
            case 3 : nameDay="wednesday";break;
            case 4 : nameDay="thursday";break;
            case 5 : nameDay="friday";break;
            case 6 : nameDay="saturday";break;
            default:
        }

        StringBuilder sb = new StringBuilder();
        sb.append(HEAD_FILE).append(nameDay.toUpperCase());

        return sb.toString();
    }

    /**
     * get previous transaction date filename
     *
     * @param fileName
     * @return previous transaction date
     */
    public static String getPrevFilename(String fileName) {

        Calendar calendar = getTrasactionDate(fileName);

        calendar.add(Calendar.DAY_OF_YEAR, -1);

        StringBuilder sb = new StringBuilder();
        sb.append(HEAD_FILE);

        String sDate = null;
        int date = calendar.get(Calendar.DATE);
        if (date < 10) {
            sDate = new StringBuilder("0").append(date).toString();
        } else {
            sDate = String.valueOf(date);
        }

        String sMonth = null;
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month < 10) {
            sMonth = new StringBuilder("0").append(month).toString();
        } else {
            sMonth = String.valueOf(month);
        }

        int year = calendar.get(Calendar.YEAR);
        String sYear = String.valueOf(year);

        sb.append(sYear).append(sMonth).append(sDate);

        return sb.toString();
    }
    
    public static String getPrevFilename(String fileName, String filePattern) {

        Calendar calendar = getTrasactionDate(fileName, filePattern);

        calendar.add(Calendar.DAY_OF_YEAR, -1);

        StringBuilder sb = new StringBuilder();
        sb.append(HEAD_FILE);

        String sDate = null;
        int date = calendar.get(Calendar.DATE);
        if (date < 10) {
            sDate = new StringBuilder("0").append(date).toString();
        } else {
            sDate = String.valueOf(date);
        }

        String sMonth = null;
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month < 10) {
            sMonth = new StringBuilder("0").append(month).toString();
        } else {
            sMonth = String.valueOf(month);
        }

        int year = calendar.get(Calendar.YEAR);
        String sYear = String.valueOf(year);

        sb.append(sYear).append(sMonth).append(sDate);

        return sb.toString();
    }

    public static String getPrevFilenameDate(String fileNameDatePlusAccountNo, String datePattern) {
		String rekeningNumber = fileNameDatePlusAccountNo.substring(0, fileNameDatePlusAccountNo.indexOf("+"));
		Calendar calendar = getTrasactionDate(fileNameDatePlusAccountNo, datePattern, 0);

		calendar.add(Calendar.DAY_OF_YEAR, -1);

		StringBuilder sb = new StringBuilder();

		String sDate = null;
		int date = calendar.get(Calendar.DATE);
		if (date < 10) {
			sDate = new StringBuilder("0").append(date).toString();
		} else {
			sDate = String.valueOf(date);
		}

		String sMonth = null;
		int month = calendar.get(Calendar.MONTH) + 1;
		if (month < 10) {
			sMonth = new StringBuilder("0").append(month).toString();
		} else {
			sMonth = String.valueOf(month);
		}

		int year = calendar.get(Calendar.YEAR);
		String sYear = String.valueOf(year);

		sb.append(rekeningNumber).append("+").append(sYear).append(sMonth).append(sDate);

		return sb.toString();
	}
}
