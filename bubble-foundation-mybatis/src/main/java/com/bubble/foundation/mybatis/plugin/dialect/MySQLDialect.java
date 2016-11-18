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
package com.bubble.foundation.mybatis.plugin.dialect;

import com.bubble.foundation.mybatis.plugin.DataBaseDialect;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2015年2月2日
 */
public class MySQLDialect extends DataBaseDialect {

	@Override
	public String getPageSelectSQL(String sql, int offset, int limit) {
		sql = trim(sql);
		StringBuffer sb = new StringBuffer(sql.length() + 20);
		sb.append(sql);
		if (offset > 0)
			sb.append(" limit ").append(offset).append(',').append(limit).append(";");
		else {
			sb.append(" limit ").append(limit).append(";");
		}
		return sb.toString();
	}

	private String trim(String sql) {
		sql = sql.trim();
		if (sql.endsWith(";")) {
			sql = sql.substring(0, sql.length() - 1 - ";".length());
		}
		return sql;
	}

}
