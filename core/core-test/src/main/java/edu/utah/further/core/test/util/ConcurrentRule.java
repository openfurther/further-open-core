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
package edu.utah.further.core.test.util;

import java.util.concurrent.CountDownLatch;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import edu.utah.further.core.test.annotation.Concurrent;

/**
 * Suppose you want to run some concurrency test: you may need to execute your test method
 * on 15 threads each starting at the same time, and then wait for all threads to finish.
 * All this plumbing can now be resumed by the {@link Concurrent} annotation.
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
 * @version Aug 10, 2010
 */
public final class ConcurrentRule implements TestRule
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.junit.rules.TestRule#apply(org.junit.runners.model.Statement,
	 * org.junit.runner.Description)
	 */
	@Override
	public Statement apply(final Statement statement, final Description description)
	{
		// ========================= IMPL: TestRule ==========================

		return new Statement()
		{
			@Override
			public void evaluate() throws Throwable
			{
				final Concurrent concurrent = description.getAnnotation(Concurrent.class);
				if (concurrent == null)
				{
					statement.evaluate();
				}
				else
				{
					final String name = description.getMethodName();
					final Thread[] threads = new Thread[concurrent.value()];
					final CountDownLatch go = new CountDownLatch(1);
					final CountDownLatch finished = new CountDownLatch(threads.length);
					for (int i = 0; i < threads.length; i++)
					{
						threads[i] = new Thread(new Runnable()
						{
							@Override
							public void run()
							{
								try
								{
									go.await();
									statement.evaluate();
								}
								catch (final InterruptedException e)
								{
									Thread.currentThread().interrupt();
								}
								catch (final Throwable throwable)
								{
									if (throwable instanceof RuntimeException)
										throw (RuntimeException) throwable;
									if (throwable instanceof Error)
										throw (Error) throwable;
									final RuntimeException r = new RuntimeException(
											throwable.getMessage(), throwable);
									r.setStackTrace(throwable.getStackTrace());
									throw r;
								}
								finally
								{
									finished.countDown();
								}
							}
						}, name + "-Thread-" + i);
						threads[i].start();
					}
					go.countDown();
					finished.await();
				}
			}
		};
	}
}
