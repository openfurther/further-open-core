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
package edu.utah.further.core.util.aop;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.ProxyCreationContext;
import org.springframework.core.PriorityOrdered;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.regex.RegExUtil;
import edu.utah.further.core.api.text.StringUtil;

/**
 * An extension of {@link AnnotationAwareAspectJAutoProxyCreator}, the aspect behind the
 * &lt;aop:aspectj-autoproxy&gt; element that scans and advises beans whose names match
 * any number of specified regular expressions wired to this creator.
 * <p>
 * That is, while &lt;aop:include&gt; allows specifying which aspects are used by
 * {@link AnnotationAwareAspectJAutoProxyCreator}, this bean also also allows specifying a
 * subset of the beans in the application context to be auto-proxied. This is done using
 * the analogous elements &lt;core:include-bean&gt; within the
 * &lt;core:aspectj-autoproxy&gt; that hides {@link SelectiveAspectJAutoProxyCreator}'s
 * bean definition. For example, to only proxy beans under the
 * <code>edu.utah.further</code> package, use
 * 
 * <pre>
 *      &lt;core:aspectj-autoproxy&gt;
 *         &lt;/core:include-bean value="edu\.utah\.further\..+" /&gt;
 *      &lt;/core:aspectj-autoproxy&gt;
 * </pre>
 * <p>
 * Additionally, this aspect prevents any dynamic CGLIB proxies from being auto-proxied
 * because they will always trigger the error below when an AspectJ class-level annotation
 * matching pattern such as <code>@within(edu.utah.further.SomeAnnotationType)</code> is
 * used in a point-cut.
 * <p>
 * This is not only faster, but especially useful in an OSGi setting, because some OSGi
 * services like <code>pooledConnectionFactory</code> are dynamic proxies, and crash
 * {@link AnnotationAwareAspectJAutoProxyCreator} on the error
 * 
 * <pre>
 * Caused by: org.aspectj.weaver.reflect.ReflectionWorld$ReflectionWorldException: warning can't determine annotations of missing type com.springsource.kernel.dmfragment.internal.KernelMBeanExporter
 *  [Xlint:cantFindType]
 * </pre>
 * <p>
 * This class was built for extension.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @see https://jira.chpc.utah.edu/browse/FUR-900
 * @see http://forum.springsource.org/showthread.php?t=85396
 * @see https://issuetracker.springsource.com/browse/DMS-2390
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version May 28, 2010
 */
public class SelectiveAspectJAutoProxyCreator extends
		AnnotationAwareAspectJAutoProxyCreator implements PriorityOrdered
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(SelectiveAspectJAutoProxyCreator.class);

	/**
	 * A sub-string characterizing a CGLIB dynamic proxy class name.
	 */
	private static final String CGLIB_CLASS_NAME_PATTERN = "CGLIB$$";

	// ========================= FIELDS ====================================

	/**
	 * List of regular expressions of fully-qualified bean class names to be auto-proxied.
	 */
	private List<Pattern> includeBeanPatterns;

	/**
	 * List of regular expressions of fully-qualified AspectJ <i>bean names</i> (not class
	 * names) to be auto-proxied.
	 */
	private List<Pattern> aspectIncludeBeanPatterns;

	// ========================= CONSTRUCTORS ==============================

	/**
	 *
	 */
	public SelectiveAspectJAutoProxyCreator()
	{
		super();
		if (log.isDebugEnabled())
		{
			log.debug("Creating auto-proxy creator");
		}
	}

	// ========================= IMPL: AbstractAdvisorAutoProxyCreator =====

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.aop.framework.autoproxy. AbstractAdvisorAutoProxyCreator#
	 * getAdvicesAndAdvisorsForBean(java.lang.Class, java.lang.String,
	 * org.springframework.aop.TargetSource)
	 */
	@Override
	protected Object[] getAdvicesAndAdvisorsForBean(
			final Class beanClass, final String beanName,
			final TargetSource targetSource)
	{
		// Only proxy beans that match our selection criteria
		if (isBeanProxied(beanClass.getCanonicalName()))
		{
			return super.getAdvicesAndAdvisorsForBean(beanClass, beanName, targetSource);
		}
		return DO_NOT_PROXY;
	}

	// ========================= GET / SET =================================

	/**
	 * Set a list of regular expression patterns, matching eligible spring fully-qualified
	 * bean names in the application context.
	 * <p>
	 * Default is to consider all beans as eligible for advice.
	 * 
	 * @param patterns
	 *            list of bean inclusion patterns
	 */
	public void setIncludeBeanPatterns(final List<String> patterns)
	{
		this.includeBeanPatterns = CollectionUtil.newList();
		for (final String patternText : patterns)
		{
			this.includeBeanPatterns.add(Pattern.compile(patternText));
		}
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Is bean eligible for auto-proxying.
	 * 
	 * @param beanClassName
	 *            fully-qualified class name of prospective bean to be advised
	 * @return is bean to be proxied
	 */
	protected boolean isBeanProxied(final String beanClassName)
	{
		return !isBeanADynamicProxy(beanClassName)
				&& isEligibleBeanForProxying(beanClassName);
	}

	/**
	 * Is this class name of a dynamic proxy bean generated by CGLIB.
	 * 
	 * @param beanClassName
	 *            fully-qualified class name
	 * @return <code>true</code> if and only if the class is thought to be a dynamic CGLIB
	 *         proxy
	 */
	protected boolean isBeanADynamicProxy(final String beanClassName)
	{
		return beanClassName.contains(CGLIB_CLASS_NAME_PATTERN);
	}

	/**
	 * Check whether the given application context bean is eligible for auto-proxying
	 * based on inclusion patterns.
	 * <p>
	 * If no &lt;core:include-bean&gt; elements were used, then
	 * {@link #includeBeanPatterns} will be <code>null</code> and all beans are included.
	 * If "includePatterns" is non-null, then one of the patterns must match.
	 * 
	 * @param beanClassName
	 *            fully-qualified class name of prospective bean to be advised
	 * @return is bean to be proxied
	 */
	protected boolean isEligibleBeanForProxying(final String beanClassName)
	{
		return RegExUtil.matchesOneOf(beanClassName, this.includeBeanPatterns);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator
	 * #isEligibleAspectBean(java.lang.String)
	 */
	@Override
	protected boolean isEligibleAspectBean(final String beanName)
	{
		return RegExUtil.matchesOneOf(beanName, this.aspectIncludeBeanPatterns);
	}

	// =========================
	// For debugging printouts
	// =========================

	/**
	 * Find all candidate Advisors to use in auto-proxying.
	 * 
	 * @return the List of candidate Advisors
	 */
	@Override
	protected List<Advisor> findCandidateAdvisors()
	{
		final List<Advisor> advisors = super.findCandidateAdvisors();
		if (log.isTraceEnabled())
		{
			log.trace("findCandidateAdvisors(): " + advisors);
		}
		return advisors;
	}

	/**
	 * Search the given candidate Advisors to find all Advisors that can apply to the
	 * specified bean.
	 * 
	 * @param candidateAdvisors
	 *            the candidate Advisors
	 * @param beanClass
	 *            the target's bean class
	 * @param beanName
	 *            the target's bean name
	 * @return the List of applicable Advisors
	 * @see ProxyCreationContext#getCurrentProxiedBeanName()
	 */
	@Override
	protected List<Advisor> findAdvisorsThatCanApply(
			final List<Advisor> candidateAdvisors,
			final Class beanClass, final String beanName)
	{
		final List<Advisor> advisors = super.findAdvisorsThatCanApply(candidateAdvisors,
				beanClass, beanName);
		if (log.isTraceEnabled())
		{
			log.trace("findAdvisorsThatCanApply(" + StringUtil.quote(beanName) + "): "
					+ advisors);
		}
		return advisors;
	}
}
