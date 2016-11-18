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
package com.bubble.foundation.redis.datasource;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;

import com.bubble.foundation.common.utils.StringUtil;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2015年12月28日
 */
public class RedisDataSource {

	@Value("${redis.maxTotal}")
	private int maxTotal; // properties key redis.maxTotal

	@Value("${redis.maxIdle}")
	private int maxIdle; // properties key redis.maxIdle

	@Value("${redis.maxWaitMillis}")
	private int maxWaitMillis; // properties key redis.maxWaitMillis

	@Value("${redis.minIdle}")
	private int minIdle; // properties key redis.minIdle

	@Value("${redis.testOnBorrow}")
	private boolean testOnBorrow; // properties key redis.testOnBorrow

	@Value("${redis.hostName}")
	private String hostName; // properties key redis.hostName

	@Value("${redis.port}")
	private int port; // properties key redis.port

	@Value("${redis.timeOut}")
	private int timeOut; // properties key redis.timeOut(ms)

	@Value("${redis.password}")
	private String password; // properties key redis.password

	@Value("${redis.masterName}")
	private String masterName; // properties key redis.masterName

	@Value("${redis.sentinelNodes}")
	private String sentinels;

	private Set<String> sentinelNodes;

	/**
	 * @return the maxTotal
	 */
	public int getMaxTotal() {
		return maxTotal;
	}

	/**
	 * @param maxTotal
	 *            the maxTotal to set
	 */
	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	/**
	 * @return the maxIdle
	 */
	public int getMaxIdle() {
		return maxIdle;
	}

	/**
	 * @param maxIdle
	 *            the maxIdle to set
	 */
	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	/**
	 * @return the maxWaitMillis
	 */
	public int getMaxWaitMillis() {
		return maxWaitMillis;
	}

	/**
	 * @param maxWaitMillis
	 *            the maxWaitMillis to set
	 */
	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	/**
	 * @return the minIdle
	 */
	public int getMinIdle() {
		return minIdle;
	}

	/**
	 * @param minIdle
	 *            the minIdle to set
	 */
	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	/**
	 * @return the testOnBorrow
	 */
	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	/**
	 * @param testOnBorrow
	 *            the testOnBorrow to set
	 */
	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName
	 *            the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the timeOut
	 */
	public int getTimeOut() {
		return timeOut;
	}

	/**
	 * @param timeOut
	 *            the timeOut to set
	 */
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the masterName
	 */
	public String getMasterName() {
		return masterName;
	}

	/**
	 * @param masterName
	 *            the masterName to set
	 */
	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	/**
	 * @return the sentinelNodes
	 */
	public Set<String> getSentinelNodes() {
		if (sentinelNodes == null || sentinelNodes.isEmpty()) {
			sentinelNodes = new HashSet<String>();
			if (!StringUtil.isEmpty(this.sentinels)) {
				String[] sentinels = this.sentinels.split(",");
				for (String sentinel : sentinels) {
					sentinelNodes.add(sentinel);
				}
			}
		}
		return sentinelNodes;
	}

	/**
	 * @param sentinelNodes
	 *            the sentinelNodes to set
	 */
	public void setSentinelNodes(Set<String> sentinelNodes) {
		this.sentinelNodes = sentinelNodes;
	}

}
