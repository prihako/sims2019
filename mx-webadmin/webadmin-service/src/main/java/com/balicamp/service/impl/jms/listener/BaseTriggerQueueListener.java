// package com.balicamp.service.impl.jms.listener;
//
// import static com.balicamp.Constants.SystemParameter.TimeOut.GROUP;
// import static
// com.balicamp.Constants.SystemParameter.TimeOut.WAIT_ON_THREADPOOL_FULL;
//
// import java.io.Serializable;
//
// import javax.jms.JMSException;
// import javax.jms.Message;
// import javax.jms.ObjectMessage;
//
// import org.apache.commons.logging.Log;
// import org.apache.commons.logging.LogFactory;
// import org.springframework.beans.factory.InitializingBean;
// import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
// import com.balicamp.model.jms.trigger.TriggerJms;
// import com.balicamp.model.parameter.SystemParameterId;
// import com.balicamp.service.jms.listener.TriggerQueueListener;
// import com.balicamp.service.parameter.SystemParameterManager;
// import com.balicamp.util.LogUtil;
//
// /**
// * Base implementation TriggerQueueListener
// *
// * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
// */
// public abstract class BaseTriggerQueueListener<T extends TriggerJms>
// implements TriggerQueueListener<T>,
// InitializingBean {
// protected final String simpleClassName = getClass().getSimpleName();
//
// protected final Log log = LogFactory.getLog(getClass());
//
// protected ThreadPoolTaskExecutor taskExecutor;
// protected SystemParameterManager systemParameterManager;
//
// protected Object taskExecutorSync = new Object();
//
// /**
// * counter for thread worker name
// */
// private long threadWorkerNamecounter = 0l;
//
// /**
// * wait time when threadpool full
// */
// private long waitOnThreadPoolFull = 1000l;
//
// public void afterPropertiesSet() throws Exception {
// waitOnThreadPoolFull = systemParameterManager.getLongValue(
// new SystemParameterId(GROUP, WAIT_ON_THREADPOOL_FULL), waitOnThreadPoolFull);
// }
//
// public T extractTriggerJms(Message message) {
// T triggerJms = null;
// try {
// ObjectMessage objectMessage = (ObjectMessage) message;
// Serializable serializeableObject = objectMessage.getObject();
// if (serializeableObject instanceof TriggerJms) {
// triggerJms = (T) serializeableObject;
// } else {
// log.error("Unknown messege receive on web queue " + serializeableObject);
// }
// } catch (JMSException e) {
// log.error("fail extract JmsTrigger from " + message, e);
// }
// return triggerJms;
// }
//
// protected void assignJob(T triggerJms) {
// synchronized (taskExecutorSync) {
// boolean stop;
// do {
// stop = this.isTaskExecutorEmpty();
// if (stop)
// waiting();
// else
// taskExecutor.execute(createWorker(triggerJms));
//
// } while (stop);
// }
// }
//
// protected void assignJob(Message message) {
// synchronized (taskExecutorSync) {
// boolean stop;
// do {
// stop = this.isTaskExecutorEmpty();
// if (stop)
// waiting();
// else
// taskExecutor.execute(createWorker(message));
//
// } while (stop);
// }
// }
//
// private Runnable createWorker(final Message message) {
// return new Runnable() {
//
// public void run() {
// try {
// processMessage(message);
// } catch (Exception e) {
// log.error(LogUtil.concatMessage("Fail process ", message), e);
// }
// }
//
// };
// }
//
// private Runnable createWorker(final T triggerJms) {
// return new Runnable() {
//
// public void run() {
// try {
// // set thread name
// if (triggerJms != null && triggerJms.getId() != null) {
// Thread.currentThread().setName(LogUtil.concatMessage(simpleClassName, "-",
// triggerJms.getId()));
// } else {
// Thread.currentThread().setName(
// LogUtil.concatMessage(simpleClassName, "-", threadWorkerNamecounter));
// threadWorkerNamecounter++;
// }
//
// processTrigger(triggerJms);
// } catch (Exception e) {
// log.error(LogUtil.concatMessage("Fail process ", triggerJms), e);
// }
// }
//
// };
// }
//
// /**
// * this method will be called by tread pool
// *
// * @param triggerJms
// */
// protected abstract void processTrigger(T triggerJms);
//
// protected void processMessage(Message message) {
//
// }
//
// private boolean isTaskExecutorEmpty() {
// boolean status = false;
//
// if ((this.taskExecutor.getCorePoolSize() -
// this.taskExecutor.getActiveCount()) < 1) {
// status = true;
// }
//
// return status;
// }
//
// private void waiting() {
// try {
// log.info("Waiting ... until taskExecutor available ... ");
// Thread.sleep(waitOnThreadPoolFull);
// } catch (Exception e) {// NOPMD
// // ignore
// }
// }
//
// // setter
// public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
// this.taskExecutor = taskExecutor;
// }
//
// public void setSystemParameterManager(SystemParameterManager
// systemParameterManager) {
// this.systemParameterManager = systemParameterManager;
// }
// }
