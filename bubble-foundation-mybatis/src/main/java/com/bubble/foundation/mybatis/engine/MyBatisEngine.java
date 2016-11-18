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
package com.bubble.foundation.mybatis.engine;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.bubble.foundation.mybatis.AbstractEntity;
import com.bubble.foundation.mybatis.config.EnableRepositories;
import com.bubble.foundation.mybatis.plugin.interceptor.PaginationInterceptor;
import com.github.pagehelper.PageHelper;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2015年2月1日
 */
@Configuration
@EnableTransactionManagement
@EnableRepositories(basePackages = "com.bubble.**.repositories", includeFilters = @ComponentScan.Filter(value = Repository.class, type = FilterType.ANNOTATION))
public class MyBatisEngine implements ResourceLoaderAware {

	private ResourceLoader resourceLoader;

	@javax.annotation.Resource(name = "dataSource") // 指定名称注入
	private DataSource dataSource;

	@Value("${mybatis.dialect}")
	private String dialect;

	@Value("${mybatis.entity.package}")
	private String entityBasePackage = "com.bubble.entities";

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setTypeAliasesPackage(entityBasePackage);
		sessionFactory.setTypeAliasesSuperType(AbstractEntity.class);
		sessionFactory.setMapperLocations(getResources("classpath*:config/mybatis/mapper/**/*Mapper.xml"));
		Interceptor[] interceptors = new Interceptor[1];
		interceptors[0] = paginationInterceptor();
		// interceptors[0] = pageHelperInterceptor();
		sessionFactory.setPlugins(interceptors);
		Properties properties = new Properties();
		properties.setProperty("dialect", dialect);
		sessionFactory.setConfigurationProperties(properties);
		return sessionFactory.getObject();
	}

	@Bean
	@Scope("prototype")
	public SqlSessionTemplate sqlSessionTemplate() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory(), ExecutorType.SIMPLE);
	}

	@Bean
	public Interceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}

	@Deprecated
	@Bean
	public Interceptor pageHelperInterceptor() {
		return new PageHelper();
	}

	/*
	 * @Bean MapperScannerConfigurer mapperScannerConfigurer() { MapperScannerConfigurer mapperScannerConfigurer = new
	 * MapperScannerConfigurer(); mapperScannerConfigurer.setSqlSessionTemplateBeanName( "sqlSessionTemplate");
	 * mapperScannerConfigurer.setBasePackage( "com.bubble.datacenter.persistence.**.repositories");
	 * mapperScannerConfigurer.setMarkerInterface(MybatisRepository.class); return mapperScannerConfigurer; }
	 */

	private Resource[] getResources(String packagePath) throws IOException {
		ResourcePatternResolver resourceResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
		Resource[] resources = resourceResolver.getResources(packagePath);
		return resources;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

}
