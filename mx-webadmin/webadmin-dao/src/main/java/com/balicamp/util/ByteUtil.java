package com.balicamp.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balicamp.exception.ApplicationException;

public class ByteUtil {
	protected final Log log = LogFactory.getLog(getClass());
	

	public String decript(String encriptedText, byte[] key) {
		try {			
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key,"DES"));
			
            byte[] byteArray = cipher.doFinal(hexStringToByteArray(encriptedText));

            return byteArrayToHexString(byteArray);
		} catch (Exception e) {
			throw new ApplicationException("fail decript pin", e);
		} 
	}

	public static byte[] hexStringToByteArray( String hexString ){
		byte[] byteArray = new byte[hexString.length() / 2];
		for (int i = 0; i < byteArray.length; i++) {
			byteArray[i] = (byte) Integer.parseInt(hexString.substring(i*2, (i*2)+2), 16);			
		}
		return byteArray;
	}
	
	public static String byteArrayToHexString( byte[] byteArray ){
		StringBuffer hexString = new StringBuffer();
		for (int tmpByte : byteArray) {
			String tmpHexString = Integer.toHexString(tmpByte);
			while( tmpHexString.length() < 2 ){
				tmpHexString = "0" + tmpHexString;
			}
			if ( tmpHexString.length() > 2 ){
				tmpHexString = tmpHexString.substring(tmpHexString.length()-2);
			}
			hexString.append(tmpHexString);
		}
		return hexString.toString().toUpperCase();
	}

}
