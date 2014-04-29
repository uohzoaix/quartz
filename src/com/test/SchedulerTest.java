package com.test;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerTest {

	public static void main(String[] args) throws Exception {
		SchedulerTest example = new SchedulerTest();
		example.run();
	}

	public void run() throws Exception {
		// ����ִ�е�ʱ�� ��ʽ��
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		System.out.println("--------------- ��ʼ�� -------------------");

		// ��һ����15��
		Date startTime = nextGivenSecondDate(null, 15);

		// statefulJob1 ÿ3s����һ��,�������ӳ�10s
		JobDetail job = newJob(StatefulDumbJob.class).withIdentity("statefulJob1", "group1").usingJobData(StatefulDumbJob.EXECUTION_DELAY, 10000L) // ���ò���:˯��ʱ�� 10s
				.build();
		SimpleTrigger trigger = newTrigger().withIdentity("trigger1", "group1").startAt(startTime).withSchedule(simpleSchedule().withIntervalInSeconds(3).repeatForever()).build();
		Date ft = sched.scheduleJob(job, trigger);
		System.out.println(job.getKey().getName() + " ����: " + dateFormat.format(ft) + "  ʱ����.�����ظ�: " + trigger.getRepeatCount() + " ��, ÿ�μ�� " + trigger.getRepeatInterval() / 1000 + " ��");

		// statefulJob2 ��ÿ3s����һ�� , �������ӳ�10s , Ȼ�󲻶ϵĵ���
		job = newJob(StatefulDumbJob.class).withIdentity("statefulJob2", "group1").usingJobData(StatefulDumbJob.EXECUTION_DELAY, 10000L)// ���ò���:˯��ʱ�� 10s
				.build();
		trigger = newTrigger().withIdentity("trigger2", "group1").startAt(startTime).withSchedule(simpleSchedule().withIntervalInSeconds(3).repeatForever()
		// ���ô�ʧ������ĵ��Ȳ���
				.withMisfireHandlingInstructionNowWithRemainingCount()).build();

		ft = sched.scheduleJob(job, trigger);
		System.out.println(job.getKey().getName() + " ����: " + dateFormat.format(ft) + "  ʱ����.�����ظ�: " + trigger.getRepeatCount() + " ��, ÿ�μ�� " + trigger.getRepeatInterval() / 1000 + " ��");

		System.out.println("------- ��ʼ���� (����.start()����) ----------------");
		sched.start();

		// ��������ʱ������
		Thread.sleep(600L * 1000L);

		sched.shutdown(true);
		System.out.println("------- �����ѹر� ---------------------");

		// ��ʾһ�� �Ѿ�ִ�е�������Ϣ
		SchedulerMetaData metaData = sched.getMetaData();
		System.out.println("~~~~~~~~~~  ִ���� " + metaData.getNumberOfJobsExecuted() + " �� jobs.");
	}
}