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
package com.bubble.foundation.mybatis.page;

import java.util.List;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2015年2月5日
 * @param <T>
 */
public class Page<T> {

	private int pageNo = 1;

	private int pageSize = 15;

	private int totalRecord;

	private int totalPage;

	private List<T> results;

	private Object params;

	public Page(int pageNo, int pageSize, Object params) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.params = params;
	}

	/**
	 * @return page no index
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * @return page size
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @return total count
	 */
	public int getTotalRecord() {
		return totalRecord;
	}

	/**
	 * @param totalRecord
	 */
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
		this.totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;
	}

	/**
	 * @return totalPage
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * @return searchResult
	 */
	public List<T> getResults() {
		return results;
	}

	/**
	 * @param results
	 */
	public void setResults(List<T> results) {
		this.results = results;
	}

	/**
	 * @return paramsMap
	 */
	public Object getParams() {
		return params;
	}

	/**
	 * @return offset
	 */
	public int getOffset() {
		return (this.pageNo - 1) * this.pageSize;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Page [pageNo=").append(pageNo).append(", pageSize=").append(pageSize).append(", results=").append(results)
				.append(", totalPage=").append(totalPage).append(", totalRecord=").append(totalRecord).append("]");
		return builder.toString();
	}

}
