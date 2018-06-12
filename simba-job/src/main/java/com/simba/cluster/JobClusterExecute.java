package com.simba.cluster;

import org.springframework.stereotype.Component;

import com.simba.framework.distributed.ClusterExecute;
import com.simba.job.model.JobData;

/**
 * 任务集群传递之后 要执行的方法
 * 
 * @author caozj
 *
 */
@Component
public class JobClusterExecute implements ClusterExecute {

	@Override
	public void execute(Object data) {
		if (!(data instanceof JobClusterData)) {
			throw new RuntimeException("类型错误：" + data.getClass().getCanonicalName());
		}
		JobClusterData clustData = (JobClusterData) data;
		if (clustData.getMethod().equals("add")) {
			JobData.getInstance().add(clustData.getJob());
		} else if (clustData.getMethod().equals("remove")) {
			JobData.getInstance().remove(clustData.getJob());
		}
	}

}
