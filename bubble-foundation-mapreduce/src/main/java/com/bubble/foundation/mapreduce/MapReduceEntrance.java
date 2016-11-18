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
package com.bubble.foundation.mapreduce;

import javax.annotation.Resource;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bubble.foundation.mapreduce.exception.MapReduceException;
import com.bubble.foundation.mapreduce.task.AbstractMapReduce;

/**
 * Mapreduce启动类
 * 
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2016年2月4日
 */
public class MapReduceEntrance {

	private static final Logger logger = LoggerFactory.getLogger(MapReduceEntrance.class);

	@Resource(name = "mapreduceConfiguration")
	Configuration configuration;

	/**
	 *  运行mapreduce
	 * @param clz mapreduce class
	 * @param input
	 * @param output
	 * @return
	 * @throws Exception
	 */
	public int runTask(String clz, String input, String output) throws Exception {
		try {
			String[] args = new String[2];
			args[0] = input;
			args[1] = output;
			Object obj = Class.forName(clz).newInstance();
			if(obj instanceof AbstractMapReduce){
				AbstractMapReduce job = (AbstractMapReduce)obj;
				logger.info("Starting job ........");
				return ToolRunner.run(configuration, job, args);
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
			logger.info("Start job failure.");
		} catch (MapReduceException e) {
			logger.error(e.getMessage(), e);
		}
		return 1;
	}
}
