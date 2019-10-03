package com.balicamp.util.mx;

import org.jpos.iso.ISOUtil;

/**
 * @author balicamp@2007
 * @version $Id: Converter.java 218 2013-01-17 05:53:24Z bagus.sugitayasa $
 */
public class ConverterIso {

	public static byte[] int2bcd(int i) {
		byte[] b = {};
		try {
			Integer I = new Integer(i);
			b = ISOUtil.str2bcd(I.toString(), true);
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}

		return b;
	}

	public static int bcd2int(byte[] b) {
		int i = 0;
		try {
			String s = ISOUtil.bcd2str(b, 0, (b.length * 2), true);
			i = Integer.parseInt(s);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("ArrayIndexOutOfBoundsException: " + e.toString());
			i = -1;
		}

		return i;
	}

	public static String bcd2ascii(byte[] argMsg) {
		String strReturn = "";
		int iTemp;
		try {
			String stemp = "";
			for (int i = 0; i < argMsg.length; i++) {
				iTemp = (int) argMsg[i];
				if (argMsg[i] < 0)
					iTemp = 256 + (int) argMsg[i];
				stemp = Integer.toHexString(iTemp);
				if (stemp.length() == 1)
					stemp = "0" + stemp;
				strReturn += stemp;
			}
		} catch (Exception e) {
			System.err.println("Exception: " + e.toString());
		}

		return strReturn;
	}

	public static byte[] ascii2bcd(String argMsg) {
		if ((argMsg.length() % 2) != 0)
			argMsg = "0" + argMsg;

		int len = argMsg.length() / 2;
		byte[] byteReturn = new byte[len];
		try {
			int itemp;
			int j = 0;
			for (int i = 0; i < len; i++) {
				itemp = stringHex2Int(argMsg.substring(j, j + 2));
				j += 2;
				byteReturn[i] = (byte) itemp;
			}
		} catch (Exception e) {
			System.err.println("Exception: " + e.toString());
		}

		return byteReturn;
	}

	public static int stringHex2Int(String arg) {
		int retval = 0;
		try {
			// System.out.print(arg.substring(0, 2)+" = ");
			if (arg.substring(0, 2).equals("0X")) {
				retval = Integer.parseInt(arg.substring(2), 16);
			} else {
				retval = Integer.parseInt(arg, 16);
			}
			// System.out.println(retval);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Exception: " + e.toString());
		}
		return retval;
	}
}
