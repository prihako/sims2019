// package com.balicamp.service.impl.jms;
//
// import javax.annotation.Resource;
// import javax.jms.Message;
// import javax.jms.MessageListener;
// import javax.jms.ObjectMessage;
//
// import org.springframework.stereotype.Service;
//
// import com.balicamp.service.jms.JmsListener;
//
// @Service("jmsListener")
// public class JmsListenerImpl implements JmsListener, MessageListener {
//
// @Resource(name = "mat")
// private MAT mat;
//
// public void onMessage(Message msg) {
// if (msg instanceof ObjectMessage) {
// Object objMsg;
//
// try {
// objMsg = ((ObjectMessage) msg).getObject();
// } catch (Throwable t) {
// // log.info("unable to dispatch incoming request message " + msg
// // + ".", t);
// return;
// }
// //
// // if (objMsg instanceof Transaction) {
// // Transaction T = (Transaction) objMsg;
// // mat.setResponse(T.getDeliveryChannelId(), T);
// // // Long sentTime = mat.unregister( T.getDeliveryChannelId() );
// // System.out.println(T);
// //
// // if (T == null) {
// // // log.info("unmatched message found, stan = " +
// // // holder.getStan() + ", possibly timed out.");
// // } else {
// // // long elapsed = dtNow - sentTime.longValue();
// //
// // // log.info("matched message found, stan = " +
// // // holder.getStan() + ", elapsed time = " + elapsed
// // // + " ms.");
// // }
// // } else {
// // // log.info("message is not instanceof ObjectMessage");
// // }
// } else {
// // log.info("message is not instanceof ObjectMessage");
// }
//
// }
//
// }
