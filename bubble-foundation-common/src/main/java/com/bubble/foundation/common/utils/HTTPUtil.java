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
package com.bubble.foundation.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bubble.foundation.common.exception.CommonException;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2016年3月24日
 */
public class HTTPUtil {

	private static final Logger logger = LoggerFactory.getLogger(HTTPUtil.class);

	private static final String[] PROXY_REMOTE_IP_ADDRESS = { "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "HTTP_CLIENT_IP",
			"HTTP_X_FORWARDED_FOR", "WL-Proxy-Client-IP" };

	/**
	 * 获取客户端ip地址
	 * 
	 * @param request
	 * @return
	 * @throws CommonException
	 */
	public static String getRequestIP(HttpServletRequest request) throws CommonException {
		Map<String, String> headerMap = getRequestHeaders(request);
		for (String key : PROXY_REMOTE_IP_ADDRESS) {// 获取代理ip
			String forwardIp = headerMap.get(key);
			if (!StringUtil.isEmpty(forwardIp) && !"unknown".equalsIgnoreCase(forwardIp)) {
				int commaOffset = forwardIp.indexOf(',');
				if (commaOffset < 0) {
					return forwardIp;
				}
				return forwardIp.substring(0, commaOffset);
			}
		}
		return request.getRemoteAddr();// 返回真实ip
	}

	/**
	 * 获取http header内容
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getRequestHeaders(HttpServletRequest request) throws CommonException {
		try {
			Map<String, String> headers = new HashMap<>(0);
			for (Enumeration e = request.getHeaderNames(); e.hasMoreElements();) {
				String headerKey = e.nextElement().toString();
				String headerValue = request.getHeader(headerKey);
				headers.put(headerKey, headerValue);
			}
			return Collections.unmodifiableMap(headers);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new CommonException("获取http头内容失败", e);
		}

	}

	/**
	 * 获取http parameter内容
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getRequestParameters(HttpServletRequest request) throws CommonException {
		try {
			Map<String, Object> param = new HashMap<>(0);
			for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
				String paramKey = e.nextElement().toString();
				String paramValue = request.getHeader(paramKey);
				param.put(paramKey, paramValue);
			}
			return Collections.unmodifiableMap(param);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new CommonException("获取http请求参数失败", e);
		}
	}

	private static Field requestField;

	private static Field parametersParsedField;

	private static Field coyoteRequestField;

	private static Field parametersField;

	private static Field hashTabArrField;

	static {
		try {
			Class clazz = Class.forName("org.apache.catalina.connector.RequestFacade");
			requestField = clazz.getDeclaredField("request");
			requestField.setAccessible(true);

			parametersParsedField = requestField.getType().getDeclaredField("parametersParsed");
			parametersParsedField.setAccessible(true);

			coyoteRequestField = requestField.getType().getDeclaredField("coyoteRequest");
			coyoteRequestField.setAccessible(true);

			parametersField = coyoteRequestField.getType().getDeclaredField("parameters");
			parametersField.setAccessible(true);

			hashTabArrField = parametersField.getType().getDeclaredField("paramHashValues");
			hashTabArrField.setAccessible(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 添加http request参数
	 * 
	 * @param request
	 * @param paramMap
	 * @throws CommonException
	 */
	public static void addParameters(HttpServletRequest request, Map<String, Object> paramMap) throws CommonException {
		try {
			Map<String, ArrayList<String>> paramHashValues = wrapParameterField(request);
			for (Entry<String, Object> entry : paramMap.entrySet()) {
				ArrayList<String> values = paramHashValues.get(entry.getKey());
				if (values == null) {
					values = new ArrayList<>(1);
					paramHashValues.put(entry.getKey(), values);
				}
				values.add(String.valueOf(entry.getValue()));
			}
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			throw new CommonException("动态添加http请求参数失败！", e);
		}
	}

	/**
	 * 添加http request 参数
	 * 
	 * @param request
	 * @param key
	 * @param value
	 * @throws CommonException
	 */
	public static void addParameter(HttpServletRequest request, String key, String value) throws CommonException {
		try {
			Map<String, ArrayList<String>> paramHashValues = wrapParameterField(request);
			ArrayList<String> values = paramHashValues.get(key);
			if (values == null) {
				values = new ArrayList<>(1);
				paramHashValues.put(key, values);
			}
			values.add(value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new CommonException("动态添加http请求参数失败！", e);
		}
	}

	/**
	 * @param request
	 * @return
	 * @throws IllegalAccessException
	 */
	private static final Map<String, ArrayList<String>> wrapParameterField(HttpServletRequest request) throws IllegalAccessException {
		Object innerRequest = requestField.get(request);
		parametersParsedField.setBoolean(innerRequest, true);
		Object coyoteRequestObject = coyoteRequestField.get(innerRequest);
		Object parameterObject = parametersField.get(coyoteRequestObject);
		Map<String, ArrayList<String>> paramHashValues = (Map<String, ArrayList<String>>) hashTabArrField.get(parameterObject);
		return paramHashValues;
	}

	/**
	 * 读取http body
	 * 
	 * @param request
	 * @param charset
	 *            字符集，默认utf-8
	 * @param closeStream
	 *            是否关闭流，默认不关闭
	 * @return
	 * @throws CommonException
	 */
	public static String getHttpBodyString(HttpServletRequest request, String charset, boolean closeStream) throws CommonException {
		StringBuilder sbline = new StringBuilder();
		try {
			if (StringUtil.isEmpty(charset))
				charset = CharsetUtil.CHARSET_UTF_8;
			InputStream is = request.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, charset));
			String line;
			while ((line = br.readLine()) != null) {
				sbline.append(line);
			}
			if (closeStream)
				is.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new CommonException("读取httpbody失败！", e);
		}
		return sbline.toString();
	}

	/**
	 * 读取http body
	 * 
	 * @param request
	 * @param closeStream
	 *            是否关闭io流，默认不关闭
	 * @return
	 * @throws CommonException
	 */
	public static byte[] getHttpBody(HttpServletRequest request, boolean closeStream) throws CommonException {
		byte[] bytes = {};
		try {
			byte[] temp = new byte[1024];
			ServletInputStream is = request.getInputStream();
			bytes = new byte[is.available()];
			int length = 0;
			while (is.read(temp) != -1) {
				System.arraycopy(temp, 0, bytes, length, Math.min(temp.length, bytes.length));
				length += temp.length;
			}
			is.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new CommonException("读取httpbody失败！", e);
		}
		return bytes;
	}
}
