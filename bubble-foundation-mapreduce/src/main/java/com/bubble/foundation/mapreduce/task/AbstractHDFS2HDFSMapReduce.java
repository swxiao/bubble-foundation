/**
 * Copyright [2015-2017]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bubble.foundation.mapreduce.task;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2016年3月18日
 */
public abstract class AbstractHDFS2HDFSMapReduce extends AbstractMapReduce {

	@Override
	protected Job getJobInstance(String input, String output) throws IOException {
		String hdfs = this.getConf().get("fs.defaultFS");
		Job job = Job.getInstance(this.getConf());
		FileSystem fs = FileSystem.get(URI.create(""), this.getConf());
		Path in = new Path(hdfs + input);
		FileStatus[] status = fs.listStatus(in);
		Path[] paths = FileUtil.stat2Paths(status);
		FileInputFormat.setInputPaths(job, paths);
		FileOutputFormat.setOutputPath(job, new Path(hdfs + output));
		return job;
	}

}
