package com.balicamp.service.security;

/**
 * Encode and decode pin
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public interface PinEncoder {
	/**
	 * Encript virtual Pin
	 * @param plainText
	 * @param key
	 * @return
	 */
	String encript(String plainText, String key);
	
	/**
	 * decript virtual pin
	 * @param encriptedText
	 * @param key
	 * @return
	 */
	String decript(String encriptedText, String key);
	
	/**
	 * encript pin to ITM
	 * @param plainText
	 * @param keyHexString
	 * @return
	 */
	String itmPinEncript(String plainText, String keyHexString);

	/**
	 * decript pin from ITM
	 * @param encriptedHexString
	 * @param keyHexString
	 * @return
	 */
	String itmPinDecript(String encriptedHexString, String keyHexString);


}
