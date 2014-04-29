package com.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class QuartzJob implements Job {

	private static boolean running = false;
	private static List<String> pushs = Collections.synchronizedList(new ArrayList<String>());
	private static Map<Boolean, List<String>> pushStatus = new ConcurrentHashMap<Boolean, List<String>>();

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println(new Date() + "running");
		running = true;
		System.out.println(pushs.size());
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		pushs.clear();
		running = false;
		System.out.println(new Date() + "stopping");
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
