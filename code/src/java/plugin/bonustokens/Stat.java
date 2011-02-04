/*
 * Stat.java
 * Copyright 2002 (C) Greg Bingleman <byngl@hotmail.com>
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
 * Created on December 13, 2002, 9:19 AM
 *
 * Current Ver: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 *
 */
package plugin.bonustokens;

import pcgen.core.PCClass;
import pcgen.core.PCStat;
import pcgen.core.bonus.BonusObj;
import pcgen.core.bonus.util.MissingObject;
import pcgen.rules.context.LoadContext;

/**
 * <code>Stat</code>
 *
 * @author  Greg Bingleman <byngl@hotmail.com>
 */
public final class Stat extends BonusObj
{
	private static final String[] bonusTags =
			{"BASESPELLSTAT", "BASESPELLKNOWNSTAT"};

	@Override
	protected boolean parseToken(LoadContext context, final String token)
	{
		for (int i = 0; i < bonusTags.length; ++i)
		{
			if (bonusTags[i].equals(token))
			{
				addBonusInfo(Integer.valueOf(i));

				return true;
			}
		}

		if (token.startsWith("CAST=") || token.startsWith("CAST."))
		{
			PCStat stat = context.ref.getAbbreviatedObject(
					PCStat.class, token.substring(5));

			if (stat != null)
			{
				addBonusInfo(new CastStat(stat));

				return true;
			}
		}
		else
		{
			PCStat stat = context.ref.getAbbreviatedObject(
					PCStat.class, token);

			if (stat != null)
			{
				addBonusInfo(stat);
			}
			else
			{
				final PCClass aClass = context.ref
						.silentlyGetConstructedCDOMObject(PCClass.class, token);

				if (aClass != null)
				{
					addBonusInfo(aClass);
				}
				else
				{
					addBonusInfo(new MissingObject(token));
				}
			}

			return true;
		}

		return false;
	}

	@Override
	protected String unparseToken(final Object obj)
	{
		if (obj instanceof Integer)
		{
			return bonusTags[((Integer) obj).intValue()];
		}
		else if (obj instanceof CastStat)
		{
			return "CAST." + ((CastStat) obj).stat.getAbb();
		}
		else if (obj instanceof PCClass)
		{
			return ((PCClass) obj).getKeyName();
		}
		else if (obj instanceof MissingObject)
		{
			return ((MissingObject) obj).getObjectName();
		}

		return ((PCStat) obj).getAbb();
	}

	/**
	 * Deals with the Stat for casting
	 */
	public static class CastStat
	{
		/** A stat */
		public final PCStat stat;

		/**
		 * Constuctor
		 * @param argStat
		 */
		public CastStat(final PCStat argStat)
		{
			stat = argStat;
		}
	}

	@Override
	public String getBonusHandled()
	{
		return "STAT";
	}
}
