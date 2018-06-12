package com.simba.cluster;

import java.io.Serializable;

import com.simba.job.model.Job;

/**
 * 任务在集群中传递的对象
 * 
 * @author caozj
 *
 */
public class JobClusterData implements Serializable {

  private static final long serialVersionUID = 2687616734474020143L;

  /**
   * 执行的方法,add | remove
   */
  private String method;

  private Job job;

  public JobClusterData() {

  }

  public JobClusterData(int id, String method) {
    Job j = new Job();
    j.setId(id);
    this.job = j;
    this.method = method;
  }

  public JobClusterData(Job job, String method) {
    this.job = job;
    this.method = method;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public Job getJob() {
    return job;
  }

  public void setJob(Job job) {
    this.job = job;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("JobClusterData [method=");
    builder.append(method);
    builder.append(", job=");
    builder.append(job);
    builder.append("]");
    return builder.toString();
  }



}
