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
package edu.utah.further.core.data.oracle.type;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.springframework.jdbc.support.nativejdbc.C3P0NativeJdbcExtractor;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.api.xml.XmlUtil;

/**
 * This is a generic implementation of XmlType for use with Hibernate 3.0+. This converter
 * can both read and write an Oracle XMLTYPE to and from a DOM Document Java object.
 * <p>
 * This will work with Document objects > 4k. (I tested with one that is 124k)
 * 
 * To use this class you need to have your Hibernate Object have the selected object type
 * as org.w3c.dom.Document in the java class and then have the type in the hbm.xml defined
 * as OracleXmlType
 * 
 * Example:
 * 
 * <code>
 * public class SomeClass {
 *         private Document xml;
 * 
 *         public Document getXml() { return xml; }
 *         public void setXml(Document _xml) { xml = _xml; }
 * }
 * 
 * SomeClass.hbm.xml:
 * <property name="xml" column="XML" not-null="true"
 *         type="mypackage.OracleXmlType"/>
 * 
 * </code>
 * <p>
 * Required Oracle jars: xdb.jar xmlparserv2.jar (Used by <code>XmlType</code>).
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author sseaman
 * @see http://www.hibernate.org/444.html
 * @version Mar 19, 2009
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>} - improvements
 * @version Mar 20, 2009
 */
public class OracleXmlType implements UserType, Serializable
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 2308230823023l;

	private static final Class<?> returnedClass = Document.class;

	private static final int[] SQL_TYPES = new int[]
	{ 2007 };

	public static final String XML_TYPE_CLASS = "oracle.xdb.XMLType";
	
	public static final String OPAQUE_TYPE_CLASS = "oracle.sql.OPAQUE";

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(OracleXmlType.class);

	// ========================= IMPLEMENTATION: UserType ==================

	/**
	 * @return
	 * @see org.hibernate.usertype.UserType#sqlTypes()
	 */
	@Override
	public int[] sqlTypes()
	{
		return SQL_TYPES;
	}

	/**
	 * @return
	 * @see org.hibernate.usertype.UserType#returnedClass()
	 */
	@Override
	public Class<?> returnedClass()
	{
		return returnedClass;
	}

	/**
	 * @param _obj
	 * @return
	 * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
	 */
	@Override
	public int hashCode(final Object _obj)
	{
		return _obj.hashCode();
	}

	/**
	 * @param _cached
	 * @param _owner
	 * @return
	 * @throws HibernateException
	 * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable,
	 *      java.lang.Object)
	 */
	@Override
	public Document assemble(final Serializable _cached, final Object _owner)
			throws HibernateException
	{
		try
		{
			return deserialize((String) _cached);
		}
		catch (final Exception e)
		{
			throw new HibernateException("Could not assemble String to Document", e);
		}
	}

	/**
	 * @param _obj
	 * @return
	 * @throws HibernateException
	 * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
	 */
	@Override
	public String disassemble(final Object _obj) throws HibernateException
	{
		try
		{
			return serialize((Document) _obj);
		}
		catch (final Exception e)
		{
			throw new HibernateException(
					"Could not disassemble Document to Serializable", e);
		}
	}

	/**
	 * @param _orig
	 * @param _tar
	 * @param _owner
	 * @return
	 * @see org.hibernate.usertype.UserType#replace(java.lang.Object, java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public Document replace(final Object _orig, final Object _tar, final Object _owner)
	{
		return deepCopy(_orig);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws HibernateException
	 * @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean equals(final Object arg0, final Object arg1) throws HibernateException
	{
		return (arg0 == null) ? (arg1 == null) : arg0.equals(arg1);
	}

	/**
	 * @param rs
	 * @param names
	 * @param arg2
	 * @return
	 * @throws HibernateException
	 * @throws SQLException
	 * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet,
	 *      java.lang.String[], java.lang.Object)
	 */
	@Override
	public Document nullSafeGet(final ResultSet rs, final String[] names,
			final Object arg2) throws HibernateException, SQLException
	{
		final SQLXML sqlxml = (SQLXML) rs.getObject(names[0]);

		if (sqlxml == null)
		{
			return null;
		}

		// With the new SQLXML type, we could be more flexible than just return a DOM
		// Document here
		return getDocument(sqlxml);

	}

	/**
	 * @param st
	 * @param value
	 * @param index
	 * @throws HibernateException
	 * @throws SQLException
	 * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement,
	 *      java.lang.Object, int)
	 */
	@Override
	public void nullSafeSet(final PreparedStatement st, final Object value,
			final int index) throws HibernateException, SQLException
	{
		final NativeJdbcExtractor extractor = new C3P0NativeJdbcExtractor();

		try (final Connection nativeConn = extractor.getNativeConnection(st
				.getConnection()))
		{
			/*
			 * final XMLType xmlType = (value == null) ? null : new oracle.xdb.XMLType(
			 * nativeConn, serialize((Document) value));
			 */

			Object xmlType = null;
			if (value != null)
			{
				final Class<?> xmlTypeClass = Thread
						.currentThread()
						.getContextClassLoader()
						.loadClass(XML_TYPE_CLASS);
				final Constructor<?> constructor = xmlTypeClass.getConstructor(
						Connection.class, Document.class);
				xmlType = constructor
						.newInstance(nativeConn, serialize((Document) value));
			}

			st.setObject(index, xmlType);
		}
		catch (final ClassNotFoundException | NoSuchMethodException | SecurityException e)
		{
			throw new RuntimeException("Unable to load Oracle class " + XML_TYPE_CLASS, e);
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e)
		{
			throw new RuntimeException("Unable to create new instance of "
					+ XML_TYPE_CLASS, e);
		}
	}

	/**
	 * @param value
	 * @return
	 * @throws HibernateException
	 * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
	 */
	@Override
	public Document deepCopy(final Object value) throws HibernateException
	{
		return (value == null) ? null : ((Document) ((Document) value).cloneNode(true));
	}

	/**
	 * @return
	 * @see org.hibernate.usertype.UserType#isMutable()
	 */
	@Override
	public boolean isMutable()
	{
		return false;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Serialize an XML document to a string using the Java transform API.
	 * 
	 * @param document
	 *            document root node
	 * @return serialized string
	 */
	static String serialize(final Document document)
	{
		try
		{
			// Remove white space as much as possible, e.g. for the query translation
			// stored procedure
			document.normalize();
			final TransformerFactory tFactory = TransformerFactory.newInstance();
			final Transformer transformer = tFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "no");
			final DOMSource source = new DOMSource(document);
			final StringWriter sw = new StringWriter();
			final StreamResult result = new StreamResult(sw);
			transformer.transform(source, result);
			final String xmlString = sw.toString();
			final String strippedString = StringUtil.stripNewLinesAndTabs(xmlString);
			return strippedString;
		}
		catch (final TransformerException e)
		{
			log.error("An exception occurred during serialization", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Deserialize a string into an XML DOM node.
	 * 
	 * @param xmlSource
	 *            XML as string
	 * @return DOM document
	 */
	private static Document deserialize(final String xmlSource)
	{
		try
		{
			final DocumentBuilderFactory factory = XmlUtil.getDocumentBuilderFactory();
			final DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(new ByteArrayInputStream(xmlSource.getBytes("UTF-8")));
		}
		catch (final Exception e)
		{
			log.error("An exception occurred during deserialization", e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * @param sqlxml
	 * @throws SQLException
	 */
	private Document getDocument(final SQLXML sqlxml) throws SQLException
	{
		DocumentBuilder parser = null;
		Document result = null;
		try
		{
			parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			result = parser.parse(sqlxml.getBinaryStream());
		}
		catch (final ParserConfigurationException e)
		{
			log.error("Parser Error", e);
		}
		catch (final SAXException e)
		{
			log.error("SAX Exception", e);
		}
		catch (final IOException e)
		{
			log.error("Input output exception", e);
		}

		return result;
	}

}
