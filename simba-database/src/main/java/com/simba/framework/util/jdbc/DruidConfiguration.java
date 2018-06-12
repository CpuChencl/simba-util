package com.simba.framework.util.jdbc;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * Druid的配置类
 * 
 * @author caozhejun
 *
 */
@Configuration
public class DruidConfiguration {

	private static final Log logger = LogFactory.getLog(DruidConfiguration.class);

	@Value("${allow.druid.ips}")
	private String ips;

	@Bean
	public ServletRegistrationBean druidServlet() {
		logger.info("=====================init Druid Servlet Configuration ");
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
		servletRegistrationBean.setServlet(new StatViewServlet());
		servletRegistrationBean.addUrlMappings("/druid/*");
		Map<String, String> initParameters = new HashMap<String, String>();
		initParameters.put("resetEnable", "false");// 禁用HTML页面上的“Reset All”功能
		initParameters.put("allow", ips); // IP白名单 (没有配置或者为空，则允许所有访问)
		// (存在共同时，deny优先于allow)
		servletRegistrationBean.setInitParameters(initParameters);
		return servletRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		logger.info("=====================init Druid filter Configuration ");
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		return filterRegistrationBean;
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return new DruidDataSource();
	}
}