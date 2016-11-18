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
package com.bubble.foundation.mapreduce.task;

import java.io.IOException;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bubble.foundation.mapreduce.exception.MapReduceException;

/**
 * mapreduce任务抽象类
 * 
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2016年2月4日
 */
public abstract class AbstractMapReduce extends Configured implements Tool {

	private static final Logger logger = LoggerFactory.getLogger(AbstractMapReduce.class);

	protected abstract Job getJobInstance(String input, String output) throws IOException;

	protected abstract void initConfiguration(Job job) throws MapReduceException;

	@Override
	public int run(String[] args) throws Exception {
		try {
			Job job = getJobInstance(args[0], args[1]);
			initConfiguration(job);
			job.submit();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
		return 1;
	}

}
