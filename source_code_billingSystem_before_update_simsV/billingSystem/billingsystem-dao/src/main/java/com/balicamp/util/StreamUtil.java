package com.balicamp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.balicamp.exception.ApplicationException;

public class StreamUtil {
	
	/**
	 * read input stream return byte array
	 * @param inputStream
	 * @return
	 */
	public static byte[] inputStreamToByteArray(InputStream inputStream){
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			byte[] buffer = new byte[1000];
			int readLength = -1;
			while ( (readLength = inputStream.read( buffer )) != -1 ) {
				outputStream.write(buffer, 0, readLength);
			}			
		} catch (Exception e) { //NOPMD
			//ignore just return available byte
		}
		
		return outputStream.toByteArray();
	}

	/**
	 * 
	 * @param inputStream
	 * @return
	 */
	public static String inputStreamToString(InputStream inputStream){
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			byte[] buffer = new byte[1000];
			int readLength = -1;
			while ( (readLength = inputStream.read( buffer )) != -1 ) {
				outputStream.write(buffer, 0, readLength);
			}			
		} catch (Exception e) { //NOPMD
			//ignore just return available byte
		}
		
		return outputStream.toString();
	}

	/**
	 * close object by serialized object then deserialized object
	 * @param input
	 * @return
	 */
	public static Object cloneObject(Object input){
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream  = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(input);
			
			ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			
			return objectInputStream.readObject();
		} catch (Exception e) {
			throw new ApplicationException("fail serialized object", e);
		} 
	}



}
