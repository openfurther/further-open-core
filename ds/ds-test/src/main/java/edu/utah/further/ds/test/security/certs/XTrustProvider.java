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
package edu.utah.further.ds.test.security.certs;

import java.security.AccessController;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivilegedAction;
import java.security.Security;
import java.security.cert.X509Certificate;

import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactorySpi;
import javax.net.ssl.X509TrustManager;

/*
 * This code causes Java to ignore the self signed certificate from UPDB for testing
 * purposes.
 * 
 * 
 * The contents of this file are subject to the "END USER LICENSE AGREEMENT FOR F5
 * Software Development Kit for iControl"; you may not use this file except in
 * compliance with the License. The License is included in the iControl Software
 * Development Kit.
 * 
 * Software distributed under the License is distributed on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, either express or implied. See the License for the specific
 * language governing rights and limitations under the License.
 * 
 * The Original Code is iControl Code and related documentation distributed by F5.
 * 
 * Portions created by F5 are Copyright (C) 1996-2004 F5 Networks Inc. All Rights
 * Reserved. iControl (TM) is a registered trademark of F5 Networks, Inc.
 * 
 * Alternatively, the contents of this file may be used under the terms of the GNU
 * General Public License (the "GPL"), in which case the provisions of GPL are
 * applicable instead of those above. If you wish to allow use of your version of this
 * file only under the terms of the GPL and not to allow others to use your version of
 * this file under the License, indicate your decision by deleting the provisions
 * above and replace them with the notice and other provisions required by the GPL. If
 * you do not delete the provisions above, a recipient may use your version of this
 * file under either the License or the GPL.
 */
/**
 * ...
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
 * @version Jun 14, 2012
 */
public final class XTrustProvider extends java.security.Provider
{
	/**
	 *
	 */
	private static final long serialVersionUID = 4439889926540348884L;

	private static final String NAME = "XTrustJSSE";
	private static final String INFO = "XTrust JSSE Provider (implements trust factory with truststore validation disabled)";
	private static final double VERSION = 1.0D;

	public XTrustProvider()
	{
		super(NAME, VERSION, INFO);

		AccessController.doPrivileged(new PrivilegedAction<Object>()
		{
			@Override
			public Object run()
			{
				put("TrustManagerFactory." + TrustManagerFactoryImpl.getAlgorithm(),
						TrustManagerFactoryImpl.class.getName());
				return null;
			}
		});
	}

	public static void install()
	{
		if (Security.getProvider(NAME) == null)
		{
			Security.insertProviderAt(new XTrustProvider(), 2);
			Security.setProperty("ssl.TrustManagerFactory.algorithm",
					TrustManagerFactoryImpl.getAlgorithm());
		}
	}

	public static final class TrustManagerFactoryImpl extends TrustManagerFactorySpi
	{
		public TrustManagerFactoryImpl()
		{
		}

		public static String getAlgorithm()
		{
			return "XTrust509";
		}

		@Override
		protected void engineInit(final KeyStore keystore) throws KeyStoreException
		{
		}

		@Override
		protected void engineInit(final ManagerFactoryParameters mgrparams)
				throws InvalidAlgorithmParameterException
		{
			throw new InvalidAlgorithmParameterException(XTrustProvider.NAME
					+ " does not use ManagerFactoryParameters");
		}

		@Override
		protected TrustManager[] engineGetTrustManagers()
		{
			return new TrustManager[]
			{ new X509TrustManager()
			{
				@Override
				public X509Certificate[] getAcceptedIssuers()
				{
					return null;
				}

				@Override
				public void checkClientTrusted(final X509Certificate[] certs,
						final String authType)
				{
				}

				@Override
				public void checkServerTrusted(final X509Certificate[] certs,
						final String authType)
				{
				}
			} };
		}
	}
}