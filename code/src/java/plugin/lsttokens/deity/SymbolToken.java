/*
 * Copyright (c) 2008 Tom Parker <thpr@users.sourceforge.net>
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */
package plugin.lsttokens.deity;

import pcgen.cdom.enumeration.StringKey;
import pcgen.core.Deity;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.rules.persistence.token.ParseResult;

/**
 * Class deals with SYMBOL Token
 */
public class SymbolToken implements CDOMPrimaryToken<Deity>
{

	public String getTokenName()
	{
		return "SYMBOL";
	}

	public ParseResult parseToken(LoadContext context, Deity deity, String value)
	{
		if (value == null || value.length() == 0)
		{
			return new ParseResult.Fail(getTokenName() + " arguments may not be empty");
		}
		context.getObjectContext().put(deity, StringKey.HOLY_ITEM, value);
		return ParseResult.SUCCESS;
	}

	public String[] unparse(LoadContext context, Deity deity)
	{
		String holyItem =
				context.getObjectContext()
					.getString(deity, StringKey.HOLY_ITEM);
		if (holyItem == null)
		{
			return null;
		}
		return new String[]{holyItem};
	}

	public Class<Deity> getTokenClass()
	{
		return Deity.class;
	}
}
