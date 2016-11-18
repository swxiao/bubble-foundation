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
package com.bubble.foundation.common.datagram.code;

import com.bubble.foundation.common.datagram.ResponseInfo;

/**
 * 响应码
 * 
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2016年1月26日
 */
public enum GlobalResponseTips implements ResponseInfo {

	调用成功(0, "调用成功"), 未知错误(999999, "未知错误"), 数据报文错误(900001, "数据报文错误"), 非法请求(900002, "非法请求"), 验证失败(900004, "验证失败");

	private GlobalResponseTips(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private int code;

	private String msg;

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getMsg() {
		return msg;
	}

}
