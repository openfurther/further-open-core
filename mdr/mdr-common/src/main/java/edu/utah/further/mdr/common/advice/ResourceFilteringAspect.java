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
package edu.utah.further.mdr.common.advice;

import static org.slf4j.LoggerFactory.getLogger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.text.PlaceHolderResolver;
import edu.utah.further.mdr.api.annotation.Filtered;
import edu.utah.further.mdr.api.domain.asset.Resource;
import edu.utah.further.mdr.api.service.asset.ResourceContentService;

/**
 * Post-filters resources and/or list of resources returned from MDR service methods.
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
 * @version Jul 1, 2010
 */
@Aspect
@Service("resourceFilteringAspect")
// Non-final for proxy subclassing
public class ResourceFilteringAspect
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(ResourceFilteringAspect.class);

	// ========================= FIELDS ==================================

	/**
	 * Resource filtering delegate.
	 */
	@Autowired
	private ResourceContentService resourceContentService;

	/**
	 * Resolves property place-holders in resource storage.
	 */
	@Autowired
	private PlaceHolderResolver resolver;

	// ========================= POINTCUTS ===============================

	// ========================= ADVICES =================================

	/**
	 * Advise a method annotated with the {@link Filtered} annotation - post-filters the
	 * resource.
	 *
	 * @param pjp
	 * @param methodAnnotation
	 * @return
	 * @throws Throwable
	 */
	@Around(value = "@annotation(methodAnnotation)")
	public Object adviseAnnotatedMethod(final ProceedingJoinPoint pjp,
			final Filtered methodAnnotation) throws Throwable
	{
		// TODO: in the future, filter only fields that are specified by the
		// methodAnnotation's fields array.
		if (log.isDebugEnabled())
		{
			log.debug("Advising " + pjp + ", filtered fields " + methodAnnotation.fields());
		}
		try
		{
			final Object retVal = pjp.proceed();
			if (isResource(retVal))
			{
				filterResource((Resource) retVal);
			}
			else if (ReflectionUtil.instanceOf(retVal, Iterable.class))
			{
				filterList((Iterable<?>) retVal);
			}
			return retVal;
		}
		catch (final Throwable throwable)
		{
			// Do not change exceptional behaviour
			throw throwable;
		}

	}

	// ========================= GET/SET ===============================

	/**
	 * Set a new value for the resourceContentService property.
	 *
	 * @param resourceContentService the resourceContentService to set
	 */
	public void setResourceContentService(final ResourceContentService resourceContentService)
	{
		this.resourceContentService = resourceContentService;
	}

	/**
	 * Set a new value for the resolver property.
	 *
	 * @param resolver the resolver to set
	 */
	public void setResolver(final PlaceHolderResolver resolver)
	{
		this.resolver = resolver;
	}

	// ========================= PRIVATE METHODS =======================

	/**
	 * Filter all applicable resource fields using the place-holder resolver delegate.
	 *
	 * @param resource
	 *            resource to filter
	 */
	private void filterResource(final Resource resource)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Filtering resource " + resource);
		}
		resourceContentService.filterResourceMetadata(resource, resolver);
	}

	/**
	 * Filter each resource element in a list.
	 *
	 * @param list
	 *            list (not necessarily of resources)
	 */
	private void filterList(final Iterable<?> list)
	{
		for (final Object element : list)
		{
			if (isResource(element))
			{
				filterResource((Resource) element);
			}
		}
	}

	/**
	 * @param object
	 * @return
	 */
	private boolean isResource(final Object object)
	{
		return ReflectionUtil.instanceOf(object, Resource.class);
	}
}
