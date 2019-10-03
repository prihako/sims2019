package com.balicamp.service.common;

import java.io.IOException;

/**
 * 100% from M Rezha ideas with IFC added knowledge for using XStream
 * @author Harun Al Rasyid
 * @author Arif 
 *   refactor name
 */
public interface ObjectSerializerManager {

	/**
	 * Convert from object to serialized xml
	 * @param object
	 * @return
	 */
	String parseToString(Object object) throws IOException;

	/**
	 * Convert from serialized xml to Object
	 * @param serializeableObject
	 * @return
	 */
	Object parseToObject(String serializeableObject) throws IOException;

}
