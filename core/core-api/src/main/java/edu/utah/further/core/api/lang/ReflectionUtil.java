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
package edu.utah.further.core.api.lang;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.collections.CollectionUtil.newMap;
import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static edu.utah.further.core.api.message.Messages.cannotConvertMessage;
import static edu.utah.further.core.api.message.Messages.unsupportedMessage;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.exception.BusinessRuleException;

/**
 * Utilities related to reflection, class types and generic class types.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}, N. Dustin Schultz
 *         {@code <dustin.schultz@utah.edu>}
 * @version Oct 13, 2008
 */
@Api
public final class ReflectionUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * Type of executing a method set on a type (execute super-class methods before or
	 * after this object's methods).
	 */
	public enum ExecutionPhase
	{
		ARBITRARY, SUPER_BEFORE_THIS, SUPER_AFTER_THIS;
	}

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private ReflectionUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Get the underlying class for a type, or <code>null</code> if the type is a variable
	 * type.
	 *
	 * @param type
	 *            the type
	 * @return the underlying class
	 * @author Ian Robertson
	 * @see http://www.artima.com/weblogs/viewpost.jsp?thread=208860
	 */
	public static Class<?> getClass(final Type type)
	{
		if (instanceOf(type, Class.class))
		{
			return (Class<?>) type;
		}
		else if (instanceOf(type, ParameterizedType.class))
		{
			return getClass(((ParameterizedType) type).getRawType());
		}
		else if (instanceOf(type, GenericArrayType.class))
		{
			final Type componentType = ((GenericArrayType) type)
					.getGenericComponentType();
			final Class<?> componentClass = getClass(componentType);
			if (componentClass != null)
			{
				return Array.newInstance(componentClass, 0).getClass();
			}
			return null;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Get the actual type arguments a child class has used to extend a generic base
	 * class.
	 *
	 * @param baseClass
	 *            the base class
	 * @param childClass
	 *            the child class
	 * @return a list of the raw classes for the actual type arguments.
	 * @see http://www.artima.com/weblogs/viewpost.jsp?thread=208860
	 */
	public static <T> List<Class<?>> getSuperclassTypeArguments(final Class<T> baseClass,
			final Class<? extends T> childClass)
	{
		final Map<Type, Type> resolvedTypes = newMap();
		Type type = childClass;

		// Start walking up the inheritance hierarchy until we hit baseClass
		type = walkSuperclassHierarchyAndResolveTypes(baseClass, resolvedTypes, type);
		if (type == null)
		{
			throw new ApplicationException(baseClass + " is not a base class of "
					+ childClass);
		}

		return getTypeArgumentsAsClasses(resolvedTypes, type);
	}

	/**
	 * Get the actual type arguments a child class has used to extend a generic interface.
	 *
	 * @param baseInterface
	 *            the base interface
	 * @param childClass
	 *            the child class
	 * @return a list of the raw classes for the actual type arguments.
	 * @see http://www.artima.com/weblogs/viewpost.jsp?thread=208860
	 */
	public static <T> List<Class<?>> getInterfaceTypeArguments(
			final Class<T> baseInterface, final Class<? extends T> childClass)
	{
		final Map<Type, Type> resolvedTypes = newMap();
		Type type = childClass;

		// Start walking up the inheritance hierarchy until we hit baseClass
		type = walkInterfaceHierarchyAndResolveTypes(baseInterface, resolvedTypes, type);

		if (type == null)
		{
			throw new ApplicationException(baseInterface + " is not a base interface of "
					+ childClass);
		}
		return getTypeArgumentsAsClasses(resolvedTypes, type);
	}

	/**
	 * Return the list of methods that implement an annotation in a class.
	 *
	 * @param targetClass
	 *            type whose methods are scanned
	 * @param annotationClass
	 *            annotation type to look for
	 * @param executionType
	 *            type of executing a method set on a type (execute super-class methods
	 *            before or after this object's methods)
	 * @return list of methods that implement the annotation in the class in the order
	 *         specified by <code>executionType</code>
	 */
	public static List<Method> getAnnotatedMethods(final Class<?> targetClass,
			final Class<? extends Annotation> annotationClass,
			final ExecutionPhase executionType)
	{
		switch (executionType)
		{
			case ARBITRARY:
			{
				return getUnorderedAnnotatedMethods(targetClass, annotationClass);
			}

			case SUPER_BEFORE_THIS:
			case SUPER_AFTER_THIS:
			{
				return getOrderedAnnotatedMethods(targetClass, annotationClass,
						executionType);
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(executionType));
			}
		}
	}

	/**
	 * Execute annotated methods in a target class. All methods are executed with the same
	 * argument list.
	 *
	 * @param target
	 *            object whose methods are invoked
	 * @param annotationClass
	 *            methods with this annotation will be executed. Assumed to be no-argument
	 *            methods.
	 * @param executionType
	 *            type of executing a method set on a type (execute super-class methods
	 *            before or after this object's methods)
	 * @param args
	 *            the arguments used for the method call
	 * @return the result of dispatching the method represented by this object on
	 *         <code>obj</code> with parameters <code>args</code>
	 */
	public static void executeMethods(final Object target,
			final Class<? extends Annotation> annotationClass,
			final ExecutionPhase executionType, final Object... args)
	{
		final String annotationName = "@" + annotationClass.getSimpleName();
		final List<Method> methods = ReflectionUtil.getAnnotatedMethods(
				target.getClass(), annotationClass, executionType);
		for (final Method method : methods)
		{
			try
			{
				method.invoke(target, args);
			}
			catch (final Exception e)
			{
				throw new ApplicationException(annotationName + " Method " + method
						+ " failed to execute: " + e.toString(), e);
			}
		}
	}

	/**
	 * Check if an object's class is an instance of a type.
	 *
	 * @param clazz
	 *            object class to verify
	 * @param type
	 *            a type
	 * @return <code>true</code> if and only if <code>o</code> is an instance of
	 *         <code>type</code>. If <code>type</code> is <code>null</code>, returns
	 *         <code>false</code>
	 *
	 */
	public static boolean classIsInstanceOf(final Class<?> clazz, final Class<?> type)
	{
		return ((clazz == null) || (type == null)) ? false : type.isAssignableFrom(clazz);
	}

	/**
	 * Check if an object is an instance of a type.
	 *
	 * @param o
	 *            object to verify
	 * @param type
	 *            a type
	 * @return <code>true</code> if and only if <code>o</code> is an instance of
	 *         <code>type</code>. If <code>type</code> is <code>null</code>, returns
	 *         <code>false</code>
	 *
	 */
	public static boolean instanceOf(final Object o, final Class<?> type)
	{
		return ((o == null) || (type == null)) ? false : type.isAssignableFrom(o
				.getClass());
	}

	/**
	 * Check if that an object is an instance of one of several types.
	 *
	 * @param o
	 *            object to verify
	 * @param types
	 *            object must be of one of the types in this array
	 * @return <code>true</code> if and only if <code>o</code> is an instance of
	 *         <code>type</code>. If <code>type</code> is <code>null</code>, returns
	 *         <code>false</code>
	 *
	 */
	public static boolean instanceOfOneOf(final Object o, final Class<?>... types)
	{
		for (final Class<?> type : types)
		{
			if (instanceOf(o, type))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Verify that an object is an instance of one of several types.
	 *
	 * @param o
	 *            object to verify
	 * @param types
	 *            object must be of one of the types in this array
	 */
	public static void verifyType(final Object o, final Class<?>... types)
	{
		if (!instanceOfOneOf(o, types))
		{
			// No matching type found
			throw new BusinessRuleException(cannotConvertMessage(o.getClass().toString(),
					types.toString()));
		}
	}

	/**
	 * Returns a method (public or private) object for the given methodName if it exists.
	 * Note, this method wraps all exceptions as {@link RuntimeException}'s and it is the
	 * responsibility of the caller to decide whether or not to catch such exception, by
	 * default it is an unchecked exception.
	 *
	 * @param methodName
	 *            the name of the method
	 * @param obj
	 *            the target object
	 * @param argTypes
	 *            the argument class types
	 * @return a {@link Method} object representing the method
	 */
	public static Method getMethod(final String methodName, final Object obj,
			final Class<?>... argTypes)
	{
		Method method = null;
		try
		{
			method = obj.getClass().getDeclaredMethod(methodName, argTypes);
			method.setAccessible(true);
		}
		catch (final SecurityException e)
		{
			throw new RuntimeException(e);
		}
		catch (final NoSuchMethodException e)
		{
			throw new RuntimeException(e);
		}

		return method;
	}

	/**
	 * Invokes a method using reflection. Note that this method wraps all exceptions as
	 * unchecked {@link RuntimeException}'s, and that it is caller's responsibility to
	 * handle every such exception.
	 *
	 * @param method
	 *            the method to invoke
	 * @param obj
	 *            the object to invoke the method on
	 * @param args
	 *            any arguments to the method
	 * @return the result
	 * @throws RuntimeException
	 *             if invocation fails
	 */
	public static Object invoke(final Method method, final Object obj,
			final Object... args)
	{
		try
		{
			return method.invoke(obj, args);
		}
		catch (final IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch (final IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (final InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get the field represented by the supplied field name on the specified
	 * {@link Object target object}. In accordance with {@link Field#get(Object)}
	 * semantics, the returned value is automatically wrapped if the underlying field has
	 * a primitive type.
	 *
	 * @param fieldValue
	 *            new field value
	 * @param declaringClass
	 *            class declaring the field. May be a super-class or a super-interface of
	 *            <code>target.getClass()</code>
	 * @param target
	 *            the target object from which to get the field
	 * @return the field's current value
	 */
	public static Object getField(final Object target, final Class<?> declaringClass,
			final String fieldName)
	{
		return AccessController.doPrivileged(new PrivilegedAction<Object>()
		{
			@Override
			public Object run()
			{
				synchronized (declaringClass)
				{
					Field field;
					try
					{
						field = declaringClass.getDeclaredField(fieldName);
						final boolean accessible = field.isAccessible();
						field.setAccessible(true);
						final Object value = field.get(target);
						field.setAccessible(accessible);
						return value;
					}
					catch (final IllegalArgumentException e)
					{
						throw new RuntimeException(e);
					}
					catch (final IllegalAccessException e)
					{
						throw new RuntimeException(e);
					}
					catch (final SecurityException e)
					{
						throw new RuntimeException(e);
					}
					catch (final NoSuchFieldException e)
					{
						throw new RuntimeException(e);
					}
				}
			}
		});
	}

	/**
	 * Set a field on an object using reflection. No setter required - the field is
	 * accessed directly. Note that this method wraps all exceptions as unchecked
	 * {@link RuntimeException}'s, and that it is caller's responsibility to handle every
	 * such exception.
	 *
	 * @param obj
	 *            object with a field in it
	 * @param fieldName
	 *            field name
	 * @param fieldValue
	 *            new field value
	 * @throws RuntimeException
	 *             if invocation fails
	 */
	public static void setField(final Object obj, final String fieldName,
			final Object fieldValue)
	{
		AccessController.doPrivileged(new PrivilegedAction<Object>()
		{
			@Override
			public Object run()
			{
				final Class<?> clazz = obj.getClass();
				// We change the field from some accessibility level -> public -> same
				// accessibility level, so synchronize the class
				synchronized (clazz)
				{
					Field field;
					try
					{
						field = clazz.getDeclaredField(fieldName);
						final boolean accessible = field.isAccessible();
						field.setAccessible(true);
						field.set(obj, fieldValue);
						field.setAccessible(accessible);
					}
					catch (final SecurityException e)
					{
						throw new RuntimeException(e);
					}
					catch (final NoSuchFieldException e)
					{
						throw new RuntimeException(e);
					}
					catch (final IllegalArgumentException e)
					{
						throw new RuntimeException(e);
					}
					catch (final IllegalAccessException e)
					{
						throw new RuntimeException(e);
					}
				}
				return null; // nothing to return
			}
		});
	}

	/**
	 * Return the unique sub-class of a base class from a set of classes. This assumes
	 * that "sub-class-of" is a reflexive relation, so the base class will be returned if
	 * it belongs to the set.
	 *
	 * @param <T>
	 *            base class type
	 * @param <S>
	 *            a helper type to capture wildcard of <code>classes</code>
	 * @param classes
	 *            search space
	 * @param baseClass
	 *            base class <code>baseClass</code> in the <code>classes</code> set
	 * @throws ApplicationException
	 *             if no sub-class or more than one sub-class if found
	 * @return the sole sub-class of <code>baseClass</code> in the <code>classes</code>
	 *         set
	 */
	@SuppressWarnings("unchecked")
	public static <T, S> Class<? extends T> getUniqueSubclassInSet(
			final Set<Class<? extends S>> classes, final Class<T> baseClass)
	{
		Class<?> found = null;
		for (final Class<?> candidate : classes)
		{
			if (baseClass.isAssignableFrom(candidate))
			{
				if (found != null)
				{
					// Discovered second candidate
					throw new ApplicationException("Multiple classes " + found + ","
							+ candidate + " of type " + baseClass
							+ " was found in the set " + classes);
				}
				// Discovered first candidate
				found = candidate;
			}
		}

		// No candidates found
		if (found == null)
		{
			throw new ApplicationException("No class of type " + baseClass
					+ " was found in the set " + classes);
		}
		// We know it's a sub-class of baseClass even though this raises an unchecked
		// exception
		return (Class<? extends T>) found;
	}

	/**
	 * Leads to OSGi classpath problems.
	 *
	 * @param <T>
	 * @param fullyQualifiedClass
	 * @return
	 */
	public static <T> T newInstance(final String fullyQualifiedClass)
	{
		try
		{
			return (T) Thread
					.currentThread()
					.getContextClassLoader()
					.loadClass(fullyQualifiedClass)
					.newInstance();
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Loads the given class using the context class loader
	 * 
	 * @return
	 */
	public static Class<?> loadClass(final String clazz)
	{
		Class<?> clazzObject;
		try
		{
			clazzObject = Thread.currentThread().getContextClassLoader().loadClass(clazz);
		}
		catch (final ClassNotFoundException e)
		{
			throw new RuntimeException("Unable to load " + clazz, e);
		}
		return clazzObject;
	}

	/**
	 * Retrieves all interfaces implemented by a specified interface or interfaces,
	 * including all recursively extended interfaces.
	 *
	 * @param childInterfaces
	 *            a set of interfaces
	 * @return an an <b>unordered</b> set of interfaces that includes those specified in
	 *         childInterfaces plus all of those interfaces' super interfaces
	 * @see http://www.java2s.com/Code/Java/Reflection/
	 *      Getallinterfaceandobjectclassesthataregeneralizationsoftheprovidedclass.htm
	 */
	public static Set<Class<?>> getSuperInterfaces(final Class<?>... childInterfaces)
	{
		final Set<Class<?>> allInterfaces = CollectionUtil.newSet();
		for (final Class<?> childInterface : childInterfaces)
		{
			allInterfaces.add(childInterface);
			allInterfaces.addAll(getSuperInterfaces(childInterface.getInterfaces()));
		}
		return allInterfaces;
	}

	/**
	 * Builds an <b>unordered</b> set of all interface and object classes that are
	 * generalizations of the provided class.
	 *
	 * @param classObject
	 *            the class to find generalization of
	 * @return a Set of class objects
	 * @see http://www.java2s.com/Code/Java/Reflection/
	 *      Getallinterfaceandobjectclassesthataregeneralizationsoftheprovidedclass.htm
	 */
	public static Set<Class<?>> getGeneralizations(final Class<?> classObject)
	{
		final Set<Class<?>> generalizations = CollectionUtil.newSet();
		generalizations.add(classObject);

		final Class<?> superClass = classObject.getSuperclass();
		if (superClass != null)
		{
			generalizations.addAll(getGeneralizations(superClass));
		}

		for (final Class<?> superInterface : classObject.getInterfaces())
		{
			generalizations.addAll(getGeneralizations(superInterface));
		}

		return generalizations;
	}

	/**
	 * Check if a class is a generalization of another.
	 *
	 * @param clazz
	 *            sub-class
	 * @param prospectiveSuperClass
	 *            super-class
	 * @return <code>true</code> if and only if <code>code</code> is a generalization of
	 *         <code>clazz</code>
	 */
	public static boolean isGeneralizationOf(final Class<?> clazz,
			final Class<?> prospectiveSuperClass)
	{
		// TODO: cache result of getGeneralizations() in a static map instead of
		// regenerating it in every call to this method
		return getGeneralizations(clazz).contains(prospectiveSuperClass);
	}

	/**
	 * Return the list of methods that implement an annotation in a class. Methods are not
	 * sorted in any particular order. This is a slightly faster implementation that uses
	 * {@link Class#getMethods()} insteadof {@link Class#getDeclaredMethods()}.
	 *
	 * @param targetClass
	 *            type whose methods are scanned
	 * @param annotationClass
	 *            annotation type to look for
	 * @return list of methods that implement the annotation in the class including its
	 *         super-classes
	 */
	public static List<Method> getUnorderedAnnotatedMethods(final Class<?> targetClass,
			final Class<? extends Annotation> annotationClass)
	{
		// log.debug("Finding methods annotated with @"
		// + annotationClass.getSimpleName());
		final List<Method> methods = newList();
		for (final Method method : targetClass.getMethods())
		{
			// log.debug("Method: " + method.getName() + " annotated? "
			// + (method.getAnnotation(annotationClass) != null));
			if (method.getAnnotation(annotationClass) != null)
			{
				methods.add(method);
			}
		}
		return methods;
	}

	/**
	 * Return the list of methods that implement an annotation in a class.
	 *
	 * @param targetClass
	 *            type whose methods are scanned
	 * @param annotationClass
	 *            annotation type to look for
	 * @param executionType
	 *            type of executing a method set on a type (execute super-class methods
	 *            before or after this object's methods)
	 * @return list of methods that implement the annotation in the class in the order
	 *         specified by <code>executionType</code>
	 */
	public static List<Method> getOrderedAnnotatedMethods(final Class<?> targetClass,
			final Class<? extends Annotation> annotationClass,
			final ExecutionPhase executionType)
	{
		// log.debug("Finding methods annotated with @"
		// + annotationClass.getSimpleName());
		final List<Method> methods = newList();

		// Loop over super-types of the target class (inclusive)
		Class<?> superclass = targetClass;
		do
		{
			// Find all applicable methods of superclass
			final List<Method> superClassMethods = newList();
			for (final Method method : superclass.getDeclaredMethods())
			{
				// log.debug("Method: " + method.getName() + " annotated? "
				// + (method.getAnnotation(annotationClass) != null));
				if (method.getAnnotation(annotationClass) != null)
				{
					superClassMethods.add(method);
				}
			}

			// Add the superclass methods to the global list -- either at the
			// beginning or the end, depending on the requested order
			if (executionType == ExecutionPhase.SUPER_BEFORE_THIS)
			{
				methods.addAll(0, superClassMethods);
			}
			else
			{
				methods.addAll(superClassMethods);
			}

			superclass = superclass.getSuperclass();
		}
		while (superclass != null);

		return methods;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param <T>
	 * @param baseClass
	 * @param resolvedTypes
	 * @param type
	 * @return
	 */
	private static <T> Type walkSuperclassHierarchyAndResolveTypes(
			final Class<T> baseClass, final Map<Type, Type> resolvedTypes,
			final Type currentType)
	{
		Type type = currentType;
		while (type != null)
		{
			// If type is parametric, resolve parameters and break if this is the base
			// class; if it is not the base class or is a raw type, keep going up the
			// inheritance hierarchy.
			final Class<?> rawType = instanceOf(type, ParameterizedType.class) ? resolveParameterizedType(
					resolvedTypes, type) : (Class<?>) type;

			if ((rawType == null) || rawType.equals(baseClass))
			{
				// Also break if reached top of class hierarchy (rawType == null) and
				// didn't find the base class
				return rawType;
			}

			// Keep going
			type = rawType.getGenericSuperclass();
		}

		return null;
	}

	/**
	 * @param <T>
	 * @param baseInterface
	 * @param resolvedTypes
	 * @param type
	 * @return
	 */
	private static <T> Type walkInterfaceHierarchyAndResolveTypes(
			final Class<T> baseInterface, final Map<Type, Type> resolvedTypes,
			final Type currentType)
	{
		if (currentType != null)
		{
			// If type is parametric, resolve parameters and break if this is the base
			// class; if it is not the base class or is a raw type, keep going up the
			// inheritance hierarchy.
			final Class<?> rawType = instanceOf(currentType, ParameterizedType.class) ? resolveParameterizedType(
					resolvedTypes, currentType) : (Class<?>) currentType;

			if ((rawType == null) || rawType.equals(baseInterface))
			{
				// Also break if reached top of class hierarchy (rawType == null) and
				// didn't find the base class
				return rawType;
			}

			// Keep searching currentType's interfaces and super-class
			final List<Type> superTypes = newList();
			superTypes.add(rawType.getGenericSuperclass());
			superTypes.addAll(CollectionUtil.newList(rawType.getGenericInterfaces()));

			for (final Type parentType : superTypes)
			{
				final Type resolvedType = walkInterfaceHierarchyAndResolveTypes(
						baseInterface, resolvedTypes, parentType);
				if (resolvedType != null)
				{
					// Found a direct super-interface of currentType that matches
					// baseClass
					return resolvedType;
				}
			}
		}

		return null;
	}

	/**
	 * @param resolvedTypes
	 * @param type
	 * @return
	 */
	private static Class<?> resolveParameterizedType(final Map<Type, Type> resolvedTypes,
			final Type type)
	{
		final ParameterizedType parameterizedType = (ParameterizedType) type;
		final Class<?> rawType = (Class<?>) parameterizedType.getRawType();
		final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
		final TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
		for (int i = 0; i < actualTypeArguments.length; i++)
		{
			resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
		}
		return rawType;
	}

	/**
	 * @param resolvedTypes
	 * @param type
	 * @return
	 */
	private static List<Class<?>> getTypeArgumentsAsClasses(
			final Map<Type, Type> resolvedTypes, final Type type)
	{
		// Finally, for each actual type argument provided to baseClass, determine (if
		// possible) the raw class for that type argument.
		final Type[] actualTypeArguments = instanceOf(type, Class.class) ? ((Class<?>) type)
				.getTypeParameters() : ((ParameterizedType) type)
				.getActualTypeArguments();

		// Resolve types by chasing down type variables.
		final List<Class<?>> typeArgumentsAsClasses = newList();
		for (Type baseType : actualTypeArguments)
		{
			while (resolvedTypes.containsKey(baseType))
			{
				baseType = resolvedTypes.get(baseType);
			}
			typeArgumentsAsClasses.add(getClass(baseType));
		}
		return typeArgumentsAsClasses;
	}

}
