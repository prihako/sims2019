package com.balicamp.service.impl.common;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.balicamp.exception.ServiceException;
import com.balicamp.service.common.ObjectSerializerManager;
import com.thoughtworks.xstream.XStream;

/**
 * Implementation of display parser manager
 * 
 * @author Harun Al Rasyid
 * @author Arif refractor name
 * 
 */
@Service("objectSerializerManager")
public class ObjectSerializerManagerXStream implements ObjectSerializerManager {

	protected final Log log = LogFactory.getLog(getClass());

	public Object parseToObject(String serializeableObject) throws IOException {
		Object returnedObject = null;
		try {
			XStream stream = new XStream();
			returnedObject = stream.fromXML(serializeableObject);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(
					"Fail when convert from bytes to object ", e);
		}

		return returnedObject;
	}

	public String parseToString(Object object) throws IOException {
		XStream stream = new XStream();
		return stream.toXML(object);
	}

}
