/*
 * Race.java
 * Copyright 2001 (C) Bryan McRoberts <merton_monk@yahoo.com>
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
 * Created on April 21, 2001, 2:15 PM
 *
 * $Id$
 */
package pcgen.core;

import java.util.List;

import pcgen.base.formula.Formula;
import pcgen.cdom.enumeration.FormulaKey;
import pcgen.cdom.enumeration.Gender;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.SourceFormat;
import pcgen.core.facade.GenderFacade;
import pcgen.core.facade.RaceFacade;
import pcgen.core.facade.SimpleFacade;
import pcgen.core.facade.util.DefaultListFacade;
import pcgen.core.facade.util.ListFacade;
import pcgen.system.LanguageBundle;

/**
 * <code>Race</code>.
 *
 * @author Bryan McRoberts <merton_monk@users.sourceforge.net>
 * @author Michael Osterlie
 * @version $Revision$
 */
public final class Race extends PObject implements RaceFacade
{

	private static final DefaultListFacade<GenderFacade> genderList =
			new DefaultListFacade<GenderFacade>();
	private static final DefaultListFacade<SimpleFacade> handList =
			new DefaultListFacade<SimpleFacade>();

	static
	{
		Gender[] genders = Gender.values();
		for (final Gender gender : genders)
		{
			genderList.addElement(gender);
		}
		handList.addElement(new SimpleFacadeImpl(LanguageBundle
			.getString("in_handRight")));
		handList.addElement(new SimpleFacadeImpl(LanguageBundle
			.getString("in_handLeft")));
		handList.addElement(new SimpleFacadeImpl(LanguageBundle
			.getString("in_handBoth")));
		handList.addElement(new SimpleFacadeImpl(LanguageBundle
			.getString("in_comboNone")));
		handList.addElement(new SimpleFacadeImpl(LanguageBundle
			.getString("in_comboOther")));
	}

	/**
	 * Checks if this race's advancement is limited.
	 * 
	 * @return <tt>true</tt> if this race advances unlimitedly.
	 */
	public boolean isAdvancementUnlimited()
	{
		List<Integer> hda = getListFor(ListKey.HITDICE_ADVANCEMENT);
		return hda == null
				|| Integer.MAX_VALUE == hda.get(hda.size() - 1).intValue();
	}

	/**
	 * Overridden to only consider the race's name.
	 * @return hash code
	 */
	@Override
	public int hashCode()
	{
		return getKeyName().hashCode();
	}

	public int maxHitDiceAdvancement()
	{
		List<Integer> hda = getListFor(ListKey.HITDICE_ADVANCEMENT);
		return hda == null ? 0 : hda.get(hda.size() - 1);
	}

	public ListFacade<GenderFacade> getGenders()
	{
		return genderList;
	}

	public ListFacade<SimpleFacade> getHands()
	{
		return handList;
	}

	public String getSource()
	{
		return SourceFormat.getFormattedString(this,
			Globals.getSourceDisplay(), true);
	}

	public String getSize()
	{
		Formula formula = get(FormulaKey.SIZE);
		if (formula != null)
		{
			return formula.toString();
		}
		return null;
	}

	public String getMovement()
	{
		List<Movement> movements = getListFor(ListKey.MOVEMENT);
		if (movements != null && !movements.isEmpty())
		{
			return movements.get(0).toString();
		}
		return null;
	}
}
