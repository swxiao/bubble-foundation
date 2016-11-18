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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bubble.foundation.common.exception.CommonException;
import com.bubble.foundation.common.utils.BeanUtil;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2016年1月26日
 */
public class ResponseDatagram extends AbstractDatagram {

	public ResponseDatagram() {
		this.setTimestamp(System.currentTimeMillis());
	}

	private static final Logger logger = LoggerFactory.getLogger(ResponseDatagram.class);

	private Integer code;

	private String msg;

	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(Integer code) {
		this.code = code;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public void setKey(String key) {
		super.setKey(key);
	}

	public void setResponseTips(ResponseInfo responseInfo) {
		this.code = responseInfo.getCode();
		this.msg = responseInfo.getMsg();
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
				return BeanUtil.bean2JSON(new ResponseDatagram());
			} catch (CommonException e1) {
				e1.printStackTrace();
				return "";
			}
		}
	}
}
