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
package com.bubble.foundation.hdfs.support;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.bubble.foundation.hdfs.exception.HDFSException;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2016年3月10日
 */
public class HDFSTemplate implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(HDFSTemplate.class);

	private Configuration configuration;

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		// TODO Auto-generated method stub

	}

	public void mkdir(String dir) {
		try {
			FileSystem fileSystem = FileSystem.get(URI.create("/mkdir"), configuration);
			Path path = new Path(dir);
			fileSystem.mkdirs(path);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new HDFSException("创建hdfs目录失败！", e.getCause());
		}
	}

	public RemoteIterator<LocatedFileStatus> listFile(String dir, boolean recursive) {
		try {
			FileSystem fileSystem = FileSystem.get(URI.create("/listFile"), configuration);
			Path path = new Path(dir);
			return fileSystem.listFiles(path, recursive);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw new HDFSException("hdfs目录不存在！", e.getCause());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new HDFSException("查看hdfs目录失败！", e.getCause());
		}
	}

	/**
	 * 删除HDFS文件
	 * 
	 * @param hdfsPath
	 *            hdfs路径
	 * @param recursive
	 *            递归删除
	 * @throws HDFSException
	 */
	public void rm(String hdfsPath, boolean recursive) {
		try {
			FileSystem fileSystem = FileSystem.get(URI.create("/rmdir"), configuration);
			Path path = new Path(hdfsPath);
			fileSystem.delete(path, recursive);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new HDFSException("删除hdfs目录(文件)失败！", e.getCause());
		}
	}

	/**
	 * 上传文件到hdfs
	 * 
	 * @param delSrc
	 *            删除原文件
	 * @param overwrite
	 *            是否覆盖
	 * @param src
	 *            源
	 * @param dst
	 *            目的地
	 * @throws HDFSException
	 */
	public void copyFromLocal(boolean delSrc, boolean overwrite, String src, String dst) {
		try {
			FileSystem fileSystem = FileSystem.get(URI.create("/upload"), configuration);
			Path srcPath = new Path(src);
			Path dstPath = new Path(dst);
			fileSystem.copyFromLocalFile(delSrc, overwrite, srcPath, dstPath);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new HDFSException("上传hdfs失败！", e.getCause());
		}
	}

	/**
	 * HDFS文件复制到本地
	 * 
	 * @param delSrc
	 *            是否删除源文件
	 * @param src
	 *            源
	 * @param dst
	 *            目的
	 * @throws HDFSException
	 */
	public void copyToLocalFile(boolean delSrc, String src, String dst) {
		try {
			FileSystem fileSystem = FileSystem.get(URI.create("/download"), configuration);
			Path srcPath = new Path(src);
			Path dstPath = new Path(dst);
			fileSystem.copyToLocalFile(delSrc, srcPath, dstPath);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new HDFSException("下载hdfs文件失败！", e.getCause());
		}
	}
}
