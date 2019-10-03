// package com.balicamp.service.impl.jms;
//
// import javax.jms.Connection;
// import javax.jms.ConnectionFactory;
// import javax.jms.Destination;
// import javax.jms.ExceptionListener;
// import javax.jms.JMSException;
// import javax.jms.MessageConsumer;
// import javax.jms.MessageListener;
// import javax.jms.Session;
//
// import org.apache.commons.logging.Log;
// import org.apache.commons.logging.LogFactory;
// import org.springframework.beans.factory.DisposableBean;
// import org.springframework.beans.factory.InitializingBean;
//
// public class JmsListener implements InitializingBean, DisposableBean,
// MessageListener, ExceptionListener, Runnable {
// private static final Log log = LogFactory.getLog(JmsListener.class);
//
// private MessageListener messageListener;
//
// private ConnectionFactory mqConnectionFactory;
//
// private Connection mqconn;
//
// private Session mqsession;
//
// private String destinationType;
//
// private String destinationName;
//
// private Destination destination;
//
// private MessageConsumer consumer;
//
// private boolean transacted;
//
// private int ackMode = Session.AUTO_ACKNOWLEDGE;
//
// private long reconnectDelay;
//
// private volatile boolean needConnection;
//
// private final Object connectionNeeded;
//
// private Thread connectThread;
//
// public JmsListener() {
// connectionNeeded = new Object();
// }
//
// public void setMessageListener(MessageListener messageListener) {
// this.messageListener = messageListener;
// }
//
// public void setMqConnectionFactory(ConnectionFactory mqConnectionFactory) {
// this.mqConnectionFactory = mqConnectionFactory;
// }
//
// public void setDestinationType(String destinationType) {
// this.destinationType = destinationType;
// }
//
// public void setDestinationName(String destinationName) {
// this.destinationName = destinationName;
// }
//
// public void setTransacted(boolean transacted) {
// this.transacted = transacted;
// }
//
// public void setAckMode(int ackMode) {
// this.ackMode = ackMode;
// }
//
// public void setReconnectDelay(long reconnectDelay) {
// this.reconnectDelay = reconnectDelay;
// }
//
// public void onMessage(javax.jms.Message msg) {
// try {
// if (messageListener != null) {
// messageListener.onMessage(msg);
// }
// } finally {
// try {
// if (transacted) {
// mqsession.commit();
// }
//
// if (ackMode == Session.CLIENT_ACKNOWLEDGE) {
// msg.acknowledge();
// }
// } catch (Exception ex) {}
// }
// }
//
// public void afterPropertiesSet() throws Exception {
// connectThread = new Thread(this);
// connectThread.setName("JmsListener-ConnectThread");
// connectThread.setDaemon(true);
//
// connectThread.start();
// }
//
// public void destroy() throws Exception {
// if (connectThread != null) {
// connectThread.interrupt();
// connectThread = null;
// }
// }
//
// public void onException(JMSException ex) {
// needConnection = true;
//
// synchronized (connectionNeeded) {
// connectionNeeded.notifyAll();
// }
// }
//
// public void run() {
// needConnection = true;
//
// for (;;) {
// while (!needConnection) {
// try {
// synchronized (connectionNeeded) {
// connectionNeeded.wait();
// }
// } catch (InterruptedException e) {
// return;
// }
// }
//
// needConnection = false;
// close();
//
// try {
// mqconn = mqConnectionFactory.createConnection();
// mqconn.setExceptionListener(this);
// mqconn.start();
//
// mqsession = mqconn.createSession(transacted, ackMode);
//
// if ("topic".equalsIgnoreCase(destinationType)) {
// destination = mqsession.createTopic(destinationName);
// } else {
// destination = mqsession.createQueue(destinationName);
// }
//
// consumer = mqsession.createConsumer(destination);
// consumer.setMessageListener(this);
//
// log.info("connected to MQ server and ready to listen.");
// } catch (Exception e) {
// log.error("an error occured while establishing connection to MQ server.", e);
//
// close();
// needConnection = true;
// }
//
// if (needConnection) {
// try {
// Thread.sleep(reconnectDelay);
// } catch (InterruptedException e) {
// return;
// }
// }
// }
// }
//
// private void close() {
// if (consumer != null) {
// try {
// consumer.close();
// } catch (Exception ex) {
// // do nothing
// }
//
// consumer = null;
// }
//
// if (mqsession != null) {
// try {
// mqsession.close();
// } catch (Exception ex) {
// // do nothing
// }
//
// mqsession = null;
// }
//
// if (mqconn != null) {
// try {
// mqconn.stop();
// } catch (Exception ex) {
// // do nothing
// }
//
// try {
// mqconn.close();
// } catch (Exception ex) {
// // do nothing
// }
//
// mqconn = null;
// }
//
// destination = null;
// }
// }
