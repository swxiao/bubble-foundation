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
package com.bubble.foundation.common.datagram;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bubble.foundation.common.exception.CommonException;
import com.bubble.foundation.common.utils.BeanUtil;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2016年1月26日
 */
public class RequestDatagram extends AbstractDatagram {

	private static final Logger logger = LoggerFactory.getLogger(ResponseDatagram.class);

	public RequestDatagram() {
	}

	public RequestDatagram initDatagram() {
		this.setKey(UUID.randomUUID().toString());
		this.setTimestamp(System.currentTimeMillis());
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		try {
			return BeanUtil.bean2JSON(this);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			try {
				return BeanUtil.bean2JSON(new RequestDatagram());
			} catch (CommonException e1) {
				e1.printStackTrace();
				return "";
			}
		}
	}

}
