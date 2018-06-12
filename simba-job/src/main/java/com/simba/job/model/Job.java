package com.simba.job.model;

import java.io.Serializable;
import java.text.ParseException;

import org.apache.commons.lang.StringUtils;

import com.simba.annotation.DescAnnotation;
import com.simba.model.constant.ConstantData;

/**
 * 定时任务对象
 * 
 * @author caozj
 *
 */
@DescAnnotation(desc = "任务")
public class Job implements Serializable {

	private static final long serialVersionUID = -1355826321809119668L;

	private Integer id;

	/**
	 * 名称
	 */
	@DescAnnotation(desc = "名称")
	private String name;

	/**
	 * 描述
	 */
	@DescAnnotation(desc = "描述")
	private String description;

	/**
	 * cron表达式
	 */
	@DescAnnotation(desc = "cron表达式")
	private String cronExpression;

	/**
	 * 开始执行时间
	 */
	@DescAnnotation(desc = "开始执行时间")
	private String startTime;

	/**
	 * 结束执行时间
	 */
	@DescAnnotation(desc = "结束执行时间")
	private String endTime;

	/**
	 * 执行次数
	 */
	@DescAnnotation(desc = "执行次数")
	private Integer exeCount = 0;

	/**
	 * 最大执行次数
	 */
	@DescAnnotation(desc = "最大执行次数")
	private Integer maxExeCount = 0;

	/**
	 * 状态
	 */
	@DescAnnotation(desc = "状态")
	private String status;

	/**
	 * 完整类路径
	 */
	@DescAnnotation(desc = "完整类路径")
	private String className;

	/**
	 * 执行类方法名
	 */
	@DescAnnotation(desc = "执行类方法名")
	private String methodName;

	/**
	 * 延迟时间
	 */
	@DescAnnotation(desc = "延迟时间")
	private Integer delayTime = 0;

	/**
	 * 间隔时间
	 */
	@DescAnnotation(desc = "间隔时间")
	private Integer intervalTime = 0;

	/**
	 * 以下是扩展属性，不保存到数据库，只是临时使用
	 */
	private Long startTimeL;

	private Long endTimeL;

	/**
	 * 获取任务的结束执行时间
	 * 
	 * @param job
	 * @return
	 */
	public long getEndTimeL() {
		if (endTimeL != null) {
			return endTimeL;
		}
		endTimeL = Long.MAX_VALUE;
		String endTime = this.getEndTime();
		if (StringUtils.isNotEmpty(endTime)) {
			try {
				endTimeL = ConstantData.format.parse(endTime).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return endTimeL;
	}

	/**
	 * 获取任务的开始执行时间
	 * 
	 * @param job
	 * @return
	 */
	public long getStartTimeL() {
		if (startTimeL != null) {
			return startTimeL;
		}
		startTimeL = 0L;
		String startTime = this.getStartTime();
		if (StringUtils.isNotEmpty(startTime)) {
			try {
				startTimeL = ConstantData.format.parse(startTime).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return startTimeL;
	}

	public Integer getDelayTime() {
		return delayTime == null ? 0 : delayTime;
	}

	public void setDelayTime(Integer delayTime) {
		this.delayTime = delayTime;
	}

	public Integer getIntervalTime() {
		return intervalTime == null ? 0 : intervalTime;
	}

	public void setIntervalTime(Integer intervalTime) {
		this.intervalTime = intervalTime;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getExeCount() {
		return exeCount == null ? 0 : exeCount;
	}

	public void setExeCount(Integer exeCount) {
		this.exeCount = exeCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getMaxExeCount() {
		return maxExeCount == null ? 0 : maxExeCount;
	}

	public void setMaxExeCount(Integer maxExeCount) {
		this.maxExeCount = maxExeCount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Job [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", description=");
		builder.append(description);
		builder.append(", cronExpression=");
		builder.append(cronExpression);
		builder.append(", startTime=");
		builder.append(startTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append(", exeCount=");
		builder.append(exeCount);
		builder.append(", maxExeCount=");
		builder.append(maxExeCount);
		builder.append(", status=");
		builder.append(status);
		builder.append(", className=");
		builder.append(className);
		builder.append(", methodName=");
		builder.append(methodName);
		builder.append(", delayTime=");
		builder.append(delayTime);
		builder.append(", intervalTime=");
		builder.append(intervalTime);
		builder.append("]");
		return builder.toString();
	}

}
