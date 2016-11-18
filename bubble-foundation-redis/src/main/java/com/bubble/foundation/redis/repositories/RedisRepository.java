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
package com.bubble.foundation.redis.repositories;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2016年1月28日
 */
@Repository
public class RedisRepository<T> {

	private static final Logger logger = LoggerFactory.getLogger(RedisRepository.class);

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	public void save(String key, T entity) {
		redisTemplate.opsForValue().set(key, entity);
	}

	public void saveList(String key, List<T> entities) {
		long count = redisTemplate.opsForList().leftPushAll(key, entities);
		if (logger.isDebugEnabled()) {
			logger.debug("save list count: {}", count);
		}
	}

	public void saveBatch(Map<String, T> map) {
		for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			redisTemplate.opsForValue().set(key, map.get(key));
		}
	}

	public void saveHash(String key, String sonKey, T hash) {
		redisTemplate.opsForHash().put(key, sonKey, hash);
	}

	public T get(String key) {
		return (T) redisTemplate.opsForValue().get(key);
	}

	public List<T> get(List<String> keys) {
		List<T> resultList = new ArrayList<T>();
		for (String key : keys) {
			resultList.add((T) redisTemplate.opsForValue().get(key));
		}
		return resultList;
	}

	public List<T> getAll(String key) {
		return (List) redisTemplate.opsForList().range(key, 0, -1);
	}

	public void delete(String key) {
		redisTemplate.delete(key);
	}

	public void delete(List<String> keys) {
		redisTemplate.delete(keys);
	}

	public void afterPropertiesSet() throws Exception {
		if (logger.isDebugEnabled())
			logger.debug("RedisTemplate : {}", redisTemplate);
	}

}
