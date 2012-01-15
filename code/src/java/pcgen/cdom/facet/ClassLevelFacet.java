/*
 * Copyright (c) Thomas Parker, 2009.
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
package pcgen.cdom.facet;

import pcgen.cdom.enumeration.CharID;
import pcgen.cdom.facet.ClassFacet.ClassLevelChangeEvent;
import pcgen.cdom.facet.ClassFacet.ClassLevelChangeListener;
import pcgen.cdom.facet.ClassFacet.ClassLevelObjectChangeEvent;
import pcgen.cdom.inst.PCClassLevel;
import pcgen.core.PCClass;

/**
 * ClassLevelFacet is a Facet that that tracks the PCClassLevel objects that
 * have been granted to a Player Character.
 */
public class ClassLevelFacet extends AbstractSourcedListFacet<PCClassLevel>
		implements ClassLevelChangeListener
{

	private ClassFacet classFacet = FacetLibrary.getFacet(ClassFacet.class);

	/**
	 * Performs the processing to identify the newly activated class levels
	 * 
	 * @param id
	 *            The CharID representing the Player Character to be updated
	 * @param pcc
	 *            The PCClass to be udpated
	 * @param oldLevel
	 *            The previous level value for the given PCClass in the Player
	 *            Character
	 * @param level
	 *            The new level value for the given PCClass in the Player
	 *            Character
	 */
	private void update(CharID id, PCClass pcc, Integer oldLevel, int level)
	{
		int old = oldLevel == null ? 0 : oldLevel;
		for (int i = old + 1; i <= level; i++)
		{
			PCClassLevel classLevel = classFacet.getClassLevel(id, pcc, i);
			if (classLevel != null)
			{
				add(id, classLevel, pcc);
			}
		}
		for (int i = old; i > level; i--)
		{
			PCClassLevel classLevel = classFacet.getClassLevel(id, pcc, i);
			if (classLevel != null)
			{
				remove(id, classLevel, pcc);
			}
		}
	}

	/**
	 * Triggered when the Level of the Player Character changes.
	 * 
	 * @param lce
	 *            The LevelChangeEvent containing the information about the
	 *            level change
	 * 
	 * @see pcgen.cdom.facet.LevelFacet.LevelChangeListener#levelChanged(pcgen.cdom.facet.LevelFacet.LevelChangeEvent)
	 */
	@Override
	public void levelChanged(ClassLevelChangeEvent lce)
	{
		update(lce.getCharID(), lce.getPCClass(), lce.getOldLevel(), lce
				.getNewLevel());
	}

	@Override
	public void levelObjectChanged(ClassLevelObjectChangeEvent lce)
	{
		PCClassLevel old = lce.getOldLevel();
		if (old != null)
		{
			/*
			 * By defintion, if old is null, the replacement isn't meaningful
			 * for this facet
			 */
			CharID id = lce.getCharID();
			PCClass pcc = lce.getPCClass();
			if (remove(id, old, pcc))
			{
				/*
				 * Only add the new item if we had the old one "in" the PC
				 */
				add(id, lce.getNewLevel(), pcc);
			}
		}
	}

}
