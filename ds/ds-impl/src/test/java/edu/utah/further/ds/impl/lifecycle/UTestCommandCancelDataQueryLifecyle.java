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
package edu.utah.further.ds.impl.lifecycle;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.utah.further.ds.impl.fixture.DsImplFixture;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.QueryState;
import edu.utah.further.fqe.ds.api.service.CommandCancel;
import edu.utah.further.fqe.ds.api.service.CommandTrigger;
import edu.utah.further.fqe.ds.api.to.QueryContextTo;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;

/**
 * Unit test for canceling a data query life cycle.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Nov 15, 2010
 */
public final class UTestCommandCancelDataQueryLifecyle extends DsImplFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(UTestCommandCancelDataQueryLifecyle.class);

	// ========================= FIELDS ====================================

	/**
	 * The DQL
	 */
	@Autowired
	@Qualifier("dataQueryLifeCycle")
	private DataQueryLifeCycle lifeCycle;

	/**
	 * The thread pool.
	 */
	private final ExecutorService pool = Executors.newFixedThreadPool(2);

	// ========================= TESTS ====================================

	/**
	 * Cancel the {@link DataQueryLifeCycle}
	 *
	 * @throws InterruptedException
	 */
	@Test
	public void cancelDataQueryLifecycle()
	{
		final QueryContextTo queryContext = QueryContextToImpl.newInstance(1337);
		queryContext.setState(QueryState.QUEUED);

		// Start a DataQueryLifecycle
		final DataQueryTriggerThread dataQueryThread = new DataQueryTriggerThread(
				queryContext);
		pool.submit(dataQueryThread);

		// Wait for the life cycle to start and a few processors to run
		sleep(8000);

		// In another thread, cancel the life cycle (e.g. another Camel route)
		final DataQueryCancelThread dataQueryCancelThread = new DataQueryCancelThread(
				queryContext.getId());
		final Future<?> cancelFuture = pool.submit(dataQueryCancelThread);

		// Wait for a signal from the life cycle that it has returned (indicating it was
		// canceled) or fail if it doesn't after maxMilli
		waitForLifecycleOrFail(15000);

		// Wait for the cancel thread to finish and fail if for some reason it doesn't
		// after maxMilli
		waitForFutureOrFail(cancelFuture, 15000);

		// Clean up
		pool.shutdown();

		assertTrue(queryContext.isStopped());
	}

	// ========================= PRIVATE METHODS ====================================

	/**
	 * Sleep helper
	 */
	private void sleep(final long milli)
	{
		try
		{
			Thread.sleep(milli);
		}
		catch (final InterruptedException e)
		{
			// Ignore in testing
		}
	}

	/**
	 * Waits for a {@link Future} to return
	 *
	 * @param future
	 */
	private void waitForFutureOrFail(final Future<?> future, final long maxWaitMilli)
	{
		boolean waiting = true;
		while (waiting)
		{
			try
			{
				future.get(maxWaitMilli, TimeUnit.MILLISECONDS);
				waiting = false;
			}
			catch (final InterruptedException e)
			{
				// Ignore in testing, continue waiting
			}
			catch (final ExecutionException e)
			{
				// Ignore in testing, continue waiting
			}
			catch (final TimeoutException e)
			{
				fail("Waiting for future timedout.");
			}
		}
	}

	/**
	 * Waits for the Life cycle to signal that it's finished
	 *
	 * @param maxWaitMilli
	 *            the maximum milliseconds to wait for a notification
	 */
	private void waitForLifecycleOrFail(final long maxWaitMilli)
	{
		synchronized (lifeCycle)
		{
			try
			{
				// Wait for signal from DataQueryTriggerThread
				// Other threads could wake up this wake but for testing purposes we'll
				// assume only the DataQueryTriggerThread does
				lifeCycle.wait(maxWaitMilli);
			}
			catch (final InterruptedException e)
			{
				fail("Thread never received notification that it was finished");
			}
		}
	}

	// ========================= PRIVATE CLASSES ====================================

	/**
	 * Represents the {@link DataQueryLifeCycle} thread executing the
	 * {@link CommandTrigger}
	 * <p>
	 * -----------------------------------------------------------------------------------
	 * <br>
	 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
	 * Contact: {@code <further@utah.edu>}<br>
	 * Biomedical Informatics, 26 South 2000 East<br>
	 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
	 * Day Phone: 1-801-581-4080<br>
	 * -----------------------------------------------------------------------------------
	 *
	 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
	 * @version Nov 15, 2010
	 */
	private final class DataQueryTriggerThread extends Thread
	{
		/**
		 * The QueryContext for this life cycle.
		 */
		private final QueryContext queryContext;

		/**
		 * @param queryContext
		 */
		private DataQueryTriggerThread(final QueryContext queryContext)
		{
			super();
			this.queryContext = queryContext;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			try
			{
				lifeCycle.triggerCommand(this.queryContext);
				synchronized (lifeCycle)
				{
					// Notify anyone waiting for us that we're finished
					lifeCycle.notify();
				}
			}
			catch (final Throwable t)
			{
				log.error("Error in trigger command thread", t);
				fail("Error in trigger command thread");
			}
		}

	}

	/**
	 * Represents the {@link DataQueryLifeCycle} thread executing the
	 * {@link CommandCancel}
	 * <p>
	 * -----------------------------------------------------------------------------------
	 * <br>
	 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
	 * Contact: {@code <further@utah.edu>}<br>
	 * Biomedical Informatics, 26 South 2000 East<br>
	 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
	 * Day Phone: 1-801-581-4080<br>
	 * -----------------------------------------------------------------------------------
	 *
	 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
	 * @version Nov 15, 2010
	 */
	private final class DataQueryCancelThread extends Thread
	{
		/**
		 * The identifier to cancel
		 */
		private final Long id;

		/**
		 * @param id
		 */
		private DataQueryCancelThread(final Long id)
		{
			super();
			this.id = id;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			try
			{
				lifeCycle.cancel(this.id);
			}
			catch (final Throwable t)
			{
				log.error("Error in cancel command thread", t);
				fail("Error in cancel command thread");
			}
		}

	}
}
