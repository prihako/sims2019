// package com.balicamp.service.impl.jms.sender;
//
// import javax.jms.Destination;
// import javax.jms.JMSException;
// import javax.jms.Message;
// import javax.jms.ObjectMessage;
// import javax.jms.Session;
//
// import org.apache.commons.logging.Log;
// import org.apache.commons.logging.LogFactory;
// import org.springframework.beans.factory.InitializingBean;
// import org.springframework.jms.core.JmsTemplate;
// import org.springframework.jms.core.MessageCreator;
//
// import com.balicamp.model.jms.trigger.TriggerJms;
// import com.balicamp.model.jms.trigger.TriggerJmsHolder;
// import com.balicamp.model.parameter.SystemParameterId;
// import com.balicamp.service.jms.registration.TriggerJmsRegistrationManager;
// import com.balicamp.service.jms.sender.TriggerJmsSender;
// import com.balicamp.service.parameter.SystemParameterManager;
// import com.balicamp.util.LogUtil;
//
// /**
// * TriggerISO sender
// *
// * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
// */
// public class JmsTriggerSenderImpl implements TriggerJmsSender,
// InitializingBean {
// protected final Log log = LogFactory.getLog(JmsTriggerSenderImpl.class);
//
// private JmsTemplate jmsTemplate;
// private Destination destination;
// private TriggerJmsRegistrationManager triggerJmsRegistrationManager;
// private SystemParameterManager systemParameterManager;
//
// // timeout configuration
// private long defaultTimeout = 60000l;
// private SystemParameterId timeoutSystemParameterId;
//
// public void afterPropertiesSet() throws Exception {
// if (timeoutSystemParameterId != null) {
// // read default time out from systemParameter
// defaultTimeout =
// systemParameterManager.getLongValue(timeoutSystemParameterId,
// defaultTimeout);
// }
// }
//
// /**
// * {@inheritDoc}
// */
// public void sendMessage(final TriggerJms triggerJms) {
// // cek if TriggerIso, set transactiondate
// // if (triggerJms instanceof TriggerIso) {
// // TriggerIso triggerIso = (TriggerIso) triggerJms;
// // BaseTransaction data = triggerIso.getData();
// //
// // if (data != null && data.getTransactionDate() == null)
// // data.setTransactionDate(new Date());
// // }
//
// log.debug(LogUtil.concatMessage("sending to '", destination,
// "' triggerJms = ", triggerJms));
// try {
// jmsTemplate.send(destination, new MessageCreator() {
// public Message createMessage(Session session) throws JMSException {
// ObjectMessage objMessage = session.createObjectMessage();
// objMessage.setObject(triggerJms);
// return objMessage;
// }
// });
// } catch (Exception e) {
// log.error("fail send jms", e);
// }
// }
//
// /**
// * {@inheritDoc}
// */
// public TriggerJmsHolder sendMessageAndRegister(final TriggerJms triggerJms,
// long timeout) {
//
// // construct trigger
// TriggerJmsHolder triggerJmsHolder = new TriggerJmsHolder();
// triggerJmsHolder.setRequest(triggerJms);
// triggerJmsHolder.setTimestamp(System.currentTimeMillis());
// triggerJmsHolder.setTimeOutPeriode(timeout);
//
// // register to timeout
// triggerJmsRegistrationManager.register(triggerJmsHolder);
//
// // send
// sendMessage(triggerJms);
//
// return triggerJmsHolder;
// }
//
// /**
// * {@inheritDoc}
// */
// public TriggerJms sendMessageAndWaitResponse(final TriggerJms triggerJms,
// long timeout) {
//
// // mark wait response
// triggerJms.setWaitResponse(true);
//
// // init timeout periode
// if (timeout < 0)
// timeout = defaultTimeout;
//
// // send message and register
// TriggerJmsHolder triggerJmsHolder = sendMessageAndRegister(triggerJms,
// timeout);
//
// // wait until timeout or receive notify
// if (triggerJmsHolder.getResponse() == null) {
// synchronized (triggerJmsHolder) {
// try {
// triggerJmsHolder.wait(timeout);
// } catch (Exception e) { // NOPMD
// // ignore
// }
// }
// }
//
// // unregister
// triggerJmsHolder =
// triggerJmsRegistrationManager.unRegister(triggerJmsHolder.getRequest().getId());
// if (triggerJmsHolder != null)
// return triggerJmsHolder.getResponse();
//
// return null;
// }
//
// // setter
// public void setJmsTemplate(JmsTemplate jmsTemplate) {
// this.jmsTemplate = jmsTemplate;
// }
//
// public void setDestination(Destination destination) {
// this.destination = destination;
// }
//
// public void setSystemParameterManager(SystemParameterManager
// systemParameterManager) {
// this.systemParameterManager = systemParameterManager;
// }
//
// public void setTriggerJmsRegistrationManager(TriggerJmsRegistrationManager
// triggerJmsRegistrationManager) {
// this.triggerJmsRegistrationManager = triggerJmsRegistrationManager;
// }
//
// public void setTimeoutSystemParameterId(SystemParameterId
// timeoutSystemParameterId) {
// this.timeoutSystemParameterId = timeoutSystemParameterId;
// }
//
// }
