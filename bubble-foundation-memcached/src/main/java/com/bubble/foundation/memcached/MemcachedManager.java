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
package com.bubble.foundation.memcached;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

/**
 * Memcached 操作类</br> 实现对Memcached缓存CRUD以及状态查询等操作
 * 
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2015年1月28日
 */
public class MemcachedManager implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(MemcachedManager.class);

	@Value("${memcached.initConnNum}")
	private String initConnNum;

	@Value("${memcached.minConnNum}")
	private String minConnNum;

	@Value("${memcached.maxConnNum}")
	private String maxConnNum;

	@Value("${memcached.maintSleepTime}")
	private String maintSleepTime;

	@Value("${memcached.serverList}")
	private String serverList;

	/**
	 * 设置初始连接数
	 * 
	 * @param initConnNum
	 *            初始连接数，参考属性文件
	 */
	public void setInitConnNum(String initConnNum) {
		this.initConnNum = initConnNum;
	}

	/**
	 * 设置最小连接数
	 * 
	 * @param minConnNum
	 *            最小连接数，参考属性文件
	 */
	public void setMinConnNum(String minConnNum) {
		this.minConnNum = minConnNum;
	}

	/**
	 * 设置最大连接数
	 * 
	 * @param maxConnNum
	 *            最大连接数，参考属性文件
	 */
	public void setMaxConnNum(String maxConnNum) {
		this.maxConnNum = maxConnNum;
	}

	/**
	 * 设置超时间隔
	 * 
	 * @param maintSleepTime
	 *            超时间隔，参考属性文件
	 */
	public void setMaintSleepTime(String maintSleepTime) {
		this.maintSleepTime = maintSleepTime;
	}

	/**
	 * 设置缓存服务端地址
	 * 
	 * @param serverList
	 *            服务端地址，参考属性文件
	 */
	public void setServerList(String serverList) {
		this.serverList = serverList;
	}

	SockIOPool sockIOPool;

	MemCachedClient memCachedClient;

	private void initClient() {
		memCachedClient = new MemCachedClient();
	}

	private void initSockIOPool() {
		sockIOPool = SockIOPool.getInstance();
		sockIOPool.setServers(serverList.split(","));
		sockIOPool.setInitConn(Integer.parseInt(initConnNum));
		sockIOPool.setMinConn(Integer.parseInt(minConnNum));
		sockIOPool.setMaxConn(Integer.parseInt(maxConnNum));
		sockIOPool.setMaintSleep(Integer.parseInt(maintSleepTime));
		sockIOPool.setNagle(false);
		sockIOPool.initialize();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		initSockIOPool();
		initClient();
	}

	/**
	 * 获取缓存内容
	 * 
	 * @param key
	 * @return 根据key放入的缓存内容
	 */
	public Object get(String key) {
		Object obj = memCachedClient.get(key);
		if (logger.isDebugEnabled())
			logger.debug("result of exec flush content to cache is {}", obj != null ? "successful" : "failure");
		return obj;
	}

	/**
	 * 根据key将原先的内容置换成最新内容(Key需存在)
	 * 
	 * @param key
	 * @param obj
	 *            缓存内容，要求是可序列化的内容
	 * @return 将内容置入缓存成功结果，成功为true反之false
	 */
	public boolean put(String key, Object obj) {
		if (!(obj instanceof Serializable)) {
			throw new IllegalArgumentException("object must implements Serializable interface.");
		}
		boolean isSuccess = memCachedClient.set(key, obj);
		if (logger.isDebugEnabled())
			logger.debug("result of exec put content to cache is {}", isSuccess ? "successful" : "failure");
		return isSuccess;
	}

	/**
	 * 根据key将原先的内容置换成最新内容(Key需存在)
	 * 
	 * @param key
	 * @param obj
	 *            缓存内容，要求是可序列化的内容
	 * @return 将内容置入缓存成功结果，成功为true反之false
	 */
	public boolean replace(String key, Object obj) {
		if (!(obj instanceof Serializable)) {
			throw new IllegalArgumentException("object must implements Serializable interface.");
		}
		boolean isSuccess = memCachedClient.replace(key, obj);

		if (logger.isDebugEnabled())
			logger.debug("result of exec replace content to cache is {}", isSuccess ? "successful" : "failure");
		return isSuccess;
	}

	/**
	 * 根据key将原先的内容置换成最新内容(Key需存在)
	 * 
	 * @param key
	 * @param obj
	 *            缓存内容，要求是可序列化的内容
	 * @param TTL
	 *            缓存内容失效秒数
	 * @return 将内容置入缓存成功结果，成功为true反之false
	 */
	public boolean replace(String key, Object obj, int TTL) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, TTL);
		if (!(obj instanceof Serializable)) {
			throw new IllegalArgumentException("object must implements Serializable interface.");
		}
		boolean isSuccess = memCachedClient.replace(key, obj, calendar.getTime());
		if (logger.isDebugEnabled())
			logger.debug("result of exec replace content to cache is {}", isSuccess ? "successful" : "failure");
		return isSuccess;
	}

	/**
	 * 根据key将原先置入的内容移除，移除后key和内容一起被清除
	 * 
	 * @param key
	 * @return 将内容从缓存中移除结果，成功为true反之为false
	 */
	public boolean remove(String key) {
		boolean isSuccess = memCachedClient.delete(key);
		if (logger.isDebugEnabled())
			logger.debug("result of exec remove content to cache is {}", isSuccess ? "successful" : "failure");
		return isSuccess;
	}

	/**
	 * 获取缓存服务器当前状态
	 * 
	 * @return 当前缓存服务器的运行状态及运行参数
	 */
	public Map stats() {
		return memCachedClient.stats();
	}

	@Deprecated
	public Map statsItems() {
		return memCachedClient.statsItems();
	}

	/**
	 * 添加缓存(Key需不存在)
	 * 
	 * @param key
	 * @param value
	 *            缓存内容，要求是可序列化的内容
	 * @return 将内容置入缓存成功结果，成功为true反之false
	 */
	public boolean add(String key, Object value) {
		if (!(value instanceof Serializable)) {
			throw new IllegalArgumentException("object must implements Serializable interface.");
		}
		boolean isSuccess = memCachedClient.add(key, value);
		if (logger.isDebugEnabled())
			logger.debug("result of exec add content to cache is {}", isSuccess ? "successful" : "failure");
		return isSuccess;
	}

	/**
	 * 添加指定周期缓存(Key需不存在) <br/>
	 * 指定缓存失效时间已客户端时间为准，服务端时间跟客户端不一致时会出现缓存时效误差
	 * 
	 * @param key
	 * @param value
	 *            缓存内容，要求是可序列化的内容
	 * @param expiry
	 *            缓存内容截止有效期
	 * @return 将内容置入缓存成功结果，成功为true反之false
	 */
	public boolean add(String key, Object value, Date expiry) {
		if (!(value instanceof Serializable)) {
			throw new IllegalArgumentException("object must implements Serializable interface.");
		}
		boolean isSuccess = memCachedClient.add(key, value, expiry);
		if (logger.isDebugEnabled())
			logger.debug("result of exec add content to cache is {}", isSuccess ? "successful" : "failure");
		return isSuccess;
	}

	/**
	 * 添加指定周期缓存(Key需不存在) <br/>
	 * 指定缓存失效时间已客户端时间为准，服务端时间跟客户端不一致时会出现缓存时效误差
	 * 
	 * @param key
	 * @param value
	 *            缓存内容，要求是可序列化的内容
	 * @param TTL
	 *            缓存内容失效秒数
	 * @return 将内容置入缓存成功结果，成功为true反之false
	 */
	public boolean add(String key, Object value, int TTL) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, TTL);
		if (!(value instanceof Serializable)) {
			throw new IllegalArgumentException("object must implements Serializable interface.");
		}
		boolean isSuccess = memCachedClient.add(key, value, calendar.getTime());
		if (logger.isDebugEnabled())
			logger.debug("result of exec add content to cache is {}", isSuccess ? "successful" : "failure");
		return isSuccess;
	}

	/**
	 * 添加指定周期缓存(Key需不存在)
	 * 
	 * @param key
	 * @param value
	 *            缓存内容，要求是可序列化的内容
	 * @param TTL
	 *            缓存内容失效秒数
	 * @param obey
	 *            缓存时间是否遵守服务端时间标准，true为遵守，false为不遵守
	 * @return 将内容置入缓存成功结果，成功为true反之false
	 */
	public boolean add(String key, Object value, int TTL, boolean obey) {
		Calendar calendar = Calendar.getInstance();
		if (obey) {// 遵守服务器时间
			Object[] status = memCachedClient.stats().keySet().toArray();
			if (status.length > 0) {
				long stamp = Long.parseLong(memCachedClient.stats().get(status[0]).get("time").trim()) * 1000;
				calendar.setTimeInMillis(stamp);
			} else {
				logger.warn("the content of memcached server stats is empty, load this server Time");
			}
		}
		calendar.add(Calendar.SECOND, TTL);
		if (!(value instanceof Serializable)) {
			throw new IllegalArgumentException("object must implements Serializable interface.");
		}
		boolean isSuccess = memCachedClient.add(key, value, calendar.getTime());
		if (logger.isDebugEnabled())
			logger.debug("result of exec add content to cache is {}", isSuccess ? "successful" : "failure");
		return isSuccess;
	}

	/**
	 * 刷新服务端缓存
	 * 
	 * @param servers
	 *            缓存服务端格式(地址:端口)
	 * @return 缓存服务端刷新结果，成功为true反之false
	 */
	public boolean flush(String... servers) {
		boolean isSuccess = memCachedClient.flushAll(servers);
		if (logger.isDebugEnabled())
			logger.debug("result of exec flush content to cache is {}", isSuccess ? "successful" : "failure");
		return isSuccess;
	}

	/**
	 * 在当前已有的缓存key追加内容
	 * 
	 * @param key
	 * @param value
	 *            缓存内容，要求是可序列化的内容
	 * @return 将内容追加到缓存成功结果，成功为true反之false
	 */
	public boolean append(String key, Object value) {
		if (!(value instanceof Serializable)) {
			throw new IllegalArgumentException("object must implements Serializable interface.");
		}
		boolean isSuccess = memCachedClient.append(key, value);
		if (logger.isDebugEnabled())
			logger.debug("result of append flush content to cache is {}", isSuccess ? "successful" : "failure");
		return isSuccess;
	}

	/**
	 * 检测缓存是否包含指定Key
	 * 
	 * @param key
	 * @return 是否存在结果，存在为true反之false
	 */
	public boolean containsKey(String key) {
		return memCachedClient.keyExists(key);
	}
}
