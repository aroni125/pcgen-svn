/*
 * EqBuilderEqTypeToken.java
 * Copyright 2007 (C) James Dempsey <jdempsey@users.sourceforge.net>
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
 * Created on 12/01/2008
 *
 * $Id$
 */
package plugin.lsttokens.equipmentmodifier.choose;

import java.util.StringTokenizer;

import pcgen.cdom.base.Constants;
import pcgen.cdom.enumeration.StringKey;
import pcgen.core.EquipmentModifier;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.CDOMSecondaryToken;
import pcgen.rules.persistence.token.ParseResult;

/**
 * <code>EqBuilderEqTypeToken</code> parses the EQ Builder specific choose
 * string to allow the selection of equipent types.
 *
 * Last Editor: $Author$ Last Edited: $Date: 2008-05-06 22:19:45 -0400
 * (Tue, 06 May 2008) $
 *
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision$
 */
public class EqBuilderEqTypeToken implements
		CDOMSecondaryToken<EquipmentModifier>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see pcgen.persistence.lst.LstToken#getTokenName()
	 */
	public String getTokenName()
	{
		return "EQBUILDER.EQTYPE";
	}

	public String getParentToken()
	{
		return "CHOOSE";
	}

	public ParseResult parseToken(LoadContext context, EquipmentModifier obj,
		String value)
	{
		if (value == null)
		{
			context.obj.put(obj, StringKey.CHOICE_STRING, getTokenName());
			return ParseResult.SUCCESS;
		}
		if (value.indexOf(',') != -1)
		{
			return new ParseResult.Fail("CHOOSE:" + getTokenName()
					+ " arguments may not contain , : " + value);
		}
		if (value.indexOf('[') != -1)
		{
			return new ParseResult.Fail("CHOOSE:" + getTokenName()
					+ " arguments may not contain [] : " + value);
		}
		if (value.charAt(0) == '|')
		{
			return new ParseResult.Fail("CHOOSE:" + getTokenName()
					+ " arguments may not start with | : " + value);
		}
		if (value.charAt(value.length() - 1) == '|')
		{
			return new ParseResult.Fail("CHOOSE:" + getTokenName()
					+ " arguments may not end with | : " + value);
		}
		if (value.indexOf("||") != -1)
		{
			return new ParseResult.Fail("CHOOSE:" + getTokenName()
					+ " arguments uses double separator || : " + value);
		}
		int pipeLoc = value.indexOf("|");
		if (pipeLoc == -1)
		{
			return new ParseResult.Fail("CHOOSE:" + getTokenName()
							+ " must have two or more | delimited arguments : "
							+ value);
		}
		StringTokenizer tok = new StringTokenizer(value, Constants.PIPE);
		if (tok.countTokens() != 2)
		{
			return new ParseResult.Fail("COUNT:" + getTokenName()
					+ " requires two arguments: " + value);
		}
		// New format: CHOOSE:EQBUILDER.EQTYPE|COUNT=ALL|TITLE=desired TYPE(s)
		String first = tok.nextToken();
		if (!first.startsWith("COUNT="))
		{
			return new ParseResult.Fail("CHOOSE:" + getTokenName()
					+ " must have COUNT= as its first argument : " + value);
		}
		String second = tok.nextToken();
		if (!second.startsWith("TITLE="))
		{
			return new ParseResult.Fail("CHOOSE:" + getTokenName()
					+ " must have TITLE= as its second argument : " + value);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(first).append('|').append(second.substring(6));
		sb.append("|TYPE=EQTYPES");
		// Old format: CHOOSE:COUNT=ALL|desired TYPE(s)|TYPE=EQTYPES
		context.obj.put(obj, StringKey.CHOICE_STRING, sb.toString());
		return ParseResult.SUCCESS;
	}

	public String[] unparse(LoadContext context, EquipmentModifier eqMod)
	{
		String chooseString = context.getObjectContext().getString(eqMod,
				StringKey.CHOICE_STRING);
		if (chooseString == null)
		{
			return null;
		}
		String returnString;
		if (getTokenName().equals(chooseString))
		{
			returnString = "";
		}
		else
		{
			if (chooseString.indexOf(getTokenName() + '|') == -1)
			{
				return null;
			}
			returnString = chooseString.substring(getTokenName().length() + 1);
		}
		return new String[] { returnString };
	}

	public Class<EquipmentModifier> getTokenClass()
	{
		return EquipmentModifier.class;
	}
}
