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
package com.bubble.foundation.hdfs.engine;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.hadoop.conf.Configuration;
import org.springframework.context.annotation.Bean;

import com.bubble.foundation.hdfs.support.HDFSTemplate;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2015年12月28日
 */
public class HDFSEngine {

	@Resource(name="hadoopConfiguration")
	private Configuration configuration;

	@Bean
	HDFSTemplate HDFSTemplate() throws IOException {
		HDFSTemplate hdfsTemplate = new HDFSTemplate();
		hdfsTemplate.setConfiguration(configuration);
		hdfsTemplate.afterPropertiesSet();
		return hdfsTemplate;
	}
}
