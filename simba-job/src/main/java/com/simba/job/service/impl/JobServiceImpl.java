package com.simba.job.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simba.cluster.JobClusterData;
import com.simba.cluster.JobClusterExecute;
import com.simba.cluster.JobScheduleClusterExecute;
import com.simba.framework.distributed.ClusterMessage;
import com.simba.framework.distributed.DistributedUtil;
import com.simba.framework.util.common.ReflectUtil;
import com.simba.framework.util.jdbc.Pager;
import com.simba.job.dao.JobDao;
import com.simba.job.model.Job;
import com.simba.job.model.JobData;
import com.simba.job.model.enums.JobStatus;
import com.simba.job.service.JobService;
import com.simba.util.ScheduleUtil;

/**
 * 任务 Service实现类
 * 
 * @author caozj
 * 
 */
@Service
@Transactional
public class JobServiceImpl implements JobService {

	private static final Log logger = LogFactory.getLog(JobServiceImpl.class);

	@Autowired
	private JobDao jobDao;

	@Autowired
	private DistributedUtil distributedUtil;

	@PostConstruct
	private void init() {
		this.dealFinishJob();
		this.initJobData();
		this.initStartJobs();
	}

	@Override
	public void add(Job job) throws ParseException {
		checkJob(job);
		long now = System.currentTimeMillis();
		if (now > job.getEndTimeL()) {
			throw new RuntimeException("任务的结束执行时间不能早于当前时间");
		}
		job.setStatus(JobStatus.WAITING.getName());
		jobDao.add(job);
		executeJobDataCluster(new JobClusterData(job, "add"));
	}

	@Override
	public void update(Job job) throws ParseException {
		checkJob(job);
		Job oldJob = this.get(job.getId());
		job.setStatus(oldJob.getStatus());
		job.setExeCount(oldJob.getExeCount());
		long now = System.currentTimeMillis();
		if (job.getStatus().equals(JobStatus.SUSPEND.getName())) {
			if (now > job.getEndTimeL() || (job.getMaxExeCount() > 0 && job.getMaxExeCount() <= job.getExeCount())) {
				job.setStatus(JobStatus.FINISH.getName());
			} else {
				job.setStatus(JobStatus.SUSPEND.getName());
			}
		} else if (job.getStatus().equals(JobStatus.FINISH.getName())) {
			if ((job.getMaxExeCount() < 1 || job.getMaxExeCount() > job.getExeCount()) && (now <= job.getEndTimeL())) {
				job.setStatus(JobStatus.WAITING.getName());
				executeJobDataCluster(new JobClusterData(job, "add"));
			} else {
				job.setStatus(JobStatus.FINISH.getName());
			}
		} else {
			if ((job.getMaxExeCount() > 1 && job.getMaxExeCount() <= job.getExeCount()) || now > job.getEndTimeL()) {
				job.setStatus(JobStatus.FINISH.getName());
				executeJobDataCluster(new JobClusterData(job, "remove"));
				executeJobScheduleCluster(new JobClusterData(job, "remove"));
			} else {
				job.setStatus(JobStatus.WAITING.getName());
				executeJobDataCluster(new JobClusterData(job, "remove"));
				executeJobScheduleCluster(new JobClusterData(job, "remove"));
				executeJobDataCluster(new JobClusterData(job, "add"));
			}
		}
		jobDao.updateWithoutExeCount(job);
	}

	@Override
	public void execute(Job job) {
		long end = job.getEndTimeL();
		if (!checkExecuteJob(job, end)) {
			return;
		}
		boolean updateStatus = false;
		try {
			if (job.getStatus().equals(JobStatus.WAITING.getName())) {
				job.setStatus(JobStatus.RUNNING.getName());
				updateStatus = true;
			}
			ReflectUtil.invokeObjectMethod(job.getClassName(), job.getMethodName());
		} catch (Exception e) {
			logger.info("执行任务异常:" + job.toString(), e);
			job.setStatus(JobStatus.ERROR.getName());
			updateStatus = true;
		}
		job.setExeCount(job.getExeCount() + 1);
		jobDao.incrExeCount(job.getId());
		if (job.getExeCount() == job.getMaxExeCount() || System.currentTimeMillis() >= end) {
			deleteFinishJob(job);
		} else if (updateStatus) {
			jobDao.updateStatus(job.getId(), job.getStatus());
		}
	}

	@Override
	public void startAllWaitingJobs() {
		List<Job> allJobs = JobData.getInstance().getAll();
		startJobs(allJobs);
	}

	@Override
	public void initJobData() {
		List<Job> list = this.listBy("status", JobStatus.WAITING.getName());
		JobData.getInstance().add(list);
	}

	@Override
	public void initStartJobs() {
		List<Job> allJobs = this.listAll();
		startJobs(allJobs);
	}

	@Override
	public void dealFinishJob() {
		List<Job> allJobs = this.listAll();
		allJobs.forEach((job) -> {
			if (!job.getStatus().equals(JobStatus.FINISH.getName())) {
				if (job.getMaxExeCount() > 0 && job.getMaxExeCount() <= job.getExeCount()) {
					job.setStatus(JobStatus.FINISH.getName());
				} else if (System.currentTimeMillis() > job.getEndTimeL()) {
					job.setStatus(JobStatus.FINISH.getName());
				}
				if (job.getStatus().equals(JobStatus.FINISH.getName())) {
					jobDao.updateStatus(job.getId(), job.getStatus());
				}
			}
		});
	}

	private void startJobs(List<Job> allJobs) {
		List<Job> successJobs = new ArrayList<>(allJobs.size());
		allJobs.forEach((job) -> {
			try {
				if (ScheduleUtil.getInstance().addJob(job)) {
					successJobs.add(job);
				}
			} catch (Exception e) {
				logger.error("启动任务异常:" + job.toString(), e);
			}
		});
		successJobs.forEach((job) -> {
			JobData.getInstance().remove(job);
		});
	}

	@Override
	public void startJob(int id) throws ParseException {
		Job job = this.get(id);
		if (!job.getStatus().equals(JobStatus.SUSPEND.getName())) {
			throw new RuntimeException("任务状态不是暂停，不需要重新启动");
		}
		if (job.getMaxExeCount() > 0 && job.getMaxExeCount() <= job.getExeCount()) {
			throw new RuntimeException("任务已经达到最大执行次数");
		}
		if (job.getEndTimeL() < System.currentTimeMillis()) {
			throw new RuntimeException("任务已经结束,不能启动");
		}
		job.setStatus(JobStatus.WAITING.getName());
		jobDao.updateStatus(job.getId(), job.getStatus());
		executeJobDataCluster(new JobClusterData(job, "add"));
	}

	@Override
	public void stopJob(int id) throws ParseException {
		Job job = this.get(id);
		if (job.getStatus().equals(JobStatus.FINISH.getName()) || job.getStatus().equals(JobStatus.SUSPEND.getName())) {
			throw new RuntimeException("任务不能暂停");
		}
		job.setStatus(JobStatus.SUSPEND.getName());
		jobDao.updateStatus(job.getId(), job.getStatus());
		executeJobDataCluster(new JobClusterData(job, "remove"));
		executeJobScheduleCluster(new JobClusterData(job, "remove"));
	}

	/**
	 * 检查任务是否填写正确
	 * 
	 * @param job
	 */
	private void checkJob(Job job) {
		if (StringUtils.isEmpty(job.getName())) {
			throw new RuntimeException("任务名称不能为空");
		}
		if (StringUtils.isEmpty(job.getCronExpression()) && job.getIntervalTime() < 1) {
			throw new RuntimeException("间隔时间和cron表达式不能同时为空");
		}
	}

	@Override
	public void delete(int id) throws ParseException {
		jobDao.delete(id);
		executeJobDataCluster(new JobClusterData(id, "remove"));
		executeJobScheduleCluster(new JobClusterData(id, "remove"));
	}

	@Override
	@Transactional(readOnly = true)
	public Job get(int id) {
		return jobDao.get(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Job> page(Pager page) {
		return jobDao.page(page);
	}

	@Override
	@Transactional(readOnly = true)
	public int count() {
		return jobDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public int countBy(String field, Object value) {
		return jobDao.countBy(field, value);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Job> listAll() {
		return jobDao.listAll();
	}

	@Override
	public void batchDelete(List<Integer> idList) throws ParseException {
		for (Integer id : idList) {
			this.delete(id);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Job getBy(String field, Object value) {
		return jobDao.getBy(field, value);
	}

	@Override
	@Transactional(readOnly = true)
	public Job getByAnd(String field1, Object value1, String field2, Object value2) {
		return jobDao.getByAnd(field1, value1, field2, value2);
	}

	@Override
	@Transactional(readOnly = true)
	public Job getByOr(String field1, Object value1, String field2, Object value2) {
		return jobDao.getByOr(field1, value1, field2, value2);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Job> listBy(String field, Object value) {
		return jobDao.listBy(field, value);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Job> listByAnd(String field1, Object value1, String field2, Object value2) {
		return jobDao.listByAnd(field1, value1, field2, value2);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Job> listByOr(String field1, Object value1, String field2, Object value2) {
		return jobDao.listByOr(field1, value1, field2, value2);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Job> pageBy(String field, Object value, Pager page) {
		return jobDao.pageBy(field, value, page);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Job> pageByAnd(String field1, Object value1, String field2, Object value2, Pager page) {
		return jobDao.pageByAnd(field1, value1, field2, value2, page);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Job> pageByOr(String field1, Object value1, String field2, Object value2, Pager page) {
		return jobDao.pageByOr(field1, value1, field2, value2, page);
	}

	/**
	 * 检查任务是否能被执行
	 * 
	 * @param job
	 * @param end
	 * @return
	 */
	private boolean checkExecuteJob(Job job, long end) {
		if (System.currentTimeMillis() > end || job.getStatus().equals(JobStatus.FINISH.getName())) {
			deleteFinishJob(job);
			return false;
		}
		if (job.getStatus().equals(JobStatus.SUSPEND.getName())) {
			throw new RuntimeException("不能执行任务:" + job.toString());
		}
		if (job.getMaxExeCount() > 0) {
			job = this.get(job.getId());
		}
		if (job.getMaxExeCount() > 0 && job.getExeCount() >= job.getMaxExeCount()) {
			logger.info("已经达到最大执行次数:" + job.toString());
			deleteFinishJob(job);
			return false;
		}
		return true;
	}

	/**
	 * 删除已经执行完成的任务
	 * 
	 * @param job
	 */
	private void deleteFinishJob(Job job) {
		try {
			executeJobScheduleCluster(new JobClusterData(job, "remove"));
		} catch (Exception e) {
			logger.error("任务执行完成,删除定时任务异常", e);
		}
		if (!job.getStatus().equals(JobStatus.FINISH.getName())) {
			job.setStatus(JobStatus.FINISH.getName());
			jobDao.updateStatus(job.getId(), job.getStatus());
		}
	}

	/**
	 * 执行任务的集群处理
	 * 
	 * @param clustData
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	private void executeJobScheduleCluster(JobClusterData clustData) throws ParseException {
		distributedUtil
				.executeInCluster(new ClusterMessage(JobScheduleClusterExecute.class.getCanonicalName(), clustData));
	}

	/**
	 * 任务数据的集群处理
	 * 
	 * @param clustData
	 */
	private void executeJobDataCluster(JobClusterData clustData) {
		distributedUtil.executeInCluster(new ClusterMessage(JobClusterExecute.class.getCanonicalName(), clustData));
	}

}
