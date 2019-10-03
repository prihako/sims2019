package com.balicamp.util;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * String Utility Class This is used to encode passwords programmatically
 *
 * <p>
 * <a h
 * ref="StringUtil.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class StringUtil {
    //~ Static fields/initializers =============================================

    private final static Log log = LogFactory.getLog(StringUtil.class);

    //~ Methods ================================================================

    /**
     * Encode a string using algorithm specified in web.xml and return the
     * resulting encrypted password. If exception, the plain credentials
     * string is returned
     *
     * @param password Password or other credentials to use in authenticating
     *        this username
     * @param algorithm Algorithm used to do the digest
     *
     * @return encypted password based on the algorithm.
     */
    public static String encodePassword(String password, String algorithm) {
        byte[] unencodedPassword = password.getBytes();

        MessageDigest md = null;

        try {
            // first create an instance, given the provider
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            log.error("Exception: " + e);

            return password;
        }

        md.reset();

        // call the update method one or more times
        // (useful when you don't know the size of your data, eg. stream)
        md.update(unencodedPassword);

        // now calculate the hash
        byte[] encodedPassword = md.digest();

        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < encodedPassword.length; i++) {
            if ((encodedPassword[i] & 0xff) < 0x10) {
                buf.append("0");
            }

            buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
        }

        return buf.toString();
    }
    
    /**
     * get Email domain
     * @param email
     * @return
     */
    public static String getEmailDomain(String email){
    	String[] emailSplitted = email.split("@");
    	return emailSplitted[1];
    }

	public static String getTail(String input, int length){
		if ( CommonUtil.isEmpty(input) )
			return input;
		
    	length = input.length() >= length ? length : input.length();
    	String result = input.substring(input.length() - length);
    	return result;
	}
	
	public static String addSeparator(String input, int partLength, String separator){
		StringBuffer output = new StringBuffer();
		
		int pos = 0;
		while ( true ){
			if ( input.length() <= pos )
				break;
			
			if ( output.length() > 0 )
				output.append(separator);
			
			int end = pos+partLength;
			if ( end > input.length() )
				end = input.length();
			
			output.append( input.subSequence(pos, end));
			pos = pos + partLength;
		}
		
		return output.toString();
	}
	
	public static int searchIndex(String[] array, String element){
		int index = 0;
		for (String string : array) {
			if (string.equals(element))
				return index;
			index++;
		}
		return -1;
	}

	public static String frontPadding(String string, int length, char paddingCharacter) {
		StringBuffer sb = new StringBuffer(string);
		while (sb.length() < length) {
			sb.insert(0, paddingCharacter);
		}		
		return sb.toString();
	}
	
	public static String toUpperFirstChar(String input) {
		StringBuilder output = new StringBuilder();
		
		output.append(input.substring(0,1).toUpperCase());
		output.append(input.substring(1));
		
		return output.toString();
	}
	
	public static String insertAt(String str, List karList, List posList) {
		if ( karList!=null && posList!=null ) {
			Iterator iter1 = karList.iterator();
			Iterator iter2 = posList.iterator();
			while ( iter1.hasNext() && iter2.hasNext() ) {
				try {
					String kar = (String) iter1.next();
					int pos = Integer.parseInt( (String) iter2.next() );
					str = insertAt( str, kar, pos );
				} catch ( Exception ex ) { }
			}
		}
		return str;
	}

	public static String insertAt(String str, String kar, int pos) {
		StringBuffer sb = new StringBuffer();
		for ( int i=0; i<str.length(); i++ ) {
			if ( (i+1)==pos ) 
				sb.append( kar );
			sb.append( str.charAt(i) );
		}
		return sb.toString();
	}
	
	public static int negateIndexOf(String str, Pattern charPattern, int startIndex) {
		int len = str.length();
		
		for (int i = startIndex; i < len; ++i) {
			if (!charPattern.matcher(str.substring(i, i + 1)).matches()) {
				return i - 1;
			}
		}
		
		return -1;
	}
}
