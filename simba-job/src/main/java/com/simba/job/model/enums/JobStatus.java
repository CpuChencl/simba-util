package com.simba.job.model.enums;

/**
 * 任务状态
 * 
 * @author caozj
 *
 */
public enum JobStatus {

  WAITING("waiting", "未启动"),

  RUNNING("running", "运行中"),

  ERROR("error", "运行异常"),

  FINISH("finish", "运行完成"),

  SUSPEND("suspend", "暂停");

  private String name;

  private String description;

  private JobStatus(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }


}
