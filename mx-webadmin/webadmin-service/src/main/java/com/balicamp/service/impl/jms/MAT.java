package com.balicamp.service.impl.jms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service("mat")
public class MAT implements InitializingBean, DisposableBean, Runnable {
	public static class Pair<P1, P2> {
		public P1 p1;

		public P2 p2;

		public Pair(P1 p1, P2 p2) {
			this.p1 = p1;
			this.p2 = p2;
		}
	}

	private static final Log log = LogFactory.getLog(MAT.class);

	private Map<String, Pair<Long, HashMap>> maps;

	private Thread th;

	private volatile boolean stopped;

	public MAT() {
		this.maps = new HashMap<String, Pair<Long, HashMap>>();
	}

	public void afterPropertiesSet() throws Exception {
		th = new Thread(this);
		th.setName("MAT-GC");
		th.setDaemon(true);

		th.start();
	}

	public void register(String deliveryChannelId, long expiredDate) {
		synchronized (maps) {
			maps.put(deliveryChannelId, new Pair<Long, HashMap>(new Long(expiredDate), null));
			maps.notify();
		}
	}

	public void unregister(String deliveryChannelId) {
		synchronized (maps) {
			if (maps.containsKey(deliveryChannelId)) {
				Pair<Long, HashMap> pair = maps.remove(deliveryChannelId);
			}
		}
	}

	/*
	 * If Pair == null -> time out
	 * If pair.p2 == null -> belum ada response
	 */
	public HashMap getResponse(String deliveryChannelId) {
		synchronized (maps) {
			Pair<Long, HashMap> pair = maps.get(deliveryChannelId);
			if (pair == null && pair.p2 == null) {
				return null;
			}
			return pair.p2;
		}
	}

	public void setResponse(String deliveryChannelId, HashMap T) {
		synchronized (maps) {
			Pair<Long, HashMap> pair = maps.get(deliveryChannelId);
			if (pair != null) {
				pair.p2 = T;
			}
		}
	}

	/*
	 * Jika time-out return true
	 */
	public boolean isTimeOut(String deliveryChannelId) {
		synchronized (maps) {
			Pair<Long, HashMap> pair = maps.get(deliveryChannelId);
			if (pair == null) {
				return true;
			}
			return false;
		}
	}

	public void run() {
		stopped = false;
		while (!stopped) {
			synchronized (maps) {
				while (maps.isEmpty()) {
					try {
						maps.wait();
					} catch (InterruptedException ex) {
						stopped = true;
						return;
					}
				}

				long sleepTime = -1;
				long dtNow = System.currentTimeMillis();

				Iterator<Entry<String, Pair<Long, HashMap>>> it = maps.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, Pair<Long, HashMap>> e = it.next();

					long expiredDate = e.getValue().p1;

					if (dtNow >= expiredDate) {
						it.remove();
						log.info("message removed with stan = " + e.getKey());
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

				if (sleepTime > 0) {
					try {
						maps.wait(sleepTime);
					} catch (InterruptedException ex) {
						stopped = true;
					}
				}
			}
		}
	}

	public void destroy() throws Exception {
		stopped = true;
		th.interrupt();
	}
}
