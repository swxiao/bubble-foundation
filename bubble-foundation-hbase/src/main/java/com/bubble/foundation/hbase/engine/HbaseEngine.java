/**
 * Copyright [2015-2017]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.bubble.foundation.hbase.engine;

import javax.annotation.Resource;

import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.hadoop.hbase.HbaseConfigurationFactoryBean;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

import com.bubble.foundation.hbase.datasource.HbaseDataSource;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2015年12月28日
 */
@org.springframework.context.annotation.Configuration
@ComponentScan(basePackages = "com.bubble.**.repositories", includeFilters = @ComponentScan.Filter(value = Repository.class, type = FilterType.ANNOTATION))
public class HbaseEngine {

	@Resource(name = "hbaseConfiguration") // 默认按指定名称注入
	private Configuration configuration;

	@Autowired
	private HbaseDataSource dataSource;

	@Bean
	HbaseTemplate hbaseTemplate() {
		HbaseConfigurationFactoryBean hbaseConfigurationFactoryBean = new HbaseConfigurationFactoryBean();
		hbaseConfigurationFactoryBean.setConfiguration(configuration);
		hbaseConfigurationFactoryBean.setZkPort(dataSource.getPort());
		hbaseConfigurationFactoryBean.setZkQuorum(dataSource.getZookeeperQuorum());
		hbaseConfigurationFactoryBean.afterPropertiesSet();
		HbaseTemplate hbaseTemplate = new HbaseTemplate(hbaseConfigurationFactoryBean.getObject());
		hbaseTemplate.setEncoding("UTF-8");
		hbaseTemplate.setAutoFlush(true);
		hbaseTemplate.afterPropertiesSet();
		return hbaseTemplate;
	}
}
