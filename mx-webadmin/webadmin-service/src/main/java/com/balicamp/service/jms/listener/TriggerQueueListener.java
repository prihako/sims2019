// package com.balicamp.service.jms.listener;
//
// import javax.jms.Message;
// import javax.jms.MessageListener;
//
// import com.balicamp.model.jms.trigger.TriggerJms;
//
// /**
// * Jmstrigger listener ( web to scheduller and scheduller to web)
// * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
// */
// public interface TriggerQueueListener<T extends TriggerJms> extends
// MessageListener {
//
// /**
// * Extract TriggerJms from JMS message
// * @param message
// * @return
// */
// T extractTriggerJms(Message message);
// }
