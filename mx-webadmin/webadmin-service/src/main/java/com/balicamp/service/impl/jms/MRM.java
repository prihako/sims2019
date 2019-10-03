package com.balicamp.service.impl.jms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service("mrm")
public class MRM implements InitializingBean, DisposableBean, Runnable {
	public static class Pair<P1, P2> {
		public P1 p1;

		public volatile P2 p2;

		public Pair(P1 p1, P2 p2) {
			this.p1 = p1;
			this.p2 = p2;
		}
	}

	private static final Log log = LogFactory.getLog(MRM.class);

	private ReadLock sharedLock;

	private WriteLock exclusiveLock;

	private Map<String, Pair<Long, HashMap>> maps;

	private Thread th;

	private volatile boolean stopped;

	public MRM() {
		maps = new ConcurrentHashMap<String, Pair<Long, HashMap>>();

		ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
		sharedLock = lock.readLock();
		exclusiveLock = lock.writeLock();
	}

	public void afterPropertiesSet() throws Exception {
		th = new Thread(this);
		th.setName("Message-GC");
		th.setDaemon(true);

		th.start();
	}

	public void register(String deliveryChannelId, long expiredDate) {
		Pair<Long, HashMap> p = new Pair<Long, HashMap>(new Long(expiredDate), null);

		sharedLock.lock();
		maps.put(deliveryChannelId, p);
		sharedLock.unlock();
	}

	public void unregister(String deliveryChannelId) {
		sharedLock.lock();
		maps.remove(deliveryChannelId);
		sharedLock.unlock();
	}

	/*
	 * If Pair == null -> time out If pair.p2 == null -> belum ada response
	 */
	public HashMap getResponse(String deliveryChannelId) {
		sharedLock.lock();
		Pair<Long, HashMap> pair = maps.get(deliveryChannelId);
		sharedLock.unlock();

		if (pair == null) {
			return null;
		}

		return pair.p2;
	}

	public void setResponse(String deliveryChannelId, HashMap T) {
		sharedLock.lock();
		Pair<Long, HashMap> pair = maps.get(deliveryChannelId);
		sharedLock.unlock();

		if (pair != null) {
			pair.p2 = T;
		}
	}

	/*
	 * Jika time-out return true
	 */
	public boolean isTimeOut(String deliveryChannelId) {
		sharedLock.lock();
		boolean contains = maps.containsKey(deliveryChannelId);
		sharedLock.unlock();

		return !contains;
	}

	public void run() {
		stopped = false;

		while (!stopped) {
			long sleepTime = -1;
			long dtNow = System.currentTimeMillis();

			exclusiveLock.lock();
			try {
				Iterator<Entry<String, Pair<Long, HashMap>>> it = maps.entrySet().iterator();

				while (it.hasNext()) {
					Entry<String, Pair<Long, HashMap>> e = it.next();

					long expiredDate = e.getValue().p1;

					if (dtNow >= expiredDate) {
						it.remove();
						log.info("message with stan = '" + e.getKey() + "' has been expired and removed.");
					} else {
						long delta = dtNow - expiredDate;

						if (delta > 0) {
							if (sleepTime == -1) {
								sleepTime = delta;
							} else if (sleepTime > delta) {
								sleepTime = delta;
							}
						}
					}
				}
			} finally {
				exclusiveLock.unlock();
			}

			if (sleepTime == -1) {
				sleepTime = 1000;
			}

			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException ex) {
					stopped = true;
				}
			}
		}
	}

	public void destroy() throws Exception {
		stopped = true;
		th.interrupt();
	}
}
