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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import com.bubble.foundation.common.exception.CommonException;



/**
 * Spring容器内Bean相关操作类
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2015年1月28日
 */
public class SpringContextManager implements ApplicationContextAware{

	private static final Logger logger = LoggerFactory.getLogger(SpringContextManager.class);
	
	private ConfigurableApplicationContext applicationContext = null;
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext)applicationContext;
	}

	/**
	 * 从Spring容器中获取已加载的对象实例
	 * @param beanName 对象名(beanId)
	 * @return Object 容器内对象实例
	 */
	public Object getBean(String beanName){
		return getBean(beanName, null);
	}
	
	/**
	 * 从Spring容器中获取已加载的对象实例(重载)
	 * @param beanName 对象名(beanId)
	 * @param args 对象参数
	 * @return Object 容器内对象实例
	 */
	public Object getBean(String beanName,Object... args){
		logger.info("BeanName is:{}",beanName);
		return this.applicationContext.getBean(beanName, args);
	}
	
	/**
	 * 从Spring容器中获取已加载的对象实例(重载)
	 * @param beanClass 对象class
	 * @return Object 容器内对象实例
	 */
	public Object getBean(Class<?> beanClass){
		return getBean(beanClass,null);
	}
	
	/**
	 * 从Spring容器中获取已加载的对象实例(重载)
	 * @param beanClass 对象class
	 * @param args 对象参数
	 * @return Object 容器内对象实例
	 */
	public Object getBean(Class<?> beanClass,Object... args){
		logger.info("BeanName is:{}",beanClass.getName());
		return this.applicationContext.getBean(beanClass, args);
	}
	
	/**
	 * 动态加载自定义Spring配置</br>
	 * 容器启动后动态加载war/ear包外的配置文件，并将配置的对象内容追加到容器，不支持动态扫描
	 * @param configurationLocations 自定义spring文件地址
	 * @throws CommonException Common操作异常
	 */
	public void loadCustomizedConfiguration(List<String> configurationLocations) throws CommonException{
		logger.info("Loading custom configuration file ......");
		if (configurationLocations == null || configurationLocations.size() < 1) {
			logger.info("No custom configuration file !");
			return;
		}
		try {
			XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader((BeanDefinitionRegistry) applicationContext.getBeanFactory());
			beanDefinitionReader.setResourceLoader(applicationContext);
			beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(applicationContext));
			for (String localtion : configurationLocations)
				beanDefinitionReader.loadBeanDefinitions(applicationContext.getResources(localtion));
		} catch (BeansException e) {
			logger.error(e.getMessage(), e);
			throw new CommonException("Load custom bean definitions has error !", e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new CommonException("Read configuration file has error !", e);
		}
		logger.info("Load Custom configuration file is finish !");
	}

	/**
	 * 从Spring容器中获取指定注解对象集合</br>
	 * 此方法可以用于自定义注解扩展
	 * @param clz 注解class对象
	 * @return Map<String,Object> 对象Map
	 * @throws CommonException 加载被指定注解标注对象异常
	 */
	public Map<String, Object> getBeanMapWithAnnotation(Class<? extends Annotation> clz) throws CommonException {
		try {
			Map<String, Object> objMap = new HashMap<String, Object>();
			Map<String, Object> proxyMap = this.applicationContext.getBeansWithAnnotation(clz);
			for(Iterator<String> iterator = proxyMap.keySet().iterator();iterator.hasNext();){
				String key = iterator.next();
				Object obj = getTargetObject(proxyMap.get(key));
				objMap.put(key, obj);
			}
			return objMap;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new CommonException("Get object map with annotation has error!");
		}
		
	}
	
	
	/**
	 * 根据容器返回对象得到真实目标对象
	 * @param proxy 代理对象
	 * @return Object 目标对象
	 * @throws CommonException 获取目标对象操作异常
	 */
	public Object getTargetObject(Object proxy) throws CommonException{
		try {
			if (!AopUtils.isAopProxy(proxy)) {// 不是代理对象
				return proxy;
			}
			if (AopUtils.isCglibProxy(proxy)) {// cglib代理对象
				return getCglibProxyTargetObject(proxy);
			}
			if (AopUtils.isJdkDynamicProxy(proxy)) {// jdk动态代理
				return getJdkDynamicProxyTargetObject(proxy);
			}
			//其它情况时返回null
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new CommonException("Get target Object has error!");
		}
	}
	
	/**
	 * 获取cglib代理目标对象</br>
	 * Spring容器对象未实现接口且需要进行AOP管理时由cglib动态代理加载对象。
	 * @param proxy 代理对象
	 * @return Object 目标对象
	 * @throws CommonException 从cblib动态代理获取目标对象异常
	 */
	public Object getCglibProxyTargetObject(Object proxy) throws CommonException{
		try {
			Field field = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
			field.setAccessible(true);  
	        Object dynamicAdvisedInterceptor = field.get(proxy);  
	        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");  
	        advised.setAccessible(true);  
	        return ((AdvisedSupport)advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();  
		} catch (SecurityException e) {
			logger.error(e.getMessage(),e);
			throw new CommonException("The method of object with cglib can't be read !",e);
		} catch (NoSuchFieldException e) {
			logger.error(e.getMessage(),e);
			throw new CommonException("Can't find field in cglib object !",e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(),e);
			throw new CommonException("Inject parameter to cglib object has error !",e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(),e);
			throw new CommonException("Access cglib object has error !",e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new CommonException("Get cglib target object has error !",e);
		}  
	}

	/**
	 * 获取JDK动态代理目标对象</br>
	 * Spring容器对象已实现接口且需要进行AOP管理时由JDK动态代理加载对象。
	 * @param proxy 代理对象
	 * @return Object 目标对象
	 * @throws CommonException 从jdk动态代理获取目标对象异常
	 */
	public Object getJdkDynamicProxyTargetObject(Object proxy) throws CommonException{
		try {
			Field field = proxy.getClass().getSuperclass().getDeclaredField("h");
			field.setAccessible(true);  
	        AopProxy aopProxy = (AopProxy) field.get(proxy);  
	        Field advised = aopProxy.getClass().getDeclaredField("advised");  
	        advised.setAccessible(true);  
	        Object target = ((AdvisedSupport)advised.get(aopProxy)).getTargetSource().getTarget();  
			return target;
		} catch (SecurityException e) {
			logger.error(e.getMessage(),e);
			throw new CommonException("The method of object with Dynamic jdk proxy can't be read !",e);
		} catch (NoSuchFieldException e) {
			logger.error(e.getMessage(),e);
			throw new CommonException("Can't find field in Dynamic jdk proxy object !",e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(),e);
			throw new CommonException("Inject parameter to Dynamic jdk proxy object has error !",e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(),e);
			throw new CommonException("Access Dynamic jdk proxy object has error !",e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new CommonException("Get Dynamic jdk proxy target object has error !",e);
		}  
        
	}
}
