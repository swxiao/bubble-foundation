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
public class SQLServerDialect extends DataBaseDialect {

	@Override
	public String getPageSelectSQL(String sql, int offset, int limit) {
		if (offset == 0) {
			return new StringBuffer(sql.length() + 8).append(sql).insert(getSqlAfterSelectInsertPoint(sql), " top " + limit).toString();
		}
		int orderByIndex = sql.toLowerCase().lastIndexOf("order by");
		if (orderByIndex <= 0) {
			throw new UnsupportedOperationException(
					"must specify 'order by' statement to support limit operation with offset in sql server 2005");
		}
		return new StringBuilder().append(") select * from tempPagination where RowNumber between ").append(offset + 1).append(" and ")
				.append(offset + limit).toString();
	}

	private static int getSqlAfterSelectInsertPoint(String sql) {
		int selectIndex = sql.toLowerCase().indexOf("select");
		int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");
		return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
	}

}
