package com.simba.job.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.simba.job.service.JobService;

/**
 * 启动定时任务
 * 
 * @author caozj
 *
 */
@Component
public class StartJob {

  @Autowired
  private JobService jobService;

  @Scheduled(fixedRate = 1000, initialDelay = 60000)
  private void initJobs() {
    jobService.startAllWaitingJobs();
  }

}
