package com.balicamp.service.impl.mx;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.balicamp.service.HsmMessageParser;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.util.mx.HsmThalesPackager;

@Component("hsmMessageParser")
public class HsmMessageParserImpl implements HsmMessageParser {

	@Autowired
	private SystemParameterManager parameterManager;

	private HsmThalesPackager requestPackager;
	private HsmThalesPackager responsePackager;

	private String basePath;
	private String baseSchema;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(HsmMessageParserImpl.class);

	@Override
	public String parse(String message, boolean html, boolean isRequest) {
		// TODO Auto-generated method stub
		String formatted = "";

		if (basePath == null) {
			basePath = parameterManager.findParamValueByParamName("other.messageLogs.configFile")
					+ "/HThalesAdaptor-FSDXML/hsm-";
			baseSchema = "base";

			requestPackager = new HsmThalesPackager(basePath, baseSchema);
			responsePackager = new HsmThalesPackager(basePath + "resp-", baseSchema);
		}

		Map hsmMessage = new HashMap();
		try {
			if (isRequest)
				requestPackager.unpack(message.getBytes("UTF-8"), hsmMessage);
			else
				responsePackager.unpack(message.getBytes("UTF-8"), hsmMessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String separator = html ? "<br />" : "\n";

		Iterator iterator = hsmMessage.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry mapEntry = (Map.Entry) iterator.next();
			formatted = formatted + mapEntry.getKey() + " = " + mapEntry.getValue() + separator;
		}

		return formatted;
	}

	@Override
	public HashMap parseToHashMap(String message, boolean isRequest) {
		HashMap hsmMessage = new HashMap();
		try {
			if (isRequest)
				requestPackager.unpack(message.getBytes("UTF-8"), hsmMessage);
			else
				responsePackager.unpack(message.getBytes("UTF-8"), hsmMessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hsmMessage;
	}

}