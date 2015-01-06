/*******************************************************************************
 * Source File: StringFieldBigIntegerColumn.java
 ******************************************************************************/
package edu.utah.further.core.data.oracle.type;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.w3c.dom.Document;

/**
 * Converts a Java String field to a {@link BigInteger} database column and back.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Jun 24, 2010
 */
public class StringFieldBigIntegerColumn implements UserType, Serializable
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1l;

	private static final Class<String> returnedClass = String.class;

	private static final int[] SQL_TYPES = new int[]
	{ Types.NUMERIC };

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(StringFieldBigIntegerColumn.class);

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
	public String assemble(final Serializable _cached, final Object _owner)
			throws HibernateException
	{
		try
		{
			return deserialize((BigInteger) _cached);
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
	public BigInteger disassemble(final Object _obj) throws HibernateException
	{
		try
		{
			return serialize((String) _obj);
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
	public String nullSafeGet(final ResultSet rs, final String[] names, final Object arg2)
			throws HibernateException, SQLException
	{
		final BigInteger xmlType = (BigInteger) rs.getObject(names[0]);
		// Note: replaced xmlType.getDOM() by xmlType.getDocument() by Oren 20-MAR-2009
		// because the former is depracated.
		return (xmlType == null) ? null : xmlType.toString();
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
		try
		{
			final BigInteger xmlType = (value == null) ? null : serialize((String) value);
			st.setObject(index, xmlType);
		}
		catch (final Exception e)
		{
			throw new SQLException("Could not covert Document to String for storage");
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
	static BigInteger serialize(final String string)
	{
		try
		{
			return new BigInteger(string);
		}
		catch (final Exception e)
		{
			// this is fatal, just dump the stack and throw a runtime exception
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Deserialize a string into an XML DOM node.
	 * 
	 * @param value
	 *            XML as string
	 * @return DOM document
	 */
	private static String deserialize(final BigInteger value)
	{
		try
		{
			return value.toString();
		}
		catch (final Exception e)
		{
			// this is fatal, just dump the stack and throw a runtime exception
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}
}
