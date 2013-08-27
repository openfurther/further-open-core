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
package edu.utah.further.core.test.parameterized;

import java.lang.reflect.Modifier;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * <p>
 * The custom runner <code>Parameterized</code> implements parameterized tests. When
 * running a parameterized test class, instances are created for the cross-product of the
 * test methods and the test data elements.
 * </p>
 *
 * For example, to test a Fibonacci function, write:
 *
 * <pre>
 * &#064;RunWith(Parameterized.class)
 * public class FibonacciTest
 * {
 * 	&#064;Parameters
 * 	public static List&lt;Object[]&gt; data()
 * 	{
 * 		return Arrays.asList(new Object[][]
 * 		{ Fibonacci,
 * 		{
 * 		{ 0, 0 },
 * 		{ 1, 1 },
 * 		{ 2, 1 },
 * 		{ 3, 2 },
 * 		{ 4, 3 },
 * 		{ 5, 5 },
 * 		{ 6, 8 } } });
 * 	}
 *
 * 	private int fInput;
 *
 * 	private int fExpected;
 *
 * 	public FibonacciTest(int input, int expected)
 * 	{
 * 		fInput = input;
 * 		fExpected = expected;
 * 	}
 *
 * 	&#064;Test
 * 	public void test()
 * 	{
 * 		assertEquals(fExpected, Fibonacci.compute(fInput));
 * 	}
 * }
 * </pre>
 *
 * <p>
 * Each instance of <code>FibonacciTest</code> will be constructed using the two-argument
 * constructor and the data values in the <code>&#064;Parameters</code> method.
 * </p>
 */
public final class FlattenedParameterized extends Suite
{

	private RunnerScheduler fScheduler = new RunnerScheduler()
	{
		@Override
		public void schedule(final Runnable childStatement)
		{
			childStatement.run();
		}

		@Override
		public void finished()
		{
			// do nothing
		}
	};

	/**
	 * Sets a scheduler that determines the order and parallelization of children. Highly
	 * experimental feature that may change.
	 */
	@Override
	public void setScheduler(final RunnerScheduler scheduler)
	{
		this.fScheduler = scheduler;
	}

	private class TestClassRunnerForParameters extends BlockJUnit4ClassRunner
	{
		private final int fParameterSetNumber;

		private final List<Object[]> fParameterList;

		TestClassRunnerForParameters(final Class<?> type,
				final List<Object[]> parameterList, final int i)
				throws InitializationError
		{
			super(type);
			fParameterList = parameterList;
			fParameterSetNumber = i;
		}

		@Override
		public Object createTest() throws Exception
		{
			return getTestClass().getOnlyConstructor().newInstance(computeParams());
		}

		private Object[] computeParams() throws Exception
		{
			try
			{
				return fParameterList.get(fParameterSetNumber);
			}
			catch (final ClassCastException e)
			{
				throw new Exception(
						String.format("%s.%s() must return a Collection of arrays.",
								getTestClass().getName(),
								getParametersMethod(getTestClass()).getName()));
			}
		}

		@SuppressWarnings("boxing")
		@Override
		protected String getName()
		{
			return String.format("[%s]", fParameterSetNumber);
		}

		@SuppressWarnings("boxing")
		@Override
		protected String testName(final FrameworkMethod method)
		{
			return String.format("%s[%c]", method.getName(),
					(char) ('A' + fParameterSetNumber));
		}

		@Override
		protected void validateConstructor(final List<Throwable> errors)
		{
			validateOnlyOneConstructor(errors);
		}

		@Override
		protected Statement classBlock(final RunNotifier notifier)
		{
			return childrenInvoker(notifier);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.junit.runners.BlockJUnit4ClassRunner#getChildren()
		 */
		@Override
		public List<FrameworkMethod> getChildren()
		{
			return super.getChildren();
		}

		/*
		 * (non-Javadoc)
		 * @see org.junit.runners.BlockJUnit4ClassRunner#runChild(org.junit.runners.model.FrameworkMethod, org.junit.runner.notification.RunNotifier)
		 */
		@Override
		public void runChild(final FrameworkMethod method, final RunNotifier notifier)
		{
			super.runChild(method, notifier);
		}

		private List<FrameworkMethod> getFilteredChildren()
		{
			return getChildren();
		}
	}

	// private final List<FrameworkMethod> runners = CollectionUtil.newList();
	private final List<Runner> runners = CollectionUtil.newList();

	/**
	 * Only called reflectively. Do not use programmatically.
	 */
	public FlattenedParameterized(final Class<?> klass) throws Throwable
	{
		// super(klass);
		super(klass, CollectionUtil.<Runner> newList());
		final List<Object[]> parametersList = getParametersList(getTestClass());
		for (int i = 0; i < parametersList.size(); i++)
		{
			final TestClassRunnerForParameters runnerForParameters = new TestClassRunnerForParameters(
					getTestClass().getJavaClass(), parametersList, i);
			runners.add(runnerForParameters);
			// runners.addAll(runnerForParameters.getChildren());
		}
	}

	@Override
	// protected List<FrameworkMethod> getChildren()
	protected List<Runner> getChildren()
	{
		return runners;
	}

	// @Override
	// protected void collectInitializationErrors(final List<Throwable> errors)
	// {
	// validatePublicVoidNoArgMethods(BeforeClass.class, true, errors);
	// validatePublicVoidNoArgMethods(AfterClass.class, true, errors);
	//
	// validateOnlyOneConstructor(errors);
	// // validateConstructor(errors);
	// // validateInstanceMethods(errors);
	// validateFields(errors);
	// }
	//
	// private List<FrameworkField> ruleFields()
	// {
	// return getTestClass().getAnnotatedFields(Rule.class);
	// }
	//
	// private void validateFields(final List<Throwable> errors)
	// {
	// for (final FrameworkField each : ruleFields())
	// validateRuleField(each.getField(), errors);
	// }
	//
	// private void validateRuleField(final Field field, final List<Throwable> errors)
	// {
	// if (!MethodRule.class.isAssignableFrom(field.getType()))
	// errors.add(new Exception("Field " + field.getName()
	// + " must implement MethodRule"));
	// if (!Modifier.isPublic(field.getModifiers()))
	// errors.add(new Exception("Field " + field.getName() + " must be public"));
	// }

	private List<Runner> getFilteredChildren()
	{
		return getChildren();
	}

	/**
	 * Returns a {@link Statement}: Call {@link #runChild(Object, RunNotifier)} on each
	 * object returned by {@link #getChildren()} (subject to any imposed filter and sort)
	 */
	@Override
	protected Statement childrenInvoker(final RunNotifier notifier)
	{
		return new Statement()
		{
			@Override
			public void evaluate()
			{
				runChildren(notifier);
			}
		};
	}

	private void runChildren(final RunNotifier notifier)
	{
		for (final Runner each : getFilteredChildren())
		{
			final TestClassRunnerForParameters childRunner = (TestClassRunnerForParameters) each;
			for (final FrameworkMethod method : childRunner.getFilteredChildren())
			{
				fScheduler.schedule(new Runnable()
				{
					@Override
					public void run()
					{
						childRunner.runChild(method, notifier);
						// runChild(method, notifier);
					}
				});
			}
		}
		fScheduler.finished();
	}

	private List<Object[]> getParametersList(final TestClass klass) throws Throwable
	{
		return (List<Object[]>) getParametersMethod(klass).invokeExplosively(null);
	}

	private FrameworkMethod getParametersMethod(final TestClass testClass)
			throws Exception
	{
		final List<FrameworkMethod> methods = testClass
				.getAnnotatedMethods(Parameters.class);
		for (final FrameworkMethod each : methods)
		{
			final int modifiers = each.getMethod().getModifiers();
			if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))
				return each;
		}

		throw new Exception("No public static parameters method on class "
				+ testClass.getName());
	}

}
