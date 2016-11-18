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
package com.bubble.foundation.hbase.repositories;

import java.util.List;

import org.apache.hadoop.hbase.client.Scan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.ResultsExtractor;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.stereotype.Repository;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2016年1月29日
 */
@Repository
public class HbaseRepository<T>{

	@Autowired
	HbaseTemplate hbaseTemplate;

	public void save(String tableName, TableCallback<T> action) {
		hbaseTemplate.execute(tableName, action);
	}

	public void delete(String tableName, String rowName, String familyName) {
		hbaseTemplate.delete(tableName, rowName, familyName);
	}

	public List<T> findAll(String tableName, String family, RowMapper<T> action) {
		return hbaseTemplate.find(tableName, family, action);
	}

	public T find(String tableName, String family, ResultsExtractor<T> action) {
		return hbaseTemplate.find(tableName, family, action);
	}

	public List<T> findList(String tableName, Scan scan, RowMapper<T> action) {
		return hbaseTemplate.find(tableName, scan, action);
	}
	
	public T get(String tableName, String rowName, String familyName, String qualifier, RowMapper<T> mapper) {
		return hbaseTemplate.get(tableName, rowName, familyName, qualifier, mapper);
	}

	public T get(String tableName, String rowName, String familyName, RowMapper<T> mapper) {
		return hbaseTemplate.get(tableName, rowName, familyName, mapper);
	}

	public T get(String tableName, String rowName, RowMapper<T> mapper) {
		return hbaseTemplate.get(tableName, rowName, mapper);
	}
}
