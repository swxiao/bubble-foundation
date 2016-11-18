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
package com.bubble.foundation.mybatis.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.transaction.PlatformTransactionManager;

import com.bubble.foundation.mybatis.factory.RepositoryFactoryBean;

/**
 * Annotation to enable MyBatis repositories. Will scan the package of the
 * annotated configuration class for Spring Data repositories by default.
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2015年2月4日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(RepositoriesRegister.class)
public @interface EnableRepositories {
	/**
	 * Alias for the {@link #basePackages()} attribute. Allows for more concise
	 * annotation declarations e.g.:
	 * {@code @EnableRepositories("org.my.pkg")} instead of
	 * {@code @EnableRepositories(basePackages="org.my.pkg")}.
	 */
	String[] value() default {};

	/**
	 * Base packages to scan for annotated components. {@link #value()} is an
	 * alias for (and mutually exclusive with) this attribute. Use
	 * {@link #basePackageClasses()} for a type-safe alternative to String-based
	 * package names.
	 */
	String[] basePackages() default {};

	/**
	 * Type-safe alternative to {@link #basePackages()} for specifying the
	 * packages to scan for annotated components. The package of each class
	 * specified will be scanned. Consider creating a special no-op marker class
	 * or interface in each package that serves no purpose other than being
	 * referenced by this attribute.
	 */
	Class<?>[] basePackageClasses() default {};

	/**
	 * Specifies which types are eligible for component scanning. Further
	 * narrows the set of candidate components from everything in
	 * {@link #basePackages()} to everything in the base packages that matches
	 * the given filter or filters.
	 */
	Filter[] includeFilters() default {};

	/**
	 * Specifies which types are not eligible for component scanning.
	 */
	Filter[] excludeFilters() default {};

	/**
	 * Returns the postfix to be used when looking up custom repository
	 * implementations. Defaults to {@literal Impl}. So for a repository named
	 * {@code PersonRepository} the corresponding implementation class will be
	 * looked up scanning for {@code PersonRepositoryImpl}.
	 * @return
	 */
	String repositoryImplementationPostfix() default "Impl";
	
	/**
	 * Configures the location of where to find the Spring Data named queries properties file. Will default to
	 * {@code META-INFO/jpa-named-queries.properties}.
	 * 
	 * @return
	 */
	String namedQueriesLocation() default "";

	/**
	 * Returns the key of the {@link QueryLookupStrategy} to be used for lookup
	 * queries for query methods. Defaults to {@link Key#USE_DECLARED_QUERY}.
	 * 
	 * @return
	 */
	Key queryLookupStrategy() default Key.USE_DECLARED_QUERY;

	/**
	 * Returns the {@link FactoryBean} class to be used for each repository
	 * instance. Defaults to {@link JpaRepositoryFactoryBean}.
	 * 
	 * @return
	 */
	Class<?> repositoryFactoryBeanClass() default RepositoryFactoryBean.class;

	String sqlSessionTemplateRef() default "sqlSessionTemplate";

	/**
	 * /** Configures the name of the {@link PlatformTransactionManager} bean
	 * definition to be used to create repositories discovered through this
	 * annotation. Defaults to {@code transactionManager}.
	 * 
	 * @return
	 */
	String transactionManagerRef() default "transactionManager";
	
	/**
	   * This property specifies the parent that the scanner will search for.
	   * <p>
	   * The scanner will register all interfaces in the base package that also have
	   * the specified interface class as a parent.
	   * <p>
	   * Note this can be combined with annotationClass.
	   */
	  Class<?> markerInterface() default Class.class;
}
