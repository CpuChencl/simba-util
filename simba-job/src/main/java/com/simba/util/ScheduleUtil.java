package com.simba.util;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import com.simba.framework.util.applicationcontext.ApplicationContextUtil;
import com.simba.job.model.Job;
import com.simba.job.model.enums.JobStatus;
import com.simba.job.service.JobService;
import com.simba.model.constant.ConstantData;

/**
 * Spring定时器工具类(单例)
 * 
 * @author caozj
 *
 */
public class ScheduleUtil {

	private static final Log logger = LogFactory.getLog(ScheduleUtil.class);

	private ConcurrentTaskScheduler ct = null;

	private Map<Integer, ScheduledFuture<?>> scheduledFutrueMap = null;

	private ScheduleUtil() {
		init();
	}

	private void init() {
		ct = new ConcurrentTaskScheduler(Executors.newScheduledThreadPool(ConstantData.SCHEDULE_POLL_SIZE));
		scheduledFutrueMap = new HashMap<Integer, ScheduledFuture<?>>();
	}

	private static final class ScheduleUtilHolder {
		private static final ScheduleUtil instance = new ScheduleUtil();
	}

	public static ScheduleUtil getInstance() {
		return ScheduleUtilHolder.instance;
	}

	/**
	 * 新增任务
	 * 
	 * @param job
	 *            任务对象
	 * @return
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public boolean addJob(Job job) throws ParseException {
		if (needExecute(job)) {
			// 启动任务
			startJob(job);
			logger.info("启动任务成功:" + job.getName());
			return true;
		}
		return false;
	}

	/**
	 * 删除任务
	 * 
	 * @param job
	 *            任务对象
	 */
	public void deleteJob(Job job) {
		this.deleteJob(job.getId());
	}

	/**
	 * 删除任务
	 * 
	 * @param id
	 *            任务id
	 */
	public void deleteJob(int id) {
		ScheduledFuture<?> scheduledFuture = scheduledFutrueMap.get(id);
		if (scheduledFuture != null) {
			if (!scheduledFuture.isCancelled()) {
				/** false 表示当前任务若正在执行，则待其执行结束，再结束此任务. */
				scheduledFuture.cancel(false);
				scheduledFutrueMap.remove(id);
				logger.info("删除任务成功:" + id);
			}
		}
	}

	/**
	 * 返回所有正在运行的Job的id列表
	 * 
	 * @return
	 */
	public Set<Integer> getAllRunningJobs() {
		return scheduledFutrueMap.keySet();
	}

	private void startJob(Job job) throws ParseException {
		ScheduledFuture<?> scheduledFuture = null;
		if (StringUtils.isNotEmpty(job.getCronExpression())) {
			scheduledFuture = startCronJob(job);
		} else {
			scheduledFuture = startIntervalJob(job);
		}
		scheduledFutrueMap.put(job.getId(), scheduledFuture);
	}

	private ScheduledFuture<?> startIntervalJob(Job job) throws ParseException {
		ScheduledFuture<?> scheduledFuture;
		long start = job.getStartTimeL();
		long now = System.currentTimeMillis();
		if (start <= now) {
			start = now;
		}
		start = start + job.getDelayTime() * 1000;
		int intervalTime = job.getIntervalTime() * 1000;
		Date startDate = new Date(start);
		if (intervalTime > 0) {
			scheduledFuture = ct.scheduleAtFixedRate(() -> {
				executeJob(job);
			}, startDate, intervalTime);
		} else {
			scheduledFuture = ct.schedule(() -> {
				executeJob(job);
			}, startDate);
		}
		return scheduledFuture;
	}

	private void executeJob(Job job) {
		JobService jobService = ApplicationContextUtil.getBean(JobService.class);
		if (jobService != null) {
			jobService.execute(job);
		}
	}

	private ScheduledFuture<?> startCronJob(Job job) {
		ScheduledFuture<?> scheduledFuture = ct.schedule(() -> {
			ApplicationContextUtil.getBean(JobService.class).execute(job);
		}, new CronTrigger(job.getCronExpression()));
		return scheduledFuture;
	}

	private boolean needExecute(Job job) throws ParseException {
		if (getAllRunningJobs().contains(job.getId())) {
			return false;
		}
		if (job.getMaxExeCount() > 0 && job.getMaxExeCount() <= job.getExeCount()) {
			return false;
		}
		if (job.getStatus().equals(JobStatus.SUSPEND.getName()) || job.getStatus().equals(JobStatus.FINISH.getName())) {
			return false;
		}
		long now = System.currentTimeMillis();
		long start = job.getStartTimeL();
		long end = job.getEndTimeL();
		if (now > end) {
			return false;
		}
		if (now < start) {
			return false;
		}
		return true;
	}

}
