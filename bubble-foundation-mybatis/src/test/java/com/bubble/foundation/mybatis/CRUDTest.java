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
package com.bubble.foundation.mybatis;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bubble.entities.Message;
import com.bubble.foundation.mybatis.page.Page;
import com.bubble.foundation.mybatis.repositories.ICRUDRepository;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2016年11月3日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:config/spring/foundation-mybatis.xml" })
public class CRUDTest {

	private static final Logger logger = LoggerFactory.getLogger(CRUDTest.class);

	@Autowired
	private ICRUDRepository repository;

	@Test
	public void testSave() {
		Message message = new Message();
		message.setId("2016110316142626715");
		message.setHead("test2 head");
		message.setBody("test2 body");
		message.setCreatedBy("bubble");
		message.setCreatedDate("2016-11-11 12:12:12");
		message.setLastModifiedBy("bubble");
		message.setLastModifiedDate("2016-11-11 12:12:12");
		repository.save(message);
	}

	@Test
	public void testSaveOrUpdate() {
		Message message = new Message();
		message.setId("2016110316142626715");
		message.setHead("test2 head");
		message.setBody("test123123213123 body");
		message.setCreatedBy("bubble");
		message.setCreatedDate("2016-11-11 12:12:12");
		message.setLastModifiedBy("bubble");
		message.setLastModifiedDate("2016-11-11 12:12:12");
		Message msg = repository.saveOrUpdate(message);
		logger.info("result : {}", msg);
	}

	@Test
	public void testQueryOne() {
		logger.info("result : {}", repository.findOne("2016110316142626715"));
	}

	@Test
	public void testQueryCount() {
		logger.info("result : {}", repository.count());
	}

	@Test
	public void testQueryAll() {
		for (Message message : repository.findAll()) {
			logger.info("result : {}", message);
		}
	}

	@Test
	public void queryPage() {
		Message message = new Message();
		message.setCreatedBy("bubble");
		Page<Message> page = new Page<>(1, 2, message);
		List<Message> messageList = repository.queryPage(page);
		logger.info("page : {}", page);
	}

	@Test
	public void query() {
		Message message = new Message();
		message.setCreatedBy("bubble");
		List<Message> messageList = repository.query(message);
		logger.info("result : {}", messageList);
	}

	@Test
	public void delete() {
		repository.delete("2016110316142626712");
	}

}