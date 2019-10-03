package com.balicamp.util.mx;

//
// Copyright (c) 2007 BaliCamp. All Rights Reserved.
//

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.jpos.iso.ISOComponent;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.GenericPackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Collection of ISO Message Utilities Methods
 * using JPOS component
 *
 * @author Marudut Panggabean
 * @last update: 05-02-2007
 * @version $Id: ISOMessageUtil.java 504 2013-05-24 08:15:06Z rudi.sadria $
 */
public class ISOMessageUtil {

	public static final char LEFT_ALIGN = 'l';

	public static final char RIGHT_ALIGN = 'r';

	public static final int NUMERIC_TYPE = 0;

	public static final int TEXT_TYPE = 1;

	public static final int RAW_TYPE = -1;

	private static final String ERROR_PARSING = "Your array element size didn't match the long string : ";

	private static final transient Logger LOGGER = LoggerFactory.getLogger(ISOMessageUtil.class);

	/**
	 * unpacking ISO 8583 String into bit-fields
	 *
	 * @param ISO 8583 message to be parsed
	 * @return bit-fields presented in hashtable
	* @author Marudut Panggabean
	 */
	@SuppressWarnings("rawtypes")
	public static Hashtable unpackISOMessage(String argMsg, String configPath, String endpointCode) {
		Hashtable ht = null;

		try {
			GenericPackager packager = new GenericPackager(configPath + System.getProperty("file.separator")
					+ "iso87ascii" + ".xml");
			ISOComponent c;
			// Thread.currentThread().getContextClassLoader();
			byte[] b = new byte[argMsg.length()];
			for (int i = 0; i < b.length; i++) {
				b[i] = (byte) (int) argMsg.charAt(i);
			}

			// Create packager
			ISOMsg m1 = new ISOMsg();
			m1.setPackager(packager);
			try {
				m1.unpack(b);
			} catch (Exception up) {}
			Map hData = m1.getChildren();
			c = (ISOComponent) hData.get(new Integer(-1));
			String bitHidup = c.getValue().toString();
			String[] arBit = getBit(bitHidup);
			ht = new Hashtable();

			// Get MTI
			ht.put("MTI", m1.getMTI());
			for (int i = 0; i < arBit.length; i++) {
				if (arBit[i].trim().equals("1")) {
					// do nothing
				} else {
					ht.put(arBit[i], m1.getString(Integer.parseInt(arBit[i].trim())));
				}
			}

			// printHashtable(ht);
		} catch (Exception e) {
			LOGGER.error("Raw message:" + argMsg, e);
		}

		return ht;
	}

	/**
	 * parse message into string array
	 *
	 * @param string that need to be parsed (eg: {0, 1, 3, ...}
	 * @return string array (eg: String[0] = "0", String[1] = "1", etc)
	* @author Marudut Panggabean
	 */
	public static String[] getBit(String arg) {
		StringBuffer sb = new StringBuffer();

		int j = 0;
		for (int i = 0; i < arg.length(); i++) {
			j++;
			if (!(arg.substring(i, j).equals("{") || arg.substring(i, j).equals(",") || arg.substring(i, j).equals("}"))) {
				sb.append(arg.substring(i, j));
			}
		}

		StringTokenizer st = new StringTokenizer(sb.toString());
		int jmlToken = st.countTokens();
		String[] arrBit = new String[jmlToken];

		int y = 0;
		while (st.hasMoreTokens()) {
			arrBit[y] = st.nextToken();
			y++;
		}

		return arrBit;
	}

	/**
	 * print keys and values from hashtable
	 *
	 * @param hashtable to be printed
	* @author arvin
	* 
	 */
	public static String printHashtable(Hashtable hashtable) {
		if (hashtable == null)
			return null;

		StringBuffer strBuf = new StringBuffer();
		try {
			List keyList = new ArrayList();
			Set keySet = hashtable.keySet();
			Iterator itr = keySet.iterator();

			while (itr.hasNext()) {
				String keyObj = (String) itr.next();
				try {
					int key = Integer.parseInt(keyObj);
					keyList.add(new Integer(key));
				} catch (Exception e) {}
			}

			Collections.sort(keyList);
			keyList.add(0, "MTI");

			strBuf.append("<isomsg>\n");
			for (int i = 0; i < keyList.size(); i++) {
				Object key = keyList.get(i);

				strBuf.append("<field id=\"" + (key.equals("MTI") ? "0" : key) + "\"" + " value=\""
						+ (String) hashtable.get(String.valueOf(key)) + "\">\n");
			}
			strBuf.append("</isomsg>");
			System.out.println(strBuf.toString());

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error Print Hashtable !!!");
		}
		return strBuf.toString();
	}

	public static String printFormattedIso(Hashtable hashtable, boolean isHtml) {
		if (isHtml) {
			return printHashtableToHTMLFormated(hashtable);
		}
		return printHashtable(hashtable);
	}

	public static String printHashtableToHTMLFormated(Hashtable hashtable) {
		if (hashtable == null)
			return null;

		StringBuffer strBuf = new StringBuffer();
		String tmpBuffer = "";
		try {
			List keyList = new ArrayList();
			Set keySet = hashtable.keySet();
			Iterator itr = keySet.iterator();

			while (itr.hasNext()) {
				String keyObj = (String) itr.next();
				try {
					int key = Integer.parseInt(keyObj);
					keyList.add(new Integer(key));
				} catch (Exception e) {}
			}

			Collections.sort(keyList);
			keyList.add(0, "MTI");

			strBuf.append("<dl><dt>&ltisomsg&gt</dt><dd>");
			for (int i = 0; i < keyList.size(); i++) {
				Object key = keyList.get(i);

				strBuf.append("&ltfield id=\"" + (key.equals("MTI") ? "0" : key) + "\"" + " value=\""
						+ (String) hashtable.get(String.valueOf(key)) + "\"&gt<br/>");
			}
			// tmpBuffer = strBuf.toString().substring(0, strBuf.length() - 5);
			strBuf.append("</dd><dt>&ltisomsg&gt</dt></dl>");
			System.out.println(strBuf.toString());

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error Print Hashtable !!!");
		}
		return strBuf.toString();
	}

	/**
	* Calculate the message length
	*
	* @param message to be calculated
	* @return 4-digit message length (eg:0093, 0120, etc)
	* @author Marudut Panggabean
	*/
	public static String build4DigitMessageLength(String argText) {
		int myInt = argText.length();
		String isoHeader = Integer.toString(myInt);
		int isoHeaderLength = isoHeader.length();
		String staticHeader = "0000";
		String isoHeaderBegin = staticHeader + isoHeader;
		return isoHeaderBegin.substring(isoHeaderLength);
	}

	/**
	 * Pad Left with zero ("0")
	 * eg: input: 1234 -> output: 000000001234
	 *
	 * @param the string to be padded
	 * @param length of padded string (eg:000000001234 -> length = 12)
	 * @return padded string
	* @author Marudut Panggabean
	 */
	public static String padLeftWithZero(String in, int length) {
		String pad = "";
		for (int i = 0; i < (length - in.length()); i++) {
			pad += "0";
		}
		return (pad + in);
	}

	/**
	    * Pad Left with zero (" ")
	    * eg: input: 1234 -> output: '      1234'
	    *
	    * @param the string to be padded
	    * @param length of padded string (eg:'        1234' -> length = 12)
	    * @return padded string
	 	* @author Aria Yudanto
	    */
	public static String padLeftWithSpace(String in, int length) {
		String pad = "";
		for (int i = 0; i < (length - in.length()); i++) {
			pad += " ";
		}
		return (pad + in);
	}

	/**
	 * Pad Right with space (" ")
	 * eg: input: 'abcd' -> output: 'abcd    '
	 *
	 * @param the string to be padded
	 * @param length of padded string (eg:'abcd    ' -> length = 8)
	 * @return padded string
	* @author Marudut Panggabean
	 */
	public static String padRightWithSpace(String in, int length) {
		String pad = "";

		for (int i = 0; i < (length - in.length()); i++) {
			pad += " ";
		}
		return (in + pad);
	}

	/**
	 * Parse Card Expiration Data from SCCTTRTX MSGREQ
	 *
	 * @param SCCTTRTX MSGREQ (String)
	 * @return String
	* @author Rangga Pratama
	 */
	public static String parseCardExpDate(String argMSGREQ) {
		StringTokenizer st = new StringTokenizer(argMSGREQ, "=", false);

		st.nextToken().trim();
		String cardExpDateRaw = st.nextToken().trim();
		String cardExpDate = cardExpDateRaw.substring(0, 4);

		return cardExpDate;
	}

	/**
	* convert ascii string to hex format
	*
	* @param string to be converted
	* @return parsed string
	* @author Rangga
	*/
	public static String ConvertToHex(String argString) {
		byte[] bb = ConverterIso.ascii2bcd(argString);
		String temp = "";
		for (int y = 0; y < bb.length; y++) {
			temp += (char) bb[y];
		}
		return temp;
	}

	/**
	* conver hex to ascii format
	*
	* @param string to be parsed
	* @return parsed string
	* @author Rangga
	*/
	public static String ConvertToAscii(String s) {
		int len = (s == null ? 0 : s.length());

		byte[] b = new byte[len];

		for (int i = 0; i < len; i++) {
			b[i] = (byte) ((int) s.charAt(i));
		}

		String temp = ISOUtil.bcd2str(b, 0, (b.length * 2), true);

		return temp;
	}

	/**
	 * remove leading zero
	 * eg: input: '00709' will be convert '709'
	 *
	 * @param the string to be convert
	 * @return converted string
	 * @author Bayu Rimba Pratama
	 */
	public static String removeLeadingZero(String s) {
		return s.replaceFirst("^0+(?!$)", "");
	}

	/**
	 * Generate key to used in SCCTTRTX table (column FIELD4). Use this method
	 * for transaction that need key to distinguish each transaction (ex :
	 * Mitracomm, Finnet, Nusapro).
	 * 
	 * @param bitForKey
	 *            String for key. The format must be payeeID + billerID +
	 *            prodyctID. Usually bit 63 if following the convention.
	 * @return a key for used in FIELD4 on table SCCTTRTX. The format is AABBCCDD where :
	 * 	<li>AA = last 2 digit payee id</li>
	 * 	<li>BB = last 2 digit industry code</li>
	 * 	<li>CC = last 2 digit biller code</li>
	 * 	<li>DD = last 2 digit product id</li>
	 */
	public static String generateFieldKey(String bitForKey) {
		StringBuffer fieldKey = new StringBuffer();
		try {

			String payeeID = bitForKey.substring(0, 4);
			String billerID = bitForKey.substring(4, 9);
			String industryID = billerID.substring(0, 2);
			String billerCode = billerID.substring(2);
			String productID = bitForKey.substring(9);

			fieldKey.append(payeeID.substring(payeeID.length() - 2));
			fieldKey.append(industryID.substring(industryID.length() - 2));
			fieldKey.append(billerCode.substring(billerCode.length() - 2));
			fieldKey.append(productID.substring(productID.length() - 2));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fieldKey.toString();
	}

	/**
	 * Generate trace number 2. Trace number 2 is the primary key for SCCTTRTX table.
	 * Use this to get the primary key on one record in SCCTTRTX.
	 * @param isoTable
	 * @return trace number 2 (trcnm2)
	 */
	public static String generateTrcnm2(Hashtable isoTable) {
		String bit11 = ((String) isoTable.get("11")).trim();
		String bit7 = ((String) isoTable.get("7")).trim();
		return bit11 + bit7;
	}

	public static String buildMessageFromArray(String[] messageArray) {
		StringBuffer buffer = new StringBuffer();
		try {
			for (int i = 0; i < messageArray.length; i++) {
				buffer.append(messageArray[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public static String[] parseMessage(final int[] elementLength, final String longString) {
		int element = ISOMessageUtil.RAW_TYPE;
		int[] elementType = new int[elementLength.length];

		for (int i = 0; i < elementLength.length; i++) {
			elementType[i] = element;
		}

		return parseMessage(elementLength, elementType, longString);
	}

	/**
	 * Parse message to array of string based on element lenth, element type from long string
	 * @param elementLength
	 * @param longString
	 * @return
	 * @param elementType
	 */
	public static String[] parseMessage(final int[] elementLength, final int[] elementType, final String longString) {
		String[] messageDetail = null;
		if (elementLength != null && elementLength.length > 0 && longString != null) {
			messageDetail = new String[elementLength.length];

			if (isLongStringParsed(elementLength, longString)) {
				int progress = 0;
				for (int i = 0; i < elementLength.length; i++) {
					int length = elementLength[i];
					String rawString = longString.substring(progress, progress + length);
					messageDetail[i] = parseString(elementType[i], rawString);
					progress += length;
				}
			} else {
				throw new RuntimeException(ERROR_PARSING);
			}

		}
		return messageDetail;
	}

	static private boolean isLongStringParsed(int[] elementLength, String longString) {
		boolean result = false;
		int length = 0;
		for (int i = 0; i < elementLength.length; i++) {
			length += elementLength[i];
		}

		if (length != longString.length()) {
			result = false;
			throw new RuntimeException(ERROR_PARSING);
		} else {
			result = true;
		}
		return result;
	}

	static private String parseString(int type, String rawString) {
		String result = null;
		if (type == NUMERIC_TYPE) {

			char[] chars = rawString.toCharArray();
			boolean stop = false;
			for (int i = 0; i < chars.length && !stop; i++) {
				if (chars[i] != '0') {
					stop = true;
					result = rawString.substring(i, rawString.length());
				}
			}

			if (!stop) {
				result = "0";
			} else {
				char sign = result.charAt(result.length() - 1);
				if (sign == '+' || sign == '-')
					result = sign + result.substring(0, result.length() - 1);
			}

			if (result.equals("+") || result.equals("-"))
				result = result + "0";

		} else if (type == TEXT_TYPE) {
			result = rawString.trim();
		} else if (type == RAW_TYPE) {
			result = rawString;
		}
		return result;
	}
}
