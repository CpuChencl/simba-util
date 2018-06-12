package com.simba.job.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.simba.job.model.Job;

/**
 * 待启动任务数据
 * 
 * @author caozj
 *
 */
public class JobData {

  private List<Job> list = null;

  private JobData() {
    list = new ArrayList<>();
  }

  private static final class JobDataHolder {
    private static final JobData instance = new JobData();
  }

  public static JobData getInstance() {
    return JobDataHolder.instance;
  }

  public void add(Job job) {
    list.add(job);
  }

  public void add(List<Job> jobList) {
    list.addAll(jobList);
  }

  public void remove(Job job) {
    for (Iterator<Job> iterator = list.iterator(); iterator.hasNext();) {
      Job j = (Job) iterator.next();
      if (job.getId() == j.getId()) {
        iterator.remove();
      }
    }
  }

  public List<Job> getAll() {
    return this.list;
  }
}
