package com.simba.job.service;

import java.text.ParseException;
import java.util.List;

import com.simba.framework.util.jdbc.Pager;
import com.simba.job.model.Job;

/**
 * 任务 Service
 * 
 * @author caozj
 * 
 */
public interface JobService {

	/**
	 * 执行
	 * 
	 * @param job
	 */
	void execute(Job job);

	/**
	 * 把所有可以启动的待启动任务启动
	 */
	void startAllWaitingJobs();

	/**
	 * 初始化要执行的任务对象数据
	 */
	void initJobData();

	/**
	 * 启动所有可以启动的任务
	 */
	void initStartJobs();

	/**
	 * 启动任务
	 * 
	 * @param id
	 */
	void startJob(int id) throws ParseException;

	/**
	 * 暂停任务
	 * 
	 * @param id
	 */
	void stopJob(int id) throws ParseException;

	/**
	 * 处理所有已经完成的任务
	 */
	void dealFinishJob();

	void add(Job job) throws ParseException;

	void update(Job job) throws ParseException;

	void delete(int id) throws ParseException;

	List<Job> listAll();

	int count();

	int countBy(String field, Object value);

	List<Job> page(Pager page);

	Job get(int id);

	void batchDelete(List<Integer> idList) throws ParseException;

	Job getBy(String field, Object value);

	Job getByAnd(String field1, Object value1, String field2, Object value2);

	Job getByOr(String field1, Object value1, String field2, Object value2);

	List<Job> listBy(String field, Object value);

	List<Job> listByAnd(String field1, Object value1, String field2, Object value2);

	List<Job> listByOr(String field1, Object value1, String field2, Object value2);

	List<Job> pageBy(String field, Object value, Pager page);

	List<Job> pageByAnd(String field1, Object value1, String field2, Object value2, Pager page);

	List<Job> pageByOr(String field1, Object value1, String field2, Object value2, Pager page);
}
