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
package com.bubble.foundation.redis.engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.bubble.foundation.common.utils.StringUtil;
import com.bubble.foundation.redis.datasource.RedisDataSource;
import com.bubble.foundation.redis.serializer.DefaultRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2015年12月28日
 */
@org.springframework.context.annotation.Configuration
@ComponentScan(basePackages = "com.bubble.**.repositories", includeFilters = @ComponentScan.Filter(value = Repository.class, type = FilterType.ANNOTATION))
public class RedisEngine {

	@Autowired
	private RedisDataSource dataSource;

	@Bean
	JedisPoolConfig jedisPoolConfig() throws Exception {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(dataSource.getMaxTotal());
		jedisPoolConfig.setMaxIdle(dataSource.getMaxIdle());
		jedisPoolConfig.setMaxWaitMillis(dataSource.getMaxWaitMillis());
		jedisPoolConfig.setMinIdle(dataSource.getMinIdle());
		jedisPoolConfig.setTestOnBorrow(dataSource.isTestOnBorrow());
		jedisPoolConfig.setTestOnReturn(true);
		jedisPoolConfig.setTestWhileIdle(true);
		return jedisPoolConfig;
	}

	@Bean
	RedisConnectionFactory redisConnectionFactory() throws Exception {
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory(redisSentinelConfiguration(), jedisPoolConfig());
		connectionFactory.setHostName(dataSource.getHostName());
		connectionFactory.setPort(dataSource.getPort());
		connectionFactory.setPassword(dataSource.getPassword());
		connectionFactory.setTimeout(dataSource.getTimeOut());
		connectionFactory.afterPropertiesSet();
		connectionFactory.setUsePool(true);
		connectionFactory.setPoolConfig(jedisPoolConfig());
		return connectionFactory;
	}

	@Bean
	RedisSentinelConfiguration redisSentinelConfiguration() throws Exception {
		if (StringUtil.isEmpty(dataSource.getMasterName()) || dataSource.getSentinelNodes().isEmpty()) {
			return null;// No sentinel model,return null object.
		}
		RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration(dataSource.getMasterName(),
				dataSource.getSentinelNodes());
		return redisSentinelConfiguration;
	}

	@Bean
	RedisTemplate<String, Object> redisTemplate() throws Exception {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(DefaultRedisSerializer.StringSerializer.INSTANCE);
		redisTemplate.setValueSerializer(DefaultRedisSerializer.StringSerializer.INSTANCE);
		redisTemplate.setHashKeySerializer(DefaultRedisSerializer.StringSerializer.INSTANCE);
		redisTemplate.setHashValueSerializer(DefaultRedisSerializer.StringSerializer.INSTANCE);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	/*@Bean
	RedisMessageListenerContainer container(List<MessageWrapper> messageWrappers) throws Exception {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(jedisConnectionFactory());
		container.setTopicSerializer(DefaultRedisSerializer.StringSerializer.INSTANCE);
		for (MessageWrapper wrapper : messageWrappers) {
			MessageListener listener = new MessageListenerAdapter(wrapper.getReceiver(), "receiveMessage");
			List<Topic> topicList = new ArrayList<>();
			for (String name : wrapper.getTopics()) {
				topicList.add(new ChannelTopic(name));
			}
			container.addMessageListener(listener, topicList);
		}
		return container;
	}*/
}
