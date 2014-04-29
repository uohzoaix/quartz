package com.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class StatefulDumbJob implements Job {

	// 静态常量，作为任务在调用间，保持数据的键(key)
	// NUM_EXECUTIONS，保存的计数每次递增1
	// EXECUTION_DELAY，任务在执行时，中间睡眠的时间。本例中睡眠时间过长导致了错失触发
	public static final String NUM_EXECUTIONS = "NumExecutions";
	public static final String EXECUTION_DELAY = "ExecutionDelay";

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		// 任务执行的时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String jobRunTime = dateFormat.format(Calendar.getInstance().getTime());

		System.err.println("---" + context.getJobDetail().getKey().getName() + " 在  : [" + jobRunTime + "] 执行了!!");

		// 任务执行计数 累加
		JobDataMap map = context.getJobDetail().getJobDataMap();
		int executeCount = 0;
		if (map.containsKey(NUM_EXECUTIONS)) {
			executeCount = map.getInt(NUM_EXECUTIONS);
		}
		executeCount++;
		map.put(NUM_EXECUTIONS, executeCount);

		// 睡眠时间: 由调度类重新设置值 ,本例为 睡眠10s
		long delay = 5000l;
		if (map.containsKey(EXECUTION_DELAY)) {
			delay = map.getLong(EXECUTION_DELAY);
		}

		try {
			Thread.sleep(delay);
		} catch (Exception ignore) {
		}

		// 睡眠醒来后，打印任务执行结束的信息
		System.err.println("  -" + context.getJobDetail().getKey().getName() + " 完成次数  : " + executeCount);
	}
}