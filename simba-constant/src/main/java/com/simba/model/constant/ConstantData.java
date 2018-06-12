package com.simba.model.constant;

import java.text.SimpleDateFormat;

/**
 * 常量类
 * 
 * @author caozj
 * 
 */
public interface ConstantData {

	/**
	 * 树的根节点ID
	 */
	int TREE_ROOT_ID = -1;

	/**
	 * 默认的编码集
	 */
	String DEFAULT_CHARSET = "UTF-8";

	/**
	 * session过期时间
	 */
	int SESSION_TIMEOUT = 30 * 60 * 1000;

	/**
	 * redis连接超时时间
	 */
	int REDIS_TOMEOUT = 30 * 1000;

	/**
	 * session的cookie
	 */
	String SESSIONID_COOKIE_NAME = "SESSIONID";

	/**
	 * 默认的时间格式
	 */
	String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 流程的发起人变量名
	 */
	String START_USERNAME = "startUserName";

	/**
	 * 流程的标题变量名
	 */
	String TITLE = "title";

	/**
	 * 自定义的freemarker流程表单引擎
	 */
	String FREEMARKER_FORMENGINE = "freemarker";

	/**
	 * 定时器使用的线程池数
	 */
	int SCHEDULE_POLL_SIZE = 100;

	/**
	 * 时间格式化
	 */
	SimpleDateFormat format = new SimpleDateFormat(ConstantData.TIME_FORMAT);

	/**
	 * 手机会话session保存标识
	 */
	String MOBILE_SESSION_FLAG_NAME = "mobile_session_flag";

	/**
	 * 手机会话session状态值
	 */
	String MOBILE_SESSION_TOKEN_NAME = "session_token";
}
