/*
 * Copyright 2007 (C) Thomas Parker <thpr@users.sourceforge.net>
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
 */
package plugin.lsttokens.equipmentmodifier.choose;

import pcgen.cdom.enumeration.StringKey;
import pcgen.core.EquipmentModifier;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.CDOMSecondaryToken;
import pcgen.rules.persistence.token.ParseResult;

public class StringToken implements CDOMSecondaryToken<EquipmentModifier>
{

	public String getTokenName()
	{
		return "STRING";
	}

	public String getParentToken()
	{
		return "CHOOSE";
	}

	public ParseResult parseToken(LoadContext context, EquipmentModifier obj,
		String value)
	{
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
		StringBuilder sb = new StringBuilder();
		sb.append(getTokenName()).append('|').append(value);
		context.obj.put(obj, StringKey.CHOICE_STRING, sb.toString());
		return ParseResult.SUCCESS;
	}

	public String[] unparse(LoadContext context, EquipmentModifier eqMod)
	{
		String chooseString = context.getObjectContext().getString(eqMod,
				StringKey.CHOICE_STRING);
		if (chooseString == null
				|| chooseString.indexOf(getTokenName() + '|') == -1)
		{
			return null;
		}
		return new String[] { chooseString
				.substring(getTokenName().length() + 1) };
	}

	public Class<EquipmentModifier> getTokenClass()
	{
		return EquipmentModifier.class;
	}
}
