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
package com.bubble.foundation.common.mvc.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import com.bubble.foundation.common.mvc.annotaions.CrossDomain;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2016年5月9日
 */
@ControllerAdvice(annotations = { CrossDomain.class })
public class ControllerCrossDomainAdvice extends  AbstractMappingJacksonResponseBodyAdvice {

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice#beforeBodyWriteInternal(org.springframework.http.converter.json.MappingJacksonValue, org.springframework.http.MediaType, org.springframework.core.MethodParameter, org.springframework.http.server.ServerHttpRequest, org.springframework.http.server.ServerHttpResponse)
	 */
	@Override
	protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType,
			ServerHttpRequest request, ServerHttpResponse response) {
		response.getHeaders().set("Access-Control-Allow-Headers",
				"Origin,No-Cache,X-Requested-with,If-Modified-Since,Last-Modified,Cache-Control,Expires,Content-Type");
		response.getHeaders().set("Access-Control-Allow-Origin", "*");
		response.getHeaders().set("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE");
		response.getHeaders().set("Access-Control-Max-Age", "1512000");
	}

}
