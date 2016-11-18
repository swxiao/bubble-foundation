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

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.data.repository.query.RepositoryQuery;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2015年2月4日
 */
public class LocalQueryLookupStrategy {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private LocalQueryLookupStrategy() {
	}

	/**
	 * @author Vincent xiao<xiaosw@msn.cn>
	 * @since 2015年2月4日
	 */
	private static class DeclaredQueryLookupStrategy implements QueryLookupStrategy {

		private final SqlSessionTemplate sqlSessionTemplate;

		// TODO
		private final EvaluationContextProvider evaluationContextProvider;

		public DeclaredQueryLookupStrategy(SqlSessionTemplate sqlSessionTemplate, EvaluationContextProvider evaluationContextProvider) {
			this.sqlSessionTemplate = sqlSessionTemplate;
			this.evaluationContextProvider = evaluationContextProvider;
		}

		@Override
		public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, NamedQueries namedQueries) {
			return resolveQuery(new LocalQueryMethod(method, metadata), sqlSessionTemplate, namedQueries);
		}

		protected RepositoryQuery resolveQuery(LocalQueryMethod method, SqlSessionTemplate sqlSessionTemplate, NamedQueries namedQueries) {
			return new LocalRepositoryQuery(method, sqlSessionTemplate);
		}
	}

	/**
	 * @param sqlSessionTemplate
	 * @param key
	 * @return
	 */
	public static QueryLookupStrategy create(SqlSessionTemplate sqlSessionTemplate, Key key) {
		if (key == null) {
			return new DeclaredQueryLookupStrategy(sqlSessionTemplate, null);
		}
		if (Key.USE_DECLARED_QUERY.equals(key)) {
			return new DeclaredQueryLookupStrategy(sqlSessionTemplate, null);
		} else {
			throw new IllegalArgumentException(String.format("Unsupported query lookup strategy %s!", key));
		}
	}

	/**
	 * @param sqlSessionTemplate
	 * @param key
	 * @param evaluationContextProvider
	 * @return
	 */
	public static QueryLookupStrategy create(SqlSessionTemplate sqlSessionTemplate, Key key,
			EvaluationContextProvider evaluationContextProvider) {
		if (key == null) {
			return new DeclaredQueryLookupStrategy(sqlSessionTemplate, evaluationContextProvider);
		}
		if (Key.USE_DECLARED_QUERY.equals(key)) {
			return new DeclaredQueryLookupStrategy(sqlSessionTemplate, evaluationContextProvider);
		} else {
			throw new IllegalArgumentException(String.format("Unsupported query lookup strategy %s!", key));
		}
	}

}
