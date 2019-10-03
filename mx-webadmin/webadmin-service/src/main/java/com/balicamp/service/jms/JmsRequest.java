package com.balicamp.service.jms;

import java.util.HashMap;

public interface JmsRequest {

	public abstract void sendObject(HashMap T) throws Exception;

}
