package id.co.sigma.mx.endpoint.bils0.helper;

import id.co.sigma.mx.message.InternalMessage;

public class CommonPropertySetter {
	public InternalMessage apply(InternalMessage message) {

		// Set log storage key
		String transmissionDateTime = message.getValue("/data/transmissionDateAndTime/text()");
		String channelRefNum = message.getValue("/data/retrievalReferenceNumber/text()");
		message.setValue("/data/logStorageKey/text()", transmissionDateTime + "+" + channelRefNum);

		return message;
	}
}