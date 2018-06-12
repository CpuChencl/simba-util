package com.simba.elasticsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.searchbox.client.JestClient;

/**
 * ElasticSearch工具类
 * 
 * @author caozhejun
 *
 */
@Component
public class ElasticSearchUtil {

	@Autowired
	private JestClient jestClient;

	public JestClient getJestClient() {
		return jestClient;
	}

}
