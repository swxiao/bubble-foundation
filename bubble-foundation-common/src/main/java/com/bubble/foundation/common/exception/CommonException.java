/**
 * Copyright [2015-2017] 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bubble.foundation.common.exception;

import java.io.Serializable;


/**
 * Common操作异常
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2015年1月28日
 */
public class CommonException extends Exception implements Serializable{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3938805407657465096L;

	/**
	 * Common操作异常构造函数
	 * @param message 异常信息
	 * @param cause 异常原因
	 */
	public CommonException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Common操作异常构造函数(重载)
	 * @param message 异常信息
	 */
	public CommonException(String message) {
		super(message);
	}

	/**
	 * Common操作异常构造函数(重载)
	 * @param cause 异常原因
	 */
	public CommonException(Throwable cause) {
		super(cause);
	}
}
