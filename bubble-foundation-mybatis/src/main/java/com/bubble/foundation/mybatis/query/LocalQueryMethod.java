/**
 * Copyright 2015-2017 
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
package com.bubble.foundation.mybatis.query;

import java.lang.reflect.Method;

import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.util.Assert;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2015年2月4日
 */
public class LocalQueryMethod extends QueryMethod {
	
	private Class<?> mapperInterface;
	private final Method method;

	public LocalQueryMethod(Method method, RepositoryMetadata metadata) {
		super(method, metadata);
		Assert.notNull(method, "Method must not be null!");
		Assert.notNull(metadata, "RepositoryMetadata must not be null!");
		this.method = method;
		mapperInterface = metadata.getRepositoryInterface();
	}
	
	public String getMappedStatementId() {
		return mapperInterface.getName() + "." + method.getName();
	}

	public Class<?> getRepositoryInterface() {
		return mapperInterface;
	}
	
	public Method getMethod() {
		return method;
	}
	
	public String getNamedQueryName() {
		return super.getNamedQueryName();
	}
}
