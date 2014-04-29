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

	// ��̬��������Ϊ�����ڵ��ü䣬�������ݵļ�(key)
	// NUM_EXECUTIONS������ļ���ÿ�ε���1
	// EXECUTION_DELAY��������ִ��ʱ���м�˯�ߵ�ʱ�䡣������˯��ʱ����������˴�ʧ����
	public static final String NUM_EXECUTIONS = "NumExecutions";
	public static final String EXECUTION_DELAY = "ExecutionDelay";

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		// ����ִ�е�ʱ��
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String jobRunTime = dateFormat.format(Calendar.getInstance().getTime());

		System.err.println("---" + context.getJobDetail().getKey().getName() + " ��  : [" + jobRunTime + "] ִ����!!");

		// ����ִ�м��� �ۼ�
		JobDataMap map = context.getJobDetail().getJobDataMap();
		int executeCount = 0;
		if (map.containsKey(NUM_EXECUTIONS)) {
			executeCount = map.getInt(NUM_EXECUTIONS);
		}
		executeCount++;
		map.put(NUM_EXECUTIONS, executeCount);

		// ˯��ʱ��: �ɵ�������������ֵ ,����Ϊ ˯��10s
		long delay = 5000l;
		if (map.containsKey(EXECUTION_DELAY)) {
			delay = map.getLong(EXECUTION_DELAY);
		}

		try {
			Thread.sleep(delay);
		} catch (Exception ignore) {
		}

		// ˯�������󣬴�ӡ����ִ�н�������Ϣ
		System.err.println("  -" + context.getJobDetail().getKey().getName() + " ��ɴ���  : " + executeCount);
	}
}