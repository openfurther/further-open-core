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
package edu.utah.further.core.xml.jaxb;

import static edu.utah.further.core.api.lang.ReflectionUtil.instanceOf;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import com.sun.xml.bind.v2.model.annotation.AbstractInlineAnnotationReaderImpl;
import com.sun.xml.bind.v2.model.annotation.Locatable;
import com.sun.xml.bind.v2.model.annotation.RuntimeAnnotationReader;
import com.sun.xml.bind.v2.model.annotation.RuntimeInlineAnnotationReader;

import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * <p>
 * Patched version of JAXB's RuntimeAnnotationReader that can annotate set of classes,
 * fields, and methods as XmlTransient. Note that "<tt>@XmlTransient</tt> is mutually
 * exclusive with all other JAXB defined annotations.
 * </p>
 *
 * <p>
 * Usage:
 *
 * <pre>
 *     // initialize our custom reader
 *     TransientAnnotationReader reader = new TransientAnnotationReader();
 *     reader.addTransientField(Throwable.class.getDeclaredField(&quot;stackTrace&quot;));
 *     reader.addTransientMethod(Throwable.class.getDeclaredMethod(&quot;getStackTrace&quot;));
 *     // initialize JAXB context
 *     Map&lt;String, Object&gt; jaxbConfig = new HashMap&lt;String, Object&gt;();
 *     jaxbConfig.put(JAXBRIContext.ANNOTATION_READER, reader);
 *     JAXBContext ctx = JAXBContext.newInstance (PACKAGE_PATH, TransientAnnotationReader.class.getClassLoader(), jaxbConfig);
 *     // XMLlize something
 *     Marshaller m = ctx.create ();
 *     m.marshal (...);
 * </pre>
 *
 * </p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Andy Malakov
 * @version Jul 16, 2009
 * @see <a href="http://wiki.jboss.org/wiki/Wiki.jsp?page=JAXBIntroductions">JAXB
 *      Introductions</a>
 * @see http://mail-archives.apache.org/mod_mbox/cxf-users/200806.mbox/%3C486192D0.4040201@fluxcorp
 *      .com%3E
 */
@SuppressWarnings(
{ "unchecked", "rawtypes" })
public final class TransientAnnotationReader extends
		AbstractInlineAnnotationReaderImpl<Type, Class, Field, Method> implements
		RuntimeAnnotationReader
{
	private static class XmlTransientProxyHandler implements InvocationHandler
	{
		@Override
		public Object invoke(final Object proxy, final Method method, final Object[] args)
				throws Throwable
		{
			if (args == null || args.length == 0)
			{
				if (method.getName().equals("annotationType"))
					return XmlTransient.class;
				if (method.getName().equals("toString"))
					return "@XmlTransient";
			}
			throw new UnsupportedOperationException(
					"@XmlTransient doesn't support method call: " + method.getName());
		}

		private static XmlTransient create()
		{
			return (XmlTransient) Proxy.newProxyInstance(
					XmlTransientProxyHandler.class.getClassLoader(), new Class[]
					{ XmlTransient.class }, new XmlTransientProxyHandler());
		}
	}

	private static final Annotation XML_TRANSIENT_ANNOTATION = XmlTransientProxyHandler
			.create();
	private static final Annotation[] XML_TRANSIENT_ANNOTATION_ONLY =
	{ XML_TRANSIENT_ANNOTATION };

	private final RuntimeInlineAnnotationReader delegate = new RuntimeInlineAnnotationReader();
	private final List<Class<?>> transientClasses = CollectionUtil.newList();
	private final List<Field> transientFields = CollectionUtil.newList();
	private final List<Method> transientMethods = CollectionUtil.newList();

	// API

	/**
	 * @param cls
	 */
	public void addTransientClass(final Class cls)
	{
		transientClasses.add(cls);
	}

	/**
	 * @param field
	 */
	public void addTransientField(final Field field)
	{
		transientFields.add(field);
	}

	/**
	 * @param method
	 */
	public void addTransientMethod(final Method method)
	{
		transientMethods.add(method);
	}

	// / Classes

	@Override
	public boolean hasClassAnnotation(final Class clazz,
			final Class<? extends Annotation> annotationType)
	{
		if (transientClasses.contains(clazz))
			return true;
		return delegate.hasClassAnnotation(clazz, annotationType);
	}

	/**
	 * @param <A>
	 * @param annotationType
	 * @param clazz
	 * @param srcPos
	 * @return
	 * @see com.sun.xml.bind.v2.model.annotation.AnnotationReader#getClassAnnotation(java.lang.Class,
	 *      C, com.sun.xml.bind.v2.model.annotation.Locatable)
	 */
	@Override
	public <A extends Annotation> A getClassAnnotation(final Class<A> annotationType,
			final Class clazz, final Locatable srcPos)
	{
		if (transientClasses.contains(clazz))
			return (A) XML_TRANSIENT_ANNOTATION;

		// return LocatableAnnotation.create(((Class<?>)
		// clazz).getAnnotation(annotationType), srcPos);
		return delegate.getClassAnnotation(annotationType, clazz, srcPos);
	}

	// / Fields

	/**
	 * @param annotationType
	 * @param field
	 * @return
	 * @see com.sun.xml.bind.v2.model.annotation.AnnotationReader#hasFieldAnnotation(java.lang.Class,
	 *      java.lang.Object)
	 */
	@Override
	public boolean hasFieldAnnotation(final Class<? extends Annotation> annotationType,
			final Field field)
	{
		if (instanceOf(annotationType, XmlTransient.class))
		{
			if (transientFields.contains(field))
			{
				return true;
			}
		}
		return delegate.hasFieldAnnotation(annotationType, field);
	}

	/**
	 * @param <A>
	 * @param annotationType
	 * @param field
	 * @param srcPos
	 * @return
	 * @see com.sun.xml.bind.v2.model.annotation.AnnotationReader#getFieldAnnotation(java.lang.Class,
	 *      F, com.sun.xml.bind.v2.model.annotation.Locatable)
	 */
	@Override
	public <A extends Annotation> A getFieldAnnotation(final Class<A> annotationType,
			final Field field, final Locatable srcPos)
	{
		if (instanceOf(annotationType, XmlTransient.class))
		{
			if (transientFields.contains(field))
			{
				return (A) XML_TRANSIENT_ANNOTATION;
			}
		}
		return delegate.getFieldAnnotation(annotationType, field, srcPos);
	}

	/**
	 * @param field
	 * @param srcPos
	 * @return
	 * @see com.sun.xml.bind.v2.model.annotation.AnnotationReader#getAllFieldAnnotations(java.lang.Object,
	 *      com.sun.xml.bind.v2.model.annotation.Locatable)
	 */
	@Override
	public Annotation[] getAllFieldAnnotations(final Field field, final Locatable srcPos)
	{
		if (transientFields.contains(field))
			return XML_TRANSIENT_ANNOTATION_ONLY;

		return delegate.getAllFieldAnnotations(field, srcPos);
	}

	// / Methods

	/**
	 * @param annotationType
	 * @param method
	 * @return
	 * @see com.sun.xml.bind.v2.model.annotation.AnnotationReader#hasMethodAnnotation(java.lang.Class,
	 *      java.lang.Object)
	 */
	@Override
	public boolean hasMethodAnnotation(final Class<? extends Annotation> annotationType,
			final Method method)
	{
		if (instanceOf(annotationType, XmlTransient.class))
		{
			if (transientMethods.contains(method))
			{
				return true;
			}

		}
		return delegate.hasMethodAnnotation(annotationType, method);
	}

	/**
	 * @param <A>
	 * @param annotationType
	 * @param method
	 * @param srcPos
	 * @return
	 * @see com.sun.xml.bind.v2.model.annotation.AnnotationReader#getMethodAnnotation(java.lang.Class,
	 *      M, com.sun.xml.bind.v2.model.annotation.Locatable)
	 */
	@Override
	public <A extends Annotation> A getMethodAnnotation(final Class<A> annotationType,
			final Method method, final Locatable srcPos)
	{
		if (instanceOf(annotationType, XmlTransient.class))
		{
			if (transientMethods.contains(method))
			{
				return (A) XML_TRANSIENT_ANNOTATION;
			}

		}
		return delegate.getMethodAnnotation(annotationType, method, srcPos);
	}

	/**
	 * @param method
	 * @param srcPos
	 * @return
	 * @see com.sun.xml.bind.v2.model.annotation.AnnotationReader#getAllMethodAnnotations(java.lang.Object,
	 *      com.sun.xml.bind.v2.model.annotation.Locatable)
	 */
	@Override
	public Annotation[] getAllMethodAnnotations(final Method method,
			final Locatable srcPos)
	{
		if (transientMethods.contains(method))
			return XML_TRANSIENT_ANNOTATION_ONLY;

		return delegate.getAllMethodAnnotations(method, srcPos);
	}

	// default

	/**
	 * @param <A>
	 * @param annotation
	 * @param method
	 * @param paramIndex
	 * @param srcPos
	 * @return
	 * @see com.sun.xml.bind.v2.model.annotation.AnnotationReader#getMethodParameterAnnotation(java.lang.Class,
	 *      M, int, com.sun.xml.bind.v2.model.annotation.Locatable)
	 */
	@Override
	public <A extends Annotation> A getMethodParameterAnnotation(
			final Class<A> annotation, final Method method, final int paramIndex,
			final Locatable srcPos)
	{
		return delegate.getMethodParameterAnnotation(annotation, method, paramIndex,
				srcPos);
	}

	/**
	 * @param <A>
	 * @param a
	 * @param clazz
	 * @param srcPos
	 * @return
	 * @see com.sun.xml.bind.v2.model.annotation.AnnotationReader#getPackageAnnotation(java.lang.Class,
	 *      C, com.sun.xml.bind.v2.model.annotation.Locatable)
	 */
	@Override
	public <A extends Annotation> A getPackageAnnotation(final Class<A> a,
			final Class clazz, final Locatable srcPos)
	{
		return delegate.getPackageAnnotation(a, clazz, srcPos);
	}

	/**
	 * @param a
	 * @param name
	 * @return
	 * @see com.sun.xml.bind.v2.model.annotation.AnnotationReader#getClassValue(java.lang.annotation.Annotation,
	 *      java.lang.String)
	 */
	@Override
	public Class getClassValue(final Annotation a, final String name)
	{
		return delegate.getClassValue(a, name);
	}

	/**
	 * @param a
	 * @param name
	 * @return
	 * @see com.sun.xml.bind.v2.model.annotation.AnnotationReader#getClassArrayValue(java.lang.annotation.Annotation,
	 *      java.lang.String)
	 */
	@Override
	public Class[] getClassArrayValue(final Annotation a, final String name)
	{
		return delegate.getClassArrayValue(a, name);
	}

	/**
	 * @param m
	 * @return
	 * @see com.sun.xml.bind.v2.model.annotation.AbstractInlineAnnotationReaderImpl#fullName(java.lang.Object)
	 */
	@Override
	protected String fullName(final Method m)
	{
		// same as RuntimeInlineAnnotationReader.fullName()
		return m.getDeclaringClass().getName() + '#' + m.getName();
	}

}
