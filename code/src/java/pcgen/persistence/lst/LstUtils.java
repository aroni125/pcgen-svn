/*
 * LstUtils.java
 * Copyright 2003 (C) PCGen team
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Current Ver: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 */
package pcgen.persistence.lst;

import java.net.URI;

import pcgen.cdom.base.CDOMObject;
import pcgen.core.bonus.BonusObj;
import pcgen.util.Logging;
import pcgen.system.LanguageBundle;

/**
 * Utility class to assist with LST files
 */
public class LstUtils
{

	/** Constant representing the pipe "|" character */
	public static final String PIPE = "|"; //$NON-NLS-1$
	/** Constant representing the comma "," character */
	public static final String COMMA = ","; //$NON-NLS-1$

	/**
	 * Checks a LST token to see if it's deprecated
	 * @param token
	 * @param obj
	 * @param value
	 */
	public static void deprecationCheck(LstToken token, CDOMObject obj,
		String value)
	{
		if (token instanceof DeprecatedToken)
		{
			final String msg =
					LanguageBundle.getFormattedString(
						"Warnings.LstTokens.Deprecated", //$NON-NLS-1$
						token.getTokenName(), value, obj.getDisplayName(), obj
							.getSourceURI(),
						((DeprecatedToken) token).getMessage(obj, value));
			deprecationWarning(msg);
		}
	}

	/**
	 * Checks to see if a LST Token is deprecated
	 * @param token The token to be checked.
	 * @param name The tag name that was used
	 * @param source The object in which the tag was found 
	 * @param value The value of the tag
	 */
	public static void deprecationCheck(LstToken token, String name,
		URI source, String value)
	{
		if (token instanceof DeprecatedToken)
		{
			deprecationWarning(token.getTokenName(), name, source, value,
				((DeprecatedToken) token).getMessage(null, value));
		}
	}

	/**
	 * Checks to see if a bonus Token is deprecated
	 * @param token The token to be checked.
	 * @param name The tag name that was used
	 * @param value The value of the tag
	 */
	public static void deprecationCheck(BonusObj token, String name,
		String value)
	{
		if (token instanceof DeprecatedToken)
		{
			final String msg =
					LanguageBundle.getFormattedString(
						"Warnings.Bonus.Deprecated", //$NON-NLS-1$
						name, value, ((DeprecatedToken) token).getMessage(null,
							value));
			deprecationWarning(msg);
		}
	}

	/**
	 * Log the deprecation warning.
	 * @param tokenName The name of the deprecated token
	 * @param name The tag name that was used
	 * @param source The object in which the tag was found 
	 * @param value The value of the tag
	 * @param message A message describing corrective action.
	 */
	public static void deprecationWarning(String tokenName, String name,
		URI source, String value, String message)
	{
		final String msg =
				LanguageBundle.getFormattedString(
					"Warnings.LstTokens.Deprecated", //$NON-NLS-1$
					tokenName, value, name, source, message);
		deprecationWarning(msg);
	}

	/**
	 * Log the deprecation warning
	 * @param warning
	 */
	public static void deprecationWarning(String warning)
	{
		Logging.deprecationPrint(warning);
	}
}
