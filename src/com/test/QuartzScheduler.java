package com.test;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.ParseException;
import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzScheduler {

	public static void main(String[] args) throws SchedulerException, ParseException {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		Date startTime = nextGivenSecondDate(null, 5);

		JobDetail job = newJob(QuartzJob.class).withIdentity("statefulJob1", "group1").build();

		// CronTrigger trigger = newTrigger().withIdentity("trigger1", "group1").startAt(startTime).withSchedule(cronSchedule("0/15 * * * * ?").withMisfireHandlingInstructionIgnoreMisfires()).build();
		SimpleTrigger trigger = newTrigger().withIdentity("trigger1", "group1").startAt(startTime).withSchedule(simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();
		//Trigger trigger = newTrigger().withIdentity("trigger1", "group1").startAt(startTime).withSchedule(simpleSchedule().withIntervalInSeconds(5).withMisfireHandlingInstructionIgnoreMisfires()).build();
		sched.scheduleJob(job, trigger);
		sched.start();
		for (int i = 0; i < 100000; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			QuartzJob.addToList(i + "");
		}
	}
	
}
