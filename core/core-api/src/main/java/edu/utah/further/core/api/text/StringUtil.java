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
package edu.utah.further.core.api.text;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.collections.CollectionUtil.newMap;
import static edu.utah.further.core.api.constant.Constants.INVALID_VALUE_BOXED_LONG;
import static edu.utah.further.core.api.constant.Constants.INVALID_VALUE_INTEGER;
import static edu.utah.further.core.api.constant.Constants.INVALID_VALUE_LONG;
import static edu.utah.further.core.api.constant.Strings.DEFAULT_ENCODING;
import static edu.utah.further.core.api.constant.Strings.DOUBLE_QUOTE;
import static edu.utah.further.core.api.constant.Strings.EMPTY_STRING;
import static edu.utah.further.core.api.constant.Strings.ENUM_NAME_SEPARATOR;
import static edu.utah.further.core.api.constant.Strings.ENUM_NAME_SEPARATOR_CHAR;
import static edu.utah.further.core.api.constant.Strings.NEW_LINE_STRING;
import static edu.utah.further.core.api.constant.Strings.NULL_TO_STRING;
import static edu.utah.further.core.api.constant.Strings.SINGLE_QUOTE;
import static edu.utah.further.core.api.constant.Strings.SPACE_STRING;
import static edu.utah.further.core.api.constant.Strings.SQL_WILDCARD;
import static edu.utah.further.core.api.constant.Strings.TAB_CHAR;
import static edu.utah.further.core.api.constant.Strings.TAB_STRING;
import static edu.utah.further.core.api.constant.Strings.XML_NAME_SEPARATOR;
import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static org.apache.commons.lang.WordUtils.uncapitalize;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.constant.Constants;
import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Named;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.context.Valued;
import edu.utah.further.core.api.discrete.HasIdentifier;
import edu.utah.further.core.api.tree.Printer;

/**
 * Utilities related to strings and text processing.
 * <p>
 * Adapted in part from static String formatting and query routines, Copyright (C)
 * 2001-2005 Stephen Ostermiller
 * http://ostermiller.org/contact.pl?regarding=Java+Utilities This program is free
 * software; you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details. See COPYING.TXT for details. Utilities for String formatting,
 * manipulation, and queries. More information about this class is available from <a
 * target="_top" href=
 * "http://ostermiller.org/utils/StringHelper.html">ostermiller.org</a>.
 * <p>
 * ---------------------------------------------------------------------------- -------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * ---------------------------------------------------------------------------- -------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 13, 2008
 */
@SuppressWarnings("boxing")
// Note: the "final" keyword may help the Java compiler inline some low-level
// useful methods, like isInvalidId().
@Utility
@Api
public final class StringUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A regular expression of camel case strings in the FURTHeR context.
	 */
	public static final String CAMEL_CASE_REGEXP = "[a-z]+[a-z|0-9]*([A-Z][a-z|0-9]*)*";

	/**
	 * A regular expression of Java package names in the FURTHeR context.
	 */
	public static final String PACKAGE_NAME_REGEXP = "[a-z]+[a-z|0-9]*";

	/**
	 * A regular expression of Java class names in the FURTHeR context.
	 */
	public static final String CLASS_NAME_REGEXP = "([A-Z][a-z|0-9]*)+";

	// /**
	// * A set of characters with ASCII codes &lt; 32 to ignore. Note: its usage is
	// potentially slow
	// * because of boxing char to {@link Character} during HTML escaping.
	// */
	// private static final Set<Character> SPECIAL_CHARACTERS_TO_IGNORE = newSet();
	// static
	// {
	// SPECIAL_CHARACTERS_TO_IGNORE.add('\r');
	// SPECIAL_CHARACTERS_TO_IGNORE.add('\n');
	// SPECIAL_CHARACTERS_TO_IGNORE.add(Strings.TAB_CHAR);
	// SPECIAL_CHARACTERS_TO_IGNORE.add('\f');
	// }

	/**
	 * A map of special XML characters and their hexadecimal code translation.
	 * 
	 * @see http://en.wikipedia.org/wiki/Character_encodings_in_HTML
	 */
	private static final Map<Character, String> XML_ENTITIES = newMap();
	static
	{
		XML_ENTITIES.put('&', "0026");
		XML_ENTITIES.put('<', "003C");
		XML_ENTITIES.put('>', "003E");
		XML_ENTITIES.put('\"', "0022");
		XML_ENTITIES.put('\'', "0027");
	}

	/**
	 * A map of special HTML characters and their code translation.
	 */
	private static Map<String, Integer> HTML_ENTITIES = newMap();
	static
	{
		HTML_ENTITIES.put("nbsp", 160);
		HTML_ENTITIES.put("iexcl", 161);
		HTML_ENTITIES.put("cent", 162);
		HTML_ENTITIES.put("pound", 163);
		HTML_ENTITIES.put("curren", 164);
		HTML_ENTITIES.put("yen", 165);
		HTML_ENTITIES.put("brvbar", 166);
		HTML_ENTITIES.put("sect", 167);
		HTML_ENTITIES.put("uml", 168);
		HTML_ENTITIES.put("copy", 169);
		HTML_ENTITIES.put("ordf", 170);
		HTML_ENTITIES.put("laquo", 171);
		HTML_ENTITIES.put("not", 172);
		HTML_ENTITIES.put("shy", 173);
		HTML_ENTITIES.put("reg", 174);
		HTML_ENTITIES.put("macr", 175);
		HTML_ENTITIES.put("deg", 176);
		HTML_ENTITIES.put("plusmn", 177);
		HTML_ENTITIES.put("sup2", 178);
		HTML_ENTITIES.put("sup3", 179);
		HTML_ENTITIES.put("acute", 180);
		HTML_ENTITIES.put("micro", 181);
		HTML_ENTITIES.put("para", 182);
		HTML_ENTITIES.put("middot", 183);
		HTML_ENTITIES.put("cedil", 184);
		HTML_ENTITIES.put("sup1", 185);
		HTML_ENTITIES.put("ordm", 186);
		HTML_ENTITIES.put("raquo", 187);
		HTML_ENTITIES.put("frac14", 188);
		HTML_ENTITIES.put("frac12", 189);
		HTML_ENTITIES.put("frac34", 190);
		HTML_ENTITIES.put("iquest", 191);
		HTML_ENTITIES.put("Agrave", 192);
		HTML_ENTITIES.put("Aacute", 193);
		HTML_ENTITIES.put("Acirc", 194);
		HTML_ENTITIES.put("Atilde", 195);
		HTML_ENTITIES.put("Auml", 196);
		HTML_ENTITIES.put("Aring", 197);
		HTML_ENTITIES.put("AElig", 198);
		HTML_ENTITIES.put("Ccedil", 199);
		HTML_ENTITIES.put("Egrave", 200);
		HTML_ENTITIES.put("Eacute", 201);
		HTML_ENTITIES.put("Ecirc", 202);
		HTML_ENTITIES.put("Euml", 203);
		HTML_ENTITIES.put("Igrave", 204);
		HTML_ENTITIES.put("Iacute", 205);
		HTML_ENTITIES.put("Icirc", 206);
		HTML_ENTITIES.put("Iuml", 207);
		HTML_ENTITIES.put("ETH", 208);
		HTML_ENTITIES.put("Ntilde", 209);
		HTML_ENTITIES.put("Ograve", 210);
		HTML_ENTITIES.put("Oacute", 211);
		HTML_ENTITIES.put("Ocirc", 212);
		HTML_ENTITIES.put("Otilde", 213);
		HTML_ENTITIES.put("Ouml", 214);
		HTML_ENTITIES.put("times", 215);
		HTML_ENTITIES.put("Oslash", 216);
		HTML_ENTITIES.put("Ugrave", 217);
		HTML_ENTITIES.put("Uacute", 218);
		HTML_ENTITIES.put("Ucirc", 219);
		HTML_ENTITIES.put("Uuml", 220);
		HTML_ENTITIES.put("Yacute", 221);
		HTML_ENTITIES.put("THORN", 222);
		HTML_ENTITIES.put("szlig", 223);
		HTML_ENTITIES.put("agrave", 224);
		HTML_ENTITIES.put("aacute", 225);
		HTML_ENTITIES.put("acirc", 226);
		HTML_ENTITIES.put("atilde", 227);
		HTML_ENTITIES.put("auml", 228);
		HTML_ENTITIES.put("aring", 229);
		HTML_ENTITIES.put("aelig", 230);
		HTML_ENTITIES.put("ccedil", 231);
		HTML_ENTITIES.put("egrave", 232);
		HTML_ENTITIES.put("eacute", 233);
		HTML_ENTITIES.put("ecirc", 234);
		HTML_ENTITIES.put("euml", 235);
		HTML_ENTITIES.put("igrave", 236);
		HTML_ENTITIES.put("iacute", 237);
		HTML_ENTITIES.put("icirc", 238);
		HTML_ENTITIES.put("iuml", 239);
		HTML_ENTITIES.put("eth", 240);
		HTML_ENTITIES.put("ntilde", 241);
		HTML_ENTITIES.put("ograve", 242);
		HTML_ENTITIES.put("oacute", 243);
		HTML_ENTITIES.put("ocirc", 244);
		HTML_ENTITIES.put("otilde", 245);
		HTML_ENTITIES.put("ouml", 246);
		HTML_ENTITIES.put("divide", 247);
		HTML_ENTITIES.put("oslash", 248);
		HTML_ENTITIES.put("ugrave", 249);
		HTML_ENTITIES.put("uacute", 250);
		HTML_ENTITIES.put("ucirc", 251);
		HTML_ENTITIES.put("uuml", 252);
		HTML_ENTITIES.put("yacute", 253);
		HTML_ENTITIES.put("thorn", 254);
		HTML_ENTITIES.put("yuml", 255);
		HTML_ENTITIES.put("fnof", 402);
		HTML_ENTITIES.put("Alpha", 913);
		HTML_ENTITIES.put("Beta", 914);
		HTML_ENTITIES.put("Gamma", 915);
		HTML_ENTITIES.put("Delta", 916);
		HTML_ENTITIES.put("Epsilon", 917);
		HTML_ENTITIES.put("Zeta", 918);
		HTML_ENTITIES.put("Eta", 919);
		HTML_ENTITIES.put("Theta", 920);
		HTML_ENTITIES.put("Iota", 921);
		HTML_ENTITIES.put("Kappa", 922);
		HTML_ENTITIES.put("Lambda", 923);
		HTML_ENTITIES.put("Mu", 924);
		HTML_ENTITIES.put("Nu", 925);
		HTML_ENTITIES.put("Xi", 926);
		HTML_ENTITIES.put("Omicron", 927);
		HTML_ENTITIES.put("Pi", 928);
		HTML_ENTITIES.put("Rho", 929);
		HTML_ENTITIES.put("Sigma", 931);
		HTML_ENTITIES.put("Tau", 932);
		HTML_ENTITIES.put("Upsilon", 933);
		HTML_ENTITIES.put("Phi", 934);
		HTML_ENTITIES.put("Chi", 935);
		HTML_ENTITIES.put("Psi", 936);
		HTML_ENTITIES.put("Omega", 937);
		HTML_ENTITIES.put("alpha", 945);
		HTML_ENTITIES.put("beta", 946);
		HTML_ENTITIES.put("gamma", 947);
		HTML_ENTITIES.put("delta", 948);
		HTML_ENTITIES.put("epsilon", 949);
		HTML_ENTITIES.put("zeta", 950);
		HTML_ENTITIES.put("eta", 951);
		HTML_ENTITIES.put("theta", 952);
		HTML_ENTITIES.put("iota", 953);
		HTML_ENTITIES.put("kappa", 954);
		HTML_ENTITIES.put("lambda", 955);
		HTML_ENTITIES.put("mu", 956);
		HTML_ENTITIES.put("nu", 957);
		HTML_ENTITIES.put("xi", 958);
		HTML_ENTITIES.put("omicron", 959);
		HTML_ENTITIES.put("pi", 960);
		HTML_ENTITIES.put("rho", 961);
		HTML_ENTITIES.put("sigmaf", 962);
		HTML_ENTITIES.put("sigma", 963);
		HTML_ENTITIES.put("tau", 964);
		HTML_ENTITIES.put("upsilon", 965);
		HTML_ENTITIES.put("phi", 966);
		HTML_ENTITIES.put("chi", 967);
		HTML_ENTITIES.put("psi", 968);
		HTML_ENTITIES.put("omega", 969);
		HTML_ENTITIES.put("thetasym", 977);
		HTML_ENTITIES.put("upsih", 978);
		HTML_ENTITIES.put("piv", 982);
		HTML_ENTITIES.put("bull", 8226);
		HTML_ENTITIES.put("hellip", 8230);
		HTML_ENTITIES.put("prime", 8242);
		HTML_ENTITIES.put("Prime", 8243);
		HTML_ENTITIES.put("oline", 8254);
		HTML_ENTITIES.put("frasl", 8260);
		HTML_ENTITIES.put("weierp", 8472);
		HTML_ENTITIES.put("image", 8465);
		HTML_ENTITIES.put("real", 8476);
		HTML_ENTITIES.put("trade", 8482);
		HTML_ENTITIES.put("alefsym", 8501);
		HTML_ENTITIES.put("larr", 8592);
		HTML_ENTITIES.put("uarr", 8593);
		HTML_ENTITIES.put("rarr", 8594);
		HTML_ENTITIES.put("darr", 8595);
		HTML_ENTITIES.put("harr", 8596);
		HTML_ENTITIES.put("crarr", 8629);
		HTML_ENTITIES.put("lArr", 8656);
		HTML_ENTITIES.put("uArr", 8657);
		HTML_ENTITIES.put("rArr", 8658);
		HTML_ENTITIES.put("dArr", 8659);
		HTML_ENTITIES.put("hArr", 8660);
		HTML_ENTITIES.put("forall", 8704);
		HTML_ENTITIES.put("part", 8706);
		HTML_ENTITIES.put("exist", 8707);
		HTML_ENTITIES.put("empty", 8709);
		HTML_ENTITIES.put("nabla", 8711);
		HTML_ENTITIES.put("isin", 8712);
		HTML_ENTITIES.put("notin", 8713);
		HTML_ENTITIES.put("ni", 8715);
		HTML_ENTITIES.put("prod", 8719);
		HTML_ENTITIES.put("sum", 8721);
		HTML_ENTITIES.put("minus", 8722);
		HTML_ENTITIES.put("lowast", 8727);
		HTML_ENTITIES.put("radic", 8730);
		HTML_ENTITIES.put("prop", 8733);
		HTML_ENTITIES.put("infin", 8734);
		HTML_ENTITIES.put("ang", 8736);
		HTML_ENTITIES.put("and", 8743);
		HTML_ENTITIES.put("or", 8744);
		HTML_ENTITIES.put("cap", 8745);
		HTML_ENTITIES.put("cup", 8746);
		HTML_ENTITIES.put("int", 8747);
		HTML_ENTITIES.put("there4", 8756);
		HTML_ENTITIES.put("sim", 8764);
		HTML_ENTITIES.put("cong", 8773);
		HTML_ENTITIES.put("asymp", 8776);
		HTML_ENTITIES.put("ne", 8800);
		HTML_ENTITIES.put("equiv", 8801);
		HTML_ENTITIES.put("le", 8804);
		HTML_ENTITIES.put("ge", 8805);
		HTML_ENTITIES.put("sub", 8834);
		HTML_ENTITIES.put("sup", 8835);
		HTML_ENTITIES.put("nsub", 8836);
		HTML_ENTITIES.put("sube", 8838);
		HTML_ENTITIES.put("supe", 8839);
		HTML_ENTITIES.put("oplus", 8853);
		HTML_ENTITIES.put("otimes", 8855);
		HTML_ENTITIES.put("perp", 8869);
		HTML_ENTITIES.put("sdot", 8901);
		HTML_ENTITIES.put("lceil", 8968);
		HTML_ENTITIES.put("rceil", 8969);
		HTML_ENTITIES.put("lfloor", 8970);
		HTML_ENTITIES.put("rfloor", 8971);
		HTML_ENTITIES.put("lang", 9001);
		HTML_ENTITIES.put("rang", 9002);
		HTML_ENTITIES.put("loz", 9674);
		HTML_ENTITIES.put("spades", 9824);
		HTML_ENTITIES.put("clubs", 9827);
		HTML_ENTITIES.put("hearts", 9829);
		HTML_ENTITIES.put("diams", 9830);
		HTML_ENTITIES.put("quot", 34);
		HTML_ENTITIES.put("amp", 38);
		HTML_ENTITIES.put("lt", 60);
		HTML_ENTITIES.put("gt", 62);
		HTML_ENTITIES.put("OElig", 338);
		HTML_ENTITIES.put("oelig", 339);
		HTML_ENTITIES.put("Scaron", 352);
		HTML_ENTITIES.put("scaron", 353);
		HTML_ENTITIES.put("Yuml", 376);
		HTML_ENTITIES.put("circ", 710);
		HTML_ENTITIES.put("tilde", 732);
		HTML_ENTITIES.put("ensp", 8194);
		HTML_ENTITIES.put("emsp", 8195);
		HTML_ENTITIES.put("thinsp", 8201);
		HTML_ENTITIES.put("zwnj", 8204);
		HTML_ENTITIES.put("zwj", 8205);
		HTML_ENTITIES.put("lrm", 8206);
		HTML_ENTITIES.put("rlm", 8207);
		HTML_ENTITIES.put("ndash", 8211);
		HTML_ENTITIES.put("mdash", 8212);
		HTML_ENTITIES.put("lsquo", 8216);
		HTML_ENTITIES.put("rsquo", 8217);
		HTML_ENTITIES.put("sbquo", 8218);
		HTML_ENTITIES.put("ldquo", 8220);
		HTML_ENTITIES.put("rdquo", 8221);
		HTML_ENTITIES.put("bdquo", 8222);
		HTML_ENTITIES.put("dagger", 8224);
		HTML_ENTITIES.put("Dagger", 8225);
		HTML_ENTITIES.put("permil", 8240);
		HTML_ENTITIES.put("lsaquo", 8249);
		HTML_ENTITIES.put("rsaquo", 8250);
		HTML_ENTITIES.put("euro", 8364);
	}

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private StringUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Return a new instance of an empty string buffer.
	 * 
	 * @return a new empty string buffer instance
	 */
	public static StringBuilder newStringBuilder()
	{
		return new StringBuilder();
	}

	/**
	 * Is object a string.
	 * 
	 * @param obj
	 *            an object to test
	 * @return is object a string
	 */
	public static boolean isString(final Object obj)
	{
		return (obj == null) ? true : String.class.isInstance(obj);
	}

	/**
	 * Is a string of camel case pattern or not.
	 * 
	 * @param s
	 *            string to check
	 * @return does <b>s</b> match a camel case pattern or not
	 */
	public static boolean isCamelCaseString(final String s)
	{
		return (s == null) ? false : s.matches(CAMEL_CASE_REGEXP);
	}

	/**
	 * Is a string a valid Java package name. in the FURTHeR context
	 * 
	 * @param s
	 *            string to check
	 * @return does <b>s</b> match a Java package name pattern
	 */
	public static boolean isPackageNameString(final String s)
	{
		return (s == null) ? false : s.matches(PACKAGE_NAME_REGEXP);
	}

	/**
	 * Is a string a valid Java class name in the FURTHeR context.
	 * 
	 * @param s
	 *            string to check
	 * @return does <b>s</b> match a Java package name pattern
	 */
	public static boolean isClassNameString(final String s)
	{
		return (s == null) ? false : s.matches(CLASS_NAME_REGEXP);
	}

	/**
	 * Is a value an valid integer or not.
	 * 
	 * @param value
	 * @return is a value an invalid integer
	 */
	public static boolean isValidInteger(final int value)
	{
		return (value != INVALID_VALUE_INTEGER) /* && (value != 0) */;
	}

	/**
	 * Is a value a valid integer.
	 * 
	 * @param value
	 * @return Is a value an invalid integer
	 */
	public static boolean isInvalidInteger(final int value)
	{
		return (value == INVALID_VALUE_INTEGER) /* || (value == 0) */;
	}

	/**
	 * Is a value a valid ID.
	 * 
	 * @param value
	 * @return is a value a valid ID
	 */
	public static boolean isValidId(final Long value)
	{
		return (!INVALID_VALUE_BOXED_LONG.equals(value))
		/* && (!LONG_ZERO.equals(value) ) */;
	}

	/**
	 * Is a value an invalid ID.
	 * 
	 * @param value
	 * @return is a value an invalid ID
	 */
	public static boolean isInvalidId(final Long value)
	{
		return (INVALID_VALUE_BOXED_LONG.equals(value))
		/* || (LONG_ZERO.equals(value)) */;
	}

	/**
	 * Is a value an valid long or not.
	 * 
	 * @param value
	 * @return is a value an invalid long
	 */
	public static boolean isValidLong(final long value)
	{
		return (value != INVALID_VALUE_INTEGER) /* && (value != 0) */;
	}

	/**
	 * Is a value an invalid long or not.
	 * 
	 * @param value
	 * @return is a value an invalid long
	 */
	public static boolean isInvalidLong(final long value)
	{
		return (value == INVALID_VALUE_LONG) /* && (value != 0) */;
	}

	/**
	 * Convert a {@link String} to {@link Integer}.
	 * 
	 * @param value
	 *            string value
	 * @return integer value. If fail to convert, returns
	 *         <code>INVALID_VALUE_INTEGER</code>
	 */
	public static int getAsInteger(final String value)
	{
		try
		{
			return parseInt(value);
		}
		catch (final Exception e)
		{
			return INVALID_VALUE_INTEGER;
		}
	}

	/**
	 * Convert a {@link String} to {@link Integer}.
	 * 
	 * @param value
	 *            string value
	 * @return Integer value. If fail to convert, returns <code>null</code>
	 */
	public static Integer getAsBoxedInteger(final String value)
	{
		try
		{
			return Integer.parseInt(value);
		}
		catch (final Exception e)
		{
			return null;
		}
	}

	/**
	 * Convert a {@link String} to a {@link Long}.
	 * 
	 * @param value
	 *            string value
	 * @return long value. If fail to convert, returns
	 *         <code>INVALID_VALUE_BOXED_INTEGER</code>
	 */
	public static Long getAsId(final String value)
	{
		try
		{
			return Long.valueOf(value);
		}
		catch (final Exception e)
		{
			return INVALID_VALUE_BOXED_LONG;
		}
	}

	/**
	 * Convert a {@link String} to <code>long</code>.
	 * 
	 * @param value
	 *            string value
	 * @return long value. If fail to convert, returns <code>INVALID_VALUE_LONG</code>
	 */
	public static long getAsLong(final String value)
	{
		try
		{
			return parseLong(value);
		}
		catch (final Exception e)
		{
			return INVALID_VALUE_LONG;
		}
	}

	/**
	 * Convert a {@link String} to {@link Boolean}.
	 * 
	 * @param value
	 *            string value
	 * @return boolean value. If fail to convert, returns <code>false</code>
	 */
	public static boolean getAsBoolean(final String value)
	{
		try
		{
			return parseBoolean(value);
		}
		catch (final Exception e)
		{
			return false;
		}
	}

	/**
	 * Convert a {@link String} to {@link Double}.
	 * 
	 * @param value
	 *            string value
	 * @return {@link Double} value. If fail to convert, returns <code>null</code>
	 */
	public static Double getAsBoxedDouble(final String value)
	{
		try
		{
			return Double.parseDouble(value);
		}
		catch (final Exception e)
		{
			return null;
		}
	}

	/**
	 * Convert a string to an enum. Encapsulates unchecked warning suppression.
	 * 
	 * @param enumType
	 * @param value
	 * @return
	 */
	public static <T extends Enum<T>> T getAsEnum(final Class<T> enumType,
			final String value)
	{
		try
		{
			return Enum.valueOf(enumType, value);
		}
		catch (final Exception e)
		{
			return null;
		}
	}

	/**
	 * Pad the beginning of the given String with spaces until the String is of the given
	 * length.
	 * <p>
	 * If a String is longer than the desired length, it will not be truncated, however no
	 * padding will be added.
	 * 
	 * @param s
	 *            String to be padded.
	 * @param length
	 *            desired length of result.
	 * @return padded String.
	 * @since ostermillerutils 1.00.00
	 */
	public static String prePad(final String s, final int length)
	{
		return prePad(s, length, ' ');
	}

	/**
	 * Pre-pend the given character to the String until the result is the desired length.
	 * <p>
	 * If a String is longer than the desired length, it will not be truncated, however no
	 * padding will be added.
	 * 
	 * @param s
	 *            String to be padded.
	 * @param length
	 *            desired length of result.
	 * @param c
	 *            padding character.
	 * @return padded String.
	 * @since ostermillerutils 1.00.00
	 */
	public static String prePad(final String s, final int length, final char c)
	{
		final int needed = length - s.length();
		if (needed <= 0)
		{
			return s;
		}
		final StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < needed; i++)
		{
			sb.append(c);
		}
		sb.append(s);
		return (sb.toString());
	}

	/**
	 * Pad the end of the given String with spaces until the String is of the given
	 * length.
	 * <p>
	 * If a String is longer than the desired length, it will not be truncated, however no
	 * padding will be added.
	 * 
	 * @param s
	 *            string to be padded
	 * @param length
	 *            desired length of result
	 * @return padded String.
	 * @since ostermillerutils 1.00.00
	 */
	public static String postPad(final String s, final int length)
	{
		return postPad(s, length, ' ');
	}

	/**
	 * Append the given character to the String until the result is the desired length.
	 * <p>
	 * If a String is longer than the desired length, it will not be truncated, however no
	 * padding will be added.
	 * 
	 * @param s
	 *            String to be padded.
	 * @param length
	 *            desired length of result.
	 * @param c
	 *            padding character.
	 * @return padded String.
	 * @since ostermillerutils 1.00.00
	 */
	public static String postPad(final String s, final int length, final char c)
	{
		final int needed = length - s.length();
		if (needed <= 0)
		{
			return s;
		}
		final StringBuilder sb = new StringBuilder(length);
		sb.append(s);
		for (int i = 0; i < needed; i++)
		{
			sb.append(c);
		}
		return (sb.toString());
	}

	/**
	 * Pad the beginning and end of the given String with spaces until the String is of
	 * the given length. The result is that the original String is centered in the middle
	 * of the new string.
	 * <p>
	 * If the number of characters to pad is even, then the padding will be split evenly
	 * between the beginning and end, otherwise, the extra character will be added to the
	 * end.
	 * <p>
	 * If a String is longer than the desired length, it will not be truncated, however no
	 * padding will be added.
	 * 
	 * @param s
	 *            String to be padded.
	 * @param length
	 *            desired length of result.
	 * @return padded String.
	 * @since ostermillerutils 1.00.00
	 */
	public static String midPad(final String s, final int length)
	{
		return midpad(s, length, ' ');
	}

	/**
	 * Pad the beginning and end of the given String with the given character until the
	 * result is the desired length. The result is that the original String is centered in
	 * the middle of the new string.
	 * <p>
	 * If the number of characters to pad is even, then the padding will be split evenly
	 * between the beginning and end, otherwise, the extra character will be added to the
	 * end.
	 * <p>
	 * If a String is longer than the desired length, it will not be truncated, however no
	 * padding will be added.
	 * 
	 * @param s
	 *            String to be padded.
	 * @param length
	 *            desired length of result.
	 * @param c
	 *            padding character.
	 * @return padded String.
	 * @since ostermillerutils 1.00.00
	 */
	public static String midpad(final String s, final int length, final char c)
	{
		final int needed = length - s.length();
		if (needed <= 0)
		{
			return s;
		}
		final int beginning = needed / 2;
		final int end = beginning + needed % 2;
		final StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < beginning; i++)
		{
			sb.append(c);
		}
		sb.append(s);
		for (int i = 0; i < end; i++)
		{
			sb.append(c);
		}
		return (sb.toString());
	}

	/**
	 * Split the given String into tokens.
	 * <P>
	 * This method is meant to be similar to the split function in other programming
	 * languages but it does not use regular expressions. Rather the String is split on a
	 * single String literal.
	 * <P>
	 * Unlike java.util.StringTokenizer which accepts multiple character tokens as
	 * delimiters, the delimiter here is a single String literal.
	 * <P>
	 * Each null token is returned as an empty String. Delimiters are never returned as
	 * tokens.
	 * <P>
	 * If there is no delimiter because it is either empty or null, the only element in
	 * the result is the original String.
	 * <P>
	 * StringHelper.split("1-2-3", "-");<br>
	 * result: {"1","2","3"}<br>
	 * StringHelper.split("-1--2-", "-");<br>
	 * result: {Names.MISC.EMPTY_STRING,"1",Names.MISC.EMPTY_STRING,"2",Names.MISC
	 * .EMPTY_STRING}<br>
	 * StringHelper.split("123", CommonNames.EMPTY_STRING);<br>
	 * result: {"123"}<br>
	 * StringHelper.split("1-2---3----4", "--");<br>
	 * result: {"1-2","-3",Names.MISC.EMPTY_STRING,"4"}<br>
	 * 
	 * @param s
	 *            String to be split.
	 * @param delimiter
	 *            String literal on which to split.
	 * @return an array of tokens.
	 * @since ostermillerutils 1.00.00
	 */
	public static String[] split(final String s, final String delimiter)
	{
		int delimiterLength;
		// the next statement has the side effect of throwing a null pointer
		// exception if s is null.
		final int stringLength = s.length();
		if (delimiter == null || (delimiterLength = delimiter.length()) == 0)
		{
			// it is not inherently clear what to do if there is no delimiter
			// On one hand it would make sense to return each character because
			// the null String can be found between each pair of characters in
			// a String. However, it can be found many times there and we don'
			// want to be returning multiple null tokens.
			// returning the whole String will be defined as the correct
			// behavior
			// in this instance.
			return new String[]
			{ s };
		}

		// a two pass solution is used because a one pass solution would
		// require the possible resizing and copying of memory structures
		// In the worst case it would have to be resized n times with each
		// resize having a O(n) copy leading to an O(n^2) algorithm.

		int count;
		int start;
		int end;

		// Scan s and count the tokens.
		count = 0;
		start = 0;
		while ((end = s.indexOf(delimiter, start)) != -1)
		{
			count++;
			start = end + delimiterLength;
		}
		count++;

		// allocate an array to return the tokens,
		// we now know how big it should be
		final String[] result = new String[count];

		// Scan s again, but this time pick out the tokens
		count = 0;
		start = 0;
		while ((end = s.indexOf(delimiter, start)) != -1)
		{
			result[count] = (s.substring(start, end));
			count++;
			start = end + delimiterLength;
		}
		end = stringLength;
		result[count] = s.substring(start, end);

		return (result);
	}

	/**
	 * Split the given String into tokens. Delimiters will be returned as tokens.
	 * <P>
	 * This method is meant to be similar to the split function in other programming
	 * languages but it does not use regular expressions. Rather the String is split on a
	 * single String literal.
	 * <P>
	 * Unlike java.util.StringTokenizer which accepts multiple character tokens as
	 * delimiters, the delimiter here is a single String literal.
	 * <P>
	 * Each null token is returned as an empty String. Delimiters are never returned as
	 * tokens.
	 * <P>
	 * If there is no delimiter because it is either empty or null, the only element in
	 * the result is the original String.
	 * <P>
	 * StringHelper.split("1-2-3", "-");<br>
	 * result: {"1","-","2","-","3"}<br>
	 * StringHelper.split("-1--2-", "-");<br>
	 * result: {Names.MISC.EMPTY_STRING,"-","1","-",Names.MISC.EMPTY_STRING,"-","2"
	 * ,"-",Names .MISC.EMPTY_STRING}<br>
	 * StringHelper.split("123", CommonNames.EMPTY_STRING);<br>
	 * result: {"123"}<br>
	 * StringHelper.split("1-2--3---4----5", "--");<br>
	 * result: {"1-2","--","3","--","-4","--",Names.MISC.EMPTY_STRING,"--","5"} <br>
	 * 
	 * @param s
	 *            String to be split.
	 * @param delimiter
	 *            String literal on which to split.
	 * @return an array of tokens.
	 * @since ostermillerutils 1.05.00
	 */
	public static String[] splitIncludeDelimiters(final String s, final String delimiter)
	{
		int delimiterLength;
		// the next statement has the side effect of throwing a null pointer
		// exception if s is null.
		final int stringLength = s.length();
		if (delimiter == null || (delimiterLength = delimiter.length()) == 0)
		{
			// it is not inherently clear what to do if there is no delimiter
			// On one hand it would make sense to return each character because
			// the null String can be found between each pair of characters in
			// a String. However, it can be found many times there and we don'
			// want to be returning multiple null tokens.
			// returning the whole String will be defined as the correct
			// behavior
			// in this instance.
			return new String[]
			{ s };
		}

		// a two pass solution is used because a one pass solution would
		// require the possible resizing and copying of memory structures
		// In the worst case it would have to be resized n times with each
		// resize having a O(n) copy leading to an O(n^2) algorithm.

		int count;
		int start;
		int end;

		// Scan s and count the tokens.
		count = 0;
		start = 0;
		while ((end = s.indexOf(delimiter, start)) != -1)
		{
			count += 2;
			start = end + delimiterLength;
		}
		count++;

		// allocate an array to return the tokens,
		// we now know how big it should be
		final String[] result = new String[count];

		// Scan s again, but this time pick out the tokens
		count = 0;
		start = 0;
		while ((end = s.indexOf(delimiter, start)) != -1)
		{
			result[count] = (s.substring(start, end));
			count++;
			result[count] = delimiter;
			count++;
			start = end + delimiterLength;
		}
		end = stringLength;
		result[count] = s.substring(start, end);

		return (result);
	}

	/**
	 * Return the last part of a string after delimiting. E.g. if s = "a.b.c" and
	 * delimiter=".", this method returns "c".
	 * 
	 * @param s
	 *            string to delimit
	 * @param delimiter
	 *            delimiter
	 * @return last part of string
	 */
	public static String lastPartOf(final String s, final String delimiter)
	{
		final String[] parts = split(s, delimiter);
		return parts[parts.length - 1];
	}

	/**
	 * Chain all the elements of a collection into a single {@link String} according to
	 * the collection's iteration order.
	 * <p>
	 * If the given list is null or empty, an empty string will be returned.
	 * <code>null</code> elements of the array are allowed and will be treated like empty
	 * Strings.
	 * 
	 * @param collection
	 *            collection to be joined into a string
	 * @return concatenation of all the elements of the given list
	 */
	public static String chain(final Collection<?> collection)
	{
		return chain(collection, EMPTY_STRING);
	}

	/**
	 * Chain all the elements of a collection into a single {@link String} with a
	 * delimiter according to the collection's iteration order.
	 * <p>
	 * If the given array empty an empty string will be returned. Null elements of the
	 * array are allowed and will be treated like empty Strings.
	 * 
	 * @param collection
	 *            collection to be joined into a string.
	 * @param delimiter
	 *            string to place between list elements
	 * @return concatenation of all the elements of the given list with the the delimiter
	 *         in between
	 */
	public static String chain(final Collection<?> collection, final String delimiter)
	{
		// Nothing in the array return empty string
		// has the side effect of throwing a NullPointerException if
		// the array is null.
		if ((collection == null) || collection.isEmpty())
		{
			return EMPTY_STRING;
		}

		// Iterate over collection and concatenate elements into a string builder
		final Iterator<?> iterator = collection.iterator();
		final StringBuilder result = StringUtil.newStringBuilder();
		while (iterator.hasNext())
		{
			final Object element = iterator.next();
			if (element != null)
			{
				result.append(element);
			}
			if (iterator.hasNext())
			{
				result.append(delimiter);
			}
		}

		return result.toString();
	}

	/**
	 * Replace occurrences of a substring. StringHelper.replace("1-2-3", "-", "|");<br>
	 * result: "1|2|3"<br>
	 * StringHelper.replace("-1--2-", "-", "|");<br>
	 * result: "|1||2|"<br>
	 * StringHelper.replace("123", CommonNames.EMPTY_STRING, "|");<br>
	 * result: "123"<br>
	 * StringHelper.replace("1-2---3----4", "--", "|");<br>
	 * result: "1-2|-3||4"<br>
	 * StringHelper.replace("1-2---3----4", "--", "---");<br>
	 * result: "1-2----3------4"<br>
	 * 
	 * @param s
	 *            String to be modified.
	 * @param find
	 *            String to find.
	 * @param replaceStr
	 *            String to replace.
	 * @return a string with all the occurrences of the string to find replaced.
	 * @since ostermillerutils 1.00.00
	 */
	public static String replace(final String s, final String find,
			final String replaceStr)
	{
		String replace = replaceStr;
		int findLength;
		// the next statement has the side effect of throwing a null pointer
		// exception if s is null.
		final int stringLength = s.length();
		if (find == null || (findLength = find.length()) == 0)
		{
			// If there is nothing to find, we won't try and find it.
			return s;
		}
		if (replace == null)
		{
			// a null string and an empty string are the same
			// for replacement purposes.
			replace = EMPTY_STRING;
		}
		final int replaceLength = replace.length();

		// We need to figure out how long our resulting string will be.
		// This is required because without it, the possible resizing
		// and copying of memory structures could lead to an unacceptable
		// runtime.
		// In the worst case it would have to be resized n times with each
		// resize having a O(n) copy leading to an O(n^2) algorithm.
		int length;
		if (findLength == replaceLength)
		{
			// special case in which we don't need to count the replacements
			// because the count falls out of the length formula.
			length = stringLength;
		}
		else
		{
			int count;
			int start;
			int end;

			// Scan s and count the number of times we find our target.
			count = 0;
			start = 0;
			while ((end = s.indexOf(find, start)) != -1)
			{
				count++;
				start = end + findLength;
			}
			if (count == 0)
			{
				// special case in which on first pass, we find there is nothing
				// to be replaced. No need to do a second pass or create a
				// string buffer.
				return s;
			}
			length = stringLength - (count * (findLength - replaceLength));
		}

		int start = 0;
		int end = s.indexOf(find, start);
		if (end == -1)
		{
			// nothing was found in the string to replace.
			// we can get this if the find and replace strings
			// are the same length because we didn't check before.
			// in this case, we will return the original string
			return s;
		}
		// it looks like we actually have something to replace
		// *sigh* allocate memory for it.
		final StringBuilder sb = new StringBuilder(length);

		// Scan s and do the replacements
		while (end != -1)
		{
			sb.append(s.substring(start, end));
			sb.append(replace);
			start = end + findLength;
			end = s.indexOf(find, start);
		}
		end = stringLength;
		sb.append(s.substring(start, end));

		return (sb.toString());
	}

	/**
	 * Remove all new lines and tabs from a string
	 * 
	 * @param s
	 *            original string
	 * @return stripped string
	 */
	public static String stripNewLinesAndTabs(final String s)
	{
		if (s == null)
		{
			return null;
		}
		String t = s;
		t = t.replaceAll("\r\n", EMPTY_STRING);
		t = t.replaceAll(NEW_LINE_STRING, EMPTY_STRING);
		t = t.replaceAll(TAB_STRING, " "); // Convert tabs to spaces
		t = t.replaceAll("\\s+", " "); // Trim consecutive spaces
		t = t.replaceAll(">\\s+", ">"); // Trim space after end of element
		t = t.replaceAll("\\s+</", "</"); // Trim space before beginning of element
		t = t.replaceAll("\\s/>", "/>"); // Trim space before the end of an empty element
		return t;
		// Older version that seems less portable and robust
		// return s.replaceAll("\r\n", EMPTY_STRING).replaceAll(NEW_LINE_STRING,
		// EMPTY_STRING).replaceAll(TAB_STRING, EMPTY_STRING);
	}

	/**
	 * Replaces characters that may be confused by an HTML/XML parser with their
	 * equivalent character entity references.
	 * <p>
	 * Any data that will appear as text on a web page should be be escaped. This is
	 * especially important for data that comes from untrusted sources such as Internet
	 * users. A common mistake in CGI programming is to ask a user for data and then put
	 * that data on a web page. For example:
	 * 
	 * <pre>
	 * Server: What is your name?
	 * User: &lt;b&gt;Joe&lt;b&gt;
	 * Server: Hello &lt;b&gt;Joe&lt;/b&gt;, Welcome
	 * </pre>
	 * 
	 * If the name is put on the page without checking that it doesn't contain HTML code
	 * or without sanitizing that HTML code, the user could reformat the page, insert
	 * scripts, and control the the content on your web server.
	 * <p>
	 * This method will replace HTML characters such as &gt; with their HTML entity
	 * reference (&amp;gt;) so that the html parser will be sure to interpret them as
	 * plain text rather than HTML or script.
	 * <p>
	 * This method should be used for both data to be displayed in text in the html
	 * document, and data put in form elements. For example:<br>
	 * <code>&lt;html&gt;&lt;body&gt;<i>This in not a &amp;lt;tag&amp;gt;
	 * in HTML</i>&lt;/body&gt;&lt;/html&gt;</code><br>
	 * and<br>
	 * <code>&lt;form&gt;&lt;input type="hidden" name="date" value="<i>This data could
	 * be &amp;quot;malicious&amp;quot;</i>"&gt;&lt;/form&gt;</code><br>
	 * In the second example, the form data would be properly be resubmitted to your cgi
	 * script in the URLEncoded format:<br>
	 * <code><i>This data could be %22malicious%22</i></code>
	 * 
	 * @param s
	 *            String to be escaped
	 * @return escaped String
	 * @since ostermillerutils 1.00.00
	 */
	public static String escapeHtml(final String s)
	{
		// Check for characters that might be dangerous and calculate a length
		// of the string that has escapes.
		final int newLength = computeEscapedStringLength(s);
		if (isInvalidInteger(newLength))
		{
			// Nothing to escape in the string
			return s;
		}

		// Build the escaped string
		final StringBuilder sb = new StringBuilder(newLength);
		for (int i = 0; i < s.length(); i++)
		{
			final char c = s.charAt(i);
			final int cint = 0xffff & c;
			if ((cint < 32) && !isSpecialCharacter(c))
			{
				sb.append(c);
			}
			else
			{
				// Normal character, escape if exists in entity lookup map
				if (XML_ENTITIES.containsKey(c))
				{
					sb.append("&#x").append(XML_ENTITIES.get(c)).append(";");
				}
				else
				{
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Is this a special character with ASCII code &lt; 32 that should be ignored from
	 * escaped HTML strings or not.
	 * 
	 * @param c
	 *            character with ASCII code &lt; 32
	 */
	private static boolean isSpecialCharacter(final char c)
	{
		switch (c)
		{
			case '\r':
			case '\n':
			case Strings.TAB_CHAR:
			case '\f':
			{
				return true;
			}
			default:
			{
				return false;
			}
		}
	}

	/**
	 * Check for characters that might be dangerous and calculate a length of the string
	 * that has escapes. If no characters should be escaped, this method returns
	 * {@link Constants#INVALID_VALUE_INTEGER}.
	 * 
	 * @param s
	 *            original string
	 * @return length of escaped string
	 */
	private static int computeEscapedStringLength(final String s)
	{
		final int length = s.length();
		int newLength = length;
		boolean someCharacterEscaped = false;
		for (int i = 0; i < length; i++)
		{
			final char c = s.charAt(i);
			final int cint = 0xffff & c;
			if (cint < 32)
			{
				switch (c)
				{
					case '\r':
					case '\n':
					case TAB_CHAR:
					case '\f':
					{
						break;
					}
					default:
					{
						newLength -= 1;
						someCharacterEscaped = true;
					}
				}
			}
			else
			{
				switch (c)
				{
					case '\"':
					{
						newLength += 5;
						someCharacterEscaped = true;
						break;
					}
					case '&':
					case '\'':
					{
						newLength += 4;
						someCharacterEscaped = true;
						break;
					}
					case '<':
					case '>':
					{
						newLength += 3;
						someCharacterEscaped = true;
						break;
					}
				}
			}
		}
		if (!someCharacterEscaped)
		{
			// nothing to escape in the string
			newLength = INVALID_VALUE_INTEGER;
		}
		return newLength;
	}

	/**
	 * Replaces characters that may be confused by an SQL parser with their equivalent
	 * escape characters.
	 * <p>
	 * Any data that will be put in an SQL query should be be escaped. This is especially
	 * important for data that comes from untrusted sources such as Internet users.
	 * <p>
	 * For example if you had the following SQL query:<br>
	 * <code>"SELECT * FROM addresses WHERE name='" + name + "' AND private='N'"</code> <br>
	 * Without this function a user could give <code>" OR 1=1 OR ''='"</code> as their
	 * name causing the query to be:<br>
	 * <code>"SELECT * FROM addresses WHERE name='' OR 1=1 OR ''='' AND private='N'"</code>
	 * <br>
	 * which will give all addresses, including private ones.<br>
	 * Correct usage would be:<br>
	 * <code>"SELECT * FROM addresses WHERE name='" + StringHelper.escapeSQL(name) + "' AND private='N'"</code>
	 * <br>
	 * <p>
	 * Another way to avoid this problem is to use a PreparedStatement with appropriate
	 * placeholders.
	 * 
	 * @param s
	 *            String to be escaped
	 * @return escaped String
	 * @since ostermillerutils 1.00.00
	 */
	public static String escapeSQL(final String s)
	{
		final int length = s.length();
		int newLength = length;
		// first check for characters that might
		// be dangerous and calculate a length
		// of the string that has escapes.
		for (int i = 0; i < length; i++)
		{
			final char c = s.charAt(i);
			switch (c)
			{
				case '\\':
				case '\"':
				case '\'':
				case '\0':
				{
					newLength += 1;
					break;
				}
			}
		}
		if (length == newLength)
		{
			// nothing to escape in the string
			return s;
		}
		final StringBuilder sb = new StringBuilder(newLength);
		for (int i = 0; i < length; i++)
		{
			final char c = s.charAt(i);
			switch (c)
			{
				case '\\':
				{
					sb.append("\\\\");
					break;
				}
				case '\"':
				{
					sb.append("\\\"");
					break;
				}
				case '\'':
				{
					sb.append("\\\'");
					break;
				}
				case '\0':
				{
					sb.append("\\0");
					break;
				}
				default:
				{
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Replaces characters that are not allowed in a Java style string literal with their
	 * escape characters. Specifically quote ("), single quote ('), new line (\n),
	 * carriage return (\r), and backslash (\), and tab (\t) are escaped.
	 * 
	 * @param s
	 *            String to be escaped
	 * @return escaped String
	 * @since ostermillerutils 1.00.00
	 */
	public static String escapeJavaLiteral(final String s)
	{
		final int length = s.length();
		int newLength = length;
		// first check for characters that might
		// be dangerous and calculate a length
		// of the string that has escapes.
		for (int i = 0; i < length; i++)
		{
			final char c = s.charAt(i);
			switch (c)
			{
				case '\"':
				case '\'':
				case '\n':
				case '\r':
				case TAB_CHAR:
				case '\\':
				{
					newLength += 1;
					break;
				}
			}
		}
		if (length == newLength)
		{
			// nothing to escape in the string
			return s;
		}

		// Now replace special characters
		final StringBuilder sb = new StringBuilder(newLength);
		for (int i = 0; i < length; i++)
		{
			final char c = s.charAt(i);
			switch (c)
			{
				case '\"':
				{
					sb.append("\\\"");
					break;
				}
				case '\'':
				{
					sb.append("\\\'");
					break;
				}
				case '\n':
				{
					sb.append("\\n");
					break;
				}
				case '\r':
				{
					sb.append("\\r");
					break;
				}
				case TAB_CHAR:
				{
					sb.append("\\t");
					break;
				}
				case '\\':
				{
					sb.append("\\\\");
					break;
				}
				default:
				{
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Trim any of the characters contained in the second string from the beginning and
	 * end of the first.
	 * 
	 * @param s
	 *            String to be trimmed.
	 * @param c
	 *            list of characters to trim from s.
	 * @return trimmed string
	 * @since ostermillerutils 1.00.00
	 */
	public static String trim(final String s, final String c)
	{
		if (s == null)
		{
			return null;
		}
		final int length = s.length();
		if (c == null)
		{
			return s;
		}
		final int cLength = c.length();
		if (c.length() == 0)
		{
			return s;
		}
		int start = 0;
		int end = length;
		boolean found; // trim-able character found.
		int i;
		// Start from the beginning and find the
		// first non-trim-able character.
		found = false;
		for (i = 0; !found && i < length; i++)
		{
			final char ch = s.charAt(i);
			found = true;
			for (int j = 0; found && j < cLength; j++)
			{
				if (c.charAt(j) == ch)
					found = false;
			}
		}
		// if all characters are trim-able.
		if (!found)
			return EMPTY_STRING;
		start = i - 1;
		// Start from the end and find the
		// last non-trim-able character.
		found = false;
		for (i = length - 1; !found && i >= 0; i--)
		{
			final char ch = s.charAt(i);
			found = true;
			for (int j = 0; found && j < cLength; j++)
			{
				if (c.charAt(j) == ch)
					found = false;
			}
		}
		end = i + 2;
		return s.substring(start, end);
	}

	/**
	 * Turn any HTML escape entities in the string into characters and return the
	 * resulting string.
	 * 
	 * @param s
	 *            String to be unescaped
	 * @return unescaped String
	 * @since ostermillerutils 1.00.00
	 */
	public static String unescapeHtml(final String s)
	{
		final StringBuilder result = new StringBuilder(s.length());
		int ampInd = s.indexOf("&");
		int lastEnd = 0;
		while (ampInd >= 0)
		{
			final int nextAmp = s.indexOf("&", ampInd + 1);
			final int nextSemi = s.indexOf(";", ampInd + 1);
			if (nextSemi != -1 && (nextAmp == -1 || nextSemi < nextAmp))
			{
				int value = -1;
				final String escape = s.substring(ampInd + 1, nextSemi);
				try
				{
					if (escape.startsWith("#"))
					{
						value = Integer.parseInt(escape.substring(1), 10);
					}
					else
					{
						if (HTML_ENTITIES.containsKey(escape))
						{
							value = HTML_ENTITIES.get(escape).intValue();
						}
					}
				}
				catch (final NumberFormatException x)
				{
				}
				result.append(s.substring(lastEnd, ampInd));
				lastEnd = nextSemi + 1;
				if (value >= 0 && value <= 0xffff)
				{
					result.append((char) value);
				}
				else
				{
					result.append("&").append(escape).append(";");
				}
			}
			ampInd = nextAmp;
		}
		result.append(s.substring(lastEnd));
		return result.toString();
	}

	/**
	 * Escapes characters that have special meaning to regular expressions
	 * 
	 * @param s
	 *            String to be escaped
	 * @return escaped String
	 * @since ostermillerutils 1.02.25
	 */
	public static String escapeRegularExpressionLiteral(final String s)
	{
		// According to the documentation in the Pattern class:
		//
		// The backslash character ('\') serves to introduce escaped constructs,
		// as defined in the table above, as well as to quote characters that
		// otherwise would be interpreted as unescaped constructs. Thus the
		// expression \\ matches a single backslash and \{ matches a left brace.
		//
		// It is an error to use a backslash prior to any alphabetic character
		// that does not denote an escaped construct; these are reserved for
		// future
		// extensions to the regular-expression language. A backslash may be
		// used
		// prior to a non-alphabetic character regardless of whether that
		// character
		// is part of an unescaped construct.
		//
		// As a result, escape everything except [0-9a-zA-Z]

		final int length = s.length();
		int newLength = length;
		// first check for characters that might
		// be dangerous and calculate a length
		// of the string that has escapes.
		for (int i = 0; i < length; i++)
		{
			final char c = s.charAt(i);
			if (!((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')))
			{
				newLength += 1;
			}
		}
		if (length == newLength)
		{
			// nothing to escape in the string
			return s;
		}
		final StringBuilder sb = new StringBuilder(newLength);
		for (int i = 0; i < length; i++)
		{
			final char c = s.charAt(i);
			if (!((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')))
			{
				sb.append('\\');
			}
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * Compile a pattern that can will match a string if the string contains any of the
	 * given terms.
	 * <p>
	 * Usage:<br>
	 * <code>boolean b = getContainsAnyPattern(terms).matcher(s).matches();</code>
	 * <p>
	 * If multiple strings are matched against the same set of terms, it is more efficient
	 * to reuse the pattern returned by this function.
	 * 
	 * @param terms
	 *            Array of search strings.
	 * @return Compiled pattern that can be used to match a string to see if it contains
	 *         any of the terms.
	 * @since ostermillerutils 1.02.25
	 */
	public static Pattern getContainsAnyPattern(final String[] terms)
	{
		final StringBuilder sb = StringUtil.newStringBuilder();
		sb.append("(?s).*");
		buildFindAnyPattern(terms, sb);
		sb.append(".*");
		return Pattern.compile(sb.toString());
	}

	/**
	 * Compile a pattern that can will match a string if the string equals any of the
	 * given terms.
	 * <p>
	 * Usage:<br>
	 * <code>boolean b = getEqualsAnyPattern(terms).matcher(s).matches();</code>
	 * <p>
	 * If multiple strings are matched against the same set of terms, it is more efficient
	 * to reuse the pattern returned by this function.
	 * 
	 * @param terms
	 *            Array of search strings.
	 * @return Compiled pattern that can be used to match a string to see if it equals any
	 *         of the terms.
	 * @since ostermillerutils 1.02.25
	 */
	public static Pattern getEqualsAnyPattern(final String[] terms)
	{
		final StringBuilder sb = StringUtil.newStringBuilder();
		sb.append("(?s)\\A");
		buildFindAnyPattern(terms, sb);
		sb.append("\\z");
		return Pattern.compile(sb.toString());
	}

	/**
	 * Compile a pattern that can will match a string if the string starts with any of the
	 * given terms.
	 * <p>
	 * Usage:<br>
	 * <code>boolean b = getStartsWithAnyPattern(terms).matcher(s).matches();</code>
	 * <p>
	 * If multiple strings are matched against the same set of terms, it is more efficient
	 * to reuse the pattern returned by this function.
	 * 
	 * @param terms
	 *            Array of search strings.
	 * @return Compiled pattern that can be used to match a string to see if it starts
	 *         with any of the terms.
	 * @since ostermillerutils 1.02.25
	 */
	public static Pattern getStartsWithAnyPattern(final String[] terms)
	{
		final StringBuilder sb = StringUtil.newStringBuilder();
		sb.append("(?s)\\A");
		buildFindAnyPattern(terms, sb);
		sb.append(".*");
		return Pattern.compile(sb.toString());
	}

	/**
	 * Compile a pattern that can will match a string if the string ends with any of the
	 * given terms.
	 * <p>
	 * Usage:<br>
	 * <code>boolean b = getEndsWithAnyPattern(terms).matcher(s).matches();</code>
	 * <p>
	 * If multiple strings are matched against the same set of terms, it is more efficient
	 * to reuse the pattern returned by this function.
	 * 
	 * @param terms
	 *            Array of search strings.
	 * @return Compiled pattern that can be used to match a string to see if it ends with
	 *         any of the terms.
	 * @since ostermillerutils 1.02.25
	 */
	public static Pattern getEndsWithAnyPattern(final String[] terms)
	{
		final StringBuilder sb = StringUtil.newStringBuilder();
		sb.append("(?s).*");
		buildFindAnyPattern(terms, sb);
		sb.append("\\z");
		return Pattern.compile(sb.toString());
	}

	/**
	 * Compile a pattern that can will match a string if the string contains any of the
	 * given terms.
	 * <p>
	 * Case is ignored when matching using Unicode case rules.
	 * <p>
	 * Usage:<br>
	 * <code>boolean b = getContainsAnyPattern(terms).matcher(s).matches();</code>
	 * <p>
	 * If multiple strings are matched against the same set of terms, it is more efficient
	 * to reuse the pattern returned by this function.
	 * 
	 * @param terms
	 *            Array of search strings.
	 * @return Compiled pattern that can be used to match a string to see if it contains
	 *         any of the terms.
	 * @since ostermillerutils 1.02.25
	 */
	public static Pattern getContainsAnyIgnoreCasePattern(final String[] terms)
	{
		final StringBuilder sb = StringUtil.newStringBuilder();
		sb.append("(?i)(?u)(?s).*");
		buildFindAnyPattern(terms, sb);
		sb.append(".*");
		return Pattern.compile(sb.toString());
	}

	/**
	 * Compile a pattern that can will match a string if the string equals any of the
	 * given terms.
	 * <p>
	 * Case is ignored when matching using Unicode case rules.
	 * <p>
	 * Usage:<br>
	 * <code>boolean b = getEqualsAnyPattern(terms).matcher(s).matches();</code>
	 * <p>
	 * If multiple strings are matched against the same set of terms, it is more efficient
	 * to reuse the pattern returned by this function.
	 * 
	 * @param terms
	 *            Array of search strings.
	 * @return Compiled pattern that can be used to match a string to see if it equals any
	 *         of the terms.
	 * @since ostermillerutils 1.02.25
	 */
	public static Pattern getEqualsAnyIgnoreCasePattern(final String[] terms)
	{
		final StringBuilder sb = StringUtil.newStringBuilder();
		sb.append("(?i)(?u)(?s)\\A");
		buildFindAnyPattern(terms, sb);
		sb.append("\\z");
		return Pattern.compile(sb.toString());
	}

	/**
	 * Compile a pattern that can will match a string if the string starts with any of the
	 * given terms.
	 * <p>
	 * Case is ignored when matching using Unicode case rules.
	 * <p>
	 * Usage:<br>
	 * <code>boolean b = getStartsWithAnyPattern(terms).matcher(s).matches();</code>
	 * <p>
	 * If multiple strings are matched against the same set of terms, it is more efficient
	 * to reuse the pattern returned by this function.
	 * 
	 * @param terms
	 *            Array of search strings.
	 * @return Compiled pattern that can be used to match a string to see if it starts
	 *         with any of the terms.
	 * @since ostermillerutils 1.02.25
	 */
	public static Pattern getStartsWithAnyIgnoreCasePattern(final String[] terms)
	{
		final StringBuilder sb = StringUtil.newStringBuilder();
		sb.append("(?i)(?u)(?s)\\A");
		buildFindAnyPattern(terms, sb);
		sb.append(".*");
		return Pattern.compile(sb.toString());
	}

	/**
	 * Compile a pattern that can will match a string if the string ends with any of the
	 * given terms.
	 * <p>
	 * Case is ignored when matching using Unicode case rules.
	 * <p>
	 * Usage:<br>
	 * <code>boolean b = getEndsWithAnyPattern(terms).matcher(s).matches();</code>
	 * <p>
	 * If multiple strings are matched against the same set of terms, it is more efficient
	 * to reuse the pattern returned by this function.
	 * 
	 * @param terms
	 *            Array of search strings.
	 * @return Compiled pattern that can be used to match a string to see if it ends with
	 *         any of the terms.
	 * @since ostermillerutils 1.02.25
	 */
	public static Pattern getEndsWithAnyIgnoreCasePattern(final String[] terms)
	{
		final StringBuilder sb = StringUtil.newStringBuilder();
		sb.append("(?i)(?u)(?s).*");
		buildFindAnyPattern(terms, sb);
		sb.append("\\z");
		return Pattern.compile(sb.toString());
	}

	/**
	 * Tests to see if the given string contains any of the given terms.
	 * <p>
	 * This implementation is more efficient than the brute force approach of testing the
	 * string against each of the terms. It instead compiles a single regular expression
	 * that can test all the terms at once, and uses that expression against the string.
	 * <p>
	 * This is a convenience method. If multiple strings are tested against the same set
	 * of terms, it is more efficient not to compile the regular expression multiple
	 * times.
	 * 
	 * @see #getContainsAnyPattern(String[])
	 * @param s
	 *            String that may contain any of the given terms.
	 * @param terms
	 *            list of substrings that may be contained in the given string.
	 * @return true iff one of the terms is a substring of the given string.
	 * @since ostermillerutils 1.02.25
	 */
	public static boolean containsAny(final String s, final String[] terms)
	{
		return getContainsAnyPattern(terms).matcher(s).matches();
	}

	/**
	 * Tests to see if the given string equals any of the given terms.
	 * <p>
	 * This implementation is more efficient than the brute force approach of testing the
	 * string against each of the terms. It instead compiles a single regular expression
	 * that can test all the terms at once, and uses that expression against the string.
	 * <p>
	 * This is a convenience method. If multiple strings are tested against the same set
	 * of terms, it is more efficient not to compile the regular expression multiple
	 * times.
	 * 
	 * @see #getEqualsAnyPattern(String[])
	 * @param s
	 *            String that may equal any of the given terms.
	 * @param terms
	 *            list of strings that may equal the given string.
	 * @return true iff one of the terms is equal to the given string.
	 * @since ostermillerutils 1.02.25
	 */
	public static boolean equalsAny(final String s, final String[] terms)
	{
		return getEqualsAnyPattern(terms).matcher(s).matches();
	}

	/**
	 * Tests to see if the given string equals any of the given terms.
	 * <p>
	 * This implementation is more efficient than the brute force approach of testing the
	 * string against each of the terms. It instead compiles a single regular expression
	 * that can test all the terms at once, and uses that expression against the string.
	 * <p>
	 * This is a convenience method. If multiple strings are tested against the same set
	 * of terms, it is more efficient not to compile the regular expression multiple
	 * times.
	 * 
	 * @see #getEqualsAnyPattern(String[])
	 * @param s
	 *            String that may equal any of the given terms.
	 * @param terms
	 *            list of strings that may equal the given string.
	 * @return true iff one of the terms is equal to the given string.
	 * @since ostermillerutils 1.02.25
	 */
	public static boolean equalsAny(final String s, final List<String> terms)
	{
		return equalsAny(s, terms.toArray(CollectionUtil.EMPTY_STRING_ARRAY));
	}

	/**
	 * Tests to see if the given string starts with any of the given terms.
	 * <p>
	 * This implementation is more efficient than the brute force approach of testing the
	 * string against each of the terms. It instead compiles a single regular expression
	 * that can test all the terms at once, and uses that expression against the string.
	 * <p>
	 * This is a convenience method. If multiple strings are tested against the same set
	 * of terms, it is more efficient not to compile the regular expression multiple
	 * times.
	 * 
	 * @see #getStartsWithAnyPattern(String[])
	 * @param s
	 *            String that may start with any of the given terms.
	 * @param terms
	 *            list of strings that may start with the given string.
	 * @return true iff the given string starts with one of the given terms.
	 * @since ostermillerutils 1.02.25
	 */
	public static boolean startsWithAny(final String s, final String[] terms)
	{
		return getStartsWithAnyPattern(terms).matcher(s).matches();
	}

	/**
	 * Tests to see if the given string ends with any of the given terms.
	 * <p>
	 * This implementation is more efficient than the brute force approach of testing the
	 * string against each of the terms. It instead compiles a single regular expression
	 * that can test all the terms at once, and uses that expression against the string.
	 * <p>
	 * This is a convenience method. If multiple strings are tested against the same set
	 * of terms, it is more efficient not to compile the regular expression multiple
	 * times.
	 * 
	 * @see #getEndsWithAnyPattern(String[])
	 * @param s
	 *            String that may end with any of the given terms.
	 * @param terms
	 *            list of strings that may end with the given string.
	 * @return true iff the given string ends with one of the given terms.
	 * @since ostermillerutils 1.02.25
	 */
	public static boolean endsWithAny(final String s, final String[] terms)
	{
		return getEndsWithAnyPattern(terms).matcher(s).matches();
	}

	/**
	 * Tests to see if the given string contains any of the given terms.
	 * <p>
	 * Case is ignored when matching using Unicode case rules.
	 * <p>
	 * This implementation is more efficient than the brute force approach of testing the
	 * string against each of the terms. It instead compiles a single regular expression
	 * that can test all the terms at once, and uses that expression against the string.
	 * <p>
	 * This is a convenience method. If multiple strings are tested against the same set
	 * of terms, it is more efficient not to compile the regular expression multiple
	 * times.
	 * 
	 * @see #getContainsAnyIgnoreCasePattern(String[])
	 * @param s
	 *            String that may contain any of the given terms.
	 * @param terms
	 *            list of substrings that may be contained in the given string.
	 * @return true iff one of the terms is a substring of the given string.
	 * @since ostermillerutils 1.02.25
	 */
	public static boolean containsAnyIgnoreCase(final String s, final String[] terms)
	{
		return getContainsAnyIgnoreCasePattern(terms).matcher(s).matches();
	}

	/**
	 * Tests to see if the given string equals any of the given terms.
	 * <p>
	 * Case is ignored when matching using Unicode case rules.
	 * <p>
	 * This implementation is more efficient than the brute force approach of testing the
	 * string against each of the terms. It instead compiles a single regular expression
	 * that can test all the terms at once, and uses that expression against the string.
	 * <p>
	 * This is a convenience method. If multiple strings are tested against the same set
	 * of terms, it is more efficient not to compile the regular expression multiple
	 * times.
	 * 
	 * @see #getEqualsAnyIgnoreCasePattern(String[])
	 * @param s
	 *            String that may equal any of the given terms.
	 * @param terms
	 *            list of strings that may equal the given string.
	 * @return true iff one of the terms is equal to the given string.
	 * @since ostermillerutils 1.02.25
	 */
	public static boolean equalsAnyIgnoreCase(final String s, final String[] terms)
	{
		return getEqualsAnyIgnoreCasePattern(terms).matcher(s).matches();
	}

	/**
	 * Tests to see if the given string starts with any of the given terms.
	 * <p>
	 * Case is ignored when matching using Unicode case rules.
	 * <p>
	 * This implementation is more efficient than the brute force approach of testing the
	 * string against each of the terms. It instead compiles a single regular expression
	 * that can test all the terms at once, and uses that expression against the string.
	 * <p>
	 * This is a convenience method. If multiple strings are tested against the same set
	 * of terms, it is more efficient not to compile the regular expression multiple
	 * times.
	 * 
	 * @see #getStartsWithAnyIgnoreCasePattern(String[])
	 * @param s
	 *            String that may start with any of the given terms.
	 * @param terms
	 *            list of strings that may start with the given string.
	 * @return true iff the given string starts with one of the given terms.
	 * @since ostermillerutils 1.02.25
	 */
	public static boolean startsWithAnyIgnoreCase(final String s, final String[] terms)
	{
		return getStartsWithAnyIgnoreCasePattern(terms).matcher(s).matches();
	}

	/**
	 * Tests to see if the given string ends with any of the given terms.
	 * <p>
	 * Case is ignored when matching using Unicode case rules.
	 * <p>
	 * This implementation is more efficient than the brute force approach of testing the
	 * string against each of the terms. It instead compiles a single regular expression
	 * that can test all the terms at once, and uses that expression against the string.
	 * <p>
	 * This is a convenience method. If multiple strings are tested against the same set
	 * of terms, it is more efficient not to compile the regular expression multiple
	 * times.
	 * 
	 * @see #getEndsWithAnyIgnoreCasePattern(String[])
	 * @param s
	 *            String that may end with any of the given terms.
	 * @param terms
	 *            list of strings that may end with the given string.
	 * @return true iff the given string ends with one of the given terms.
	 * @since ostermillerutils 1.02.25
	 */
	public static boolean endsWithAnyIgnoreCase(final String s, final String[] terms)
	{
		return getEndsWithAnyIgnoreCasePattern(terms).matcher(s).matches();
	}

	/**
	 * Return a string or <code>NULL_TO_STRING</code> if the string is <code>null</code>.
	 * 
	 * @param s
	 *            string
	 * @return the string or <code>NULL_TO_STRING</code> if <code>s</code> is
	 *         <code>null</code>.
	 */
	public static String getNullSafeString(final String s)
	{
		return (s == null) ? NULL_TO_STRING : s;
	}

	/**
	 * Return an object's string representation or <code>NULL_TO_STRING</code> if the
	 * object is <code>null</code>.
	 * 
	 * @param obj
	 *            object to convert to a string
	 * @return the object's {@link object#toString()} or <code>NULL_TO_STRING</code> if
	 *         <code>o</code> is <code>null</code>.
	 */
	public static String getNullSafeToString(final Object obj)
	{
		return (obj == null) ? NULL_TO_STRING : obj.toString();
	}

	/**
	 * Return an item's identifier representation, or <code>null</code>, if the item is
	 * <code>null</code>.
	 * 
	 * @param item
	 *            an identifiable item
	 * @return the item's ID , or <code>null</code>, if the item is <code>null</code>
	 */
	public static <ID extends Comparable<ID> & Serializable> ID getIdNullSafe(
			final HasIdentifier<ID> item)
	{
		return ((item == null) || (item.getId() == null)) ? null : item.getId();
	}

	/**
	 * Return an item's identifier string representation, or <code>NULL_TO_STRING</code>,
	 * if the item is <code>null</code>.
	 * 
	 * @param item
	 *            an identifiable item
	 * @return the item's ID string representation, or <code>NULL_TO_STRING</code>, if the
	 *         item is <code>null</code>
	 */
	public static String getIdAsStringNullSafe(final HasIdentifier<?> item)
	{
		return ((item == null) || (item.getId() == null)) ? NULL_TO_STRING : item
				.getId()
				.toString();
	}

	/**
	 * Return an item's name, or <code>NULL_TO_STRING</code>, if the item is
	 * <code>null</code>.
	 * 
	 * @param item
	 *            a named item
	 * @return the item's name, or <code>NULL_TO_STRING</code>, if the item is
	 *         <code>null</code>
	 */
	public static String getNameNullSafe(final Named item)
	{
		return (item == null) ? NULL_TO_STRING : item.getName();
	}

	/**
	 * Return an item's value, or <code>null</code>, if the item is <code>null</code>.
	 * 
	 * @param item
	 *            a valued item
	 * @return the item's name, or <code>null</code>, if the item is <code>null</code>
	 */
	public static <T> T getValueNullSafe(final Valued<T> item)
	{
		return (item == null) ? null : item.getValue();
	}

	/**
	 * Print an integer list.
	 * 
	 * @param title
	 *            list's title
	 * @param list
	 *            the list
	 * @return string builder containing the list's printout
	 */
	public static StringBuilder printIntegerList(final String title, final List<?> list)
	{
		StringBuilder s = new StringBuilder(title);
		s.append(" = [");
		for (final Object i : list)
		{
			s = s.append(i);
			s = s.append(Strings.SPACE_STRING);
		}
		s = s.append("]\n");
		return s;
	}

	/**
	 * Clean and trim a string read from a spring context file.
	 * 
	 * @param string
	 *            string to clean
	 * @return cleaned string
	 */
	public static String cleanContextFileString(final String string)
	{
		// return
		// org.springframework.util.StringUtils.trimAllWhitespace(string.replaceAll("[\t\n]",
		// ""));
		return string.replaceAll("[\t\n]", "").trim();
	}

	/**
	 * Convert a <code>List</code> of String objects to a CSV list of strings
	 * 
	 * @param stringsToDelimit
	 *            The <code>List</code> of strings to delimit
	 * 
	 * @return The comma delimited list of values
	 */
	public static String getCsvStringsFromList(final List<String> stringsToDelimit)
	{
		final StringBuilder sbCSVStrings = newStringBuilder();
		if (stringsToDelimit != null)
		{
			for (int count = 0; count < stringsToDelimit.size(); count++)
			{
				sbCSVStrings.append(stringsToDelimit.get(count));
				if (count + 1 < stringsToDelimit.size())
				{
					sbCSVStrings.append(",");
				}
			}
		}
		return sbCSVStrings.toString();
	}

	/**
	 * Return the list of names of a list of items.
	 * 
	 * @param items
	 *            an item list
	 * @return the corresponding item name list
	 */
	public static List<String> getAsNameList(final List<? extends Named> items)
	{
		final List<String> names = newList();
		for (final Named element : items)
		{
			names.add(element == null ? null : element.getName());
		}
		return names;
	}

	/**
	 * Truncate a string to a certain length. If the string is <code>null</code> or
	 * shorter than the specified length, it is returned intact.
	 * 
	 * @param str
	 *            string to truncate
	 * @param numChars
	 *            maximum length of string after truncation
	 * @return trunacted string
	 */
	public static String truncate(final String str, final int numChars)
	{
		return ((str == null) || (str.length() <= numChars)) ? str : str.substring(0,
				numChars);
	}

	/**
	 * Truncates a string to a specified number of characters.
	 * 
	 * @param str
	 *            The string to truncate
	 * @param numChars
	 *            The number of characters to truncate
	 * @param showEllipse
	 *            Flag indicating whether or not to show the default ellipse symbol if the
	 *            string is truncated
	 * @return The truncated string if string length is more than <code>numChars</code>
	 *         otherwise return the full string
	 */
	public static String truncate(final String str, final int numChars,
			final boolean showEllipse)
	{
		return truncate(str, numChars, showEllipse, Strings.ELIPSIS);
	}

	/**
	 * Truncates a string to a specified number of characters.
	 * 
	 * @param str
	 *            The string to truncate
	 * @param numChars
	 *            The number of characters to truncate
	 * @param showEllipse
	 *            Flag indicating whether or not to show an ellipse if the string is
	 *            truncated
	 * @param ellipsisSymbol
	 *            ellipsis symbol to append at the end of the string if truncated
	 * @return The truncated string if string length is more than <code>numChars</code>
	 *         otherwise return the full string
	 */
	public static String truncate(final String str, final int numChars,
			final boolean showEllipse, final String ellipsisSymbol)
	{
		final StringBuffer truncatedStr = new StringBuffer(str != null ? str : "");
		if (str != null && str.length() >= numChars)
		{
			truncatedStr.setLength(numChars);
			if (showEllipse)
			{
				truncatedStr.append(ellipsisSymbol);
			}
		}
		return truncatedStr.toString();
	}

	/**
	 * Return the camel case string representation of an enumerated constant name. For
	 * instance, if the constant name is <code>SUB_TOPIC</code>, the camel case form is
	 * "subTopic". The returned string always starts with a lower case letter. The
	 * separation of strings into parts is done by splitting around
	 * {@link Strings#ENUM_NAME_SEPARATOR}.
	 * 
	 * @param constantName
	 *            enumerated constant name. Must be non-<code>null</code>
	 * @return the camel case string representation
	 */
	public static String constantNameToCamelCase(final String constantName)
	{
		return uncapitalize(constantName, new char[]
		{ ENUM_NAME_SEPARATOR_CHAR });
	}

	/**
	 * Return the XML tag name of an enumerated constant name. For instance, if the
	 * constant name is <code>SUB_TOPIC</code>, the XML tag name is "sub-topic".
	 * 
	 * @param constantName
	 *            enumerated constant name. Must be non-<code>null</code>
	 * @return the camel case string representation
	 */
	public static String constantNameToXmlName(final String constantName)
	{
		return replace(constantName, ENUM_NAME_SEPARATOR, XML_NAME_SEPARATOR)
				.toLowerCase();
	}

	/**
	 * Return a singly-quoted text.
	 * 
	 * @param text
	 *            text to quote
	 * @return singly-quoted text
	 */
	public static String quote(final String text)
	{
		return SINGLE_QUOTE + text + SINGLE_QUOTE;
	}

	/**
	 * Return a doubly-quoted text.
	 * 
	 * @param text
	 *            text to quote
	 * @return doubly-quoted text
	 */
	public static String doubleQuote(final String text)
	{
		return DOUBLE_QUOTE + text + DOUBLE_QUOTE;
	}

	/**
	 * Return a plural form suffix string. Useful in printing <code>item</code> or
	 * <code>items</code> depending on the number of items in question.
	 * 
	 * @param number
	 *            number of items
	 * @return if number equals <code>1</code>, returns an empty string, otherwise returns
	 *         <code>"s"</code>
	 */
	public static String pluralFormSuffix(final int number)
	{
		return (number == 1) ? EMPTY_STRING : "s";
	}

	/**
	 * Return a default plural form string. Example: prints <code>item</code> or
	 * <code>items</code> depending on the number of items in question.
	 * 
	 * @param string
	 *            string to append, e.g. <code>"item"</code>
	 * @param number
	 *            number of items
	 * @return if number equals <code>1</code>, returns the original string, otherwise
	 *         returns <code>string + "s"</code>
	 */
	public static String pluralForm(final String string, final int number)
	{
		return (number == 1) ? string : (string + "s");
	}

	/**
	 * Return a default plural form string. Example: prints <code>item</code> or
	 * <code>items</code> depending on the number of items in question.
	 * 
	 * @param string
	 *            string to append, e.g. <code>"item"</code>
	 * @param number
	 *            number of items
	 * @return if number equals <code>1</code>, returns the original string, otherwise
	 *         returns <code>string + "s"</code>
	 */
	public static String pluralForm(final String string, final long number)
	{
		return (number == 1) ? string : (string + "s");
	}

	/**
	 * Return a flexible plural form string. Example: prints <code>status</code> or
	 * <code>stati</code> depending on the number of status objects in question.
	 * 
	 * @param singleForm
	 *            single form of the object in question
	 * @param plural
	 *            plural form of the object in question
	 * @param number
	 *            number of items
	 * @return if number equals <code>1</code>, returns the single form, otherwise returns
	 *         the plural form
	 */
	public static String pluralForm(final String singleForm, final String pluralForm,
			final int number)
	{
		return (number == 1) ? singleForm : pluralForm;
	}

	/**
	 * Converts a string to first character capitalized case. That is the first letter
	 * capitalized and the rest lower case.
	 * 
	 * 
	 * <code>
	 * StringUtils.toLowercaseFirstCharUppercase("HELLO WORLD); //prints Hello world
	 * </code>
	 * 
	 * @param string
	 *            the String to convert to first char uppercase and rest lowercase
	 * @return
	 */
	public static String toLowercaseFirstCharUppercase(final String string)
	{
		final String lower = string.toLowerCase();
		return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
	}

	/**
	 * Return a view of a list.
	 * 
	 * @param list
	 *            list to be printed
	 * @param printer
	 *            converts each element into a string
	 * @return corresponding list of element string representation
	 */
	public static <T> List<String> printList(final List<T> list, final Printer<T> printer)
	{
		final List<String> strings = CollectionUtil.newList();
		for (final T element : list)
		{
			strings.add(printer.visit(element));
		}
		return strings;
	}

	/**
	 * Adds the ANSI SQL Wildcard to the end of the string.
	 * 
	 * @param string
	 *            the string to add the wildcard too
	 * @return a new string with the ANSI SQL wildcard appended to the end
	 */
	public static String addSqlWildcardToEnd(final String string)
	{
		return string + SQL_WILDCARD;
	}

	/**
	 * Adds the ANSI SQL Wildcard to the beginning of the string.
	 * 
	 * @param string
	 *            the string to add the wildcard too
	 * @return a new string with the ANSI SQL wildcard appended to the beginning
	 */
	public static String addSqlWildcardToBeginning(final String string)
	{
		return SQL_WILDCARD + string;
	}

	/**
	 * Adds the ANSI SQL Wildcard to the beginning and end of the string.
	 * 
	 * @param string
	 *            the string to add the wildcards too
	 * @return a new string with the ANSI SQL wildcard appended to the beginning and end
	 */
	public static String surroundWithSqlWildcard(final String string)
	{
		return addSqlWildcardToBeginning(addSqlWildcardToEnd(string));
	}

	/**
	 * <p>
	 * Centers a String in a larger String of size <code>size</code>. Uses a supplied
	 * String as the value to pad the String with.
	 * </p>
	 * 
	 * <p>
	 * If the size is less than the String length, the String is returned. A
	 * <code>null</code> String returns <code>null</code>. A negative size is treated as
	 * zero.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.centerWithSpace(null, *, *)     = null
	 * StringUtil.centerWithSpace("header", 12, '=')    = "== Header =="
	 * </pre>
	 */
	public static String centerWithSpace(final String str, final int size,
			final String padStr)
	{
		return StringUtils.center(SPACE_STRING + str + SPACE_STRING, size, padStr);
	}

	/**
	 * The same as {@link #centerWithSpace(String, int, String)}, only that the size is
	 * the size of the padding on each side of <code>str</code> rather than the total
	 * output string length.
	 * 
	 * @param str
	 * @param padSize
	 * @param padStr
	 * @return
	 */
	public static String centerWithSpacePadSize(final String str, final int padSize,
			final String padStr)
	{
		return StringUtils.center(SPACE_STRING + str + SPACE_STRING, str.length() + 2
				+ padSize, padStr);
	}

	/**
	 * Generate a string that is a repetition of a string up to fill a certain length.
	 * 
	 * @param padStr
	 *            string to repeat
	 * @param size
	 *            size of returned string
	 * @return repeated string
	 */
	public static String repeat(final String padStr, final int size)
	{
		return StringUtils.leftPad(EMPTY_STRING, size, padStr);
	}

	/**
	 * Print an exception's stack trace to a string
	 * 
	 * @param e
	 *            exception
	 * @return stack trace printout as a string
	 */
	public static String getStackTraceAsString(final Throwable e)
	{
		try (final StringPrintWriter out = new StringPrintWriter())
		{
			e.printStackTrace(out);
			final String trace = out.getString();
			return trace;
		}
	}

	/**
	 * @param object
	 * @return
	 */
	public static String getClassAsStringNullSafe(final Object object)
	{
		return ((object == null) ? Strings.NULL_TO_STRING : object.getClass().toString());
	}

	/**
	 * Re-encode an input stream in UTF8 encoding.
	 * 
	 * @param source
	 * @return
	 * @throws IOException
	 */
	public static InputStream toUtf8(final InputStream source) throws IOException
	{
		final StringBuilder stringSource = StringUtil.newStringBuilder();
		final byte[] b = new byte[4096];
		for (int n; (n = source.read(b)) != -1;)
		{
			stringSource.append(new String(b, 0, n));
		}
		return new ByteArrayInputStream(stringSource
				.toString()
				.getBytes(DEFAULT_ENCODING));
	}

	// See equalsAny()
	// /**
	// * Return <code>true</code> if and only if a string equals one or more strings in a
	// * collection.
	// * <p>
	// * If s is <code>null</code> or the list is empty or <code>null</code> , this method
	// * always returns <code>false</code>.
	// *
	// * @param s
	// * string
	// * @param strings
	// * list of strings to match
	// * @return result of comparison
	// */
	// public static boolean equalsOneOf(final String s, final Collection<String> strings)
	// {
	// if ((s == null) || (strings == null))
	// {
	// return false;
	// }
	// else
	// {
	// for (final String element : strings)
	// {
	// if (s.equals(element))
	// {
	// return true;
	// }
	// }
	// return false;
	// }
	// }

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Build a regular expression that is each of the terms or'd together.
	 * 
	 * @param terms
	 *            a list of search terms
	 * @param sb
	 *            place to build the regular expression.
	 * @since ostermillerutils 1.02.25
	 */
	private static void buildFindAnyPattern(final String[] terms, final StringBuilder sb)
	{
		if (terms.length == 0)
		{
			return;
		}
		sb.append("(?:");
		for (int i = 0; i < terms.length; i++)
		{
			if (i > 0)
				sb.append("|");
			sb.append("(?:");
			sb.append(escapeRegularExpressionLiteral(terms[i]));
			sb.append(")");
		}
		sb.append(")");
	}

}
