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
package com.bubble.foundation.redis.mq;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import com.bubble.foundation.redis.mq.listener.RedisMQListener;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2016年4月13日
 */
public class RedisQueue<T> implements InitializingBean, DisposableBean,ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	private RedisMQListener<T> listener;// 异步回调

	/**
	 * @param listener
	 *            the listener to set
	 */
	public void setListener(RedisMQListener<T> listener) {
		this.listener = listener;
	}

	private String queueName;

	/**
	 * @param queueName
	 *            the queueName to set
	 */
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	private byte[] rawKey;

	private RedisConnection connection;// for blocking

	private BoundListOperations<String, T> listOperations;// noblocking

	private Lock lock = new ReentrantLock();// 基于底层IO阻塞考虑

	private Thread listenerThread;

	private boolean isClosed;

	@Override
	public void afterPropertiesSet() throws Exception {
		connection = RedisConnectionUtils.getConnection(redisConnectionFactory);
		rawKey = redisTemplate.getKeySerializer().serialize(queueName);
		listOperations = redisTemplate.boundListOps(queueName);
	}

	/**
	 * blocking remove and get last item from queue:BRPOP
	 * 
	 * @return
	 */
	public T takeFromTail(int timeout) throws InterruptedException {
		lock.lockInterruptibly();
		try {
			List<byte[]> results = connection.bRPop(timeout, rawKey);
			if (CollectionUtils.isEmpty(results)) {
				return null;
			}
			return (T) redisTemplate.getValueSerializer().deserialize(results.get(1));
		} finally {
			lock.unlock();
		}
	}

	public T takeFromTail() throws InterruptedException {
		return takeFromTail(0);
	}

	/**
	 * 从队列的头，插入
	 */
	public void pushFromHead(T value) {
		listOperations.leftPush(value);
	}

	public void pushFromTail(T value) {
		listOperations.rightPush(value);
	}

	/**
	 * noblocking
	 * 
	 * @return null if no item in queue
	 */
	public T removeFromHead() {
		return listOperations.leftPop();
	}

	public T removeFromTail() {
		return listOperations.rightPop();
	}

	/**
	 * blocking remove and get first item from queue:BLPOP
	 * 
	 * @return
	 */
	public T takeFromHead(int timeout) throws InterruptedException {
		lock.lockInterruptibly();
		try {
			List<byte[]> results = connection.bLPop(timeout, rawKey);
			if (CollectionUtils.isEmpty(results)) {
				return null;
			}
			return (T) redisTemplate.getValueSerializer().deserialize(results.get(1));
		} finally {
			lock.unlock();
		}
	}

	public T takeFromHead() throws InterruptedException {
		return takeFromHead(0);
	}

	@Override
	public void destroy() throws Exception {
		if (isClosed) {
			return;
		}
		shutdown();
		RedisConnectionUtils.releaseConnection(connection, redisConnectionFactory);
	}

	private void shutdown() {
		try {
			listenerThread.interrupt();
		} catch (Exception e) {
			//
		}
	}

	class ListenerThread extends Thread {

		@Override
		public void run() {
			try {
				while (true) {
					T value = takeFromHead();// cast exception? you should check.
					// 逐个执行
					if (value != null) {
						try {
							listener.onMessage(value);
						} catch (Exception e) {
							//
						}
					}
				}
			} catch (InterruptedException e) {
				//
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (listener != null) {
			listenerThread = new ListenerThread();
			listenerThread.setDaemon(true);
			listenerThread.start();
		}
		
	}

}
