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
package edu.utah.further.fqe.ds.api.factory;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * Based on configuration and current time, this class produces the default stale date and
 * time for a given {@link QueryContext}. If no properties are set, this class defaults to
 * 1 HOUR for stale date times.
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
 * @version Mar 25, 2010
 */
@Service("defaultStaleDateTimeFactory")
public final class DefaultStaleDateTimeFactory implements StaleDateTimeFactory
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(DefaultStaleDateTimeFactory.class);

	// ========================= FIELDS ====================================

	/**
	 * The {@link Calendar} field.
	 */
	private edu.utah.further.core.api.time.Calendar unit = edu.utah.further.core.api.time.Calendar.HOUR;

	/**
	 * A value relative in the future to the field and todays date (i.e. 1 hour, 1 day, 10
	 * hours, etc)
	 */
	private int amount = 1;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize the sealer.
	 */
	@PostConstruct
	public void afterPropertiesSet()
	{
		if (log.isInfoEnabled())
		{
			log.info("Initializing with query timeout of " + getAmount() + " "
					+ StringUtil.pluralForm(getUnit().name().toLowerCase(), getAmount()));
		}
	}

	// =============== IMPL: StaleDateTimeFactory ===========================

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.factory.StaleDateTimeFactory#getStaleDateTime()
	 */
	@Override
	public Date getStaleDateTime()
	{
		if (log.isTraceEnabled())
		{
			log.trace("Using " + amount + " " + unit.name()
					+ " as default stale date time configuration");
		}

		final Calendar calendar = Calendar.getInstance();
		calendar.add(unit.getField(), amount);
		final Date stale = calendar.getTime();
		return stale;
	}

	// ========================= GET/SET ==============================

	/**
	 * @return the unit
	 */
	public edu.utah.further.core.api.time.Calendar getUnit()
	{
		return unit;
	}

	/**
	 * @param unit
	 *            the unit to set
	 */
	public void setUnit(final edu.utah.further.core.api.time.Calendar unit)
	{
		this.unit = unit;
	}

	/**
	 * @return the amount
	 */
	public int getAmount()
	{
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(final int amount)
	{
		this.amount = amount;
	}

}
