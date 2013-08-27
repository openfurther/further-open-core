/**
 * Copyright (C) [2013] [The FURTHeR Project]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.utah.further.core.util.schema;

import static edu.utah.further.core.util.schema.CoreSchemaConstants.ELEMENT_INCLUDE;
import static edu.utah.further.core.util.schema.CoreSchemaConstants.ELEMENT_INCLUDE_BEAN;
import static edu.utah.further.core.util.schema.SchemaUtil.getChildElementsByTagNameNullSafe;
import static edu.utah.further.core.util.schema.SchemaUtil.parseValueElements;
import static org.springframework.aop.config.AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME;

import java.util.List;

import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.aop.config.AopNamespaceUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.Ordered;
import org.w3c.dom.Element;

import edu.utah.further.core.util.aop.SelectiveAspectJAutoProxyCreator;

/**
 * Parses our custom AOP autoproxy creator that only scans selected packages of beans to
 * be advised. Gets around FUR-900, because we can avoid the automatic scanning OSGi
 * packages by {@link AnnotationAwareAspectJAutoProxyCreator}.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version May 4, 2010
 */
final class SelectiveAspectJAutoProxyCreatorBeanDefinitionParser extends
		AbstractUniqueBeanDefinitionParser
{
	// ========================= CONSTANTS =================================

	/**
	 * The bean name of the internally managed bean configurer aspect. Unfortunately,
	 * cannot be the same as {@link AopConfigUtils#AUTO_PROXY_CREATOR_BEAN_NAME} because
	 * {@link AopConfigUtils} uses a hard-coded list of class names (called
	 * <code>APC_PRIORITY_LIST</code>) that are permitted to be deployed as autoproxy
	 * creators.
	 */
	private static final String ASPECTJ_AUTOPROXY_CREATOR_BEAN_NAME = SelectiveAspectJAutoProxyCreator.class
			.getCanonicalName();

	// ========================= FIELDS ====================================

	// ========================= IMPLEMENTATION: AbstractUniqueIdBeanDef. ==

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.util.schema.AbstractUniqueBeanDefinitionParser#getGeneratedId
	 * ()
	 */
	@Override
	protected String getGeneratedId()
	{
		return ASPECTJ_AUTOPROXY_CREATOR_BEAN_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.util.schema.AbstractUniqueBeanDefinitionParser#parseInternal
	 * (org.w3c.dom.Element, org.springframework.beans.factory.xml.ParserContext)
	 */
	@Override
	protected BeanDefinition parseInternal(final Element element,
			final ParserContext parserContext)
	{
		// A preemptive strike: remove a previously-deployed standard AOP autoproxy
		// creator, redeploy it but disable its functionality (set its include patterns to
		// an empty list rather than null -- utilize FUR-908's reason to our advantage!).
		// This way, subsequent aop:aspectj-autoproxy elements do not create another
		// standard AOP autoproxy creator, so only ours is used.
		//
		// Note: we use a fake source element (our core:aspectj-autoproxy) because
		// AopConfigUtils calls require one. We could dig the code that deploys the
		// creator without depending on an element, but that's easier and more
		// compatible with future Spring upgrades.
		final BeanDefinitionRegistry registry = parserContext.getRegistry();
		if (registry.containsBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME))
		{
			registry.removeBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME);
		}
		registerAspectJAnnotationAutoProxyCreatorAndDisableIt(element, parserContext);

		// Deploy our own aspect
		final BeanDefinitionBuilder factory = parseCreatorElement(element);
		addCollectionProperty(element, factory, ELEMENT_INCLUDE, "includePatterns");
		addCollectionProperty(element, factory, ELEMENT_INCLUDE_BEAN,
				"includeBeanPatterns");
		final BeanDefinition def = factory.getBeanDefinition();
		parserContext.registerBeanComponent(new BeanComponentDefinition(def,
				getGeneratedId()));

		// For FUR-1203
		// Ensure that Selective comes before AnnotationAware on the bean post-processor
		// list, so that Selective has a chance to proxy beans before AnnotationAware
		// does. Since the latter is disabled anyway, that's the desired behavior.
		// Otherwise, AnnotationAware might proxy using some Advisors (even though include
		// patterns are set to the empty list) and Selective won't be able to re-proxy
		// with our aspects.
		factory.addPropertyValue("order", new Integer(Ordered.HIGHEST_PRECEDENCE));

		return factory.getBeanDefinition();
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param element
	 * @return
	 */
	private static BeanDefinitionBuilder parseCreatorElement(final Element element)
	{
		final BeanDefinitionBuilder factory = BeanDefinitionBuilder
				.rootBeanDefinition(SelectiveAspectJAutoProxyCreator.class);
		// Add test battery attributes here in the future
		return factory;
	}

	/**
	 * @param element
	 * @param factory
	 * @param elementName
	 * @param propertyName
	 */
	private void addCollectionProperty(final Element element,
			final BeanDefinitionBuilder factory, final String elementName,
			final String propertyName)
	{
		final List<Element> elements = getChildElementsByTagNameNullSafe(element,
				elementName);
		// Very important check: do not set an empty collection property on
		// an autoproxy creator, because it relies on leaving the corresponding property
		// null in this case. If you set it to an empty collection, it won't advice any
		// beans!! (This solves FUR-908)
		if (!elements.isEmpty())
		{
			factory.addPropertyValue(propertyName, parseValueElements(elements));
		}
	}

	/**
	 * @param element
	 * @param parserContext
	 */
	private void registerAspectJAnnotationAutoProxyCreatorAndDisableIt(
			final Element element, final ParserContext parserContext)
	{
		AopNamespaceUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(
				parserContext, element);
		final BeanDefinition beanDef = parserContext.getRegistry().getBeanDefinition(
				AUTO_PROXY_CREATOR_BEAN_NAME);
		disableProxyCreatorBeanDefinition(beanDef);
	}

	/**
	 * @param beanDef
	 */
	private void disableProxyCreatorBeanDefinition(final BeanDefinition beanDef)
	{
		beanDef.getPropertyValues().addPropertyValue("includePatterns",
				new ManagedList<AbstractBeanDefinition>());
	}
}
