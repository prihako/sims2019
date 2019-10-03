// package com.balicamp.service.impl.jms.listener;
//
// import java.util.HashMap;
//
// import javax.jms.JMSException;
// import javax.jms.MapMessage;
// import javax.jms.Message;
// import javax.jms.MessageListener;
//
// import org.apache.commons.logging.Log;
// import org.apache.commons.logging.LogFactory;
// import org.springframework.jms.support.converter.SimpleMessageConverter;
//
// import com.balicamp.service.impl.jms.MRM;
// import com.balicamp.service.impl.jms.listener.handler.CustomHandler;
//
// public class DeliveryChannelResponseListener extends SimpleMessageConverter
// implements MessageListener {
// private static final Log log =
// LogFactory.getLog(DeliveryChannelResponseListener.class);
//
// private MRM mrm;
//
// private String originator;
//
// private CustomHandler nonRequestResponseHandler;
//
// public void setMrm(MRM mrm) {
// this.mrm = mrm;
// }
//
// public void setOriginator(String originator) {
// this.originator = originator;
// }
//
// public void setNonRequestResponseHandler(CustomHandler
// nonRequestResponseHandler) {
// this.nonRequestResponseHandler = nonRequestResponseHandler;
// }
//
// public void onMessage(Message msg) {
// if (msg instanceof MapMessage) {
// MapMessage obj;
//
// try {
// obj = ((MapMessage) msg);
//
// // if (obj instanceof HashMap) {
// HashMap bean = (HashMap) extractMapFromMessage(obj);
//
// String bdcId = (String) bean.get("DeliveryChannelId");
// String boriginator = (String) bean.get("Originator");
//
// if ((bdcId != null) && originator.equals(boriginator)) {
// mrm.setResponse(bdcId, bean);
// } else if (nonRequestResponseHandler != null) {
// nonRequestResponseHandler.doHandle(bean);
// }
// } catch (JMSException e) {
// log.warn("an error occured while retrieving object.", e);
// return;
// }
// // } else {
// // log.warn("object retrieved is not an instance of Bean.");
// // }
// }
// }
// }
