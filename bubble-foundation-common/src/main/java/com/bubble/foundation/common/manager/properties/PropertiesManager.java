/**
 * Copyright [2015-2017] 
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
package com.bubble.foundation.common.manager.properties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.bubble.foundation.common.exception.CommonException;

/**
 * 属性文件相关操作类
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2015年1月28日
 */
public class PropertiesManager extends PropertyPlaceholderConfigurer {

	/**
	 * 属性文件资源路径枚举 2015年1月28日
	 */
	public static enum LocationEnum {
		CLASSPATH, FILESYSTEM;
	}

	private static final Logger logger = LoggerFactory.getLogger(PropertiesManager.class);

	private static Map<String, Object> contextProperties;

	/**
	 * 获取Spring容器已加载的属性文件内容
	 * 
	 * @return Map<String,String> 属性文件内容集合
	 */
	public static Map<String, Object> getContextProperties() {
		return contextProperties;
	}

	/**
	 * 手动加载属性文件，文件路径可以是classpath也可以是文件系统
	 * @param location 文件路径
	 * @param locationKind 路径种类，使用LocationEnum指定
	 * @return Properties 属性文件描述对象
	 * @throws CommonException 文件无法找到或者读取文件异常
	 */
	public static Properties loadProperties(String location, LocationEnum locationKind) throws CommonException {
		try {
			Resource resource = null;
			switch (locationKind) {
				case CLASSPATH:
					resource = new ClassPathResource(location);
					break;
				case FILESYSTEM:
					resource = new FileSystemResource(location);
					break;
			}
			return PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new CommonException("Read property file has error !", e);
		}
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		contextProperties = new HashMap<String, Object>();
		for (Object key : props.keySet()) {
			if (logger.isDebugEnabled()) {
				logger.debug("load property content: {}={}", key, props.get(key));
			}
			contextProperties.put((String)key, props.get(key));
		}
		super.processProperties(beanFactoryToProcess, props);
	}
}
