package com.simba.cluster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.simba.framework.distributed.ClusterExecute;
import com.simba.util.ScheduleUtil;

/**
 * 任务(启动和停止)集群传递之后 要执行的方法
 * 
 * @author caozj
 *
 */
@Component
public class JobScheduleClusterExecute implements ClusterExecute {

	private static final Log logger = LogFactory.getLog(JobScheduleClusterExecute.class);

	@Override
	public void execute(Object data) {
		if (!(data instanceof JobClusterData)) {
			throw new RuntimeException("类型错误：" + data.getClass().getCanonicalName());
		}
		JobClusterData clustData = (JobClusterData) data;
		try {
			if (clustData.getMethod().equals("add")) {
				ScheduleUtil.getInstance().addJob(clustData.getJob());
			} else if (clustData.getMethod().equals("remove")) {
				ScheduleUtil.getInstance().deleteJob(clustData.getJob());
			}
		} catch (Exception e) {
			logger.error("执行任务集群方法异常：" + clustData.getJob().toString(), e);
		}
	}

}
