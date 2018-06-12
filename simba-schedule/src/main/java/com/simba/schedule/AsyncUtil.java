package com.simba.schedule;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.simba.framework.util.common.ReflectUtil;

/**
 * 异步处理工具类
 * 
 * @author caozj
 *
 */
@Component
public class AsyncUtil {

	private static final Log logger = LogFactory.getLog(AsyncUtil.class);

	@Resource
	private TaskExecutor taskExecutor;

	/**
	 * 异步执行
	 * 
	 * @param className
	 *            完整类路径
	 * @param methodName
	 *            方法名
	 * @param args
	 *            参数列表
	 */
	public void execute(String className, String methodName, Object... args) {
		taskExecutor.execute(() -> {
			try {
				logger.info("异步执行开始:" + className + "." + methodName);
				ReflectUtil.invokeObjectMethod(className, methodName, args);
				logger.info("异步执行结束" + className + "." + methodName);
			} catch (Exception e) {
				logger.error("异步执行异常:" + className + "." + methodName, e);
			}
		});
	}

}
