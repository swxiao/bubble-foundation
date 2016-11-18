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
package com.bubble.foundation.mybatis.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bubble.entities.Message;
import com.bubble.foundation.mybatis.MybatisRepository;
import com.bubble.foundation.mybatis.page.Page;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2016年11月3日
 */
@Repository
public interface ICRUDRepository extends MybatisRepository<Message, String> {

	/**
	 * @param page
	 */
	List<Message> queryPage(Page<Message> page);

	/**
	 * @param message
	 * @return
	 */
	List<Message> query(Message message);

}
