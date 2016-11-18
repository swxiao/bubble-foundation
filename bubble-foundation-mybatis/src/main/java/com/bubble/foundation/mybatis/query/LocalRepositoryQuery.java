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

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2015年2月4日
 */
public class LocalRepositoryQuery implements RepositoryQuery {
	
	/**
	 * queryMethod
	 */
	private final LocalQueryMethod queryMethod;
	
	/**
	 * sqlSessionTemplate
	 */
	private final SqlSessionTemplate sqlSessionTemplate;	
	
	/**
	 * @param queryMethod
	 * @param sqlSessionTemplate
	 */
	public LocalRepositoryQuery(LocalQueryMethod queryMethod, SqlSessionTemplate sqlSessionTemplate) {
		Assert.notNull(queryMethod,"QueryMethod must not be null!");
		Assert.notNull(sqlSessionTemplate,"SqlSessionTemplate must not be null!");
		this.queryMethod = queryMethod;
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Override
	public Object execute(Object[] parameters) {
		return ReflectionUtils
				.invokeMethod(queryMethod.getMethod(), sqlSessionTemplate.getMapper(queryMethod.getRepositoryInterface()), parameters);
	}

	@Override
	public QueryMethod getQueryMethod() {		
		return queryMethod;
	}
}

