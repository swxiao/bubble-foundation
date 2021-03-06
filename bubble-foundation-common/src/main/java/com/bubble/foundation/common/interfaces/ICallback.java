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
package com.bubble.foundation.common.interfaces;

import com.bubble.foundation.common.exception.CommonException;

/**
 * 回调函数接口声明
 * 
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2016年7月13日
 */
public interface ICallback {

	/**
	 * 回调函数声明
	 * 
	 * @param objects
	 * @throws CommonException
	 */
	void callback(Object... objects) throws CommonException;

}
