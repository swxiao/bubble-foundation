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
package com.bubble.foundation.redis.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2015年12月28日
 */
public class DefaultRedisSerializer {

	public static enum StringSerializer implements RedisSerializer<String> {
		INSTANCE;

		@Override
		public byte[] serialize(String s) throws SerializationException {
			return (null != s ? s.getBytes() : new byte[0]);
		}

		@Override
		public String deserialize(byte[] bytes) throws SerializationException {
			if (bytes.length > 0) {
				return new String(bytes);
			} else {
				return null;
			}
		}
	}

	public static enum LongSerializer implements RedisSerializer<Long> {
		INSTANCE;

		@Override
		public byte[] serialize(Long aLong) throws SerializationException {
			if (null != aLong) {
				return aLong.toString().getBytes();
			} else {
				return new byte[0];
			}
		}

		@Override
		public Long deserialize(byte[] bytes) throws SerializationException {
			if (bytes.length > 0) {
				return Long.parseLong(new String(bytes));
			} else {
				return null;
			}
		}
	}

	public static enum IntSerializer implements RedisSerializer<Integer> {
		INSTANCE;

		@Override
		public byte[] serialize(Integer i) throws SerializationException {
			if (null != i) {
				return i.toString().getBytes();
			} else {
				return new byte[0];
			}
		}

		@Override
		public Integer deserialize(byte[] bytes) throws SerializationException {
			if (bytes.length > 0) {
				return Integer.parseInt(new String(bytes));
			} else {
				return null;
			}
		}
	}
}
