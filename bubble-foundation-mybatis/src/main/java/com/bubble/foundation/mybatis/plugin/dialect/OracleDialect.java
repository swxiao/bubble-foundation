/**
 * Copyright 2015-2017 
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
package com.bubble.foundation.mybatis.plugin.dialect;

import com.bubble.foundation.mybatis.plugin.DataBaseDialect;


/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2015年2月2日
 */
public class OracleDialect extends DataBaseDialect{

	private static String ORACLE_PAGEDSQL_FORMATTER = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (%z%) A WHERE ROWNUM <= %x%) WHERE RN >= %y%";
	
	@Override
	public String getPageSelectSQL(String sql, int offset, int limit) {
		offset += 1;
	    int endset = offset + limit - 1;
	    String rs = ORACLE_PAGEDSQL_FORMATTER.replaceFirst("%y%", offset + "").replaceFirst("%x%", endset + "").replaceFirst("%z%", sql);
	    return rs;
	}

}
