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
package plugin.lsttokens.equipment;

import pcgen.cdom.enumeration.IntegerKey;
import pcgen.core.Equipment;
import pcgen.rules.persistence.token.AbstractIntToken;
import pcgen.rules.persistence.token.CDOMPrimaryToken;

/**
 * Deals with ACCHECK token
 */
public class AccheckToken extends AbstractIntToken<Equipment> implements
		CDOMPrimaryToken<Equipment>
{
	@Override
	public String getTokenName()
	{
		return "ACCHECK";
	}

	@Override
	protected IntegerKey integerKey()
	{
		return IntegerKey.AC_CHECK;
	}

	@Override
	protected int maxValue()
	{
		return 0;
	}

	public Class<Equipment> getTokenClass()
	{
		return Equipment.class;
	}
}
