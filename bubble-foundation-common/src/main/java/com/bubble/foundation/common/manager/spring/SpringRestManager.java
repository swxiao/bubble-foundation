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
package com.bubble.foundation.common.manager.spring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


/**
 * Spring RestTemplate操作类
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2015年1月28日
 */
public class SpringRestManager  extends RestTemplate implements InitializingBean{
private List<MediaType> supportedMediaTypes = Collections.emptyList();
	
	public SpringRestManager() {
		super();
	}

	public SpringRestManager(ClientHttpRequestFactory requestFactory) {
		super(requestFactory);
	}

	public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
		this.supportedMediaTypes = supportedMediaTypes;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initCustomizedRest();
	}
	/**
	 * 初始化自定义Rest
	 * @author xiaosw
	 * 2014-9-18
	 */
	private void initCustomizedRest(){
		//添加自定义mediaType
		List<MediaType> mediaTypeList = new ArrayList<MediaType>();
		for(HttpMessageConverter<?> hmc : this.getMessageConverters()){
			mediaTypeList.addAll(supportedMediaTypes);
			mediaTypeList.addAll(hmc.getSupportedMediaTypes());
			if(hmc instanceof AbstractHttpMessageConverter){
				((AbstractHttpMessageConverter)hmc).setSupportedMediaTypes(mediaTypeList);
			}
			mediaTypeList.clear();
			if(logger.isDebugEnabled()){
				logger.debug(hmc.getSupportedMediaTypes());
			}
		}
	}
}
