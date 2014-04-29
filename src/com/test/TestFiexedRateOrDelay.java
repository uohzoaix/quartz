package com.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestFiexedRateOrDelay {

	private static boolean running = false;
	private static List<String> pushs = Collections.synchronizedList(new ArrayList<String>());
	private static Map<Boolean, List<String>> pushStatus = new ConcurrentHashMap<Boolean, List<String>>();

	static {

		ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
		service.scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println(new Date() + "running");
					running = true;
					System.out.println(pushs.size());
					Thread.sleep(30000);
					pushs.clear();
					running = false;
					System.out.println(new Date() + "stopping");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
				}
			}
		}, 10, 20, TimeUnit.SECONDS);

		// new Timer().schedule(new TimerTask() {
		// @Override
		// public void run() {
		// try {
		// System.out.println(new Date() + "running");
		// running = true;
		// System.out.println(pushs.size());
		// Thread.sleep(30000);
		// pushs.clear();
		// running = false;
		// System.out.println(new Date() + "stopping");
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// }
		// }
		// }, 10000, 20000);
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100000; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			addToList(i + "");
		}
	}

	public static void addToList(String push) {
		try {
			if (running) {
				System.out.println("pushstatus:" + push);
				List<String> ads = pushStatus.get(false);
				if (ads == null) {
					ads = Collections.synchronizedList(new ArrayList<String>());
					pushStatus.put(false, ads);
				}
				ads.add(push);
			} else {
				System.out.println("pushs:" + push);
				pushs.add(push);
				if (pushStatus.get(false) != null) {
					pushs.addAll(pushStatus.get(false));
					pushStatus.put(false, Collections.synchronizedList(new ArrayList<String>()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
